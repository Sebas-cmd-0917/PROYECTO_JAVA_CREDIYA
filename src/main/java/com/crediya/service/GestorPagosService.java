package com.crediya.service;

import java.time.LocalDate;
import java.util.List;

import com.crediya.data.repositories.PagoDAOImpl;
import com.crediya.data.repositories.PrestamoDAOImpl;
import com.crediya.model.EstadoDeCuenta;
import com.crediya.model.EstadoPrestamo; // Importante para validar estados
import com.crediya.model.Pago;
import com.crediya.model.Prestamo;
import com.crediya.repository.PagoRepository;
import com.crediya.repository.PrestamoRepository;

public class GestorPagosService {
    private PagoRepository pagoRepo = new PagoDAOImpl();
    private PrestamoRepository prestamoRepo = new PrestamoDAOImpl();

    public String procesarPago(int idPrestamo, double dineroAbonado) throws Exception {
        
        // 1. VALIDACIÓN BÁSICA: Monto positivo
        if (dineroAbonado <= 0) {
            throw new Exception("El monto a pagar debe ser mayor a 0.");
        }

        // 2. BUSCAR PRÉSTAMO
        Prestamo p = prestamoRepo.obtenerPorId(idPrestamo);
        if (p == null) {
            throw new Exception("El préstamo con ID " + idPrestamo + " no existe.");
        }

        // 3. VALIDAR ESTADO (Regla de Negocio)
        // No se puede abonar a algo que ya se cerró
        if (p.getEstado() == EstadoPrestamo.PAGADO) {
            throw new Exception("❌ El préstamo ya está totalmente PAGADO.");
        }
        if (p.getEstado() == EstadoPrestamo.ANULADO) {
            throw new Exception("❌ No se pueden recibir abonos de un préstamo ANULADO.");
        }

        // 4. CÁLCULOS DE DEUDA
        double capitalBase = p.getMonto();
        double valorInteres = capitalBase * (p.getInteres() / 100);
        double deudaTotal = capitalBase + valorInteres;

        // Historial de lo que ya ha pagado
        List<Pago> historial = pagoRepo.ListarPagosPorPrestamo(idPrestamo);
        double yaPagado = 0;
        for (Pago pagoViejo : historial) {
            yaPagado += pagoViejo.getMonto();
        }

        double saldoPendiente = deudaTotal - yaPagado;

        // 5. VALIDAR SOBREPAGO (Con margen de 100 pesos por decimales)
        if (dineroAbonado > (saldoPendiente + 100)) {
            throw new Exception(String.format("❌ Estás intentando pagar $%,.0f pero la deuda es solo de $%,.0f", dineroAbonado, saldoPendiente));
        }

        // 6. GUARDAR EL PAGO
        Pago nuevoPago = new Pago();
        nuevoPago.setPrestamoId(idPrestamo);
        nuevoPago.setMonto(dineroAbonado);
        nuevoPago.setFechaPago(LocalDate.now()); 

        pagoRepo.registrarPago(nuevoPago);

        // 7. ACTUALIZAR ESTADO SI TERMINÓ
        double nuevoSaldo = saldoPendiente - dineroAbonado;
        
        if (nuevoSaldo <= 100) {
            // Actualizamos la BD a PAGADO
            prestamoRepo.actualizarEstado(idPrestamo, EstadoPrestamo.PAGADO.toString());
            return "¡Pago registrado y PRÉSTAMO PAGADO COMPLETAMENTE! (Estado actualizado a PAGADO)";
        }

        return "Pago registrado con éxito. Nuevo saldo pendiente: " + String.format("$%,.0f", nuevoSaldo);
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

    public List<Pago> obtenerPagosPorCliente(String documento) {
        return pagoRepo.listarPorDocumento(documento);
    }

    /* // ESTADO DE CUENTA (VERSIÓN CON IMPRESIÓN DIRECTA - COMENTADO POR SOLICITUD)
    public void generarEstadoDeCuenta(int idPrestamo) {
        // 1. Buscar el Préstamo (La Cabecera del reporte)
        Prestamo p = prestamoRepo.obtenerPorId(idPrestamo);

        if (p == null) {
            System.out.println("❌ El préstamo con ID " + idPrestamo + " no existe.");
            return;
        }

        // 2. Traer todos los pagos que se han hecho (El Detalle)
        List<Pago> historialPagos = pagoRepo.ListarPagosPorPrestamo(idPrestamo);

        // 3. Calcular Totales Matemáticos
        double capital = p.getMonto();
        double interesDecimal = p.getInteres() / 100;
        double montoInteres = capital * interesDecimal;
        double deudaTotalInicial = capital + montoInteres;

        double totalPagado = 0;

        // --- IMPRIMIR CABECERA ---
        System.out.println("\n========================================");
        System.out.println("       ESTADO DE CUENTA - CREDIYA       ");
        System.out.println("========================================");
        System.out.println("Préstamo N°:     " + p.getId());
        System.out.println("Cliente ID:      " + p.getClienteId()); 
        System.out.printf("Fecha Inicio:    %s\n", p.getFechaInicio());
        System.out.printf("Monto Prestado:  $%,.0f\n", capital);
        System.out.printf("Interés (%.1f%%): +$%,.0f\n", p.getInteres(), montoInteres);
        System.out.println("----------------------------------------");
        System.out.printf("DEUDA TOTAL:     $%,.0f\n", deudaTotalInicial);
        System.out.println("========================================\n");

        // --- IMPRIMIR HISTORIAL DE ABONOS ---
        System.out.println("HISTORIAL DE PAGOS REALIZADOS:");
        System.out.printf("%-12s %-15s %-15s\n", "FECHA", "MONTO ABONADO", "SALDO RESTANTE");
        System.out.println("----------------------------------------------");

        if (historialPagos.isEmpty()) {
            System.out.println("   (No se han registrado pagos aún)");
        } else {
            double saldoTemporal = deudaTotalInicial;

            for (Pago pago : historialPagos) {
                totalPagado += pago.getMonto();
                saldoTemporal -= pago.getMonto(); 

                System.out.printf("%-12s $%,-14.0f $%,-14.0f\n",
                        pago.getFechaPago(),
                        pago.getMonto(),
                        saldoTemporal);
            }
        }

        // --- IMPRIMIR RESUMEN FINAL ---
        double saldoFinal = deudaTotalInicial - totalPagado;

        System.out.println("----------------------------------------------");
        System.out.printf("TOTAL PAGADO:    $%,.0f\n", totalPagado);
        System.out.printf("SALDO PENDIENTE: $%,.0f\n", saldoFinal);

        String estadoActual = (saldoFinal <= 0) ? "¡PAZ Y SALVO!" : "PENDIENTE";
        System.out.println("ESTADO ACTUAL:   " + estadoActual);
        System.out.println("========================================\n");
    }
    */

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

        // 3. Empaquetar todo y devolverlo para que la UI decida cómo mostrarlo
        return new EstadoDeCuenta(p, historial, deudaTotalInicial, totalPagado, saldoFinal);
    }

    public void editarPago(Pago pago) {
        // Validación básica
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
        // Validación básica
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