package com.crediya.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.crediya.data.repositories.ClienteDAOImpl;
import com.crediya.data.repositories.EmpleadoDAOImpl;
import com.crediya.data.repositories.PagoDAOImpl;
import com.crediya.data.repositories.PrestamoDAOImpl;
import com.crediya.model.Cliente;
import com.crediya.model.Empleado;
import com.crediya.model.EstadoDeCuenta;
import com.crediya.model.EstadoPrestamo;
import com.crediya.model.Pago;
import com.crediya.model.Prestamo;
import com.crediya.repository.ClienteRepository;
import com.crediya.repository.EmpleadoRepository;
import com.crediya.repository.PagoRepository;
import com.crediya.repository.PrestamoRepository;
import com.crediya.util.Validador; // Importamos el Validador

public class PrestamoService {
    private final PrestamoRepository prestamoRepository = new PrestamoDAOImpl();
    private final CalculadoraPrestamosService calculadoraPrestamosService = new CalculadoraPrestamosService();
    private final ClienteRepository clienteRepository = new ClienteDAOImpl();
    private final EmpleadoRepository empleadoRepository = new EmpleadoDAOImpl();
    private final PagoRepository pagoRepository = new PagoDAOImpl();

    // --- REGISTRAR PRÉSTAMO ---
    public void registrarPrestamo(String clienteDoc, String empleadoDoc, double monto, double interes, int cuotas) {
        try {
            // 1. Validaciones de Formato (Documentos)
            if (!Validador.esNumericoYLongitud(clienteDoc, 5, 15)) 
                throw new Exception("❌ El documento del cliente no es válido.");
            if (!Validador.esNumericoYLongitud(empleadoDoc, 5, 15)) 
                throw new Exception("❌ El documento del empleado no es válido.");

            // 2. Reglas de Negocio Financieras
            if (monto <= 0) throw new Exception("El monto debe ser positivo");
            if (monto < 50000) throw new Exception("El monto mínimo es de $50.000");
            if (monto > 20000000) throw new Exception("El monto máximo es de $20.000.000 por políticas de riesgo.");
            
            if (interes <= 0 || interes > 10) throw new Exception("El interés debe estar entre 0.1% y 10%.");
            if (cuotas < 1 || cuotas > 72) throw new Exception("El plazo debe ser entre 1 y 72 cuotas.");

            // 3. Validar Existencia en BD
            var cliente = clienteRepository.buscarPorDocumentoCliente(clienteDoc);
            if (cliente == null) throw new Exception("❌ Cliente no encontrado con documento: " + clienteDoc);
            
            var empleado = empleadoRepository.buscarPorDocumentoEmpleado(empleadoDoc);
            if (empleado == null) throw new Exception("❌ Empleado no encontrado con documento: " + empleadoDoc);

            // 4. Crear Modelo
            Prestamo prestamo = new Prestamo();
            prestamo.setClienteId(cliente.getId());
            prestamo.setEmpleadoId(empleado.getId());
            prestamo.setMonto(monto);
            prestamo.setInteres(interes);
            prestamo.setCuotas(cuotas);
            prestamo.setFechaInicio(LocalDate.now());
            prestamo.setEstado(EstadoPrestamo.PENDIENTE);

            // 5. Guardar
            prestamoRepository.registrarPrestamo(prestamo);

            // 6. Archivos y Tickets
            guardarEnArchivoTxt(prestamo);
            generarTicketPOS(prestamo, cliente, empleado);

        } catch (Exception e) {
            System.out.println("❌ Error en el servicio de préstamos: " + e.getMessage());
        }
    }

