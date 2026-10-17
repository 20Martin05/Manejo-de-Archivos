package vista;

import java.awt.EventQueue;
import javax.swing.JFrame;

import controlador.LogicaVentana;
import vista.base.VentanaUI;

public class ventana extends VentanaUI {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
            try {
                ventana frame = new ventana();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
	}

	/**
	 * Create the frame.
	 */
	public ventana() {
		setTitle("GESTION DE CONTACTOS");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(contentPane);
		pack();
		setLocationRelativeTo(null);

		// Arreglo que contiene las categorías disponibles.
		String[] categorias = {
				"Elija una Categoria",
				"Familia",
				"Amigos",
				"Trabajo",
				"Universidad",
				"Clientes",
				"Emergencias",
				"Otros"
		};
		for (String categoria : categorias) {
			cmb_categoria.addItem(categoria);
		}

		// Instance el controlador para usar el delegado
        new LogicaVentana(this);
    }
}
