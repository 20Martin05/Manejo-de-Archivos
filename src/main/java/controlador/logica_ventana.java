package controlador;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import vista.ventana;
import modelo.*;

//Definición de la clase logica_ventana que implementa tres interfaces para manejar eventos.
public class logica_ventana implements ActionListener, ListSelectionListener, ItemListener {
	private ventana delegado; // Referencia a la ventana principal que contiene la GUI.
	private String nombres, email, telefono, categoria=""; // Variables para almacenar datos del contacto.
	private persona persona; // Objeto de tipo persona, que representa un contacto.
	private List<persona> contactos; // Lista de objetos persona que representa todos los contactos.
	private boolean favorito = false; // Booleano que indica si un contacto es favorito.

	// Constructor que inicializa la clase y configura los escuchadores de eventos para los componentes de la GUI.
	public logica_ventana(ventana delegado) {
		  // Asigna la ventana recibida como parámetro a la variable de instancia delegado.
	    this.delegado = delegado;
	    // Carga los contactos almacenados al inicializar.
	    cargarContactosRegistrados(); 
	    // Registra los ActionListener para los botones de la GUI.
	    this.delegado.btn_add.addActionListener(this);
	    this.delegado.btn_eliminar.addActionListener(this);
	    this.delegado.btn_modificar.addActionListener(this);
	    // Registra los ListSelectionListener para la lista de contactos.
	    this.delegado.lst_contactos.addListSelectionListener(this);
	    // Registra los ItemListener para el JComboBox de categoría y el JCheckBox de favoritos.
	    this.delegado.cmb_categoria.addItemListener(this);
	    this.delegado.chb_favorito.addItemListener(this);
	}

	// Método privado para inicializar las variables con los valores ingresados en la GUI.
	private void inicializacionCampos() {
		// Obtiene el texto ingresado en los campos de nombres, email y teléfono de la GUI.
		nombres = delegado.txt_nombres.getText();
		email = delegado.txt_email.getText();
		telefono = delegado.txt_telefono.getText();
	}

	// Método privado para cargar los contactos almacenados desde un archivo.
	private void cargarContactosRegistrados() {
		 try {
		        // Lee los contactos almacenados utilizando una instancia de personaDAO.
		        contactos = new personaDAO(new persona()).leerArchivo();
		        DefaultListModel modelo = new DefaultListModel();
		        
		        // INYECTAMOS EL ENCABEZADO (Fila 0)
		        String encabezado = String.format("%-10s%-40s%-40s%-40s%-40s", "ID", "NOMBRES", "TELÉFONO", "EMAIL", "CATEGORÍA");
		        modelo.addElement(encabezado);

		        // Agrega cada contacto al modelo de la lista
		        for (persona contacto : contactos) {
		            modelo.addElement(contacto.formatoLista());
		        }
		        // Establece el modelo actualizado en la lista de contactos de la GUI.
		        delegado.lst_contactos.setModel(modelo);
		    } catch (IOException e) {
		        // Muestra un mensaje de error si ocurre una excepción al cargar los contactos.
		        JOptionPane.showMessageDialog(delegado, "Existen problemas al cargar todos los contactos");
		    }
	}

	// Método privado para limpiar los campos de entrada en la GUI y reiniciar variables.
	private void limpiarCampos() {
		// Limpia los campos de nombres, email y teléfono en la GUI.
	    delegado.txt_nombres.setText("");
	    delegado.txt_telefono.setText("");
	    delegado.txt_email.setText("");
	    // Reinicia las variables de categoría y favorito.
	    categoria = "";
	    favorito = false;
	    // Desmarca la casilla de favorito y establece la categoría por defecto.
	    delegado.chb_favorito.setSelected(favorito);
	    delegado.cmb_categoria.setSelectedIndex(0);
	    // Reinicia las variables con los valores actuales de la GUI.
	    inicializacionCampos();
	    // Recarga los contactos en la lista de contactos de la GUI.
	    cargarContactosRegistrados();
	}

