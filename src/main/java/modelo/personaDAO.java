package modelo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//Definición de la clase pública "personaDAO"
public class personaDAO {
	
	// Declaración de atributos privados de la clase "personaDAO"
	private File archivo; // Archivo donde se almacenarán los datos de los contactos
	private persona persona; // Objeto "persona" que se gestionará
	
	// Constructor público de la clase "personaDAO" que recibe un objeto "persona" como parámetro
	public personaDAO(persona persona) {
		this.persona = persona; // Asigna el objeto "persona" recibido al atributo de la clase
		archivo = new File("src/main/resources/files"); // Establece la ruta donde se alojará el archivo
		// Llama al método para preparar el archivo
		prepararArchivo();
	}
	
	// Método privado para gestionar el archivo utilizando la clase File
	private void prepararArchivo() {
		// Verifica si el directorio existe
		if (!archivo.exists()) { // Si el directorio no existe, se crea
			archivo.mkdirs();
		}
		
		// Accede al archivo "datosContactos.csv" dentro del directorio especificado
		archivo = new File(archivo.getAbsolutePath(), "datosContactos.csv");
		// Verifica si el archivo existe
		if (!archivo.exists()) { // Si el archivo no existe, se crea
			try {
				archivo.createNewFile();
			} catch (IOException e) {
				// Maneja la excepción de entrada/salida
				e.printStackTrace();
			}
		}
	}
	private void escribir(String texto){
		// Prepara el archivo para escribir en la última línea
		FileWriter escribir;
		try {
			escribir = new FileWriter(archivo.getAbsolutePath(), true);
			escribir.write(texto + "\n"); // Escribe los datos del contacto en el archivo
			// Cierra el archivo
			escribir.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	// Método público para escribir en el archivo (Optimizado)
	public boolean escribirArchivo() throws IOException {
		// Abre el archivo usando BufferedWriter para mayor eficiencia
		java.io.BufferedWriter escribir = new java.io.BufferedWriter(new java.io.FileWriter(archivo.getAbsolutePath(), true));
		escribir.write(persona.datosContacto() + "\n"); // Escribe los datos del contacto en el archivo
		// Cierra el archivo
		escribir.close();
		return true; // Retorna true si la escritura fue exitosa
	}
	
	// Método público para leer los datos del archivo
	public List<persona> leerArchivo() throws IOException {
		// Crea una lista que almacenará cada persona encontrada
		List<persona> personas = new ArrayList<>();
		
		// Abre el archivo para leer (usamos BufferedReader para leer línea por línea muy rápido)
		java.io.BufferedReader leer = new java.io.BufferedReader(new java.io.FileReader(archivo.getAbsolutePath()));
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
					persona p = new persona();
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
	public void actualizarContactos(List<persona> personas) throws IOException {
		// Abre el archivo una sola vez en modo sobrescritura usando BufferedWriter
		java.io.BufferedWriter escritor = new java.io.BufferedWriter(new java.io.FileWriter(archivo.getAbsolutePath(), false));
		
		// Recorre los elementos de la lista y los escribe de golpe
		for (persona p : personas) {
			escritor.write(p.datosContacto() + "\n");
		}
		
		// Cierra el archivo una sola vez al terminar
		escritor.close();
	}
}
