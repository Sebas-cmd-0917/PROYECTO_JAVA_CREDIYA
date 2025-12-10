package com.crediya.ui;

import java.util.Scanner;
import com.crediya.model.Empleado;
import com.crediya.dao.impl.EmpleadoDAOImpl;
import com.crediya.dao.EmpleadoDAO;

public class MenuEmpleado {

    public void mostrarMenuEmpleado(){
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("--- Menú Empleados ---");
            System.out.println("1. Crear Empleado");
            System.out.println("2. Modificar Empleado");
            System.out.println("3. Eliminar Empleado");
            System.out.println("4. Listar Empleados");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea
            
            EmpleadoDAO empleadoDAO = new EmpleadoDAOImpl();

            switch(opcion){
            case 1:
                // Lógica para crear empleado
                    System.out.println("\n--- REGISTRAR EMPLEADO ---");
                    System.out.print("Nombre: ");
                    String nom = scanner.nextLine();
                    System.out.print("Documento: ");
                    String doc = scanner.nextLine();
                    System.out.print("Rol: ");
                    String rol = scanner.nextLine();
                    System.out.print("Correo: ");
                    String correo = scanner.nextLine();
                    System.out.print("Salario: ");
                    double sal = scanner.nextDouble();
                    scanner.nextLine(); 

                    Empleado nuevoEmp = new Empleado(nom, doc, rol, correo, sal);
                    empleadoDAO.guardarEmpleado(nuevoEmp);
                
                break;
            case 2:
                System.out.println("Modificar Empleado seleccionado.");
                // Lógica para modificar empleado
                break;
            case 3:
                System.out.println("Gestión de Préstamos seleccionada.");
                // Lógica para gestionar préstamos
                break;
            case 4:
                System.out.println("Listar Empleados.");
                // Lógica para listar empleados
                
                break;
            case 5:
                System.out.println("Saliendo del menu empleados!");
                MenuPrincipal miMenu = new MenuPrincipal();
                miMenu.mostrarMenu();
                return;
            default:
                System.out.println("Opción no válida. Por favor, intente de nuevo.");
            }
        }
        // Lógica para mostrar el menú de gestión de empleados
    }

}
