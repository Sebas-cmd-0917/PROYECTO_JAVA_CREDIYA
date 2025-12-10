package com.crediya.ui;

import java.util.Scanner;

public class MenuPrincipal {

    public void mostrarMenu(){
        Scanner scanner = new Scanner(System.in);
        while(true){
        System.out.println("--- Menú Principal ---");
        System.out.println("1. Gestionar Empleados");
        System.out.println("2. Gestionar Clientes");
        System.out.println("3. Gestionar Préstamos");
        System.out.println("4. Gestionar Pagos");
        System.out.println("5. Salir");
        System.out.print("Seleccione una opción: ");
        int opcion = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        switch(opcion){
            case 1:
                System.out.println("Gestión de Empleados seleccionada.");
                // Lógica para gestionar empleados
                MenuEmpleado menuEmp = new MenuEmpleado();
                menuEmp.mostrarMenuEmpleado();
                break;
            case 2:
                System.out.println("Gestión de Clientes seleccionada.");
                // Lógica para gestionar clientes
                break;
            case 3:
                System.out.println("Gestión de Préstamos seleccionada.");
                // Lógica para gestionar préstamos
                break;
            case 4:
                System.out.println("Gestión de Pagos seleccionada.");
                // Lógica para gestionar pagos
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
