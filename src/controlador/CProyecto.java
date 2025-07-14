package controlador;

import modelo.*;
import vista.FormatoProyecto;

import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;

public class CProyecto implements ActionListener, DocumentListener, ListSelectionListener {

    private final FormatoProyecto vista;
    private final Proyecto modeloProyecto;
    private final Empresa[] empresas;

    private String campoOrden = "id_proyecto";
    private boolean ordenAscendente = true;

    public CProyecto(FormatoProyecto vista, Empresa[] empresas) {
        this.vista = vista;
        this.empresas = empresas;
        this.modeloProyecto = new Proyecto();
        modeloProyecto.setEmpresas(empresas);

        inicializarVista();
        agregarEventos();
        mostrarProyectos();
    }

    private void inicializarVista() {
        vista.lblContador.setText("Proyectos registrados: 0");
        vista.btnAnadir.setEnabled(false);
        vista.btnDescartar.setEnabled(false);
    }

    private void agregarEventos() {
        vista.btnAnadir.addActionListener(this);
        vista.btnDescartar.addActionListener(this);
        vista.btnFiltro.addActionListener(this);
        vista.btnOrdenar.addActionListener(this);

        vista.txtTitulo.getDocument().addDocumentListener(this);
        vista.txtDescripcion.getDocument().addDocumentListener(this);
        vista.txtEspacios.getDocument().addDocumentListener(this);
        vista.txtEmpresa.getDocument().addDocumentListener(this);
        vista.txtBuscar.getDocument().addDocumentListener(this);

        vista.jTable1.getSelectionModel().addListSelectionListener(this);
    }

    private void validarCampos() {
        boolean camposLlenos = !vista.txtTitulo.getText().trim().isEmpty()
                && !vista.txtDescripcion.getText().trim().isEmpty()
                && !vista.txtEspacios.getText().trim().isEmpty()
                && !vista.txtEmpresa.getText().trim().isEmpty();

        boolean espaciosValidos;
        try {
            int esp = Integer.parseInt(vista.txtEspacios.getText().trim());
            espaciosValidos = esp > 0;
        } catch (NumberFormatException e) {
            espaciosValidos = false;
        }

        vista.btnAnadir.setEnabled(camposLlenos && espaciosValidos);
    }

    private Proyecto construirProyectoDesdeVista() {
        Proyecto p = new Proyecto();
        p.setId_proyecto(modeloProyecto.generarSiguienteId());
        p.setTitulo(vista.txtTitulo.getText().trim());
        p.setDescripcion(vista.txtDescripcion.getText().trim());
        p.setEspacios(Integer.parseInt(vista.txtEspacios.getText().trim()));
        p.setDisponible("Sí");
        p.setTipo("N/R");

        // Buscar empresa por nombre
        String nombreEmpresa = vista.txtEmpresa.getText().trim();
        for (Empresa e : empresas) {
            if (e.getNombre().equalsIgnoreCase(nombreEmpresa)) {
                p.setEmpresas(new Empresa[]{e});
                break;
            }
        }

        return p;
    }

    

    private void mostrarProyectos() {
        List<Proyecto> lista = modeloProyecto.obtenerTodos(empresas, campoOrden, ordenAscendente);
        DefaultTableModel modelo = new DefaultTableModel(
                new String[]{"ID", "Empresa", "Título", "Descripción", "Espacios", "Disponible"}, 0);

        for (Proyecto p : lista) {
            String nombreEmpresa = p.getEmpresas()[0] != null ? p.getEmpresas()[0].getNombre() : "N/D";
            modelo.addRow(new Object[]{
                p.getId_proyecto(),
                nombreEmpresa,
                p.getTitulo(),
                p.getDescripcion(),
                p.getEspacios(),
                p.getDisponible()
            });
        }

        vista.jTable1.setModel(modelo);
        vista.lblContador.setText("Proyectos registrados: " + lista.size());
    }

    private void insertarProyecto() {
        Proyecto nuevo = construirProyectoDesdeVista();
        if (nuevo.getEmpresas() == null || nuevo.getEmpresas()[0] == null) {
            JOptionPane.showMessageDialog(vista, "Empresa no encontrada");
            return;
        }

        if (nuevo.insertarProyecto(nuevo.getEmpresas()[0].getId_empresa())) {
            JOptionPane.showMessageDialog(vista, "Proyecto registrado");
            limpiarCampos();
            mostrarProyectos();
        } else {
            JOptionPane.showMessageDialog(vista, "Error al registrar proyecto");
        }
    }

    private void limpiarCampos() {
        vista.txtTitulo.setText("");
        vista.txtDescripcion.setText("");
        vista.txtEspacios.setText("");
        vista.txtEmpresa.setText("");
    }

    private void eliminarProyectoSeleccionado() {
        int fila = vista.jTable1.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Selecciona un proyecto primero.");
            return;
        }

        int idProyecto = (int) vista.jTable1.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(vista, "¿Eliminar este proyecto?", "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (modeloProyecto.eliminarProyecto(idProyecto)) {
                JOptionPane.showMessageDialog(vista, "Proyecto eliminado");
                mostrarProyectos();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al eliminar");
            }
        }
    }

    private void cambiarOrden() {
        ordenAscendente = !ordenAscendente;
        vista.btnOrdenar.setText(ordenAscendente ? "Ascendente" : "Descendente");
        mostrarProyectos();
    }

    private void cambiarFiltro() {
        String actual = vista.btnFiltro.getText();
        switch (actual) {
            case "Empresa" -> {
                campoOrden = "id_empresa";
                vista.btnFiltro.setText("Título");
            }
            case "Título" -> {
                campoOrden = "titulo";
                vista.btnFiltro.setText("Espacios");
            }
            case "Espacios" -> {
                campoOrden = "espacios";
                vista.btnFiltro.setText("Empresa");
            }
        }
        mostrarProyectos();
    }

    // Eventos
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == vista.btnAnadir) {
            insertarProyecto();
        } else if (src == vista.btnDescartar) {
            eliminarProyectoSeleccionado();
        } else if (src == vista.btnOrdenar) {
            cambiarOrden();
        } else if (src == vista.btnFiltro) {
            cambiarFiltro();
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        validarCampos();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        validarCampos();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        validarCampos();
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            vista.btnDescartar.setEnabled(vista.jTable1.getSelectedRow() != -1);
        }
    }
}
