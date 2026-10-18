# Sistema de Gestión de Contactos (Manejo de Archivos)

**Proyecto de Manejo de Archivos (U1) que implementa un sistema CRUD de gestión de contactos con Java, Maven, interfaz gráfica en Java Swing y persistencia de datos local mediante archivos CSV.**

El desarrollo aplica el patrón de arquitectura **MVC (Modelo-Vista-Controlador)** y optimiza las operaciones de Entrada/Salida (I/O) utilizando flujos eficientes para garantizar un alto rendimiento en la lectura y escritura secuencial de la base de datos.

---

### ⚙️ Características Técnicas

* **CRUD Completo:** Creación, lectura, actualización y eliminación de registros (Create, Read, Update, Delete).
* **Persistencia en Texto Plano:** La información se guarda y recupera desde un archivo local (`tabla_contactos.csv`).
* **Identificadores Únicos (ID):** Algoritmo de auto-incremento de Llaves Primarias para cada contacto.
* **Patrón de Arquitectura MVC:** Separación estricta de la interfaz gráfica (`vista`), lógica y eventos (`controlador`), y datos (`modelo`).
* **Optimización de Memoria (I/O):** Uso de `BufferedReader` y `BufferedWriter` para operaciones de lectura y escritura a alta velocidad.
* **Codificación UTF-8:** Lectura y escritura de datos con UTF-8 para conservar tildes y caracteres especiales.
* **Interfaz de Usuario (Swing):** Vista estilo tabla con ordenamiento, filtrado y selección de contactos.
* **Organización por pestañas:** Uso de `JTabbedPane` para separar las pestañas de Contactos y Estadísticas.
* **Barra de progreso:** Uso de `JProgressBar` para indicar la cantidad de contactos cargados.
* **Menú Contextual:** Menú con clic derecho sobre la tabla para modificar o eliminar el contacto seleccionado.
* **Atajos de Teclado:** Acceso rápido a las operaciones principales mediante combinaciones de teclas.
* **Validación de datos:** Control de campos obligatorios, nombres, teléfonos, correos electrónicos y categorías.

---

### 🛠️ Requisitos Previos
Para compilar y ejecutar este proyecto correctamente, necesitas tener instalado:
* **Java Development Kit (JDK)** versión 21 o superior.
* **Apache Maven** (gestor de dependencias y construcción).
* Un entorno de desarrollo (IDE) como **IntelliJ IDEA** (recomendado), Eclipse o NetBeans.

---

### 📂 Estructura del Proyecto (MVC)
El código está organizado bajo el patrón Modelo-Vista-Controlador de la siguiente manera:
```text
src/main/
 ├── java/
 │   ├── controlador/
 │   │   └── LogicaVentana.java   # Control de eventos, validaciones y nexo UI-Datos
 │   ├── modelo/
 │   │   ├── Persona.java         # Estructura del objeto Contacto
 │   │   └── PersonaDAO.java      # Lógica de lectura/escritura (Data Access Object)
 │   └── vista/
 │       ├── ventana.java         # Clase principal y punto de entrada de la aplicación
 │       └── base/
 │           ├── VentanaUI.java    # Componentes y diseño base de la interfaz
 │           └── VentanaUI.form   # Diseño visual de IntelliJ GUI Designer
 └── resources/
     ├── files/
     │   ├── tabla_contactos.csv         # Tabla/listado persistente de contactos
     │   └── reporte_estadisticas.txt    # Reporte exportado desde la pestaña Estadísticas
     └── images/
         ├── Pestaña de Contacto.jpg     # Captura de la pantalla de Contactos en ejecución
         └── Pestaña de Estadísticas.jpg # Captura de la pantalla de Estadísticas en ejecución
```

---

### 📥 ¿Cómo obtener el proyecto?

Para clonar el repositorio, abre una terminal y ejecuta:

```bash
git clone https://github.com/20Martin05/Manejo-de-Archivos.git
cd Manejo-de-Archivos
```

También puedes descargar el proyecto como archivo ZIP desde GitHub y extraerlo en tu computadora.

### ▶️ Compilar y ejecutar

1. Verifica que tengas instalado el **JDK 21 o superior**.
2. Abre el proyecto desde el archivo `pom.xml` en IntelliJ IDEA, Eclipse, NetBeans o Visual Studio Code.
3. Espera a que Maven descargue la dependencia `forms_rt`.
4. Ejecuta la clase principal:
   ```text
   src/main/java/vista/ventana.java
   ```

Si tienes Maven instalado, también puedes compilar el proyecto desde la raíz:

```bash
mvn clean package
```

El archivo generado quedará en:

```text
target/Manejo-de-Archivo-1.1.0.jar
```

### 🖥️ Pantallas de ejecución

Las siguientes imágenes muestran la aplicación funcionando:

#### Pestaña de Contactos

![Pantalla de Contactos en ejecución](src/main/resources/images/Pestaña%20de%20Contacto.jpg)

Esta pantalla permite agregar, modificar, eliminar, buscar, ordenar y exportar contactos.

