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

            int opcion = scanner.nextInt();
            scanner.nextLine(); // limpiar buffer

            switch (opcion) {
                case 1:
                    crearPago();
                    break;
                case 2:

                    break;
                case 3:
                    break;
                case 4:
                    historialDePagos();
                    break;
                    case 5:
                    verEstadoDeCuenta();
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

        System.out.println("FECHA       | CLIENTE           | MONTO");
        for (Pago p : historial) {
            System.out.printf("%s  | %-15s | $%,.0f\n",
                    p.getFechaPago(),
                    p.getNombreCliente(), // <--- ¡Ahora sí trae el nombre!
                    p.getMonto());
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

           menuPrestamos.verPrestamosPorDocumento(doc);

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