package com.crediya.ui;

import java.time.LocalDate;
import java.util.Scanner;
import java.util.List;
import com.crediya.model.Pago;
import com.crediya.service.GestorPagosService;
import com.crediya.model.Prestamo;

public class MenuPago {
    Scanner scanner = new Scanner(System.in);
    GestorPagosService gestorPagosService = new GestorPagosService();

    public void mostrarMenuPago(){

        while (true) {
            System.out.println("\n===== 📌 MENÚ DE PAGOS =====");
            System.out.println("1. Registrar pago");
            System.out.println("2. Ver prestamos por documento");
            System.out.println("3. Ver todos los préstamos");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");

            int opcion = scanner.nextInt();
            scanner.nextLine(); // limpiar buffer

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese el documento del cliente: ");
                    String doc = scanner.nextLine();
                    verPrestamosPorDocumento(doc);
                    System.out.println("Sleccione el # de préstamo para abonar:");
                    crearPago();
                    break;
                case 2:
                   
                   
                    break;
                case 3:
                    MenuPrestamos menuPrestamos = new MenuPrestamos();
                    menuPrestamos.listarPrestamos();
                    break;
                case 0:
                    System.out.println("Volviendo al menú principal...");
                    return;
                default:
                    System.out.println("❌ Opción inválida");
            }
            
        }
        
    }

    private void crearPago(){
        GestorPagosService gestorPagosService = new GestorPagosService();

        // Lógica para gestionar pagos
                try {
                        System.out.print("ID del Préstamo: ");
                        int pId = scanner.nextInt();
                        System.out.print("Monto a abonar: ");
                        double abono = scanner.nextDouble();
                        scanner.nextLine();

                        // Llama a Gestor de Pagos (que valida si la deuda no se excede)
                        Pago nuevoPago = new Pago(pId, LocalDate.now(), abono);
                        gestorPagosService.registrarAbono(nuevoPago);

                    } catch (Exception e) {
                        System.out.println(" Error: Datos inválidos.");
                        scanner.nextLine();
                    }
    }
    
    private void verPrestamosPorDocumento(String documento){
        
        List<Prestamo> prestamos = gestorPagosService.obtenerPrestamoPorDocumento(documento);

        if (prestamos.isEmpty()) {
            System.out.println("No se encontraron préstamos para el documento: " + documento);
        } else {
            System.out.println("Préstamos encontrados para el documento: " + documento);
            for (Prestamo p : prestamos) {
                System.out.println("- ID: " + p.getId() + ", Monto: " + p.getMonto() + ", Interés: " + p.getInteres());
            }
        }
    }
}