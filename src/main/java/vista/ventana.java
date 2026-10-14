package vista;

import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JFrame;
import javax.swing.JList;

import controlador.logica_ventana;
import vista.base.VentanaUI;

public class ventana extends VentanaUI {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ventana frame = new ventana();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
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
		String[] categorias = {"Elija una Categoria", "Familia", "Amigos", "Trabajo"};
		for (String categoria : categorias) {
			cmb_categoria.addItem(categoria);
		}

		// Renderizador personalizado para poner el encabezado en negrita y evitar que se pinte de azul
		lst_contactos.setCellRenderer(new DefaultListCellRenderer() {
			@Override
			public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				java.awt.Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (index == 0) {
					c.setFont(c.getFont().deriveFont(Font.BOLD)); // Negrita para el encabezado
					if (isSelected) {
						setBackground(list.getBackground()); // Quita el fondo azul
						setForeground(list.getForeground()); // Quita las letras blancas
					}
				}
				return c;
			}
		});

		// Instanciar el controlador para usar el delegado
		logica_ventana lv = new logica_ventana(this);
	}
}
