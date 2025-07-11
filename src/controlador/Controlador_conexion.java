/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
public class Controlador_conexion {

    static String bd = "residencia";
    static String login = "root";
    static String password = "heryodi123";
    static String url = "jdbc:mysql://localhost/" + bd;

    Connection conn = null;
    ResultSet resultSet = null;
    public Controlador_conexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, login, password );
            if (conn != null) {
                System.out.println("Se ha establecido conexion con la BDD " + bd);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public Connection getConnection() {
        return conn;
    }
    public void desconectar() {
        conn = null;
    }
}
