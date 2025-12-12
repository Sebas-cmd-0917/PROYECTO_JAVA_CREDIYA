package com.crediya.ui;

import java.util.Scanner;
import java.util.List;

import com.crediya.service.GestorPagosService;
import com.crediya.model.Prestamo;
import com.crediya.model.Pago;

public class MenuPago {
    Scanner scanner = new Scanner(System.in);
    GestorPagosService gestorPagosService = new GestorPagosService();
    GestorPagosService prestamoService = new GestorPagosService();

    public void mostrarMenuPago() {

        while (true) {
            System.out.println("\n===== 📌 MENÚ DE PAGOS =====");
            System.out.println("1. Registrar pago");
            System.out.println("2. Ver prestamos por documento");
            System.out.println("3. Ver estado de cuenta");
            System.out.println("4. Ver historial de pagos");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");

            int opcion = scanner.nextInt();
            scanner.nextLine(); // limpiar buffer

            switch (opcion) {
                case 1:
                    crearPago();
                    break;
                case 2:
                    System.out.println("Ingrese el documento del cliente:");
                    String documento = scanner.nextLine();
                    verPrestamosPorDocumento(documento);
                    break;
                case 3:
                    verEstadoDeCuenta();
                    break;
                case 4:
                    historialDePagos();
                    break;
                case 0:
                    System.out.println("Volviendo al menú principal...");
                    return;
                default:
                    System.out.println("❌ Opción inválida");
            }

        }

    }

    private void crearPago() {
        System.out.println("\n--- CAJA DE PAGOS ---");

        // 1. PIDE CÉDULA
        System.out.print("Ingrese Cédula del Cliente: ");
        String cedula = scanner.nextLine();

        // 2. LLAMA A LA LISTA (Fase 1)
        List<Prestamo> misPrestamos = gestorPagosService.obtenerPrestamoPorDocumento(cedula);

        if (misPrestamos != null && !misPrestamos.isEmpty()) {

            // 3. MUESTRA LA LISTA PARA QUE ELIJA
            System.out.println("--- Seleccione el Préstamo a Pagar ---");
            System.out.printf("%-5s %-8s %-20s %-12s %-15s %-8s %-8s %-20s %-15s %-12s %-15s\n",
                    "#", "ID_CLI", "CLIENTE", "CEDULA", "MONTO", "%INT", "CUOTAS", "EMPLEADO", "PAGADO", "ESTADO",
                    "SALDO");   
            System.out.println(
                    "-------------------------------------------------------------------------------------------------------------------------------------------------------");
            for (Prestamo p : misPrestamos) {
                System.out.printf("%-5d %-8d %-20s %-12s $%,-14.0f %-8.1f %-8d %-20s $%,-14.0f %-12s $%,-14.0f\n",
                        p.getId(), // %d para números enteros
                        p.getClienteId(),
                        p.getNombreCliente(),   // %s para texto    
                        p.getNumDocumento(),
                        p.getMonto(),
                        p.getInteres(),
                        p.getCuotas(), // %,.2f para dinero (con comas y 2 decimales)
                        p.getNombreEmpleado(),
                        p.getTotalPagado(),
                        p.getEstado(), // Cuánto ha abonado hasta hoy (¡DATO NUEVO!)
                        p.getSaldoPendiente()); // Cuánto le falta (¡DATO NUEVO!)   
            }
            System.out.println(
                    "-------------------------------------------------------------------------------------------------------------------------------------------------------");
                    

            // 4. PIDE EL ID EXACTO
            System.out.print("\nEscriba el NÚMERO (ID) del préstamo: ");
            int idElegido = scanner.nextInt();

            System.out.print("¿Cuánto va a abonar? $");
            double monto = scanner.nextDouble();

            // 5. LLAMA AL PROCESO DE PAGO (Fase 2)
            gestorPagosService.procesarPago(idElegido, monto);

        } else {
            System.out.println("Este cliente no tiene deudas activas.");
        }
    }

    private void historialDePagos() {
        System.out.println("\n--- HISTORIAL DE PAGOS ---");
        List<Pago> historial = gestorPagosService.obtenerHistorialDePagos(); // Tu función

        System.out.println("FECHA       | CLIENTE           | MONTO");
        for (Pago p : historial) {
            System.out.printf("%s  | %-15s | $%,.0f\n",
                    p.getFechaPago(),
                    p.getNombreCliente(), // <--- ¡Ahora sí trae el nombre!
                    p.getMonto());
        }
    }

    private void verPrestamosPorDocumento(String documento) {

        List<Prestamo> prestamos = gestorPagosService.obtenerPrestamoPorDocumento(documento);

        if (prestamos.isEmpty()) {
            System.out.println("No se encontraron préstamos para el documento: " + documento);
        } else {
            // 1. IMPRIMIR ENCABEZADOS DE LA TABLA
            // %-5s = Columna de Texto alineado a la Izquierda de 5 espacios
            // %-20s = Columna de Texto alineado a la Izquierda de 20 espacios
            System.out.printf("%-5s %-8s %-20s %-12s %-15s %-8s %-8s %-20s %-15s %-12s %-15s\n",
                    "#", "ID_CLI", "CLIENTE", "CEDULA", "MONTO", "%INT", "CUOTAS", "EMPLEADO", "PAGADO", "ESTADO",
                    "SALDO");

            System.out.println(
                    "-------------------------------------------------------------------------------------------------------------------------------------------------------");

            // 2. LAS FILAS (Ahora sí coinciden tipos y cantidad)
            for (Prestamo p : prestamos) {
                System.out.printf("%-5d %-8d %-20s %-12s $%,-14.0f %-8.1f %-8d %-20s $%,-14.0f %-12s $%,-14.0f\n",
                        p.getId(), // %d para números enteros
                        p.getClienteId(),
                        p.getNombreCliente(),
                        p.getNumDocumento(),
                        p.getMonto(),
                        p.getInteres(),
                        p.getCuotas(), // %,.2f para dinero (con comas y 2 decimales)
                        p.getNombreEmpleado(), // %s para texto
                        p.getTotalPagado(),
                        p.getEstado(), // Cuánto ha abonado hasta hoy (¡DATO NUEVO!)
                        p.getSaldoPendiente()); // Cuánto le falta (¡DATO NUEVO!)

            }
            System.out.println(
                    "-------------------------------------------------------------------------------------------------------------------------------------------------------");


        }
    }

    public void verEstadoDeCuenta() {
        System.out.println("\n--- GENERAR ESTADO DE CUENTA ---");

        // 1. Pedir Cédula para no obligar a memorizar IDs
        System.out.print("Ingrese Documento del Cliente: ");
        String doc = scanner.nextLine();

        // 2. Buscar préstamos de esa persona (Usando la función que ya tienes)
        List<Prestamo> lista = prestamoService.obtenerPrestamoPorDocumento(doc); // O consultarPrestamosPorCedula

        if (lista != null && !lista.isEmpty()) {

            verPrestamosPorDocumento(doc);

            // 3. Pedir el ID específico
            System.out.print("\nIngrese el NÚMERO del préstamo a consultar: ");
            int idSeleccionado = scanner.nextInt();

            // 4. ¡GENERAR EL REPORTE!
            gestorPagosService.generarEstadoDeCuenta(idSeleccionado);

        } else {
            System.out.println("❌ El cliente no tiene préstamos activos.");
        }
    }
}