package com.crediya.ui;

import java.time.LocalDate;
import java.util.Scanner;

import com.crediya.model.Pago;
import com.crediya.service.GestorPagosService;

public class MenuPago {
    Scanner scanner = new Scanner(System.in);

    public void mostrarMenuPago(){

        while (true) {
            System.out.println("\n===== 📌 MENÚ DE PAGOS =====");
            System.out.println("1. Registrar pago");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");

            int opcion = scanner.nextInt();
            scanner.nextLine(); // limpiar buffer

            switch (opcion) {
                case 1:
                    crearPago();
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

}
