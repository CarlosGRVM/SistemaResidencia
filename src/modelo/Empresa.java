/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author carlo
 */
public class Empresa {

    private int id_empresa;
    private String nombre;
    private String direccion;
    private String telefono;
    private String correo;
    private Connection conexion;

    public Empresa(int id_empresa, String nombre, String direccion, String telefono, String correo) {
        this.id_empresa = id_empresa;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correo = correo;
        this.conexion = ConexionSQL.conectar();
    }

    public Empresa() {
        this.conexion = ConexionSQL.conectar();
    }

    public int getId_empresa() {
        return id_empresa;
    }

    public void setId_empresa(int id_empresa) {
        this.id_empresa = id_empresa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @Override
    public String toString() {
        return "Empresa{" + "id_empresa=" + id_empresa + ", nombre=" + nombre + ", direccion=" + direccion + ", telefono=" + telefono + ", correo=" + correo + '}';
    }

    //METODOS DE LA BASE DE DATOS(CRUD)
    public boolean insertarEmpresa(Empresa empresa) {
        String sql = "INSERT into empresa (id_empresa, nombre, direccion, telefono, correo)"
                + " VALUES (?,?,?,?,?)";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, empresa.getId_empresa());
            stmt.setString(2, empresa.getNombre());
            stmt.setString(3, empresa.getDireccion());
            stmt.setString(4, empresa.getTelefono());
            stmt.setString(5, empresa.getCorreo());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al insertar empresa: " + e.getMessage());
            return false;
        }
    }

    public List<Empresa> obtenerTodos() {
        List<Empresa> empresas = new ArrayList();
        String sql = "Select * from empresa";

        try (PreparedStatement stmt = conexion.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Empresa empresa = new Empresa();
                empresa.setId_empresa(rs.getInt("id_empresa"));
                empresa.setNombre(rs.getString("Nombre"));
                empresa.setDireccion(rs.getString("Direccion"));
                empresa.setTelefono(rs.getString("Telefono"));
                empresa.setCorreo(rs.getString("Correo"));

                empresas.add(empresa);
            }
        } catch (SQLException e) {
            System.err.println("⚠️ Error al obtener usuarios: " + e.getMessage());
        }
        return empresas;
    }
}
