package com.crediya.ui;

import java.util.Scanner;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.List;

import com.crediya.service.GestorPagosService;
import com.crediya.service.PrestamoService;
import com.crediya.model.Prestamo;
import com.crediya.model.EstadoDeCuenta;
import com.crediya.model.Pago;

public class MenuPago {
    Scanner scanner = new Scanner(System.in);
    GestorPagosService gestorPagosService = new GestorPagosService();
    PrestamoService prestamoService = new PrestamoService();
    MenuPrestamos menuPrestamos = new MenuPrestamos();

    public void mostrarMenuPago() {

        while (true) {
            System.out.println("\n===== 📌 MENÚ DE PAGOS =====");
            System.out.println("1. Registrar pago");
            System.out.println("2. Modificar un pago");
            System.out.println("3. Eliminar un pago");
            System.out.println("4. Ver historial de pagos");
            System.out.println("5. Ver estado de cuenta");
            System.out.println("6. Ver prestamos por documento");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");
            try {
                int opcion = scanner.nextInt();
                scanner.nextLine(); // limpiar buffer

                switch (opcion) {
                    case 1:
                        crearPago();
                        break;
                    case 2:
                        modificarPago();
                        break;
                    case 3:
                        eliminarPago();
                        break;
                    case 4:
                        historialDePagos();
                        break;
                    case 5:
                        mostrarEstadoDeCuenta();
                        break;
                    case 6:
                        System.out.println("Ingrese el documento del cliente:");
                        String documento = scanner.nextLine();
                        menuPrestamos.verPrestamosPorDocumento(documento);
                        break;
                    case 0:
                        System.out.println("Volviendo al menú principal...");
                        return;
                    default:
                        System.out.println("❌ Opción inválida");
                }
            } catch (InputMismatchException e) {
                System.out.println("❌ Error: Ingresaste una letra o símbolo. Por favor ingresa un NÚMERO.");

                // ¡¡MUY IMPORTANTE!!: Limpiar el "basurero" del scanner
                scanner.nextLine();
            }

        }

    }

    private void crearPago() {
        System.out.println("\n--- CAJA DE PAGOS ---");

        // 1. PIDE CÉDULA
        System.out.print("Ingrese Cédula del Cliente: ");
        String cedula = scanner.nextLine();

        // 2. LLAMA A LA LISTA (Fase 1)
        menuPrestamos.verPrestamosPorDocumento(cedula);

        System.out.println("--- REALIZAR ABONO ---");

        System.out.print("Ingrese ID del Préstamo: ");
        int idPrestamo = scanner.nextInt();

        System.out.print("Ingrese Monto a Abonar: ");
        double monto = scanner.nextDouble();

        try {
            // Llamamos al servicio y esperamos el mensaje de éxito
            String resultado = gestorPagosService.procesarPago(idPrestamo, monto);

            // Si llega aquí, es que todo salió BIEN
            System.out.println("✅ " + resultado);

        } catch (Exception e) {
            // Si algo salió MAL (Préstamo no existe, pago excesivo, etc.), cae aquí
            System.out.println("❌ ERROR: " + e.getMessage());
        }
    }

    private void historialDePagos() {
        System.out.println("\n--- HISTORIAL DE PAGOS ---");
        List<Pago> historial = gestorPagosService.obtenerHistorialDePagos(); // Tu función

        // 1. Línea superior (El techo de la tabla)
        System.out.println("+-----+--------------+----------------------+---------------+");

        // 2. Encabezados (Usamos printf para alinearlos igual que los datos)
        // %-3s significa: Reserva 3 espacios y alinea a la izquierda
        System.out.printf("| %-3s | %-12s | %-20s | %-13s |\n", "ID", "FECHA", "CLIENTE", "MONTO");

        // 3. Línea separadora
        System.out.println("+-----+--------------+----------------------+---------------+");

        // 4. Los Datos
        for (Pago p : historial) {
            System.out.printf("| %-3d | %-12s | %-20s | $%,12.0f |\n",
                    p.getId(), // ID (número pequeño)
                    p.getFechaPago(), // Fecha (ancho 12)
                    p.getNombreCliente(), // Cliente (ancho 20 para nombres largos)
                    p.getMonto()); // Monto (alineado a la derecha para ver los ceros)
        }

        // 5. Línea inferior (El piso de la tabla)
        System.out.println("+-----+--------------+----------------------+---------------+");
    }

