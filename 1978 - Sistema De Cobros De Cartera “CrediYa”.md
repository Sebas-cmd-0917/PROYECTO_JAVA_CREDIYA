# 1978 - Sistema De Cobros De Cartera “CrediYa”

La empresa CrediYa S.A.S., dedicada a otorgar créditos personales, busca digitalizar su control de préstamos y cobros.

Actualmente maneja los datos en hojas de cálculo, por lo que se requiere desarrollar un sistema de consola en Java que gestione empleados, clientes, préstamos y pagos, permitiendo registrar, consultar y persistir la información mediante archivos y conexión MySQL (JDBC).

## Descripción del proyecto

Objetivo General:

- Desarrollar un sistema modular en Java que permita gestionar préstamos y cobros de cartera aplicando:
- Principios de POO (herencia, polimorfismo, encapsulamiento).
- Manejo de colecciones y archivos.
- Persistencia en MySQL mediante JDBC.
- Buenas prácticas de diseño (SOLID, patrones, manejo de excepciones).



Requerimientos Generales:

- El proyecto debe permitir:
- Módulo de Empleados
- Registrar y consultar empleados.
- Atributos: id, nombre, documento, rol, correo, salario.
- Persistencia en archivo y base de datos.
- Módulo de Clientes
- Registrar y listar clientes con: id, nombre, documento, correo, telefono.
- Consultar préstamos asociados.
- Módulo de Préstamos
- Crear préstamos asociando cliente y empleado.
- Calcular automáticamente monto total con interés y valor de cuota mensual.
- Cambiar estado (pendiente / pagado). pendiente
- Persistir en archivo y base de datos.
- Módulo de Pagos
- Registrar abonos a préstamos.
- Actualizar saldo pendiente.
- Mostrar histórico de pagos.
- Módulo de Reportes
- Consultar préstamos activos, préstamos vencidos, clientes morosos, etc.
- Implementar expresiones Lambda y Stream API para filtrar resultados.

Script Base de MySQL (Guía Inicial)

El estudiante puede modificarlo o ampliarlo según su diseño.

```sql
CREATE DATABASE crediya_db;

USE crediya_db;



CREATE TABLE empleados (

 id INT AUTO_INCREMENT PRIMARY KEY,

 nombre VARCHAR(80),

 documento VARCHAR(30),

 rol VARCHAR(30),

 correo VARCHAR(80),

 salario DECIMAL(10,2)

);



CREATE TABLE clientes (

 id INT AUTO_INCREMENT PRIMARY KEY,

 nombre VARCHAR(80),

 documento VARCHAR(30),

 correo VARCHAR(80),

 telefono VARCHAR(20)

);



CREATE TABLE prestamos (

 id INT AUTO_INCREMENT PRIMARY KEY,

 cliente_id INT,

 empleado_id INT,

 monto DECIMAL(12,2),

 interes DECIMAL(5,2),

 cuotas INT,

 fecha_inicio DATE,

 estado VARCHAR(20),

 FOREIGN KEY (cliente_id) REFERENCES clientes(id),

 FOREIGN KEY (empleado_id) REFERENCES empleados(id)

);



CREATE TABLE pagos (

 id INT AUTO_INCREMENT PRIMARY KEY,

 prestamo_id INT,

 fecha_pago DATE,

 monto DECIMAL(10,2),

 FOREIGN KEY (prestamo_id) REFERENCES prestamos(id)

);
```

## Resultado esperado

Entregables

- Código fuente completo del proyecto (estructura organizada por paquetes).
- Script SQL ajustado (si se realizaron cambios).
- Archivos de texto generados (empleados.txt, clientes.txt, etc.).
- Diagrama UML de clases principales.
- Documento README con descripción general, ejecución y ejemplos.
- Repositorio en GitHub.





Módulo de Préstamos
- Crear préstamos asociando cliente y empleado.
- Calcular automáticamente monto total con interés y valor de cuota mensual.
- Cambiar estado (pendiente / pagado). pendiente
- Persistir en archivo y base de datos.