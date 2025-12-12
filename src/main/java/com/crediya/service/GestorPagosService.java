package com.crediya.service;

import java.time.LocalDate;
import java.util.List;

import com.crediya.data.repositories.PagoDAOImpl;
import com.crediya.data.repositories.PrestamoDAOImpl;
import com.crediya.model.Pago;
import com.crediya.model.Prestamo;
import com.crediya.repository.PagoRepository;
import com.crediya.repository.PrestamoRepository;

public class GestorPagosService {
    private PagoRepository pagoRepo = new PagoDAOImpl();
    private PrestamoRepository prestamoRepo = new PrestamoDAOImpl();

    // 2. Fase de Pago: Recibe el ID elegido y el Dinero
    public void procesarPago(int idPrestamo, double dineroAbonado) {

        // 1. Buscamos el préstamo
    Prestamo p = prestamoRepo.obtenerPorId(idPrestamo);

    if (p == null) {
        System.out.println("❌ Error: Préstamo no encontrado.");
        return;
    }

    // --- AQUÍ ESTÁ EL CAMBIO: DESGLOSE MATEMÁTICO ---
    
    // A. Deuda Base (El dinero que le prestaste)
    double capitalBase = p.getMonto();

    // B. El Interés (El dinero extra)
    // Fórmula: 5.000.000 * (1 / 100) = 50.000
    double valorInteres = capitalBase * (p.getInteres() / 100);

    // C. La Deuda Total (Suma de los dos anteriores)
    double deudaTotal = capitalBase + valorInteres;

    // --- IMPRIMIMOS EL DESGLOSE EN PANTALLA ---
    System.out.println("\n--- DETALLE DE LA DEUDA ---");
    System.out.printf("💰 Capital Prestado:  $%,.0f\n", capitalBase);
    System.out.printf("📈 Intereses (%.1f%%): +$%,.0f\n", p.getInteres(), valorInteres);
    System.out.println("---------------------------");
    System.out.printf("TOTAL DEUDA REAL:    $%,.0f\n", deudaTotal);
    System.out.println("---------------------------\n");


    // 2. Calculamos historial (Lo que ya ha pagado antes)
    List<Pago> historial = pagoRepo.ListarPagosPorPrestamo(idPrestamo);
    double yaPagado = 0;
    for (Pago pagoViejo : historial) {
        yaPagado += pagoViejo.getMonto();
    }

    System.out.println(historial.size() + " pagos previos encontrados. Total abonado: $" + yaPagado);

    // 3. Calculamos cuánto le falta HOY
    double saldoPendiente = deudaTotal - yaPagado;

    // 4. VALIDACIÓN
    if (dineroAbonado > saldoPendiente) {
        System.out.printf("❌ Error: Estás intentando pagar $%,.0f pero solo debes $%,.0f\n", 
                          dineroAbonado, saldoPendiente);
    } else {
        // 5. GUARDAR
        Pago nuevoPago = new Pago();
        nuevoPago.setPrestamoId(idPrestamo);
        nuevoPago.setMonto(dineroAbonado);
        nuevoPago.setFechaPago(LocalDate.now());
        
        pagoRepo.registrarPago(nuevoPago);
        
        System.out.println("✅ ¡Abono registrado exitosamente!");
        System.out.printf("📉 Nuevo Saldo Pendiente: $%,.0f\n", (saldoPendiente - dineroAbonado));
    }
    }

    public List<Prestamo> obtenerPrestamoPorDocumento(String documento) {

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

        // D. Guardar los datos en el objeto para mostrarlos
        p.setTotalPagado(sumaPagos);
        p.setSaldoPendiente(saldoActual);
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
    System.out.println("Cliente ID:      " + p.getClienteId()); // Si hiciste el JOIN, usa p.getNombreCliente()
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
        // Variable temporal para ir restando mientras mostramos la lista
        double saldoTemporal = deudaTotalInicial;

        for (Pago pago : historialPagos) {
            totalPagado += pago.getMonto();
            saldoTemporal -= pago.getMonto(); // Vamos bajando la deuda renglón por renglón

            System.out.printf("%-12s $%,-14.0f $%,-14.0f\n", 
                pago.getFechaPago(), 
                pago.getMonto(), 
                saldoTemporal
            );
        }
    }

    // --- IMPRIMIR RESUMEN FINAL ---
    double saldoFinal = deudaTotalInicial - totalPagado;
    
    System.out.println("----------------------------------------------");
    System.out.printf("TOTAL PAGADO:    $%,.0f\n", totalPagado);
    System.out.printf("SALDO PENDIENTE: $%,.0f\n", saldoFinal);
    
    // Un detalle bonito: Estado del préstamo
    String estadoActual = (saldoFinal <= 0) ? "¡PAZ Y SALVO!" : "PENDIENTE";
    System.out.println("ESTADO ACTUAL:   " + estadoActual);
    System.out.println("========================================\n");
}



}
