package com.crediya.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.crediya.data.repositories.PagoDAOImpl;
import com.crediya.data.repositories.PrestamoDAOImpl;
import com.crediya.model.EstadoDeCuenta;
import com.crediya.model.Pago;
import com.crediya.model.Prestamo;
import com.crediya.repository.PagoRepository;
import com.crediya.repository.PrestamoRepository;

public class GestorPagosService {
    private PagoRepository pagoRepo = new PagoDAOImpl();
    private PrestamoRepository prestamoRepo = new PrestamoDAOImpl();

    // En GestorPagosService.java

    public String procesarPago(int idPrestamo, double dineroAbonado) throws Exception {
        // "throws Exception" avisa que este método puede fallar y el menú debe estar
        // atento

        // 1. Buscamos el préstamo
        Prestamo p = prestamoRepo.obtenerPorId(idPrestamo);
        if (p == null) {
            throw new Exception("El préstamo con ID " + idPrestamo + " no existe.");
        }

        // 2. Cálculos Matemáticos (Lógica pura, sin impresiones)
        double capitalBase = p.getMonto();
        double valorInteres = capitalBase * (p.getInteres() / 100);
        double deudaTotal = capitalBase + valorInteres;

        // 3. Calculamos historial usando el NUEVO método del repo
        List<Pago> historial = pagoRepo.ListarPagosPorPrestamo(idPrestamo);

        double yaPagado = 0;
        for (Pago pagoViejo : historial) {
            yaPagado += pagoViejo.getMonto();
        }

        double saldoPendiente = deudaTotal - yaPagado;

        // 4. VALIDACIÓN LÓGICA
        if (dineroAbonado <= 0) {
            throw new Exception("El monto a pagar debe ser mayor a 0.");
        }

        // Usamos una pequeña tolerancia (0.01) por si hay decimales locos
        if (dineroAbonado > (saldoPendiente + 0.01)) {
            throw new Exception("Estás intentando pagar " + dineroAbonado +
                    " pero el saldo pendiente es solo " + saldoPendiente);
        }

        // 5. GUARDAR (Si pasó todas las validaciones anteriores)
        Pago nuevoPago = new Pago();
        nuevoPago.setPrestamoId(idPrestamo);
        nuevoPago.setMonto(dineroAbonado);
        nuevoPago.setFechaPago(LocalDate.now()); // O la fecha que quieras

        pagoRepo.registrarPago(nuevoPago);

        // 6. RETORNAR MENSAJE DE ÉXITO (El Service le cuenta al Menu qué pasó)
        double nuevoSaldo = saldoPendiente - dineroAbonado;
        return "Pago registrado con éxito. Nuevo saldo pendiente: " + nuevoSaldo;
    }

    public List<Prestamo> obtenerPrestamoPorDocumento(String documento) {

        if (documento == null || !documento.matches("\\d{8,}")) {
            throw new IllegalArgumentException(
                    "Error: El documento debe contener solo números y tener mínimo 8 dígitos.");
        }
        // 1. PRIMERO: Traes la lista "cruda" del repositorio
        // (Esto es lo único que hacía tu función antes)
        List<Prestamo> lista = prestamoRepo.obtenerPrestamoPorDocumento(documento);

        // 2. AHORA: Le inyectamos los cálculos a cada préstamo
        for (Prestamo p : lista) {

            // A. Calcular Deuda Total (Capital + Interés)
            double interesDecimal = p.getInteres() / 100;
            double deudaTotal = p.getMonto() + (p.getMonto() * interesDecimal);

            // B. Ir a la tabla PAGOS y sumar lo que ha abonado este préstamo
            List<Pago> historial = pagoRepo.ListarPagosPorPrestamo(p.getId());
            double sumaPagos = 0;

            if (historial != null) {
                for (Pago unPago : historial) {
                    sumaPagos += unPago.getMonto();
                }
            }

            // C. Calcular el Saldo Final
            double saldoActual = deudaTotal - sumaPagos;
            // --- LÓGICA DE ESTADOS (NUEVO) ---

            // Paso A: Calcular días transcurridos desde que se creó el préstamo
            long diasTranscurridos = ChronoUnit.DAYS.between(p.getFechaInicio(), LocalDate.now());

            // Paso B: Determinar el estado
            String nuevoEstado = "PENDIENTE"; // Estado por defecto

            if (saldoActual <= 0) {
                nuevoEstado = "PAGADO";
            } else if (diasTranscurridos > 30) {
                nuevoEstado = "EN MORA";
            }

            // D. Guardar los datos en el objeto para mostrarlos
            p.setTotalPagado(sumaPagos);
            p.setSaldoPendiente(saldoActual);
            // p.setEstado(nuevoEstado);
        }

        // 3. FINALMENTE: Entregas la lista ya calculada y lista para mostrar
        return lista;
    }

    public List<Pago> obtenerHistorialDePagos() {
        return pagoRepo.HistoricoDePagos();
    }

    public List<Pago> obtenerPagosPorPrestamo(int prestamoId) {
        Prestamo p = prestamoRepo.obtenerPorId(prestamoId);
        if (p == null) {
            System.out.println("❌ Error: Préstamo no encontrado.");
            return null;
        } else {
            System.out.println("Préstamo encontrado: ID " + p.getId() + ", Monto " + p.getMonto());
        }
        return pagoRepo.ListarPagosPorPrestamo(prestamoId);
    }
    // En GestorPagosService.java

