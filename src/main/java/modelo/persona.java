package modelo;

//Definición de la clase pública "persona"
public class persona {
	
	// Declaración de variables privadas de la clase "persona"
	private int id;
	private String nombre, telefono, email, categoria;
	private boolean favorito;
	
	// Constructor público de la clase "persona"
	public persona() {
		super(); // Llama al constructor de la clase base (Object)
		//Inicializa las variables
		this.id = 0;
		this.nombre = "";
		this.telefono = "";
		this.email = "";
		this.categoria = "";
		this.favorito = false;
	}
	// Constructor público de la clase "persona" que inicializa todos los campos
	public persona(int id, String nombre, String telefono, String email, String categoria, boolean favorito) {
		super();// Llama al constructor de la clase base (Object)
		//Inicializa las variables con los valores enviados por los argumentos
		this.id = id;
		this.nombre = nombre;
		this.telefono = telefono;
		this.email = email;
		this.categoria = categoria;
		this.favorito = favorito;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	// Método público para obtener el valor de "nombre"
	public String getNombre() {
		return nombre; // Retorna el valor de la variable "nombre"
	}
	
	// Método público para establecer el valor de "nombre"
	public void setNombre(String nombre) {
		this.nombre = nombre; // Asigna el valor del parámetro "nombre" a la variable de instancia "nombre"
	}
	
	// Método público para obtener el valor de "telefono"
	public String getTelefono() {
		return telefono; // Retorna el valor de la variable "telefono"
	}
	
	// Método público para establecer el valor de "telefono"
	public void setTelefono(String telefono) {
		this.telefono = telefono; // Asigna el valor del parámetro "telefono" a la variable de instancia "telefono"
	}
	
	// Método público para obtener el valor de "email"
	public String getEmail() {
		return email; // Retorna el valor de la variable "email"
	}
	
	// Método público para establecer el valor de "email"
	public void setEmail(String email) {
		this.email = email; // Asigna el valor del parámetro "email" a la variable de instancia "email"
	}
	
	// Método público para obtener el valor de "categoria"
	public String getCategoria() {
		return categoria; // Retorna el valor de la variable "categoria"
	}
	
	

	// Método público para establecer el valor de "categoria"
	public void setCategoria(String categoria) {
		this.categoria = categoria; // Asigna el valor del parámetro "categoria" a la variable de instancia "categoria"
	}
	
	// Método público para verificar si el contacto es "favorito"
	public boolean isFavorito() {
		return favorito; // Retorna el valor de la variable "favorito"
	}
	
	// Método público para establecer si el contacto es "favorito"
	public void setFavorito(boolean favorito) {
		this.favorito = favorito; // Asigna el valor del parámetro "favorito" a la variable de instancia "favorito"
	}
	
	// Método para proveer un formato para almacenar en un archivo
	public String datosContacto() {
		// Estructurar el siguiente formato: id; nombre; telefono; email; categoria; favorito
        return String.format("%d;%s;%s;%s;%s;%s", id, nombre, telefono, email, categoria, favorito); // Retorna la cadena formateada
	}

}
