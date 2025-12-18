
Piensa en los **Streams** como una **cinta transportadora de una fábrica**.

1. Pones la lista de datos al principio (`.stream()`).
2. En el medio les haces cosas (filtrar, ordenar, transformar).
3. Al final los empacas en una caja o sacas un número (`.collect`, `.count`).

Aquí tienes la guía simplificada para tu proyecto **CrediYa**:

---

# 🎓 Guía de Estudio Simplificada (CrediYa)

### 1. `.filter(...)` → "El Portero" 🕵️‍♂️

**¿Qué hace?** Deja pasar SOLO a los que cumplen una regla. A los demás los echa.
**Úsalo cuando te pidan:** "Mostrar solo los..." o "Listar los mayores a...".

* **Ejemplo:** *"Solo préstamos mayores a 1 millón"*
```java
// Pasa: Prestamo de 2.000.000
// Se queda: Prestamo de 500.000
.filter(p -> p.getMonto() > 1000000)

```



### 2. `.map(...)` → "El Traductor" 🔄

**¿Qué hace?** Convierte el objeto en otra cosa. Entra una cosa compleja (Cliente), sale algo simple (su Correo).
**Úsalo cuando te pidan:** "Obtener una lista de nombres", "Sacar solo los correos".

* **Ejemplo:** *"Dame solo los correos de los clientes"*
```java
// Entra: Objeto Cliente completo
// Sale: "juan@gmail.com"
.map(c -> c.getCorreo())

```



### 3. `.sorted(...)` → "El Organizador" 📊

**¿Qué hace?** Ordena la fila.
**Úsalo cuando te pidan:** "Ordenar de mayor a menor", "Ver los más recientes".

* **Truco:**
* `Comparator.comparing(...)` = De menor a mayor (Ascendente).
* `... .reversed()` = De mayor a menor (Descendente).


* **Ejemplo:** *"Pagos del más reciente al más antiguo"*
```java
.sorted(Comparator.comparing(Pago::getFechaPago).reversed())

```



### 4. `.anyMatch(...)` → "El Detective" 🔍

**¿Qué hace?** Responde **SÍ o NO** (`true`/`false`). Busca si al menos UNO cumple.
**Úsalo cuando te pidan:** "¿Existe algún...?", "¿Hay alguien con cédula X?".

* **Ejemplo:** *"¿Existe algún cliente con cédula 123?"*
```java
boolean existe = lista.stream()
        .anyMatch(c -> c.getDocumento().equals("123"));

```



### 5. `.collect(...)` → "El Empacador" 📦

**¿Qué hace?** Es el final de la línea. Mete todo lo que sobró en una lista nueva.
**Úsalo siempre al final** para guardar los resultados.

* **La vieja confiable:**
```java
.collect(Collectors.toList());

```



---

## ⚡ El "Chivo" para el Examen (Copia y pega mental)

Si la pregunta del profesor dice... | Tú escribes en el código...
--- | ---
**"Filtrar", "Buscar los que sean..."** | `.filter( x -> x.getAlgo() == ... )`
**"Obtener los nombres/correos"** | `.map( x -> x.getNombre() )`
**"Ordenar por fecha/precio"** | `.sorted( Comparator.comparing(...) )`
**"¿Existe alguno que...?"** | `.anyMatch( x -> ... )`
**"Agrupar por estado"** | `.collect( Collectors.groupingBy(...) )`
**"Promedio", "Suma total"** | `.mapToDouble( ... ).summaryStatistics()`

---

### Ejemplo Completo (Para tu Taller)

Digamos que en tu `ReporteService.java` te piden: **"Lista de correos de clientes que deben mucho dinero (más de 5 millones)"**.

Así se arma el rompecabezas:

```java
public List<String> correosDeDeudoresVIP() {
    return prestamoRepo.listarPrestamos().stream() // 1. Abres la fábrica
            .filter(p -> p.getMonto() > 5000000)   // 2. El Portero: Solo deja pasar los de > 5M
            .map(p -> p.getCorreoCliente())        // 3. El Traductor: Solo quiero el correo, no todo el préstamo
            .collect(Collectors.toList());         // 4. El Empacador: Dámelos en una lista
}

```

¡Eso es todo! Con esas 5 herramientas (`filter`, `map`, `sorted`, `anyMatch`, `collect`) pasas el examen sobrado.












---

```markdown
# 🛠️ Notas de Desarrollo: Optimización con Stream API

**Fecha:** 18/12/2025
**Asunto:** Refactorización de servicios para mejorar rendimiento y legibilidad.
**Estado:** Pendiente de implementación.

A continuación, se documentan los patrones de diseño funcional (Lambda & Streams) sugeridos para reemplazar los bucles `for` tradicionales en los servicios de reportes y gestión.

---

## 1. Módulo de Filtrado (Consultas Específicas)

*Implementar en `ReporteService.java` para reducir complejidad ciclomática.*

### 🔹 Filtrar Cartera VIP (Montos Altos)
Lógica para obtener préstamos que superen cierto umbral de capital.

```java
// TODO: Integrar en ReporteService
public List<Prestamo> obtenerCarteraVip(double montoMinimo) {
    return prestamoRepo.listarPrestamos().stream()
            .filter(p -> p.getMonto() >= montoMinimo)
            .collect(Collectors.toList());
}

