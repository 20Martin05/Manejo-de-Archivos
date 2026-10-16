package controlador;

import java.awt.Font;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import vista.ventana;
import modelo.*;

//Definición de la clase lógica_ventana que implementa tres interfaces para manejar eventos.
public class logica_ventana implements ActionListener, ListSelectionListener, ItemListener {
	private final ventana delegado; // Referencia a la ventana principal que contiene la GUI.
	private String nombres, email, telefono, categoria=""; // Variables para almacenar datos del contacto.
    private List<persona> contactos; // Lista de objetos persona que representa todos los contactos.
	private boolean favorito = false; // Booleano que indica si un contacto es favorito.
	private TableRowSorter<DefaultTableModel> sorter; // Sorter y filtro para la JTable

	// Constructor que inicializa la clase y configura los escuchadores de eventos para los componentes de la GUI.
	public logica_ventana(ventana delegado) {
	    // Asigna la ventana recibida como parámetro a la variable de instancia delegado.
	    this.delegado = delegado;

	    // Configuración inicial de la tabla de contactos
	    this.delegado.lst_contactos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    this.delegado.lst_contactos.setRowHeight(24);
	    this.delegado.lst_contactos.setFont(new Font("Tahoma", Font.PLAIN, 14));
	    if (this.delegado.lst_contactos.getTableHeader() != null) {
	        this.delegado.lst_contactos.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 14));
	    }
	    this.delegado.lst_contactos.getSelectionModel().addListSelectionListener(this);

	    // Carga los contactos almacenados al inicializar.
	    cargarContactosRegistrados(); 

	    // Registra los ActionListener para los botones de la GUI.
	    this.delegado.btn_add.addActionListener(this);
	    this.delegado.btn_eliminar.addActionListener(this);
	    this.delegado.btn_modificar.addActionListener(this);
	    this.delegado.btn_exportar.addActionListener(this);
		this.delegado.btn_actualizar_estadisticas.addActionListener(this);
		this.delegado.btn_limpiar_estadisticas.addActionListener(this);
		this.delegado.btn_exportar_reporte.addActionListener(this);

	    // Registra los ItemListener para el JComboBox de categoría y el JCheckBox de favoritos.
	    this.delegado.cmb_categoria.addItemListener(this);
	    this.delegado.chb_favorito.addItemListener(this);

	    // Filtro en tiempo real para el campo de búsqueda por nombre
	    this.delegado.txt_buscar.getDocument().addDocumentListener(new DocumentListener() {
	        @Override
	        public void insertUpdate(DocumentEvent e) {
	            filtrar();
	        }

	        @Override
	        public void removeUpdate(DocumentEvent e) {
	            filtrar();
	        }

	        @Override
	        public void changedUpdate(DocumentEvent e) {
	            filtrar();
	        }
	    });
	}

	// Método privado para inicializar las variables con los valores ingresados en la GUI.
	private void inicializacionCampos() {
		// Obtiene el texto ingresado en los campos de nombres, email y teléfono de la GUI.
		nombres = delegado.txt_nombres.getText();
		email = delegado.txt_email.getText();
		telefono = delegado.txt_telefono.getText();
	}

	// Método privado para filtrar los contactos por nombre en tiempo real
	private void filtrar() {
	    if (sorter == null) return;
	    String texto = delegado.txt_buscar.getText().trim();
	    if (texto.isEmpty()) {
	        sorter.setRowFilter(null);
	    } else {
	        // Filtra por la columna NOMBRES (índice 1), insensible a mayúsculas y minúsculas
	        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(texto), 1));
	    }
	}

	// Método privado para cargar los contactos almacenados desde un archivo.
	private void cargarContactosRegistrados() {
		try {
			// Lee los contactos almacenados utilizando una instancia de personaDAO.
			contactos = new personaDAO(new persona()).leerArchivo();

			// Columnas de la tabla
			DefaultTableModel modelo = columnasDeLaTabla();

			// Agrega cada contacto como una fila en la tabla
			for (persona contacto : contactos) {
				modelo.addRow(new Object[]{
					contacto.getId(),
					contacto.getNombre(),
					contacto.getTelefono(),
					contacto.getEmail(),
					contacto.getCategoria(),
					contacto.isFavorito() ? "Sí" : "No"
				});
			}

			// Asigna el modelo y configura el TableRowSorter
			delegado.lst_contactos.setModel(modelo);
			sorter = new TableRowSorter<>(modelo);
			delegado.lst_contactos.setRowSorter(sorter);

			// Configura anchos preferidos de las columnas
			if (delegado.lst_contactos.getColumnModel().getColumnCount() >= 6) {
				delegado.lst_contactos.getColumnModel().getColumn(0).setPreferredWidth(50);
				delegado.lst_contactos.getColumnModel().getColumn(0).setMaxWidth(70);
				delegado.lst_contactos.getColumnModel().getColumn(1).setPreferredWidth(160);
				delegado.lst_contactos.getColumnModel().getColumn(2).setPreferredWidth(100);
				delegado.lst_contactos.getColumnModel().getColumn(3).setPreferredWidth(180);
				delegado.lst_contactos.getColumnModel().getColumn(4).setPreferredWidth(110);
				delegado.lst_contactos.getColumnModel().getColumn(5).setPreferredWidth(80);
				delegado.lst_contactos.getColumnModel().getColumn(5).setMaxWidth(90);
			}

			// Replica filtro de búsqueda si hay texto ingresado
			filtrar();

			barraProgreso();
			new Thread(() -> {
				try {
					int total = contactos.size();
					int pausa = total > 0 ? Math.max(10, 1000 / total) : 20;
					for (int i = 0; i < total; i++) {
						int progreso = i + 1;
						Thread.sleep(pausa); // Animación fluida de aproximadamente 1 segundo en total
						javax.swing.SwingUtilities.invokeLater(() -> delegado.barra.setValue(progreso));
					}
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}).start();

		} catch (IOException e) {
			// Muestra un mensaje de error si ocurre una excepción al cargar los contactos.
			JOptionPane.showMessageDialog(delegado, "Existen problemas al cargar todos los contactos");
		}
	}

	private static DefaultTableModel columnasDeLaTabla() {
		String[] columnas = {"ID", "NOMBRES", "TELÉFONO", "EMAIL", "CATEGORÍA", "FAVORITO"};

		// Modelo con celdas no editables directamente y tipo Integer para ID (ordena numéricamente)
        return new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Integer.class;
                }
                return String.class;
            }
        };
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
	    delegado.chb_favorito.setSelected(false);
	    delegado.cmb_categoria.setSelectedIndex(0);
	    // Limpia la selección en la tabla
	    delegado.lst_contactos.clearSelection();
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
	        if ((!nombres.isEmpty()) && (!telefono.isEmpty()) && (!email.isEmpty())) {
	            // Verifica si se ha seleccionado una categoría válida.
	            if ((!categoria.equals("Elija una Categoria")) && (!categoria.isEmpty())) {
	                // Calcula el nuevo ID (como base de datos)
	                int nuevoId = 1;
	                if (contactos != null && !contactos.isEmpty()) {
	                    nuevoId = contactos.getLast().getId() + 1;
	                }
	                // Crea un nuevo objeto persona con los datos ingresados y el ID y lo guarda.
                    // Objeto de tipo persona, que representa un contacto.
                    persona persona = new persona(nuevoId, nombres, telefono, email, categoria, favorito);
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
			int filaVista = delegado.lst_contactos.getSelectedRow();
			if (filaVista != -1) {
				int filaModelo = delegado.lst_contactos.convertRowIndexToModel(filaVista);
				try {
					contactos.remove(filaModelo);
					new personaDAO(new persona()).actualizarContactos(contactos);
					limpiarCampos();
					JOptionPane.showMessageDialog(delegado, "Eliminado con Éxito");

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(delegado, "Error al eliminar el contacto", "Error", JOptionPane.ERROR_MESSAGE);
                }

			} else {
				JOptionPane.showMessageDialog(delegado, "Por favor seleccione un contacto de la tabla para eliminar.");
			}


		} else if (e.getSource() == delegado.btn_modificar) {
			int filaVista = delegado.lst_contactos.getSelectedRow();
			if (filaVista != -1) {
				int filaModelo = delegado.lst_contactos.convertRowIndexToModel(filaVista);
				if ((!nombres.isEmpty()) && (!telefono.isEmpty()) && (!email.isEmpty())) {
					if ((!categoria.equals("Elija una Categoria")) && (!categoria.isEmpty())) {
						try {
							int idOriginal = contactos.get(filaModelo).getId();
							persona contactoModificado = new persona(idOriginal, nombres, telefono, email, categoria, favorito);

							contactos.set(filaModelo, contactoModificado);
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
				JOptionPane.showMessageDialog(delegado, "Por favor seleccione un contacto de la tabla para modificar.");
			}
		} else if (e.getSource() == delegado.btn_exportar) {
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Exportar Contactos a CSV");
			chooser.setSelectedFile(new File("contactos_exportados.csv"));

			if (chooser.showSaveDialog(delegado) == JFileChooser.APPROVE_OPTION) {
				File destino = chooser.getSelectedFile();
				if (!destino.getName().toLowerCase().endsWith(".csv")) {
					destino = new File(destino.getAbsolutePath() + ".csv");
				}
				try {
					new personaDAO(new persona()).exportarCSV(destino, contactos);
					JOptionPane.showMessageDialog(delegado, "Exportado exitosamente en:\n" + destino.getAbsolutePath());
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(delegado, "Error al exportar archivo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
        } else if (e.getSource() == delegado.btn_actualizar_estadisticas) {
            calcularYMostrarEstadisticas(contactos);

        } else if (e.getSource() == delegado.btn_limpiar_estadisticas) {
            limpiarTablaEstadisticas();

        } else if (e.getSource() == delegado.btn_exportar_reporte) {
            exportarReporteEstadisticas(contactos, delegado);
		}
	}

	// Método que maneja los eventos de selección en la tabla de contactos.
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) {
			int filaVista = delegado.lst_contactos.getSelectedRow();
			if (filaVista >= 0 && filaVista < delegado.lst_contactos.getRowCount()) {
				int filaModelo = delegado.lst_contactos.convertRowIndexToModel(filaVista);
				if (contactos != null && filaModelo >= 0 && filaModelo < contactos.size()) {
					cargarContacto(filaModelo);
				}
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
	        categoria = Objects.requireNonNull(delegado.cmb_categoria.getSelectedItem()).toString();
	        // Actualiza la categoría seleccionada en la variable "categoria".
	    } else if (e.getSource() == delegado.chb_favorito) {
	        // Verifica si el evento proviene del JCheckBox de favorito.
	        favorito = delegado.chb_favorito.isSelected();
	        // Obtiene el estado seleccionado del JCheckBox y actualiza el estado de favorito en la variable "favorito".
	    }
	}

	public void barraProgreso() {
		int totalContacto = contactos != null ? contactos.size() : 0;
		delegado.barra.setMinimum(0);
		delegado.barra.setMaximum(totalContacto);
		delegado.barra.setValue(0);
	}

	public String calcularYMostrarEstadisticas(List<persona> contactos) {
		if (contactos == null || contactos.isEmpty()) {
			limpiarTablaEstadisticas();
			return "No hay contactos disponibles para generar el reporte.";
		}

		int total = contactos.size();
		int fav = 0, noFav = 0;
		Map<String, Integer> mapaCategorias = new LinkedHashMap<>();

		for (persona p : contactos) {
			if (p == null) continue;

			if (p.isFavorito()) {
				fav++;
			} else {
				noFav++;
			}

			String cat = (p.getCategoria() != null && !p.getCategoria().trim().isEmpty())
					? p.getCategoria().trim()
					: "Sin Categoría";

			if (cat.equalsIgnoreCase("Amigo")) {
				cat = "Amigos";
			}

			mapaCategorias.put(cat, mapaCategorias.getOrDefault(cat, 0) + 1);
		}

		double porcFav = (fav * 100.0) / total;
		double porcNoFav = (noFav * 100.0) / total;

		// Anchos para separadores de sección
		String lineaDoble = "=========================================================================";
		String lineaSimple = "-------------------------------------------------------------------------";

		StringBuilder sb = new StringBuilder();

		// 1. Encabezado principal
		sb.append(lineaDoble).append("\n");
		sb.append(String.format("%-73s\n", "                    REPORTE ESTADÍSTICO DE CONTACTOS"));
		sb.append(lineaDoble).append("\n\n");

		// 2. Total general
		sb.append(String.format("- TOTAL DE CONTACTOS REGISTRADOS: %d\n\n\n", total));

		// 3. Desglose por favoritos
		sb.append(lineaSimple).append("\n");
		sb.append("  DESGLOSE POR ESTADO DE FAVORITOS:\n");
		sb.append(lineaSimple).append("\n");
		sb.append(String.format("- Favoritos:     %-5d (%.1f%%)\n\n", fav, porcFav));
		sb.append(String.format("- No Favoritos:  %-5d (%.1f%%)\n\n\n", noFav, porcNoFav));

		// 4. Distribución por categorías
		sb.append(lineaSimple).append("\n");
		sb.append("  DISTRIBUCIÓN POR CATEGORÍAS:\n");
		sb.append(lineaSimple).append("\n");

		for (Map.Entry<String, Integer> entry : mapaCategorias.entrySet()) {
			double porcCat = (entry.getValue() * 100.0) / total;
			sb.append(String.format("- %-18s: %-5d (%.1f%%)\n\n", entry.getKey(), entry.getValue(), porcCat));
		}

		sb.append("\n");

		// 5. Pie del reporte con fecha/hora
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String fechaActual = sdf.format(new Date());

		sb.append(lineaDoble).append("\n");
		sb.append(String.format("  Fecha de cálculo: %s\n", fechaActual));
		sb.append(lineaDoble).append("\n");

		String reporteTexto = sb.toString();

		// Asignamos directamente la cadena al JTextArea de Swing
		if (delegado.tbl_estadisticas != null) {
			// Se recomienda fuente monospaciada para que las columnas mantengan la alineación
			delegado.tbl_estadisticas.setFont(new Font("Monospaced", Font.PLAIN, 12));
			delegado.tbl_estadisticas.setText(reporteTexto);
			delegado.tbl_estadisticas.setCaretPosition(0); // Scroll al inicio
		}

		return reporteTexto;
	}

	public void limpiarTablaEstadisticas() {
		if (delegado.tbl_estadisticas != null) {
			delegado.tbl_estadisticas.setText("");
		}
	}

    public void exportarReporteEstadisticas(List<persona> contactos, JFrame padre) {
        String contenidoReporte = calcularYMostrarEstadisticas(contactos);

        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("reporte_estadistico.txt"));

        if (chooser.showSaveDialog(padre) == JFileChooser.APPROVE_OPTION) {
            File destino = chooser.getSelectedFile();

            try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destino), StandardCharsets.UTF_8))) {
                bw.write(contenidoReporte);
                JOptionPane.showMessageDialog(padre, "Reporte exportado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(padre, "Error al escribir el archivo: " + ex.getMessage(), "Error I/O", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
