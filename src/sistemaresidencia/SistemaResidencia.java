package sistemaresidencia;

import modelo.*;
import vista.*;
import controlador.*;

public class SistemaResidencia {

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {

            // Crear modelo de empresas para pasar al controlador de proyectos
            Empresa modeloEmpresa = new Empresa();
            Empresa[] arregloEmpresas = modeloEmpresa.obtenerTodo().toArray(new Empresa[0]);

            // Crear y mostrar vista de proyectos
            FormatoProyecto vistaProyecto = new FormatoProyecto();
            CProyecto controladorProyecto = new CProyecto(vistaProyecto, arregloEmpresas);

            vistaProyecto.setVisible(true);
            vistaProyecto.setLocationRelativeTo(null); // Centrar la ventana
        });
    }
}
