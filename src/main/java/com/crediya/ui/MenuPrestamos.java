package com.crediya.ui;

import java.time.LocalDate;
import java.util.Scanner;

import com.crediya.data.repositories.PrestamoDAOImpl;
import com.crediya.model.Prestamo;
import com.crediya.repository.PrestamoRepository;
import com.crediya.service.CalculadoraPrestamosService;
import com.crediya.service.GestorPagosService;

public class MenuPrestamos {
    Scanner scanner = new Scanner(System.in);
    PrestamoRepository prestamoRepository = new PrestamoDAOImpl();
    GestorPagosService gestorPagosService = new GestorPagosService();
    CalculadoraPrestamosService calculadoraPrestamosService = new CalculadoraPrestamosService();
    public void mostrarMenuPrestamo() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n===== 📌 MENÚ DE PRÉSTAMOS =====");
            System.out.println("1. Registrar préstamo");
            System.out.println("2. Simular préstamo");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine(); // limpiar buffer

            switch (opcion) {
                case 1:
                    registrarPrestamo();
                    break;
                case 2:
                    simularPrestamo();
                    break;
                case 0:
                    System.out.println("Volviendo al menú principal...");
                    break;
                default:
                    System.out.println("❌ Opción inválida");
            }
        }
    }

    // 👉 OPCIÓN 1: Registrar préstamo sin simulación
    private void registrarPrestamo() {
        try {
            System.out.println("\n--- Registrar préstamo ---");

            System.out.print("ID del cliente: ");
            int cId = scanner.nextInt();

            System.out.print("ID del empleado: ");
            int eId = scanner.nextInt();

            System.out.print("Monto: ");
            double monto = scanner.nextDouble();

            System.out.print("Interés (%): ");
            double interes = scanner.nextDouble();

            System.out.print("Cuotas: ");
            int cuotas = scanner.nextInt();
            scanner.nextLine();

            Prestamo nuevoP = new Prestamo(cId, eId, monto, interes, cuotas, LocalDate.now(), "ACTIVO");
            prestamoRepository.registrarPrestamo(nuevoP);

            System.out.println("✔ Préstamo registrado correctamente.");

        } catch (Exception e) {
            System.out.println("❌ Error al registrar préstamo: " + e.getMessage());
            scanner.nextLine();
        }
    }

    // Simular préstamo 
    private void simularPrestamo() {
        try {
            System.out.println("\n--- Simular préstamo ---");

            System.out.print("ID del cliente: ");
            int cId = scanner.nextInt();

            System.out.print("ID del empleado: ");
            int eId = scanner.nextInt();

            System.out.print("Monto: ");
            double monto = scanner.nextDouble();

            System.out.print("Interés (%): ");
            double interes = scanner.nextDouble();

            System.out.print("Cuotas: ");
            int cuotas = scanner.nextInt();
            scanner.nextLine();

            // Mostrar simulación
            System.out.println(calculadoraPrestamosService.imprimirTablaAmortizacion(monto, interes, cuotas));

            // Preguntar si quiere registrar después de simular
            System.out.print("\n¿Desea registrar este préstamo? (S/N): ");
            String confirmacion = scanner.nextLine();

            if (confirmacion.equalsIgnoreCase("S")) {
                Prestamo nuevoP = new Prestamo(cId, eId, monto, interes, cuotas, LocalDate.now(), "ACTIVO");
                prestamoRepository.registrarPrestamo(nuevoP);
                System.out.println("✔ Préstamo registrado correctamente.");
            } else {
                System.out.println("❌ Registro cancelado. Solo se realizó la simulación.");
            }

        } catch (Exception e) {
            System.out.println("❌ Error al simular préstamo: " + e.getMessage());
            scanner.nextLine();
        }
    }
}
