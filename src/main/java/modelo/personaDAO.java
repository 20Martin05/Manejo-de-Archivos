package modelo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

//Definición de la clase pública "personaDAO"
public class PersonaDAO {
	private static final Logger LOGGER = Logger.getLogger(PersonaDAO.class.getName());
	private static final String DATA_DIRECTORY = "src/main/resources/files";
	private static final String CONTACTS_FILE = "tabla_contactos.csv";
	
	// Declaración de atributos privados de la clase "personaDAO"
	private File archivo; // Archivo donde se almacenarán los datos de los contactos
	private final Persona persona; // Objeto "persona" que se gestionará
	
	// Constructor público de la clase "personaDAO" que recibe un objeto "persona" como parámetro
	public PersonaDAO(Persona persona) {
		this.persona = persona; // Asigna el objeto "persona" recibido al atributo de la clase
		archivo = new File(DATA_DIRECTORY); // Establece la ruta donde se alojará el archivo
		// Llama al método para preparar el archivo
		prepararArchivo();
	}
	
	// Método privado para gestionar el archivo utilizando la clase File
	private void prepararArchivo() {
		// Verifica si el directorio existe
		if (!archivo.exists()) { // Si el directorio no existe, se crea
			if (!archivo.mkdirs() && !archivo.isDirectory()) {
				LOGGER.severe("No se pudo crear el directorio de datos: " + archivo.getAbsolutePath());
				return;
			}
		}
		
		// Accede al archivo de contactos dentro del directorio especificado
		archivo = new File(archivo.getAbsolutePath(), CONTACTS_FILE);
		// Verifica si el archivo existe
		if (!archivo.exists()) { // Si el archivo no existe, se crea
			try {
				if (!archivo.createNewFile() && !archivo.exists()) {
					LOGGER.severe("No se pudo crear el archivo de contactos: " + archivo.getAbsolutePath());
				}
			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, "No se pudo crear el archivo de contactos", e);
			}
		}
	}
	// Método público para escribir en el archivo (Optimizado)
	public void escribirArchivo() throws IOException {
		// Abre el archivo usando BufferedWriter para mayor eficiencia
		try (BufferedWriter escribir = Files.newBufferedWriter(archivo.toPath(), StandardCharsets.UTF_8,
				java.nio.file.StandardOpenOption.CREATE, java.nio.file.StandardOpenOption.APPEND)) {
			escribir.write(persona.datosContacto());
			escribir.newLine();
		}
	}
	
	// Método público para leer los datos del archivo
	public List<Persona> leerArchivo() throws IOException {
		// Crea una lista que almacenará cada persona encontrada
		List<Persona> personas = new ArrayList<>();
		
		// Abre el archivo para leer (usamos BufferedReader para leer línea por línea muy rápido)
		java.io.BufferedReader leer = Files.newBufferedReader(archivo.toPath(), StandardCharsets.UTF_8);
		String linea;
		
		// Lee el archivo línea por línea hasta el final
		while ((linea = leer.readLine()) != null) {
			if (linea.trim().isEmpty()) {
				continue;
			}
			String[] partes = linea.split(";");
			// Verifica de forma segura que la línea no esté dañada (debe tener al menos 6 datos)
			if (partes.length >= 6) {
				try {
					Persona p = new Persona();
					p.setId(Integer.parseInt(partes[0]));
					p.setNombre(partes[1]);
					p.setTelefono(partes[2]);
					p.setEmail(partes[3]);
					p.setCategoria(partes[4]);
					p.setFavorito(Boolean.parseBoolean(partes[5]));
					personas.add(p);
				} catch (Exception e) {
					// Si hay un error con el número de ID, simplemente ignora esta línea dañada
				}
			}
		}
		// Cierra el archivo
		leer.close();
		// Retorna la lista de personas
		return personas;
	}
	
	// Método público para guardar los contactos modificados o eliminados (Optimizado)
	public void actualizarContactos(List<Persona> personas) throws IOException {
		// Abre el archivo una sola vez en modo sobrescritura usando BufferedWriter
		java.io.BufferedWriter escritor = Files.newBufferedWriter(archivo.toPath(), StandardCharsets.UTF_8,
				java.nio.file.StandardOpenOption.CREATE, java.nio.file.StandardOpenOption.TRUNCATE_EXISTING);
		
		// Recorre los elementos de la lista y los escribe de golpe
		for (Persona p : personas) {
			escritor.write(p.datosContacto() + "\n");
		}
		
		// Cierra el archivo una sola vez al terminar
		escritor.close();
	}

	//Exportar Archivo
	public void exportarCSV(File destino, List<Persona> lista) throws IOException {
		try (BufferedWriter bw = Files.newBufferedWriter(destino.toPath(), java.nio.charset.StandardCharsets.UTF_8)) {
			bw.write("ID;NOMBRE;TELÉFONO;EMAIL;CATEGORIA;FAVORITO\n");
			for (Persona p : lista) {
				bw.write(p.datosContacto() + "\n");
			}
		}
	}
}
