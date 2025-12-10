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
                MenuEmpleado menuEmp = new MenuEmpleado();
                menuEmp.mostrarMenuEmpleado();
                break;
            case 2:
                System.out.println("Gestión de Clientes seleccionada.");
                // Lógica para gestionar clientes
                
                break;
            case 3:
                //Sebas
                System.out.println("Gestión de Préstamos seleccionada.");
                MenuPrestamos menuPres = new MenuPrestamos();
                menuPres.mostrarMenuPrestamo();
                break;
            case 4:
                //Sebas
                System.out.println("Gestión de Pagos seleccionada.");
                MenuPago menuPago = new MenuPago();
                menuPago.mostrarMenuPago();
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
