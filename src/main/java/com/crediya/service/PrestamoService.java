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

public class PrestamoService {
    private final PrestamoRepository prestamoRepository = new PrestamoDAOImpl();
    private final CalculadoraPrestamosService calculadoraPrestamosService = new CalculadoraPrestamosService();
    private final ClienteRepository clienteRepository = new ClienteDAOImpl();
    private final EmpleadoRepository empleadoRepository = new EmpleadoDAOImpl();
    private final PagoRepository pagoRepository = new PagoDAOImpl();
    // private final EstadoDeCuenta reporteEstadoCuenta = new EstadoDeCuenta(null,
    // null, 0, 0, 0);

    public void registrarPrestamo(String clienteDoc, String empleadoDoc, double monto, double interes, int cuotas) {
        try {
            if (monto <= 0)
                throw new Exception("El monto debe ser positivo");
            if (monto < 50000)
                throw new Exception("El monto mínimo es de $50.000");
            if (monto > 20000000)
                throw new Exception("El monto máximo es de $20.000.000 por políticas de riesgo.");
            if (interes <= 0 || interes > 10)
                throw new Exception("El interés debe estar entre 0.1% y 10%.");
            if (cuotas < 1 || cuotas > 72)
                throw new Exception("El plazo debe ser entre 1 y 72 cuotas.");

            var clienteId = clienteRepository.buscarPorDocumentoCliente(clienteDoc);
            var empleadoId = empleadoRepository.buscarPorDocumentoEmpleado(empleadoDoc);

            if (clienteId == null)
                throw new Exception("Cliente no encontrado con documento: " + clienteDoc);
            if (empleadoId == null)
                throw new Exception("Empleado no encontrado con documento: " + empleadoDoc);

            int cliente = clienteId.getId();
            int empleado = empleadoId.getId();

            // model
            Prestamo prestamo = new Prestamo();
            prestamo.setClienteId(cliente);
            prestamo.setEmpleadoId(empleado);
            prestamo.setMonto(monto);
            prestamo.setInteres(interes);
            prestamo.setCuotas(cuotas);
            prestamo.setFechaInicio(LocalDate.now());
            prestamo.setEstado(EstadoPrestamo.PENDIENTE);// ESTADO INICIAL

            prestamoRepository.registrarPrestamo(prestamo);

            guardarEnArchivoTxt(prestamo);
            generarTicketPOS(prestamo, clienteId, empleadoId);

        } catch (Exception e) {
            System.out.println("❌ Error en el servicio de préstamos: " + e.getMessage());
        }
    }

