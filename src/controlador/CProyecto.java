/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import modelo.Empresa;
import modelo.Proyecto;
import vista.FormatoProyecto;

/**
 *
 * @author carlo
 */
public class CProyecto implements ActionListener, DocumentListener, KeyListener {

    private FormatoProyecto vista;
    private Proyecto proyecto;
    private String categoriaOrden = "clave";
    private boolean ordenAscendente = true;
    private List<Proyecto> proyectosTemporales = new ArrayList<>();

    public CProyecto(FormatoProyecto vista, Proyecto empresa) {

        this.vista = vista;
        this.proyecto = empresa;

        iniciarComponentes();
        configurarListener();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == vista.btnGuardar) {
            guardarEnBase();
            actualizarContador();
        } else if (src == vista.btnAnadir) {
            agregarTabla();
            actualizarContador();
        } else if (src == vista.btnDescartar) {
            descartarDato();
            actualizarContador();
        } else if (src == vista.btnFiltro) {
            cambiarCategoria();
            actualizarContador();
        } else if (src == vista.btnOrden) {
            ordenAscendente = !ordenAscendente;
            vista.btnOrden.setText(ordenAscendente ? "Ascendente" : "Descendente");
            filtrarYOrdenar();
            actualizarContador();
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {

        buscarEmpresa(vista.txtBuscar.getText());
        validarCampos();
        actualizarContador();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        validarCampos();
        buscarEmpresa(vista.txtBuscar.getText());
        actualizarContador();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        validarCampos();
        buscarEmpresa(vista.txtBuscar.getText());

    }

    private void validarCampos() {
        if (!vista.txtNoProyecto.getText().trim().isEmpty()
                && !vista.txtTitulo.getText().trim().isEmpty()
                && !vista.txtDescripcion.getText().trim().isEmpty()
                && !vista.txtCandidatos.getText().trim().isEmpty()
                && !vista.txtEmpresa.getText().trim().isEmpty()) {

            vista.btnAnadir.setEnabled(true);
        } else {
            vista.btnAnadir.setEnabled(false);
        }

    }

   private Proyecto ingresarProyectoFormulario() {
    Proyecto proyecto = new Proyecto();

    try {
        proyecto.setId_priyecto(Integer.parseInt(vista.txtNoProyecto.getText().trim()));
        proyecto.setTitulo(vista.txtTitulo.getText().trim());
        proyecto.setDescripcion(vista.txtDescripcion.getText().trim());
        proyecto.setEspacios(Integer.parseInt(vista.txtCandidatos.getText().trim()));

        // Crear una sola empresa con el nombre ingresado en el campo txtEmpresa
        Empresa empresa = new Empresa();
        empresa.setNombre(vista.txtEmpresa.getText().trim());

        // Asignar el arreglo con una sola empresa al proyecto
        proyecto.setEmpresa(new Empresa[]{empresa});
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(vista, "Por favor ingresa valores numéricos válidos.");
        return null;
    }

    return proyecto;
}


    private void iniciarComponentes() {
        vista.btnAnadir.setEnabled(false);
        vista.btnDescartar.setEnabled(false);
        vista.btnGuardar.setEnabled(false);

        String[] columnas = {"No. proyecto", "Empresa", "Titulo", "Descripcion", "Espacio"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
        vista.jTable1.setModel(modelo);

        vista.btnOrden.setText("Ascendente");
    }

    private void configurarListener() {
        vista.btnAnadir.addActionListener(this);
        vista.btnDescartar.addActionListener(this);
        vista.btnGuardar.addActionListener(this);
        vista.btnFiltro.addActionListener(this);
        vista.btnOrden.addActionListener(this);

        vista.txtNoProyecto.addKeyListener(this);

        vista.txtNoProyecto.getDocument().addDocumentListener(this);
        vista.txtEmpresa.getDocument().addDocumentListener(this);
        vista.txtTitulo.getDocument().addDocumentListener(this);
        vista.txtDescripcion.getDocument().addDocumentListener(this);
        vista.txtCandidatos.getDocument().addDocumentListener(this);
        vista.txtBuscar.getDocument().addDocumentListener(this);

        vista.jTable1.getSelectionModel().addListSelectionListener(e -> {
            // Solo habilita si se seleccionó una fila y no es ajuste de selección
            if (!e.getValueIsAdjusting() && vista.jTable1.getSelectedRow() != -1) {
                vista.btnDescartar.setEnabled(true);
            }
        });

        // Desactiva si ya no hay selección o filas
        if (vista.jTable1.getRowCount() == 0 || vista.jTable1.getSelectedRow() == -1) {
            vista.btnDescartar.setEnabled(false);
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {
        Object src = e.getSource();
        if (src == vista.txtNoProyecto) {
            char c = e.getKeyChar();
            if (!Character.isDigit(c) || vista.txtNoProyecto.getText().length() >= 10) {
                e.consume(); // Bloquea el carácter
            }
        }
    }

    private void limpiarCampos() {
        vista.txtNoProyecto.setText("");
        vista.txtEmpresa.setText("");
        vista.txtTitulo.setText("");
        vista.txtDescripcion.setText("");
        vista.txtCandidatos.setText("");
    }

    private void limpiarTabla() {
        DefaultTableModel modelo = (DefaultTableModel) vista.jTable1.getModel();
        modelo.setRowCount(0);

        vista.btnGuardar.setEnabled(false);
        vista.btnDescartar.setEnabled(false);
        actualizarContador();
    }

    private void cambiarCategoria() {
        String[] categorias = {"Clave", "Empresa", "Titulo", "Espacios"};
        String actual = categoriaOrden.toLowerCase();
        int index = java.util.Arrays.asList(categorias).indexOf(actual);
        int siguiente = (index + 1) % categorias.length;

        categoriaOrden = categorias[siguiente];
        vista.btnFiltro.setText(categorias[siguiente].substring(0, 1).toUpperCase() + categorias[siguiente].substring(1));

        filtrarYOrdenar();
    }

    private void filtrarYOrdenar() {
        List<Proyecto> datosTabla = obtenerProyectosDeTabla();
        List<Proyecto> ordenadas;

        switch (categoriaOrden.toLowerCase()) {
            case "clave" ->
                ordenadas = datosTabla.stream()
                        .sorted((a, b) -> ordenAscendente
                        ? Integer.compare(a.getId_priyecto(), b.getId_priyecto())
                        : Integer.compare(b.getId_priyecto(), a.getId_priyecto()))
                        .toList();
            case "empresa" ->
                ordenadas = datosTabla.stream()
                        .sorted((a, b) -> ordenAscendente
                        ? Arrays.toString(a.getEmpresa()).compareToIgnoreCase(Arrays.toString(b.getEmpresa()))
                        : Arrays.toString(b.getEmpresa()).compareToIgnoreCase(Arrays.toString(a.getEmpresa())))
                        .toList();
            case "titulo" ->
                ordenadas = datosTabla.stream()
                        .sorted((a, b) -> ordenAscendente
                        ? a.getTitulo().compareToIgnoreCase(b.getTitulo())
                        : b.getTitulo().compareToIgnoreCase(a.getTitulo()))
                        .toList();
            case "espacios" ->
                ordenadas = datosTabla.stream()
                        .sorted((a, b) -> ordenAscendente
                        ? Integer.compare(a.getEspacios(), b.getEspacios())
                        : Integer.compare(b.getEspacios(), a.getEspacios()))
                        .toList();
            default ->
                ordenadas = datosTabla;
        }

        llenarTablaProyecto(ordenadas); // Usa el método correcto para llenar
    }

    private List<Proyecto> obtenerProyectosDeTabla() {
        DefaultTableModel modelo = (DefaultTableModel) vista.jTable1.getModel();
        List<Proyecto> proyectos = new ArrayList<>();

        for (int i = 0; i < modelo.getRowCount(); i++) {
            Proyecto pro = new Proyecto();
            pro.setId_priyecto(Integer.parseInt(modelo.getValueAt(i, 0).toString()));

            // El campo empresa es un arreglo, aquí solo lo parseamos como texto
            Empresa emp = new Empresa();
            emp.setNombre(modelo.getValueAt(i, 1).toString());
            pro.setEmpresa(new Empresa[]{emp}); // Se guarda como arreglo

            pro.setTitulo(modelo.getValueAt(i, 2).toString());
            pro.setDescripcion(modelo.getValueAt(i, 3).toString());
            pro.setEspacios(Integer.parseInt(modelo.getValueAt(i, 4).toString()));
            proyectos.add(pro);
        }

        return proyectos;
    }

    private void agregarTabla() {
        Proyecto proyecto = ingresarProyectoFormulario();

        DefaultTableModel modelo = (DefaultTableModel) vista.jTable1.getModel();
        modelo.addRow(new Object[]{
            proyecto.getId_priyecto(),
            proyecto.getTitulo(),
            proyecto.getDescripcion(),
            proyecto.getEspacios(),
            proyecto.getEmpresa()
        });

        proyectosTemporales.add(proyecto); // ← Aquí la agregas a la lista

        limpiarCampos();
        vista.btnAnadir.setEnabled(false);
        vista.btnGuardar.setEnabled(vista.jTable1.getRowCount() > 0);
        actualizarContador();
    }

    private void guardarEnBase() {
        DefaultTableModel modelo = (DefaultTableModel) vista.jTable1.getModel();
        int filas = modelo.getRowCount();

        if (filas == 0) {
            JOptionPane.showMessageDialog(vista, "No hay datos para guardar.");
            return;
        }

        boolean exitoTotal = true;

        for (int i = 0; i < filas; i++) {
            Empresa emp = new Empresa();
            emp.setId_empresa(Integer.parseInt(modelo.getValueAt(i, 0).toString()));
            emp.setNombre(modelo.getValueAt(i, 1).toString());
            emp.setDireccion(modelo.getValueAt(i, 2).toString());
            emp.setTelefono(modelo.getValueAt(i, 3).toString());
            emp.setCorreo(modelo.getValueAt(i, 4).toString());

            if (!proyecto.insertarProyecto()) {
                exitoTotal = false;
            }
        }

        if (exitoTotal) {
            JOptionPane.showMessageDialog(vista, "Todas las empresas fueron registradas.");
        } else {
            JOptionPane.showMessageDialog(vista, "Ocurrió un error al registrar algunas empresas.");
        }

        limpiarTabla(); // Limpia la tabla local para futuras cargas
        proyectosTemporales.clear(); // Limpia la lista temporal

    }

    private void descartarDato() {
        int filaSeleccionada = vista.jTable1.getSelectedRow();

        if (filaSeleccionada != -1) {
            Proyecto pro = proyectosTemporales.get(filaSeleccionada);
            proyectosTemporales.remove(pro); // ← Quitar de la lista también

            DefaultTableModel modelo = (DefaultTableModel) vista.jTable1.getModel();
            modelo.removeRow(filaSeleccionada);

            boolean hayFilas = vista.jTable1.getRowCount() > 0;
            vista.btnGuardar.setEnabled(hayFilas);
            vista.btnDescartar.setEnabled(hayFilas);
            actualizarContador();
        } else {
            JOptionPane.showMessageDialog(vista, "Seleccione una fila para eliminar.");
        }
    }

    private void buscarEmpresa(String texto) {
        List<Proyecto> filtradas;

        if (texto.trim().isEmpty()) {
            filtradas = new ArrayList<>(proyectosTemporales);
        } else {
            filtradas = proyectosTemporales.stream()
                    .filter(pro
                            -> String.valueOf(pro.getId_priyecto()).contains(texto)
                    || pro.getTitulo().toLowerCase().contains(texto.toLowerCase())
                    || pro.getDescripcion().toLowerCase().contains(texto.toLowerCase())
                    || String.valueOf(pro.getEspacios()).contains(texto)
                    || Arrays.toString(pro.getEmpresa()).toLowerCase().contains(texto.toLowerCase())
                    )
                    .toList();
        }

        llenarTablaProyecto(filtradas); // <- Este método lo definimos abajo
    }

    private void llenarTablaProyecto(List<Proyecto> lista) {
        String[] columnas = {"No. proyecto", "Empresa", "Título", "Descripción", "Espacios"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

        for (Proyecto p : lista) {
            modelo.addRow(new Object[]{
                p.getId_priyecto(),
                Arrays.toString(p.getEmpresa()),
                p.getTitulo(),
                p.getDescripcion(),
                p.getEspacios()
            });
        }

        vista.jTable1.setModel(modelo);
    }

    private void actualizarContador() {
        int total = vista.jTable1.getRowCount();
        vista.lblContador.setText("Empresas registradas: " + total);
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
