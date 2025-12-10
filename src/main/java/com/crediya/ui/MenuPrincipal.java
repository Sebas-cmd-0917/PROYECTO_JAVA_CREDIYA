package com.crediya.ui;

import java.time.LocalDate;
import java.util.Scanner;

import com.crediya.dao.PrestamoDAO;
import com.crediya.dao.impl.PrestamoDAOImpl;
import com.crediya.model.Pago;
import com.crediya.model.Prestamo;
import com.crediya.service.CalculadoraPrestamosService;
import com.crediya.service.GestorPagosService;

public class MenuPrincipal {

    public void mostrarMenu(){
        Scanner scanner = new Scanner(System.in);

        //Sebas
        PrestamoDAO prestamoDAO = new PrestamoDAOImpl();
        GestorPagosService gestorPagosService = new GestorPagosService();
        CalculadoraPrestamosService calculadoraPrestamosService = new CalculadoraPrestamosService();


        while(true){
        System.out.println("--- Menú Principal ---");
        System.out.println("1. Gestionar Empleados");
        System.out.println("2. Gestionar Clientes");
        System.out.println("3. Gestionar Préstamos (Registrar y Simular)");
        System.out.println("4. Gestionar Pagos (Registrar Abono)");
        System.out.println("5. Salir");
        System.out.print("Seleccione una opción: ");
        int opcion = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        switch(opcion){
            case 1:
                System.out.println("Gestión de Empleados seleccionada.");
                // Lógica para gestionar empleados
                break;
            case 2:
                System.out.println("Gestión de Clientes seleccionada.");
                // Lógica para gestionar clientes
                break;
            case 3:
                //Sebas
                System.out.println("Gestión de Préstamos seleccionada.");
                try{
                    System.out.println("ID del cliente: ");
                    int cId = scanner.nextInt();
                    System.out.println("ID del empleado");
                    int eId = scanner.nextInt();
                    System.out.println("Monto: ");
                    double monto = scanner.nextDouble();
                    System.out.println("Interés (%):");
                    double interes = scanner.nextDouble();
                    System.out.print("Cuotas: ");
                    int cuotas = scanner.nextInt();
                    scanner.nextLine(); // Limpiar buffer
                    
                    //Simulacion del credito
                    calculadoraPrestamosService.imprimirTablaAmortizacion(monto, interes, cuotas);

                    //Preguntar si se quiere guardar el prestamo
                    System.out.print("¿Desea confirmar el préstamo? (S/N): ");
                    String confirmacion = scanner.nextLine();

                    if (confirmacion.equalsIgnoreCase("S")) {
                            Prestamo nuevoP = new Prestamo(cId, eId, monto, interes, cuotas, LocalDate.now(), "ACTIVO");
                            prestamoDAO.registrarPrestamo(nuevoP);
                        } else {
                            System.out.println("Operación cancelada.");
                        }

                } catch (Exception e) {
                    System.out.println("❌ Error: Datos inválidos. " + e.getMessage());
                    scanner.nextLine();
                }
                break;
            case 4:
                //Sebas
                System.out.println("Gestión de Pagos seleccionada.");
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
                        System.out.println("❌ Error: Datos inválidos.");
                        scanner.nextLine();
                    }
                    break;
            case 5:
                System.out.println("Saliendo del sistema. ¡Hasta luego!");
                scanner.close();
                return;
            default:
                System.out.println("Opción no válida. Por favor, intente de nuevo.");
        }

    }
    }
    
}
