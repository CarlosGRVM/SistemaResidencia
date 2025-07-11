package controlador;

import Modelos.Conexion;
import vista.inicioSesion;
import java.sql.*;

public class Controlador_inicio_de_sesion {

    public static void CrearUsuario(inicioSesion vista) {
    String correo = vista.usuario.getText(); // Aquí va el correo digitado
    String contrasena = new String(vista.contrasena.getPassword());

    try (Connection conn = Conexion.conectar()) {
        String sql = "SELECT * FROM usuario WHERE correo = ? AND contrasena = SHA2(?, 256)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, correo);
        ps.setString(2, contrasena);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            javax.swing.JOptionPane.showMessageDialog(null, "Inicio de sesión exitoso");
            // Abre panel principal o siguiente ventana
        } else {
            javax.swing.JOptionPane.showMessageDialog(null, "Correo o contraseña incorrectos");
        }
    } catch (SQLException e) {
        javax.swing.JOptionPane.showMessageDialog(null, "Error de conexión: " + e.getMessage());
    }
}

}