```

### 🔹 Filtrar Pagos por Fecha (Cierre de Caja)

Utilidad para obtener los movimientos de un día específico (ej. `LocalDate.now()`).

```java
public List<Pago> obtenerCierreDiario(LocalDate fecha) {
    return pagoRepo.HistoricoDePagos().stream()
            .filter(p -> p.getFechaPago().equals(fecha))
            .collect(Collectors.toList());
}

```

---

## 2. Módulo de Transformación de Datos (Mapping)

*Uso de `.map()` para extraer listas de atributos específicos sin cargar objetos completos.*

### 🔹 Extracción de Correos (Marketing)

Generar lista de Strings solo con los correos para envío masivo.

```java
public List<String> extraerCorreosClientes() {
    return clienteRepo.listarTodosClientes().stream()
            .map(Cliente::getCorreo) // Referencia a método
            .collect(Collectors.toList());
}

```

### 🔹 Obtener IDs de Préstamos en Mora

Recuperar solo los identificadores para procesos de cobranza externa.

```java
public List<Integer> obtenerIdsCarteraCastigada() {
    return prestamoRepo.listarPrestamos().stream()
            .filter(p -> p.getEstado() == EstadoPrestamo.MORA)
            .map(Prestamo::getId)
            .collect(Collectors.toList());
}

```

---

## 3. Ordenamiento y Cronología

*Reemplazo de `Collections.sort` por `Stream.sorted`.*

### 🔹 Historial de Pagos (Reciente -> Antiguo)

Ordenamiento descendente por fecha para visualizar los últimos abonos.

```java
public List<Pago> historialPagosRecientes() {
    return pagoRepo.HistoricoDePagos().stream()
            .sorted(Comparator.comparing(Pago::getFechaPago).reversed())
            .collect(Collectors.toList());
}

```

### 🔹 Ranking de Préstamos (Valor Ascendente)

Ordenamiento por monto para análisis de microcréditos.

```java
public List<Prestamo> rankingPrestamosMenorValor() {
    return prestamoRepo.listarPrestamos().stream()
            .sorted(Comparator.comparingDouble(Prestamo::getMonto))
            .collect(Collectors.toList());
}

```

### 🔹 Ordenamiento Multi-Criterio

Criterio primario: Fecha (Reciente). Criterio secundario: Monto (Mayor).

```java
public List<Pago> ordenarMovimientosComplejo() {
    return pagoRepo.HistoricoDePagos().stream()
            .sorted(Comparator.comparing(Pago::getFechaPago).reversed()
                    .thenComparing(Comparator.comparingDouble(Pago::getMonto).reversed()))
            .collect(Collectors.toList());
}

```

---

## 4. Agrupamiento de Datos (Dashboard)

*Uso de `Collectors.groupingBy` para generación de mapas y diccionarios.*

### 🔹 Distribución de Cartera por Estado

Genera un Mapa donde: `Clave = Estado`, `Valor = Lista de Préstamos`.

```java
public Map<EstadoPrestamo, List<Prestamo>> agruparCarteraPorEstado() {
    return prestamoRepo.listarPrestamos().stream()
            .collect(Collectors.groupingBy(Prestamo::getEstado));
}

```

### 🔹 Historial de Pagos por Préstamo

Agrupa todos los abonos asociados a cada ID de préstamo.

```java
public Map<Integer, List<Pago>> consolidarPagosPorPrestamo() {
    return pagoRepo.HistoricoDePagos().stream()
            .collect(Collectors.groupingBy(Pago::getPrestamoId));
}

```

---

## 5. Analítica y KPI (Estadísticas)

*Uso de `DoubleSummaryStatistics` para reportes financieros instantáneos.*

### 🔹 Métricas de Nómina

Cálculo automático de sumatorias, promedios, máx y min de salarios.

```java
public void imprimirMetricasNomina() {
    DoubleSummaryStatistics stats = empleadoRepo.listarTodosEmpleados().stream()
            .mapToDouble(Empleado::getSalario)
            .summaryStatistics();

    System.out.println("--- KPI NÓMINA ---");
    System.out.println("Promedio: " + stats.getAverage());
    System.out.println("Total:    " + stats.getSum());
    System.out.println("Máximo:   " + stats.getMax());
}

```

---

## 6. Validaciones Lógicas (Predicados)

*Verificaciones booleanas rápidas (`anyMatch`, `allMatch`, `findFirst`).*

### 🔹 Verificación de Existencia (Documento)

Retorna `true` si encuentra coincidencia.

```java
public boolean validarExistenciaCliente(String documento) {
    return clienteRepo.listarTodosClientes().stream()
            .anyMatch(c -> c.getDocumento().equals(documento));
}

```

### 🔹 Regla de Negocio: Salario Mínimo

Valida si toda la plantilla cumple con un salario base.

```java
public boolean validarPoliticaSalarial(double salarioBase) {
    return empleadoRepo.listarTodosEmpleados().stream()
            .allMatch(e -> e.getSalario() >= salarioBase);
}

```

### 🔹 Búsqueda Segura (Optional)

Busca un elemento específico sin riesgo de `NullPointerException`.

```java
public Empleado buscarEmpleadoSeguro(int id) {
    return empleadoRepo.listarTodosEmpleados().stream()
            .filter(e -> e.getId() == id)
            .findFirst()
            .orElse(null);
}

```

```

```

