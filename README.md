# Sistema de Gestión de Contactos (Manejo de Archivos)

**Proyecto de Manejo de Archivos (U1) que implementa un sistema CRUD de gestión de contactos con Java, Maven, interfaz gráfica en Java Swing y persistencia de datos local mediante archivos CSV.**

El desarrollo aplica el patrón de arquitectura **MVC (Modelo-Vista-Controlador)** y optimiza las operaciones de Entrada/Salida (I/O) utilizando flujos eficientes para garantizar un alto rendimiento en la lectura y escritura secuencial de la base de datos.

---

### ⚙️ Características Técnicas

* **CRUD Completo:** Creación, lectura, actualización y eliminación de registros (Create, Read, Update, Delete).
* **Persistencia en Texto Plano:** La información se guarda y recupera desde un archivo local (`datosContactos.csv`).
* **Identificadores Únicos (ID):** Algoritmo de auto-incremento de Llaves Primarias para cada contacto.
* **Patrón de Arquitectura MVC:** Separación estricta de la interfaz gráfica (`vista`), lógica y eventos (`controlador`), y datos (`modelo`).
* **Optimización de Memoria (I/O):** Uso de `BufferedReader` y `BufferedWriter` para operaciones de lectura y escritura a alta velocidad.
* **Interfaz de Usuario (Swing):** Vista estilo tabla con renderizado personalizado y tipografía monospaced para alineación perfecta.

---

### 🛠️ Requisitos Previos
Para compilar y ejecutar este proyecto correctamente, necesitas tener instalado:
* **Java Development Kit (JDK)** versión 17 o superior.
* **Apache Maven** (gestor de dependencias y construcción).
* Un entorno de desarrollo (IDE) como **IntelliJ IDEA** (recomendado), Eclipse o NetBeans.

---

### 📂 Estructura del Proyecto (MVC)
El código está organizado bajo el patrón Modelo-Vista-Controlador de la siguiente manera:
```text
src/main/
 ├── java/
 │   ├── controlador/
 │   │   └── logica_ventana.java  # Control de eventos, validaciones y nexo UI-Datos
 │   ├── modelo/
 │   │   ├── persona.java         # Estructura del objeto Contacto
 │   │   └── personaDAO.java      # Lógica de lectura/escritura (Data Access Object)
 │   └── vista/
 │       └── ventana.java         # Diseño de la interfaz gráfica (Java Swing) y Main
 └── resources/
     └── datosContactos.csv       # Archivo de persistencia de datos
```

---

### 🚀 Instrucciones de Uso

#### ¿Cómo ejecutar el programa?
1. Abre el proyecto en tu IDE favorito.
2. Dirígete a la ruta `src/main/java/vista/` y ejecuta la clase principal `ventana.java`.

#### ¿Cómo funciona la interfaz?
* **Agregar un contacto:** Llena todos los campos de texto (Nombres, Teléfono, Email), elige una Categoría, marca si es favorito, y presiona **AGREGAR**. El contacto aparecerá inmediatamente en la lista inferior con su ID asignado.
* **Modificar un contacto:** Haz clic sobre un contacto existente en la lista blanca inferior. Sus datos se cargarán automáticamente en los campos de texto superiores. Cambia lo que necesites y presiona **MODIFICAR**.
* **Eliminar un contacto:** Selecciona el contacto en la lista inferior y presiona **ELIMINAR**. 

*(Nota: El sistema cuenta con alertas de seguridad que te impedirán guardar datos incompletos o intentar modificar/eliminar sin haber seleccionado a nadie previamente).*

#### ¿Dónde se guardan los datos?
Toda tu información es persistente. La "base de datos" es un archivo de texto que se crea automáticamente la primera vez que ejecutas la aplicación.
* **Ruta del archivo:** `src/main/resources/files/datosContactos.csv`
* **Formato interno:** Los datos se separan por punto y coma (`;`). Ejemplo: `1;Maria Gomez;0949380301;mariagomez@icloud.com;Familia;true`

---

### 👤 Autor
* **Desarrollado por:** Martin Rody Rosero Játiva
* **Materia:** Programación de Interfaces Gráficas
* **Universidad:** Universidad Politécnica Salesiana
* **Bloque:** Bloque I - Unidad 1 (Manejo de Archivos)