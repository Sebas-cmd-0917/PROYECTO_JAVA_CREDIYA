
# 🏦 Sistema de Gestión de Créditos - CrediYa

**CrediYa MAYSE** es una aplicación de consola desarrollada en Java diseñada para automatizar y gestionar el cobro de cartera y otorgamiento de préstamos personales. Implementa una arquitectura por capas (MVC) y persistencia de datos con MySQL.

## 📋 Características Principales

  * **Gestión de Seguridad:** Inicio de sesión con validación de roles (Administrador vs. Empleado).
  * **Módulo de Empleados:** Registro y administración de personal (CRUD).
  * **Módulo de Clientes:** Base de datos de clientes con validación de duplicados.
  * **Gestión de Préstamos:**
      * Simulación de cuotas y tabla de amortización.
      * Validaciones de negocio (montos mínimos/máximos, tasas de interés).
      * Generación automática de **Tickets** en archivos `.txt`.
  * **Módulo de Pagos:** Registro de abonos, cálculo automático de saldos y actualización de estados (Pendiente, Pagado, Mora).
  * **Reportes:** Consultas mediante Stream API para filtrar deudores, préstamos activos e históricos.

## 🛠️ Tecnologías Utilizadas

  * **Lenguaje:** Java 17+
  * **Gestor de Dependencias:** Maven
  * **Base de Datos:** MySQL 8.0
  * **Conectividad:** JDBC (MySQL Connector)
  * **Infraestructura:** Docker (Contenedor para la BD)

## 🏗️ Arquitectura del Proyecto

El proyecto sigue una arquitectura limpia dividida en paquetes:

```text
com.crediya
│
├── config          # Configuración de conexión a BD (db.properties)
├── data
│   ├── entities    # Mapeo exacto de las tablas SQL
│   ├── mapper      # Convertidores Entity <-> Model
│   └── repositories # Implementación DAO (Sentencias SQL)
├── model           # Modelos de negocio (Lógica pura)
├── repository      # Interfaces (Contratos del DAO)
├── service         # Reglas de negocio, validaciones y cálculos
├── ui              # Menús de consola e interacción con usuario
└── Main.java       # Punto de entrada
```

## 🚀 Instalación y Configuración

### 1\. Base de Datos

El proyecto incluye el script necesario para crear la estructura de la base de datos.

1.  Abre tu cliente SQL favorito (MySQL Workbench, DBeaver).
2.  Ejecuta el script ubicado en: `sql/crediya_schema.sql`.
3.  *(Opcional)* Si usas Docker, puedes levantar la BD con:
    ```bash
    docker run --name mysql_container -e MYSQL_ROOT_PASSWORD=admin -p 3307:3306 -d mysql:latest
    ```

### 2\. Configuración de Credenciales

Para que la aplicación se conecte, debes configurar el archivo de propiedades.

1.  Ve a `src/main/resources/`.
2.  Crea un archivo llamado `db.properties` con el siguiente contenido:
    ```properties
    db.url=jdbc:mysql://localhost:3307/crediya_db
    db.user=root
    db.password=admin
    ```
    *(Asegúrate de que el puerto coincida con tu configuración de MySQL).*

### 3\. Ejecución

Puedes ejecutar el proyecto desde tu IDE (VS Code, IntelliJ) corriendo el archivo `src/main/java/com/crediya/Main.java`.

## 👤 Usuarios del Sistema

Para acceder a las funcionalidades administrativas, asegúrate de tener el siguiente usuario registrado en tu base de datos:

  * **Rol Administrador:**

      * **Correo:** `admin@admin.com`
      * **Permisos:** Acceso total (Incluye gestión de empleados).

  * **Rol Asesor (Ejemplo):**

      * **Correo:** `cualquiercorreoregistrado@crediya.com`
      * **Permisos:** Gestión de clientes, préstamos y pagos (Restringido el módulo de empleados).

## 📄 Licencia

Este proyecto es de uso educativo y académico.

-----

**Desarrollado por:**

  * Laura Albarracín
  * Sebastian Jaimes