    // public void verEstadoDeCuenta() {
    // System.out.println("\n--- GENERAR ESTADO DE CUENTA ---");

    // // 1. Pedir Cédula para no obligar a memorizar IDs
    // System.out.print("Ingrese Documento del Cliente: ");
    // String doc = scanner.nextLine();

    // // 2. Buscar préstamos de esa persona (Usando la función que ya tienes)
    // List<Prestamo> lista = prestamoService.obtenerPrestamoPorDocumento(doc); // O
    // consultarPrestamosPorCedula

    // if (lista != null && !lista.isEmpty()) {

    // menuPrestamos.verPrestamosPorDocumento(doc);

    // // 3. Pedir el ID específico
    // System.out.print("\nIngrese el NÚMERO del préstamo a consultar: ");
    // int idSeleccionado = scanner.nextInt();

    // // 4. ¡GENERAR EL REPORTE!
    // gestorPagosService.generarEstadoDeCuenta(idSeleccionado);

    // } else {
    // System.out.println("❌ El cliente no tiene préstamos activos.");
    // }
    // }

    public void mostrarEstadoDeCuenta() {

    System.out.println("\n--- GENERAR ESTADO DE CUENTA ---");

       //1. Pedir Cédula para no obligar a memorizar IDs
        System.out.print("Ingrese Documento del Cliente: ");
       String doc = scanner.nextLine();

        // 2. Buscar préstamos de esa persona (Usando la función que ya tienes)
        List<Prestamo> lista = prestamoService.obtenerPrestamoPorDocumento(doc); // O consultarPrestamosPorCedula
         if (lista != null && !lista.isEmpty()) {

             menuPrestamos.verPrestamosPorDocumento(doc);
    System.out.print("Ingrese el ID del Préstamo: ");
    int id = scanner.nextInt(); 
    scanner.nextLine(); // Limpiar buffer

    // LLAMADA AL SERVICIO
    EstadoDeCuenta reporte = prestamoService.generarReporte(id);

    // VALIDACIÓN VISUAL
    if (reporte == null) {
        System.out.println("❌ El préstamo con ID " + id + " no existe.");
        return;
    }

    // DESEMPAQUETAR DATOS PARA USARLOS FÁCILMENTE
    Prestamo p = reporte.getPrestamo();
    List<Pago> historial = reporte.getListaPagos();
    double deudaTotal = reporte.getDeudaTotalInicial();

    // --- IMPRIMIR CABECERA ---
    System.out.println("\n========================================");
    System.out.println("       ESTADO DE CUENTA - CREDIYA       ");
    System.out.println("========================================");
    System.out.println("Préstamo N°:     " + p.getId());
    System.out.println("Cliente ID:      " + p.getClienteId());
    System.out.printf("Fecha Inicio:    %s\n", p.getFechaInicio());
    System.out.printf("Monto Prestado:  $%,.0f\n", p.getMonto());
    System.out.printf("Interés (%.1f%%): +$%,.0f\n", p.getInteres(), (p.getMonto() * p.getInteres()/100));
    System.out.println("----------------------------------------");
    System.out.printf("DEUDA TOTAL:     $%,.0f\n", deudaTotal);
    System.out.println("========================================\n");

    // --- IMPRIMIR HISTORIAL (Lógica Visual) ---
    System.out.println("HISTORIAL DE PAGOS REALIZADOS:");
    System.out.printf("%-12s %-15s %-15s\n", "FECHA", "MONTO ABONADO", "SALDO RESTANTE");
    System.out.println("----------------------------------------------");

    if (historial == null || historial.isEmpty()) {
        System.out.println("   (No se han registrado pagos aún)");
    } else {
        // Usamos una variable temporal solo para el efecto visual de la tabla
        double saldoVisual = deudaTotal; 

        for (Pago pago : historial) {
            saldoVisual -= pago.getMonto(); // Restamos para mostrar la columna "Saldo Restante"
            
            System.out.printf("%-12s $%,-14.0f $%,-14.0f\n",
                    pago.getFechaPago(),
                    pago.getMonto(),
                    saldoVisual);
        }
    }

    // --- IMPRIMIR RESUMEN FINAL ---
    System.out.println("----------------------------------------------");
    System.out.printf("TOTAL PAGADO:    $%,.0f\n", reporte.getTotalPagado());
    System.out.printf("SALDO PENDIENTE: $%,.0f\n", reporte.getSaldoFinal());

    String estado = (reporte.getSaldoFinal() <= 0) ? "¡PAZ Y SALVO!" : "PENDIENTE";
    System.out.println("ESTADO ACTUAL:   " + estado);
    System.out.println("========================================\n");
}
    }