	// Método que maneja los eventos de acción (clic) en los botones.
	@Override
	public void actionPerformed(ActionEvent e) {
		inicializacionCampos(); // Inicializa las variables con los valores actuales de la GUI.

	    // Verifica si el evento proviene del botón "Agregar".
	    if (e.getSource() == delegado.btn_add) {
	        // Verifica si los campos de nombres, teléfono y email no están vacíos.
	        if ((!nombres.equals("")) && (!telefono.equals("")) && (!email.equals(""))) {
	            // Verifica si se ha seleccionado una categoría válida.
	            if ((!categoria.equals("Elija una Categoria")) && (!categoria.equals(""))) {
	                // Calcula el nuevo ID (como base de datos)
	                int nuevoId = 1;
	                if (contactos != null && !contactos.isEmpty()) {
	                    nuevoId = contactos.get(contactos.size() - 1).getId() + 1;
	                }
	                // Crea un nuevo objeto persona con los datos ingresados y el ID y lo guarda.
	                persona = new persona(nuevoId, nombres, telefono, email, categoria, favorito);
                    try {
                        new personaDAO(persona).escribirArchivo();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    // Limpia los campos después de agregar el contacto.
	                limpiarCampos();
	                // Muestra un mensaje de éxito.
	                JOptionPane.showMessageDialog(delegado, "Contacto Registrado!!!");
	            } else {
	                // Muestra un mensaje de advertencia si no se ha seleccionado una categoría válida.
	                JOptionPane.showMessageDialog(delegado, "Elija una Categoria!!!");
	            }
	        } else {
	            // Muestra un mensaje de advertencia si algún campo está vacío.
	            JOptionPane.showMessageDialog(delegado, "Todos los campos deben ser llenados!!!");
	        }
	    } else if (e.getSource() == delegado.btn_eliminar) {
			int index = delegado.lst_contactos.getSelectedIndex();
			// Ignoramos el índice 0 porque es el encabezado
			if (index > 0) {
				try {
					contactos.remove(index - 1);
					new personaDAO(new persona()).actualizarContactos(contactos);
					limpiarCampos();
					JOptionPane.showMessageDialog(delegado, "Eliminado con Éxito");

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(delegado, "Error al eliminar el contacto", "Error", JOptionPane.ERROR_MESSAGE);
                }

			} else {
				JOptionPane.showMessageDialog(delegado, "Por favor seleccione un contacto de la lista para eliminar.");
			}


		} else if (e.getSource() == delegado.btn_modificar) {
			int index = delegado.lst_contactos.getSelectedIndex();
			// Ignoramos el índice 0 porque es el encabezado
			if (index > 0) {
				if ((!nombres.equals("")) && (!telefono.equals("")) && (!email.equals(""))) {
					if ((!categoria.equals("Elija una Categoria")) && (!categoria.equals(""))) {
						try {
							int idOriginal = contactos.get(index - 1).getId();
							persona contratoModificado = new persona(idOriginal, nombres, telefono, email, categoria, favorito);

							contactos.set(index - 1, contratoModificado);
							new personaDAO(new persona()).actualizarContactos(contactos);
							limpiarCampos();
							JOptionPane.showMessageDialog(delegado, "Modificado con Éxito");
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(delegado, "Error al modificar el contacto", "Error", JOptionPane.ERROR_MESSAGE);
						}
					} else {
						JOptionPane.showMessageDialog(delegado, "Elija una Categoria!!!");
					}
				} else {
					JOptionPane.showMessageDialog(delegado, "Todos los campos deben ser llenados!!!");
				}
			} else {
				JOptionPane.showMessageDialog(delegado, "Por favor seleccione un contacto de la lista para modificar.");
			}
	    }
	}

	// Método que maneja los eventos de selección en la lista de contactos.
	@Override
	public void valueChanged(ListSelectionEvent e) {
		// Obtiene el índice del elemento seleccionado en la lista de contactos.
	    int index = delegado.lst_contactos.getSelectedIndex();
	    // Verifica si se ha seleccionado un índice válido en la lista.
	    if (index != -1) {
	        // Si hace clic en el encabezado (índice 0), quitamos la selección azul para que parezca un título real
	        if (index == 0) {
	            delegado.lst_contactos.clearSelection();
	        } else if (index > 0) {
	            cargarContacto(index - 1);
	        }
	    } 
	}

	// Método privado para cargar los datos del contacto seleccionado en los campos de la GUI.
	private void cargarContacto(int index) {
		// Establece el nombre del contacto en el campo de texto de nombres.
	    delegado.txt_nombres.setText(contactos.get(index).getNombre());
	    // Establece el teléfono del contacto en el campo de texto de teléfono.
	    delegado.txt_telefono.setText(contactos.get(index).getTelefono());
	    // Establece el correo electrónico del contacto en el campo de texto de correo electrónico.
	    delegado.txt_email.setText(contactos.get(index).getEmail());
	    // Establece el estado de favorito del contacto en el JCheckBox de favorito.
	    delegado.chb_favorito.setSelected(contactos.get(index).isFavorito());
	    // Establece la categoría del contacto en el JComboBox de categoría.
	    delegado.cmb_categoria.setSelectedItem(contactos.get(index).getCategoria());
	}

	// Método que maneja los eventos de cambio de estado en los componentes cmb_categoria y chb_favorito.
	@Override
	public void itemStateChanged(ItemEvent e) {
		// Verifica si el evento proviene del JComboBox de categoría.
	    if (e.getSource() == delegado.cmb_categoria) {
	        // Obtiene el elemento seleccionado en el JComboBox y lo convierte en una cadena.
	        categoria = delegado.cmb_categoria.getSelectedItem().toString();
	        // Actualiza la categoría seleccionada en la variable "categoria".
	    } else if (e.getSource() == delegado.chb_favorito) {
	        // Verifica si el evento proviene del JCheckBox de favorito.
	        favorito = delegado.chb_favorito.isSelected();
	        // Obtiene el estado seleccionado del JCheckBox y actualiza el estado de favorito en la variable "favorito".
	    }
	}
}