    public List<Pago> obtenerPagosPorCliente(String documento) {
        List<Pago> pagos = pagoRepo.listarPorDocumento(documento);

        // Opcional: Podrías lanzar una excepción si la lista está vacía si quisieras
        // pero retornar lista vacía también es válido.
        return pagos;
    }

    // public void generarEstadoDeCuenta(int idPrestamo) {
    //     // 1. Buscar el Préstamo (La Cabecera del reporte)
    //     Prestamo p = prestamoRepo.obtenerPorId(idPrestamo);

    //     if (p == null) {
    //         System.out.println("❌ El préstamo con ID " + idPrestamo + " no existe.");
    //         return;
    //     }

    //     // 2. Traer todos los pagos que se han hecho (El Detalle)
    //     List<Pago> historialPagos = pagoRepo.ListarPagosPorPrestamo(idPrestamo);

    //     // 3. Calcular Totales Matemáticos
    //     double capital = p.getMonto();
    //     double interesDecimal = p.getInteres() / 100;
    //     double montoInteres = capital * interesDecimal;
    //     double deudaTotalInicial = capital + montoInteres;

    //     double totalPagado = 0;

    //     // --- IMPRIMIR CABECERA ---
    //     System.out.println("\n========================================");
    //     System.out.println("       ESTADO DE CUENTA - CREDIYA       ");
    //     System.out.println("========================================");
    //     System.out.println("Préstamo N°:     " + p.getId());
    //     System.out.println("Cliente ID:      " + p.getClienteId()); // Si hiciste el JOIN, usa p.getNombreCliente()
    //     System.out.printf("Fecha Inicio:    %s\n", p.getFechaInicio());
    //     System.out.printf("Monto Prestado:  $%,.0f\n", capital);
    //     System.out.printf("Interés (%.1f%%): +$%,.0f\n", p.getInteres(), montoInteres);
    //     System.out.println("----------------------------------------");
    //     System.out.printf("DEUDA TOTAL:     $%,.0f\n", deudaTotalInicial);
    //     System.out.println("========================================\n");

    //     // --- IMPRIMIR HISTORIAL DE ABONOS ---
    //     System.out.println("HISTORIAL DE PAGOS REALIZADOS:");
    //     System.out.printf("%-12s %-15s %-15s\n", "FECHA", "MONTO ABONADO", "SALDO RESTANTE");
    //     System.out.println("----------------------------------------------");

    //     if (historialPagos.isEmpty()) {
    //         System.out.println("   (No se han registrado pagos aún)");
    //     } else {
    //         // Variable temporal para ir restando mientras mostramos la lista
    //         double saldoTemporal = deudaTotalInicial;

    //         for (Pago pago : historialPagos) {
    //             totalPagado += pago.getMonto();
    //             saldoTemporal -= pago.getMonto(); // Vamos bajando la deuda renglón por renglón

    //             System.out.printf("%-12s $%,-14.0f $%,-14.0f\n",
    //                     pago.getFechaPago(),
    //                     pago.getMonto(),
    //                     saldoTemporal);
    //         }
    //     }

    //     // --- IMPRIMIR RESUMEN FINAL ---
    //     double saldoFinal = deudaTotalInicial - totalPagado;

    //     System.out.println("----------------------------------------------");
    //     System.out.printf("TOTAL PAGADO:    $%,.0f\n", totalPagado);
    //     System.out.printf("SALDO PENDIENTE: $%,.0f\n", saldoFinal);

    //     // Un detalle bonito: Estado del préstamo
    //     String estadoActual = (saldoFinal <= 0) ? "¡PAZ Y SALVO!" : "PENDIENTE";
    //     System.out.println("ESTADO ACTUAL:   " + estadoActual);
    //     System.out.println("========================================\n");
    // }


    public EstadoDeCuenta generarReporte(int idPrestamo) {
    // 1. Buscar Datos
    Prestamo p = prestamoRepo.obtenerPorId(idPrestamo);
    if (p == null) {
        return null; // Retornamos null para indicar que no existe
    }
    List<Pago> historial = pagoRepo.ListarPagosPorPrestamo(idPrestamo);

    // 2. Cálculos Matemáticos
    double capital = p.getMonto();
    double interesDecimal = p.getInteres() / 100;
    double montoInteres = capital * interesDecimal;
    double deudaTotalInicial = capital + montoInteres;

    double totalPagado = 0;
    if (historial != null) {
        for (Pago pago : historial) {
            totalPagado += pago.getMonto();
        }
    }
    
    double saldoFinal = deudaTotalInicial - totalPagado;

    // 3. Empaquetar todo y devolverlo
    return new EstadoDeCuenta(p, historial, deudaTotalInicial, totalPagado, saldoFinal);
}
    // EN: PagoService.java

    public void editarPago(Pago pago) {
        // Aquí podrías validar cosas antes de enviar (ej: que el monto sea positivo)
        if (pago.getMonto() < 0) {
            System.out.println("❌ Error: No se puede actualizar a un monto negativo.");
            return;
        }

        boolean exito = pagoRepo.modificarPago(pago);

        if (exito) {
            System.out.println("✅ Pago actualizado correctamente.");
        } else {
            System.out.println("⚠ No se encontró un pago con ese ID para actualizar.");
        }
    }

    public void borrarPago(int id) {
        // Podrías validar que el ID sea lógico (mayor a 0)
        if (id <= 0) {
            System.out.println("❌ ID inválido.");
            return;
        }

        boolean exito = pagoRepo.eliminarPago(id);

        if (exito) {
            System.out.println("✅ Pago eliminado del sistema.");
        } else {
            System.out.println("⚠ No se pudo eliminar. El ID no existe.");
        }
    }

}