    private void modificarPago() {
        System.out.println("\n--- MODIFICAR PAGO ---");

        // 1. AQUÍ PEDIMOS LA CÉDULA (DOCUMENTO)
        System.out.print("Ingrese el documento del cliente para buscar sus pagos: ");
        String documento = scanner.nextLine(); // <--- ¡AQUÍ ESTÁ!

        // 2. Buscamos los pagos de esa persona
        List<Pago> resultados = gestorPagosService.obtenerPagosPorCliente(documento);

        // 3. Validamos si encontró algo
        if (resultados.isEmpty()) {
            System.out.println("⚠ No se encontraron pagos para el documento: " + documento);
            return; // Nos salimos de la función porque no hay nada que modificar
        }

        // 4. Imprimimos la tabla bonita SOLO con los pagos de esa cédula
        System.out.println("\n--- PAGOS ENCONTRADOS ---");
        System.out.println("+-----+--------------+----------------------+---------------+");
        System.out.printf("| %-3s | %-12s | %-20s | %-13s |\n", "ID", "FECHA", "CLIENTE", "MONTO");
        System.out.println("+-----+--------------+----------------------+---------------+");

        for (Pago p : resultados) {
            System.out.printf("| %-3d | %-12s | %-20s | $%,12.0f |\n",
                    p.getId(), p.getFechaPago(), p.getNombreCliente(), p.getMonto());
        }
        System.out.println("+-----+--------------+----------------------+---------------+");

        System.out.print("\nIngrese ID del Pago a modificar: ");
        int idPago = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer

        // 6. Pedimos los datos nuevos
        System.out.print("Ingrese el nuevo monto corregido: ");
        double nuevoMonto = scanner.nextDouble();
        scanner.nextLine();

        // 7. Empaquetamos y enviamos
        Pago pagoModificado = new Pago();
        pagoModificado.setId(idPago);
        pagoModificado.setMonto(nuevoMonto);
        pagoModificado.setFechaPago(LocalDate.now());

        gestorPagosService.editarPago(pagoModificado);
    }

    private void eliminarPago() {
        System.out.println("\n--- ELIMINAR PAGO ---");

        // 1. FILTRO: Pedimos el documento primero
        System.out.print("Ingrese el documento del cliente para buscar sus pagos: ");
        String documento = scanner.nextLine(); // <--- Aquí pedimos la cédula

        // 2. Buscamos los pagos de esa persona
        List<Pago> resultados = gestorPagosService.obtenerPagosPorCliente(documento);

        // 3. Validamos si hay algo que mostrar
        if (resultados.isEmpty()) {
            System.out.println("⚠ No se encontraron pagos asociados al documento: " + documento);
            return; // Si no hay nada, nos salimos
        }

        // 4. Imprimimos la tabla (Visualización)
        System.out.println("\n--- LISTA DE PAGOS DEL CLIENTE ---");
        System.out.println("+-----+--------------+----------------------+---------------+");
        System.out.printf("| %-3s | %-12s | %-20s | %-13s |\n", "ID", "FECHA", "CLIENTE", "MONTO");
        System.out.println("+-----+--------------+----------------------+---------------+");

        for (Pago p : resultados) {
            System.out.printf("| %-3d | %-12s | %-20s | $%,12.0f |\n",
                    p.getId(),
                    p.getFechaPago(),
                    p.getNombreCliente(),
                    p.getMonto());
        }
        System.out.println("+-----+--------------+----------------------+---------------+");

        // 5. SELECCIÓN: Pedimos el ID específico a borrar
        System.out.print("\nIngrese el ID del pago que desea eliminar (mira la tabla de arriba): ");
        int idPago = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer del enter

        // 6. CONFIRMACIÓN (Paso crítico de seguridad)
        System.out.print("¿Está COMPLETAMENTE SEGURO de eliminar el pago #" + idPago + "? (S/N): ");
        String confirmacion = scanner.nextLine();

        if (confirmacion.equalsIgnoreCase("S")) {
            // 7. EJECUCIÓN: Llamamos al servicio
            gestorPagosService.borrarPago(idPago);
        } else {
            System.out.println("⚠ Operación cancelada. No se borró nada.");
        }
        System.out.println("Nueva lista de pagos después de la eliminación:");
        historialDePagos(); // Mostrar la lista actualizada
    }

}