    private void generarTicketPOS(Prestamo p, Cliente c, Empleado e) {
        String nombreCarpeta = "Tickets";

        File directorio = new File(nombreCarpeta);
        if (!directorio.exists()) {
            if (directorio.mkdir()) {
                System.out.println("📂 Carpeta 'comprobantes' creada automáticamente.");
            }
        }

        String fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String nombreArchivo = nombreCarpeta + File.separator + "ticket_prestamo_" + c.getDocumento() + "_" + fechaHora
                + ".txt";

        double cuotaMensual = calculadoraPrestamosService.calcularCuotaMensual(p.getMonto(), p.getInteres(),
                p.getCuotas());
        double totalPagar = cuotaMensual * p.getCuotas();

        try (FileWriter fw = new FileWriter(nombreArchivo);
                PrintWriter pw = new PrintWriter(fw)) {

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

        if (documento == null || !documento.matches("\\d{8,}")) {
            throw new IllegalArgumentException(
                    "Error: El documento debe contener solo números y tener mínimo 8 dígitos.");
        }
        // 1. PRIMERO: Traes la lista "cruda" del repositorio
        // (Esto es lo único que hacía tu función antes)
        List<Prestamo> lista = prestamoRepository.obtenerPrestamoPorDocumento(documento);

        // 2. AHORA: Le inyectamos los cálculos a cada préstamo
        for (Prestamo p : lista) {

            // A. Calcular Deuda Total (Capital + Interés)
            double interesDecimal = p.getInteres() / 100;
            double deudaTotal = p.getMonto() + (p.getMonto() * interesDecimal);

            // B. Ir a la tabla PAGOS y sumar lo que ha abonado este préstamo
            List<Pago> historial = pagoRepository.ListarPagosPorPrestamo(p.getId());
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

            // Determinamos cuál DEBERÍA ser el estado actual
            EstadoPrestamo estadoCalculado = EstadoPrestamo.PENDIENTE;

            if (saldoActual <= 100) {
                // Usamos el valor del Enum
                estadoCalculado = EstadoPrestamo.PAGADO;
            } else if (diasTranscurridos > 30) {
                // Revisa en tu archivo EstadoPrestamo.java si se llama MORA o EN_MORA
                estadoCalculado = EstadoPrestamo.MORA;
            }

            // --- C. Actualización Inteligente ---

            // Comparar Enums es seguro con equals o ==
            if (estadoCalculado != p.getEstado()) {

                // 2. CORRECCIÓN: Ahora sí acepta el valor porque es del tipo correcto
                p.setEstado(estadoCalculado);

                // 3. OJO AQUÍ CON LA BASE DE DATOS:
                // Tu repositorio seguramente espera un String para el SQL.
                // Usamos .name() o .toString() para convertir el Enum a texto para la BD.
                prestamoRepository.actualizarEstado(p.getId(), estadoCalculado.toString());
            }

            // 3. FINALMENTE: Entregas la lista ya calculada y lista para mostrar
            // --- D. Setear datos visuales ---
            p.setTotalPagado(sumaPagos);
            p.setSaldoPendiente(saldoActual);

        }
        return lista;
    }

    public EstadoDeCuenta generarReporte(int idPrestamo) {
        // 1. Buscar Datos
        Prestamo p = prestamoRepository.obtenerPorId(idPrestamo);
        if (p == null) {
            return null; // Retornamos null para indicar que no existe
        }
        List<Pago> historial = pagoRepository.ListarPagosPorPrestamo(idPrestamo);

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

    public void finalizarPrestamo(int prestamoId) {
        Prestamo p = prestamoRepository.obtenerPorId(prestamoId);

        if (p == null) {
            System.out.println("Erro: prestamo no encontrado. ");
            return;
        }

        if (p.getEstado() == com.crediya.model.EstadoPrestamo.PAGADO) {
            System.out.println("El prestamo ya se encuentra PAGADO");
            return;
        }

        double capital = p.getMonto();
        double interes = p.getMonto() * (p.getInteres() / 100);
        double deudaTotal = capital + interes;

        List<Pago> pagosRealizados = pagoRepository.ListarPagosPorPrestamo(prestamoId);
        double totalAbonado = 0;

        for (Pago unPago : pagosRealizados) {
            totalAbonado += unPago.getMonto();
        }

        double deudaPendiente = deudaTotal - totalAbonado;

        if (deudaPendiente <= 100) {
            prestamoRepository.actualizarEstado(prestamoId, "PAGADO");
            System.out.println("Felicitaciones Prestamo finalizado");
        } else {
            System.out.printf("No se puede finalizar. El cliente aún debe $%,.0f\n", deudaPendiente);
            System.out.println("   (Deuda Total: " + deudaTotal + " - Abonado: " + totalAbonado + ")");

        }
    }

    public void actualizarPrestamo(int id, double nuevoMonto, double nuevoInteres, int nuevasCuotas) {
        Prestamo p = prestamoRepository.obtenerPorId(id);

        if (p == null) {
            System.out.println("El prestamos no exites");
            return;
        }
        if (p.getEstado() != EstadoPrestamo.PENDIENTE) {
            System.out.println("No se puede editar un prestamo que no este PENDIENTE");
            return;
        }

        p.setMonto(nuevoMonto);
        p.setInteres(nuevoInteres);
        p.setCuotas(nuevasCuotas);

        prestamoRepository.actualizarPrestamo(p);
    }

    public void eliminarPrestamo(int id) throws Exception {
        Prestamo p = prestamoRepository.obtenerPorId(id);
        if (p == null)
            throw new Exception("El préstamo no existe.");

        // Validación de seguridad:
        if (p.getEstado() == EstadoPrestamo.PAGADO) {
            throw new Exception("No se puede eliminar un préstamo que ya fue PAGADO. Queda como histórico.");
        }
        // Si tuvieras lógica de abonos parciales, deberías validar que TotalPagado sea
        // 0.

        prestamoRepository.eliminarPrestamo(id);
    }

    // Método para guardar en el archivo maestro
    private void guardarEnArchivoTxt(Prestamo p) {
        // Formato: ID;CLIENTE_ID;MONTO;INTERES;CUOTAS;FECHA;ESTADO
        String linea = String.format("%d;%d;%.2f;%.2f;%d;%s;%s",
                p.getId(), p.getClienteId(), p.getMonto(), p.getInteres(),
                p.getCuotas(), p.getFechaInicio(), p.getEstado());

        try (FileWriter fw = new FileWriter("prestamos.txt", true);
                PrintWriter pw = new PrintWriter(fw)) {
            pw.println(linea);
        } catch (IOException e) {
            System.err.println("⚠ Advertencia: No se pudo guardar en el archivo de texto: " + e.getMessage());
        }
    }

    // Devuelve solo los préstamos pendientes usando Java Streams
    public List<Prestamo> filtrarPrestamosPendientes() {
        List<Prestamo> todos = prestamoRepository.listarPrestamos();
        return todos.stream()
                .filter(p -> p.getEstado() == EstadoPrestamo.PENDIENTE)
                .toList();
    }

    // Devuelve préstamos mayores a cierto monto
    public List<Prestamo> filtrarPorMontoMayorA(double montoMinimo) {
        return prestamoRepository.listarPrestamos().stream()
                .filter(p -> p.getMonto() >= montoMinimo)
                .toList();
    }

    public List<Prestamo> obtenerTodos() {
        return prestamoRepository.listarPrestamos();
    }

}