#### Pestaña de Estadísticas

![Pantalla de Estadísticas en ejecución](src/main/resources/images/Pestaña%20de%20Estadísticas.jpg)

Esta pantalla permite consultar, limpiar y exportar el reporte estadístico de los contactos.

---

### 🚀 Instrucciones de Uso

### ✅ Requisitos de la actividad

La aplicación cumple con las funcionalidades solicitadas:

* **JTabbedPane:** organiza la interfaz en las pestañas **Contactos** y **Estadísticas**.
* **Eventos de teclado y mouse:** incluye atajos con `InputMap`/`ActionMap`, selección de filas y menú contextual con clic derecho mediante `JPopupMenu`.
* **JTable:** muestra los contactos y permite ordenarlos desde los encabezados y filtrarlos mediante el campo de búsqueda en todas sus columnas.
* **Exportación CSV:** permite exportar los contactos registrados a un archivo `.csv`.
* **JProgressBar:** muestra el total de contactos cargados al iniciar la aplicación.
* **MVC:** separa el modelo (`Persona`), el acceso a datos (`PersonaDAO`), el controlador (`LogicaVentana`) y la vista (`ventana` y `VentanaUI`).

#### ¿Cómo ejecutar el programa?
1. Abre el proyecto en tu IDE favorito.
2. Dirígete a la ruta `src/main/java/vista/` y ejecuta la clase principal `ventana.java`.

#### ¿Cómo funciona la interfaz?
* **Agregar un contacto:** Llena todos los campos de texto (Nombres, Teléfono, Email), elige una Categoría, marca si es favorito, y presiona **AGREGAR**. El contacto aparecerá inmediatamente en la lista inferior con su ID asignado.
* **Modificar un contacto:** Haz clic sobre un contacto existente en la lista blanca inferior. Sus datos se cargarán automáticamente en los campos de texto superiores. Cambia lo que necesites y presiona **MODIFICAR**.
* **Eliminar un contacto:** Selecciona el contacto en la lista inferior y presiona **ELIMINAR**.
* **Categorías disponibles:** Familia, Amigos, Trabajo, Universidad, Clientes, Emergencias y Otros.
* **Validación de información:** No se permiten campos vacíos. El nombre debe contener caracteres válidos, el teléfono debe tener entre 7 y 15 dígitos y el correo debe tener un formato válido.
* **Buscar contactos:** Escribe en el campo de búsqueda para encontrar coincidencias en el ID, nombre, teléfono, correo, categoría o estado de favorito.
* **Ordenar la tabla:** Haz clic sobre el encabezado de cualquier columna para ordenar los contactos.
* **Estado de acciones:** Los botones **MODIFICAR** y **ELIMINAR** se habilitan únicamente cuando hay una fila seleccionada.
* **Menú contextual:** Haz clic derecho sobre una fila para acceder a las opciones **Modificar** y **Eliminar**.
* **Atajos de teclado:**
  * **Supr/Delete:** Elimina el contacto seleccionado.
  * **Ctrl + S:** Modifica los datos del contacto seleccionado.
  * **Ctrl + G:** Agrega un contacto nuevo.
  * **Ctrl + E:** Exporta los contactos a un archivo CSV.
  * **Ctrl + F:** Enfoca el campo de búsqueda y selecciona su contenido.
  * **Escape:** Quita la selección de la tabla.
  * **Estadísticas:** Genera un reporte con el total de contactos, favoritos, no favoritos, distribución por categorías y fecha de cálculo.
  * **Exportar reporte:** Guarda las estadísticas generadas en un archivo de texto.
  * **Actualización automática:** El reporte estadístico se actualiza automáticamente después de agregar, modificar o eliminar contactos.
  * **Carga de datos:** La barra de progreso muestra la cantidad total de contactos cargados y la tabla se actualiza desde la información mantenida en memoria.

*(Nota: El sistema cuenta con alertas de seguridad que impiden guardar datos incompletos o inválidos, así como modificar o eliminar sin haber seleccionado previamente un contacto).*

#### ¿Dónde se guardan los datos?
Toda tu información es persistente. La "base de datos" es un archivo de texto que se crea automáticamente la primera vez que ejecutas la aplicación.
* **Tabla de contactos:** `src/main/resources/files/tabla_contactos.csv`
* **Ruta del reporte estadístico:** `src/main/resources/files/reporte_estadisticas.txt`
* **Codificación:** Los archivos se leen y escriben utilizando UTF-8.
* **Formato interno:** Los datos se separan por punto y coma (`;`). Ejemplo: `1;Maria Gomez;0949380301;mariagomez@icloud.com;Familia;true`

---

### 👤 Autor
* **Desarrollado por:** Martin Rody Rosero Játiva
* **Materia:** Programación de Interfaces Gráficas
* **Universidad:** Universidad Politécnica Salesiana
* **Bloque:** Bloque I - Unidad 1 (Manejo de Archivos)

> **Nota sobre la clase principal:** La aplicación se inicia actualmente desde `src/main/java/vista/ventana.java`.