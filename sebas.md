¡Excelente! Ya tienen la base (BD + Estructura de Carpetas + Conexión). Ahora vamos a repartir el trabajo usando la arquitectura profesional que creó tu compañero.
Mantendremos la división de roles:

* **Compañero B (Financiero):** Préstamos y Pagos (Más lógica matemática).

Aquí tienen la hoja de ruta archivo por archivo.

---

### 1. Capa `model` (Los Planos)
**Ubicación:** `src/main/java/com/crediya/model`
**Tarea:** Crear las clases simples (POJOs) con atributos, constructor vacío, constructor con datos, Getters, Setters y `toString`.


* **Compañero B:**
    * Crear `Prestamo.java`: Atributos `int id`, `int clienteId`, `int empleadoId`, `double monto`, `double interes`, `int cuotas`, `LocalDate fechaInicio`, `String estado`.
    * Crear `Pago.java`: Atributos `int id`, `int prestamoId`, `LocalDate fechaPago`, `double monto`.

---

### 2. Capa `dao` (Las Interfaces / Contratos)
**Ubicación:** `src/main/java/com/crediya/dao`
**Tarea:** Crear archivos `interface`. Aquí **NO** va código lógico, solo se definen los nombres de los métodos que usarán.


* **Compañero B:**
    * Crear `PrestamoDAO.java` y `PagoDAO.java`.
    * Definir métodos: `void registrarPrestamo(...)`, `void registrarPago(...)`, `List<Prestamo> listarPorCliente(int clienteId)`.

---

### 3. Capa `dao.impl` (El Código SQL Duro)
**Ubicación:** `src/main/java/com/crediya/dao/impl`
**Tarea:** Aquí es donde usarán la clase `DatabaseConfig` que ya probaron. Implementan las interfaces del paso anterior.


* **Compañero B:**
    * Crear `PrestamoDAOImpl.java` (implementa `PrestamoDAO`).
        * *Ojo:* Al insertar un préstamo, asegúrate de que el ID del cliente exista (esto es una validación de SQL, si el ID no existe, MySQL dará error).
    * Crear `PagoDAOImpl.java` (implementa `PagoDAO`).

---

### 4. Capa `service` (La Lógica de Negocio)
**Ubicación:** `src/main/java/com/crediya/service`
**Tarea:** Aquí va la "magia" que no es SQL. Validaciones, cálculos y archivos de texto.


* **Compañero B (El Matemático):**
    * Crear `CalculadoraPrestamosService.java`.
        * Método `calcularTablaAmortizacion(double monto, double interes, int cuotas)`.
        * Fórmula sugerida: Calcular cuánto debe pagar mensualmente.
    * Crear `GestorPagosService.java`.
        * Método `registrarAbono()`: Verifica que el monto del abono no sea mayor a la deuda actual antes de llamar al DAO para guardar.

---

### 5. Capa `ui` (El Menú)
**Ubicación:** `src/main/java/com/crediya/ui`
**Tarea:** Unir todo para el usuario final.

* **Trabajo Conjunto:**
    * Crear `MenuPrincipal.java`.
    * Usen un `switch` grande.
    * **Caso 1 y 2:** Llaman a los servicios del Compañero A (Gestionar Empleados/Clientes).
    * **Caso 3 y 4:** Llaman a los servicios del Compañero B (Gestionar Préstamos/Pagos).
    * **Caso 5:** Reportes (Compañero A).

### Resumen Visual del Flujo de Trabajo

Para que entiendan cómo viajan los datos que van a programar:


1.  El usuario escribe en la **UI**.
2.  La **UI** le pasa los datos al **Service** (este valida si los números son correctos).
3.  El **Service** le pasa los datos limpios al **DAO**.
4.  El **DAO** usa `DatabaseConfig` para enviar el SQL a la **Base de Datos**.

**Siguiente paso recomendado:**
Empiecen por el **Paso 1 (Model)** y el **Paso 2 (DAO Interfaces)**. Son rápidos y definen las reglas del juego. ¿Les parece bien comenzar por ahí?






holaaaaaaaaaaaaaaaaa