
````markdown
# 🏦 Sistema de Gestión de Créditos - CrediYa

**CrediYa S.A.S.** es una aplicación de consola desarrollada en Java diseñada para digitalizar y automatizar el control de préstamos personales y cobro de cartera. Este sistema reemplaza el manejo manual de hojas de cálculo, implementando una arquitectura profesional por capas (MVC) y persistencia de datos con MySQL.

## 📋 Características Principales

* **Gestión de Seguridad:** Sistema de Login con validación de credenciales y roles (Administrador vs. Empleado).
* **Módulo de Empleados:** Registro y administración de personal con validación de duplicados.
* **Módulo de Clientes:** Base de datos de clientes con historial crediticio.
* **Gestión de Préstamos:**
    * Simulación de cuotas y tablas de amortización.
    * Validaciones de reglas de negocio (topes máximos, tasas de interés, ética).
    * Generación automática de **Tickets de soporte** en archivos `.txt`.
* **Módulo de Pagos:** * Registro de abonos a capital.
    * Cálculo automático de saldos pendientes.
    * Actualización inteligente de estados (Pendiente, Pagado, Mora).
* **Reportes Avanzados:** Uso de **Java Stream API** para filtrar métricas en tiempo real (dinero prestado, proyección de ganancias, clientes en mora).

## 🛠️ Tecnologías Utilizadas

* **Lenguaje:** Java 17+
* **Gestor de Dependencias:** Maven
* **Base de Datos:** MySQL 8.0
* **Conectividad:** JDBC (MySQL Connector)
* **Infraestructura:** Docker (Contenedor para la BD)

## 🏗️ Arquitectura del Proyecto

El proyecto sigue una arquitectura limpia y modular:

```text
com.crediya
│
├── config          # Configuración de conexión a BD (db.properties)
├── data
│   ├── entities    # Mapeo exacto de las tablas SQL
│   ├── mapper      # Patrón Mapper (Entity <-> Model)
│   └── repositories # Implementación DAO (Sentencias SQL)
├── model           # Modelos de negocio y Herencia (Persona -> Cliente/Empleado)
├── repository      # Interfaces (Contratos del DAO)
├── service         # Reglas de negocio, validaciones y cálculos
├── ui              # Menús de consola e interacción con usuario
└── Main.java       # Punto de entrada y Login
````

## 📸 Ejemplos de Uso

### 1\. Menú Principal Interactivo

El sistema cuenta con un menú protegido por roles. Ejemplo de vista de Administrador:

```text
===== 🏦 MENÚ PRINCIPAL - CREDIYA =====
👤 Usuario: Sebastian Jaimes | Rol: ADMIN
---------------------------------------
1. 👔 Gestionar Empleados (ADMIN)
2. 👥 Gestionar Clientes
3. 💰 Gestionar Préstamos
4. 💸 Gestionar Pagos
5. 📊 Reportes e Indicadores
6. 🚪 Salir del Sistema
---------------------------------------
👉 Seleccione una opción: 
```

### 2\. Generación de Tickets

Al aprobar un préstamo, el sistema genera automáticamente un comprobante físico en la carpeta `Tickets/`:

```text
================================
        CREDIYA S.A.S.
     Nit: 900.123.456-7         
   Calle Falsa 123, Ciudad      
================================
FECHA: 2025-12-15 21:46:07
TICKET REF: 20251215_214607
--------------------------------
DATOS DEL CLIENTE
NOMBRE: Joan Jaimes
DOC:    1005330744
--------------------------------
ATENDIDO POR:
ASESOR: Joan Jaimes
================================
      DETALLE DEL CREDITO       
--------------------------------
MONTO PRESTADO:   $2,000,000.00
TASA INTERES:           3.00%
PLAZO (MESES):            10
--------------------------------
VALOR CUOTA:      $206,000.00
--------------------------------
TOTAL A PAGAR:    $2,060,000.00
================================
   GRACIAS POR SU CONFIANZA     
     CONSERVE ESTE TICKET       
================================
```

## 🚀 Instalación y Configuración

### 1\. Base de Datos

El proyecto incluye el script SQL necesario para desplegar la estructura.

1.  Abrir MySQL Workbench o DBeaver.
2.  Ejecutar el script ubicado en: `sql/crediya_schema.sql`.
3.  *(Opcional)* Despliegue con Docker:
    ```bash
    docker run --name mysql_container -e MYSQL_ROOT_PASSWORD=admin -p 3307:3306 -d mysql:latest
    docker start mysql_container
docker exec -it mysql_container mysql -h localhost -u root -p
    ```

### 2\. Configuración de Credenciales

Configure el acceso a la base de datos en `src/main/resources/db.properties`:

```properties
db.url=jdbc:mysql://localhost:3307/crediya_db
db.user=root
db.password=admin
```

### 3\. Ejecución

Ejecutar el archivo principal `src/main/java/com/crediya/Main.java`.

## 👤 Usuarios del Sistema

Para acceder a las funcionalidades, utilice las credenciales registradas en base de datos:

  * **Rol Administrador:**

      * **Correo:** `admin@admin.com`
      * **Permisos:** Acceso total (Incluye gestión de empleados y reportes financieros).

  * **Rol Asesor:**

      * **Correo:** `(Correo de empleado registrado)`
      * **Permisos:** Gestión de clientes, préstamos y pagos.

## 📄 Licencia

Este proyecto es de uso educativo y académico para el programa Campuslands.

-----

**Desarrollado por:**

  * Laura Albarracín
  * Sebastian Jaimes

````