    private void generarTicketPOS(Prestamo p, Cliente c, Empleado e) {
        String nombreCarpeta = "Tickets";
        File directorio = new File(nombreCarpeta);
        if (!directorio.exists()) {
            if (directorio.mkdir()) {
                System.out.println("📂 Carpeta 'Tickets' creada automáticamente.");
            }
        }

        String fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String nombreArchivo = nombreCarpeta + File.separator + "ticket_prestamo_" + c.getDocumento() + "_" + fechaHora + ".txt";

        double cuotaMensual = calculadoraPrestamosService.calcularCuotaMensual(p.getMonto(), p.getInteres(), p.getCuotas());
        double totalPagar = cuotaMensual * p.getCuotas();

        try (FileWriter fw = new FileWriter(nombreArchivo); PrintWriter pw = new PrintWriter(fw)) {
            pw.println("================================");
            pw.println("        CREDIYA S.A.S.          ");
            pw.println("     Nit: 900.123.456-7         ");
            pw.println("   Calle Falsa 123, Ciudad      ");
            pw.println("================================");
            pw.println("FECHA: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            pw.println("TICKET REF: " + fechaHora);
            pw.println("--------------------------------");
            pw.println("DATOS DEL CLIENTE");
            pw.println("NOMBRE: " + c.getNombre());
            pw.println("DOC:    " + c.getDocumento());
            pw.println("--------------------------------");
            pw.println("ATENDIDO POR:");
            pw.println("ASESOR: " + e.getNombre());
            pw.println("================================");
            pw.println("      DETALLE DEL CREDITO       ");
            pw.println("--------------------------------");
            pw.printf("MONTO PRESTADO:   $%,10.2f%n", p.getMonto());
            pw.printf("TASA INTERES:     %10.2f%%%n", p.getInteres());
            pw.printf("PLAZO (MESES):    %10d%n", p.getCuotas());
            pw.println("--------------------------------");
            pw.printf("VALOR CUOTA:      $%,10.2f%n", cuotaMensual);
            pw.println("--------------------------------");
            pw.printf("TOTAL A PAGAR:    $%,10.2f%n", totalPagar);
            pw.println("================================");
            pw.println("   GRACIAS POR SU CONFIANZA     ");
            pw.println("     CONSERVE ESTE TICKET       ");
            pw.println("================================");

            System.out.println("🧾 Ticket generado exitosamente: " + nombreArchivo);

        } catch (Exception ex) {
            System.out.println("⚠ No se pudo generar el ticket: " + ex.getMessage());
        }
    }

    public List<Prestamo> obtenerPrestamoPorDocumento(String documento) {
        // Validación con nuestra clase utilitaria
        if (!Validador.esNumericoYLongitud(documento, 5, 15)) {
            throw new IllegalArgumentException("Error: Documento inválido.");
        }

        List<Prestamo> lista = prestamoRepository.obtenerPrestamoPorDocumento(documento);

        // Lógica de cálculo de saldos y estados
        for (Prestamo p : lista) {
            double interesDecimal = p.getInteres() / 100;
            double deudaTotal = p.getMonto() + (p.getMonto() * interesDecimal);

            List<Pago> historial = pagoRepository.ListarPagosPorPrestamo(p.getId());
            double sumaPagos = 0;
            if (historial != null) {
                for (Pago unPago : historial) {
                    sumaPagos += unPago.getMonto();
                }
            }

            double saldoActual = deudaTotal - sumaPagos;
            
            // Actualización automática de estados (MORA / PAGADO)
            long diasTranscurridos = ChronoUnit.DAYS.between(p.getFechaInicio(), LocalDate.now());
            EstadoPrestamo estadoCalculado = p.getEstado();

            if (saldoActual <= 100) {
                estadoCalculado = EstadoPrestamo.PAGADO;
            } else if (diasTranscurridos > 30 && p.getEstado() == EstadoPrestamo.PENDIENTE) {
                estadoCalculado = EstadoPrestamo.MORA;
            }

            if (estadoCalculado != p.getEstado()) {
                p.setEstado(estadoCalculado);
                prestamoRepository.actualizarEstado(p.getId(), estadoCalculado.toString());
            }

            p.setTotalPagado(sumaPagos);
            p.setSaldoPendiente(saldoActual);
        }
        return lista;
    }

    public EstadoDeCuenta generarReporte(int idPrestamo) {
        Prestamo p = prestamoRepository.obtenerPorId(idPrestamo);
        if (p == null) return null;
        
        List<Pago> historial = pagoRepository.ListarPagosPorPrestamo(idPrestamo);

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

        return new EstadoDeCuenta(p, historial, deudaTotalInicial, totalPagado, saldoFinal);
    }

    public void finalizarPrestamo(int prestamoId) {
        Prestamo p = prestamoRepository.obtenerPorId(prestamoId);
        if (p == null) {
            System.out.println("Error: préstamo no encontrado.");
            return;
        }
        if (p.getEstado() == EstadoPrestamo.PAGADO) {
            System.out.println("El préstamo ya se encuentra PAGADO");
            return;
        }
        // ... (Lógica de finalización si se requiere manual) ...
        // Generalmente esto se maneja automático en pagos, pero se deja por si acaso.
    }

    public void actualizarPrestamo(int id, double nuevoMonto, double nuevoInteres, int nuevasCuotas) {
        Prestamo p = prestamoRepository.obtenerPorId(id);
        if (p == null) {
            System.out.println("El préstamo no existe");
            return;
        }
        if (p.getEstado() != EstadoPrestamo.PENDIENTE) {
            System.out.println("No se puede editar un préstamo que no esté PENDIENTE");
            return;
        }
        p.setMonto(nuevoMonto);
        p.setInteres(nuevoInteres);
        p.setCuotas(nuevasCuotas);
        prestamoRepository.actualizarPrestamo(p);
    }

    public void eliminarPrestamo(int id) throws Exception {
        Prestamo p = prestamoRepository.obtenerPorId(id);
        if (p == null) throw new Exception("El préstamo no existe.");
        if (p.getEstado() == EstadoPrestamo.PAGADO) {
            throw new Exception("No se puede eliminar un préstamo PAGADO (Histórico).");
        }
        prestamoRepository.eliminarPrestamo(id);
    }

    private void guardarEnArchivoTxt(Prestamo p) {
        // Formato: ID;CLIENTE_ID;MONTO;INTERES;CUOTAS;FECHA;ESTADO
        String linea = String.format("%d;%d;%.2f;%.2f;%d;%s;%s",
                p.getId(), p.getClienteId(), p.getMonto(), p.getInteres(),
                p.getCuotas(), p.getFechaInicio(), p.getEstado());

        try (FileWriter fw = new FileWriter("prestamos.txt", true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(linea);
        } catch (IOException e) {
            System.err.println("⚠ Advertencia: No se pudo guardar en prestamos.txt: " + e.getMessage());
        }
    }

    // =============================================================
    //   MÉTODOS DE REPORTES (Streams & Lambdas)
    // =============================================================

    public List<Prestamo> filtrarPrestamosPendientes() {
        return prestamoRepository.listarPrestamos().stream()
                .filter(p -> p.getEstado() == EstadoPrestamo.PENDIENTE)
                .toList();
    }

    public List<Prestamo> filtrarPorMontoMayorA(double montoMinimo) {
        return prestamoRepository.listarPrestamos().stream()
                .filter(p -> p.getMonto() >= montoMinimo)
                .toList();
    }
    
    // Estos son los nuevos métodos para el MenuReportes.java
    
    public List<Prestamo> reportePrestamosEnMora() {
        return prestamoRepository.listarPrestamos().stream()
                .filter(p -> p.getEstado() == EstadoPrestamo.MORA)
                .toList();
    }

    public List<Prestamo> reportePrestamosPagados() {
        return prestamoRepository.listarPrestamos().stream()
                .filter(p -> p.getEstado() == EstadoPrestamo.PAGADO)
                .toList();
    }
    
    public List<Prestamo> reportePrestamosPorMontoMayorA(double monto) {
        return filtrarPorMontoMayorA(monto); // Reutilizamos
    }

    public double reporteTotalDineroPrestado() {
        return prestamoRepository.listarPrestamos().stream()
                .mapToDouble(Prestamo::getMonto)
                .sum();
    }

    public List<Prestamo> obtenerTodos() {
        return prestamoRepository.listarPrestamos();
    }
}