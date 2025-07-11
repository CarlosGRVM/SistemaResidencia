/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import modelo.ConexionSQL;
/**
 *
 * @author carlo
 */
public class Proyecto {
    
    private int id_priyecto;
    private String titulo;
    private String descripcion;
    private int espacios;
    private String disponible;
    private String tipo;
    private Empresa [] empresa;

    public Proyecto(int id_priyecto, String titulo, String descripcion, int espacios, String disponible, String tipo, Empresa[] empresa) {
        this.id_priyecto = id_priyecto;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.espacios = espacios;
        this.disponible = disponible;
        this.tipo = tipo;
        this.empresa = empresa;
    }

    public Proyecto() {
        
    }
    
    

    public int getId_priyecto() {
        return id_priyecto;
    }

    public void setId_priyecto(int id_priyecto) {
        this.id_priyecto = id_priyecto;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getEspacios() {
        return espacios;
    }

    public void setEspacios(int espacios) {
        this.espacios = espacios;
    }

    public String getDisponible() {
        return disponible;
    }

    public void setDisponible(String disponible) {
        this.disponible = disponible;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Empresa[] getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa[] empresa) {
        this.empresa = empresa;
    }

    @Override
    public String toString() {
        return "Proyecto{" + "id_priyecto=" + id_priyecto + ", titulo=" + titulo + ", descripcion=" + descripcion + ", espacios=" + espacios + ", disponible=" + disponible + ", tipo=" + tipo + ", empresa=" + empresa + '}';
    }
    
    
    public boolean insertarProyecto() {
    Connection conn = null;
    PreparedStatement stmt = null;

    try {
        conn = ConexionSQL.conectar(); // ← Asegúrate que esta clase/método existe
        String sql = "INSERT INTO proyecto (id_proyecto, titulo, descripcion, espacios, disponible, tipo) VALUES (?, ?, ?, ?, ?, ?)";
        stmt = conn.prepareStatement(sql);
        stmt.setInt(1, this.id_priyecto);
        stmt.setString(2, this.titulo);
        stmt.setString(3, this.descripcion);
        stmt.setInt(4, this.espacios);
        stmt.setString(5, this.disponible != null ? this.disponible : "Sí");
        stmt.setString(6, this.tipo != null ? this.tipo : "N/R");

        int filas = stmt.executeUpdate();
        return filas > 0;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    } finally {
        try {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    }
    
}
