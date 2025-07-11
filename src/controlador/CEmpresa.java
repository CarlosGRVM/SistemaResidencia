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
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import vista.*;
import modelo.*;

/**
 *
 * @author carlo
 */
public class CEmpresa implements ActionListener, KeyListener, DocumentListener {

    private FormatoEmpresa vista;
    private Empresa empresa;
    private String categoriaOrden = "clave";
    private boolean ordenAscendente = true;
    private List<Empresa> empresasTemporales = new ArrayList<>();

    public CEmpresa(FormatoEmpresa vista, Empresa empresa) {

        this.vista = vista;
        this.empresa = empresa;

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
    public void keyTyped(KeyEvent e) {
        Object src = e.getSource();
        if (src == vista.txtTelefono) {
            char c = e.getKeyChar();
            if (!Character.isDigit(c) || vista.txtTelefono.getText().length() >= 10) {
                e.consume(); // Bloquea el carácter
            }
        } else if (src == vista.txtNoEmpresa) {
            char c = e.getKeyChar();
            if (!Character.isDigit(c) || vista.txtNoEmpresa.getText().length() >= 10) {
                e.consume(); // Bloquea el carácter
            }
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
        if (!vista.txtNoEmpresa.getText().trim().isEmpty()
                && !vista.txtNombre.getText().trim().isEmpty()
                && !vista.txtDireccion.getText().trim().isEmpty()
                && !vista.txtTelefono.getText().trim().isEmpty()
                && !vista.txtCorreo.getText().trim().isEmpty()) {

            vista.btnAnadir.setEnabled(true);
        } else {
            vista.btnAnadir.setEnabled(false);
        }

    }

    private Empresa ingresarEmpresaFormulario() {

        Empresa empresa = new Empresa();
        empresa.setId_empresa(Integer.parseInt(vista.txtNoEmpresa.getText().trim()));
        empresa.setNombre(vista.txtNombre.getText().trim());
        empresa.setDireccion(vista.txtDireccion.getText().trim());
        empresa.setTelefono(vista.txtTelefono.getText().trim());
        empresa.setCorreo(vista.txtCorreo.getText().trim());

        return empresa;
    }

    private void iniciarComponentes() {
        vista.btnAnadir.setEnabled(false);
        vista.btnDescartar.setEnabled(false);
        vista.btnGuardar.setEnabled(false);

        String[] columnas = {"No. empresa", "Nombre", "Direccion", "Telefono", "Correo"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
        vista.jTable1.setModel(modelo);

        vista.btnOrden.setText("Ascendente");
    }

    private void consultar() {
        List<Empresa> empresa = this.empresa.obtenerTodos();
        llenarTablaEmpresa(empresa);
    }

    private void llenarTablaEmpresa(List<Empresa> empresas) {
        String[] columnas = {"No. empresa", "Nombre", "Direccion", "Telefono", "Correo"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

        for (Empresa e : empresas) {
            modelo.addRow(new Object[]{
                e.getId_empresa(), e.getNombre(), e.getDireccion(), e.getTelefono(),
                e.getCorreo()
            });
        }

        vista.jTable1.setModel(modelo);
        vista.jTable1.setVisible(true);

    }

    private void configurarListener() {
        vista.btnAnadir.addActionListener(this);
        vista.btnDescartar.addActionListener(this);
        vista.btnGuardar.addActionListener(this);
        vista.btnFiltro.addActionListener(this);
        vista.btnOrden.addActionListener(this);

        vista.txtNoEmpresa.addKeyListener(this);
        vista.txtTelefono.addKeyListener(this);

        vista.txtNoEmpresa.getDocument().addDocumentListener(this);
        vista.txtNombre.getDocument().addDocumentListener(this);
        vista.txtDireccion.getDocument().addDocumentListener(this);
        vista.txtTelefono.getDocument().addDocumentListener(this);
        vista.txtCorreo.getDocument().addDocumentListener(this);
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

    private void limpiarCampos() {
        vista.txtNoEmpresa.setText("");
        vista.txtNombre.setText("");
        vista.txtDireccion.setText("");
        vista.txtTelefono.setText("");
        vista.txtCorreo.setText("");
    }

    private void limpiarTabla() {
        DefaultTableModel modelo = (DefaultTableModel) vista.jTable1.getModel();
        modelo.setRowCount(0);

        vista.btnGuardar.setEnabled(false);
        vista.btnDescartar.setEnabled(false);
        actualizarContador();
    }

    private void cambiarCategoria() {
        String[] categorias = {"clave", "nombre", "direccion", "telefono", "correo"};
        String actual = categoriaOrden.toLowerCase();
        int index = java.util.Arrays.asList(categorias).indexOf(actual);
        int siguiente = (index + 1) % categorias.length;

        categoriaOrden = categorias[siguiente];
        vista.btnFiltro.setText(categorias[siguiente].substring(0, 1).toUpperCase() + categorias[siguiente].substring(1));

        filtrarYOrdenar();
    }

    private void filtrarYOrdenar() {
        List<Empresa> datosTabla = obtenerEmpresasDeTabla();
        List<Empresa> ordenadas;

        switch (categoriaOrden.toLowerCase()) {
            case "clave" ->
                ordenadas = datosTabla.stream()
                        .sorted((a, b) -> ordenAscendente
                        ? Integer.compare(a.getId_empresa(), b.getId_empresa())
                        : Integer.compare(b.getId_empresa(), a.getId_empresa()))
                        .toList();
            case "nombre" ->
                ordenadas = datosTabla.stream()
                        .sorted((a, b) -> ordenAscendente
                        ? a.getNombre().compareToIgnoreCase(b.getNombre())
                        : b.getNombre().compareToIgnoreCase(a.getNombre()))
                        .toList();
            case "direccion" ->
                ordenadas = datosTabla.stream()
                        .sorted((a, b) -> ordenAscendente
                        ? a.getDireccion().compareToIgnoreCase(b.getDireccion())
                        : b.getDireccion().compareToIgnoreCase(a.getDireccion()))
                        .toList();
            case "telefono" ->
                ordenadas = datosTabla.stream()
                        .sorted((a, b) -> ordenAscendente
                        ? a.getTelefono().compareToIgnoreCase(b.getTelefono())
                        : b.getTelefono().compareToIgnoreCase(a.getTelefono()))
                        .toList();
            case "correo" ->
                ordenadas = datosTabla.stream()
                        .sorted((a, b) -> ordenAscendente
                        ? a.getCorreo().compareToIgnoreCase(b.getCorreo())
                        : b.getCorreo().compareToIgnoreCase(a.getCorreo()))
                        .toList();
            default ->
                ordenadas = datosTabla;
        }

        llenarTablaEmpresa(ordenadas);
    }

    private List<Empresa> obtenerEmpresasDeTabla() {
        DefaultTableModel modelo = (DefaultTableModel) vista.jTable1.getModel();
        List<Empresa> empresas = new ArrayList<>();

        for (int i = 0; i < modelo.getRowCount(); i++) {
            Empresa emp = new Empresa();
            emp.setId_empresa(Integer.parseInt(modelo.getValueAt(i, 0).toString()));
            emp.setNombre(modelo.getValueAt(i, 1).toString());
            emp.setDireccion(modelo.getValueAt(i, 2).toString());
            emp.setTelefono(modelo.getValueAt(i, 3).toString());
            emp.setCorreo(modelo.getValueAt(i, 4).toString());
            empresas.add(emp);
        }

        return empresas;
    }

    private void agregarTabla() {
        Empresa empresa = ingresarEmpresaFormulario();

        DefaultTableModel modelo = (DefaultTableModel) vista.jTable1.getModel();
        modelo.addRow(new Object[]{
            empresa.getId_empresa(),
            empresa.getNombre(),
            empresa.getDireccion(),
            empresa.getTelefono(),
            empresa.getCorreo()
        });

        empresasTemporales.add(empresa); // ← Aquí la agregas a la lista

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

            if (!empresa.insertarEmpresa(emp)) {
                exitoTotal = false;
            }
        }

        if (exitoTotal) {
            JOptionPane.showMessageDialog(vista, "Todas las empresas fueron registradas.");
        } else {
            JOptionPane.showMessageDialog(vista, "Ocurrió un error al registrar algunas empresas.");
        }

        limpiarTabla(); // Limpia la tabla local para futuras cargas
        empresasTemporales.clear(); // Limpia la lista temporal

    }

    private void descartarDato() {
        int filaSeleccionada = vista.jTable1.getSelectedRow();

        if (filaSeleccionada != -1) {
            Empresa emp = empresasTemporales.get(filaSeleccionada);
            empresasTemporales.remove(emp); // ← Quitar de la lista también

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
        List<Empresa> filtradas;

        if (texto.trim().isEmpty()) {
            filtradas = new ArrayList<>(empresasTemporales);
        } else {
            filtradas = empresasTemporales.stream()
                    .filter(emp
                            -> String.valueOf(emp.getId_empresa()).contains(texto)
                    || emp.getNombre().toLowerCase().contains(texto.toLowerCase())
                    || emp.getDireccion().toLowerCase().contains(texto.toLowerCase())
                    || emp.getTelefono().contains(texto)
                    || emp.getCorreo().toLowerCase().contains(texto.toLowerCase())
                    )
                    .toList();
        }

        llenarTablaEmpresa(filtradas);
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
