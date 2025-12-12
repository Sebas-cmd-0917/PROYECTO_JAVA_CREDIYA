package com.crediya.service;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.crediya.data.repositories.ClienteDAOImpl;
import com.crediya.data.repositories.EmpleadoDAOImpl;
import com.crediya.data.repositories.PrestamoDAOImpl;
import com.crediya.model.Cliente;
import com.crediya.model.Empleado;
import com.crediya.model.Prestamo;
import com.crediya.repository.ClienteRepository;
import com.crediya.repository.EmpleadoRepository;
import com.crediya.repository.PrestamoRepository;

public class PrestamoService {
    private final PrestamoRepository prestamoRepository = new PrestamoDAOImpl();
    private final CalculadoraPrestamosService calculadoraPrestamosService = new CalculadoraPrestamosService();
    
    private final ClienteRepository clienteRepository = new ClienteDAOImpl();
    private final EmpleadoRepository empleadoRepository = new EmpleadoDAOImpl();

    public void registrarPrestamo(String clienteDoc, String empleadoDoc, double monto, double interes, int cuotas){
        try {
            if (monto <= 0) throw new Exception("El monto debe ser positivo");
            var clienteId = clienteRepository.buscarPorDocumentoCliente(clienteDoc);
            var empleadoId = empleadoRepository.buscarPorDocumentoEmpleado(empleadoDoc);

            if (clienteId == null) throw new Exception("Cliente no encontrado con documento: " + clienteDoc);
            if (empleadoId == null) throw new Exception("Empleado no encontrado con documento: " + empleadoDoc);

            int cliente = clienteId.getId();
            int empleado = empleadoId.getId();

            //model
            Prestamo prestamo = new Prestamo();
            prestamo.setClienteId(cliente);
            prestamo.setEmpleadoId(empleado);
            prestamo.setMonto(monto);
            prestamo.setInteres(interes);
            prestamo.setCuotas(cuotas);
            prestamo.setFechaInicio(LocalDate.now());
            prestamo.setEstado("PENDIENTE");// ESTADO INICIAL

            prestamoRepository.registrarPrestamo(prestamo);

            // guardarEnArchivoTxt(prestamo);

            generarTicketPOS(prestamo, clienteId, empleadoId);

        } catch (Exception e) {
            System.out.println("❌ Error en el servicio de préstamos: " + e.getMessage());
        }
    }

    private void generarTicketPOS(Prestamo p, Cliente c, Empleado e){
        String nombreCarpeta = "Tickets";
        
        File directorio = new File(nombreCarpeta);
        if (!directorio.exists()) {
            if (directorio.mkdir()) {
                System.out.println("📂 Carpeta 'comprobantes' creada automáticamente.");
            }
        }

        String fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String nombreArchivo = nombreCarpeta + File.separator + "ticket_prestamo_" + c.getDocumento() + "_" + fechaHora + ".txt";

        
        double cuotaMensual = calculadoraPrestamosService.calcularCuotaMensual(p.getMonto(), p.getInteres(), p.getCuotas());
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

    // private void guardarEnArchivoTxt(Prestamo p){
    //     double cuotaMensual = calculadoraPrestamosService.calcularCuotaMensual(p.getMonto(), p.getInteres(), p.getCuotas());
    //     double totalPagar = cuotaMensual * p.getCuotas();

    //     String linea = String.format("%d;%.2f;%.2f;%.2f;%s;%s", p.getClienteId(), p.getMonto(), p.getInteres(), totalPagar, p.getEstado(), p.getFechaInicio());
    //     try (FileWriter fw = new FileWriter("prestamos.txt", true);
    //         PrintWriter pw = new PrintWriter(fw)){
    //             pw.println(linea);
    //             System.out.println("📁 Respaldo guardado en 'prestamos.txt'");
            
    //     } catch (Exception e) {
    //         System.out.println("⚠ No se pudo guardar en el archivo de texto: " + e.getMessage());
    //     }

    // }

    public void finalizarPrestamo(int prestamoId){
        prestamoRepository.actualizarEstado(prestamoId, "PAGADO");

    }

   public List<Prestamo> obtenerTodos(){
       return prestamoRepository.listarPrestamos();
    }

    public List<Prestamo> obtenerPorDocumento(String documento){
        return prestamoRepository.obtenerPrestamoPorDocumento(documento);
    }

}
    

