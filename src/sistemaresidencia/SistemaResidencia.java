package sistemaresidencia;

import controlador.CProyecto;
import modelo.Proyecto;
import vista.FormatoProyecto;

public class SistemaResidencia {

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            Proyecto modelo = new Proyecto(); // Modelo
            FormatoProyecto vista = new FormatoProyecto(); // Vista
            CProyecto controlador = new CProyecto(vista, modelo); // Controlador

            vista.setVisible(true);
            vista.setLocationRelativeTo(null); // Centrar la ventana
        });

        // Ruta de la imagen a probar
        String rutaImagen = "/vista/imagenes/icono.png";

        // Prueba de carga de imagen
        java.net.URL url = SistemaResidencia.class.getResource(rutaImagen);

        if (url != null) {
            System.out.println("✅ Imagen encontrada: " + url.toExternalForm());
        } else {
            System.out.println("❌ Imagen NO encontrada: " + rutaImagen + "\nRevisa la ruta o la carpeta en el classpath.");
        }
    }

}
