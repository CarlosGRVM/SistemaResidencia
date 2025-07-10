/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import vista.*;
import modelo.*;

/**
 *
 * @author carlo
 */
public class CEmpresa implements ActionListener, KeyListener {

    private FormatoEmpresa vista;
    private Empresa empresa;

    public CEmpresa() {

        iniciarComponentes();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == vista.btnGuardar) {

        }
    }

    private boolean validarCampos() {
        if (vista.txtNoEmpresa.getText().trim().isEmpty()
                || vista.txtNombre.getText().trim().isEmpty()
                || vista.txtDireccion.getText().trim().isEmpty()
                || vista.txtTelefono.getText().trim().isEmpty()
                || vista.txtCorreo.getText().trim().isEmpty()) {
            return false;
        }

        return true;
    }

    private void ingresarEmpresa() {

    }

    private void iniciarComponentes() {
        vista.btnAnadir.setEnabled(false);
        vista.btnDescartar.setEnabled(false);
        vista.btnGuardar.setEnabled(false);

    }

    @Override
    public void keyTyped(KeyEvent e) {
        Object src = e.getSource();
        if (src == vista.txtTelefono) {
            char c = e.getKeyChar();
            if (!Character.isDigit(c) || vista.txtTelefono.getText().length() >= 10) {
                e.consume(); // Bloquea el carácter
            }
        } else if (src == vista.txtNombre) {
            char c = e.getKeyChar();
            if (!Character.isDigit(c) || vista.txtTelefono.getText().length() >= 10) {
                e.consume(); // Bloquea el carácter
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
