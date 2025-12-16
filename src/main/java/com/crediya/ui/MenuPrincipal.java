package com.crediya.ui;

import java.util.InputMismatchException;
import java.util.Scanner;

import com.crediya.model.Empleado;

public class MenuPrincipal {

    // VARIABLE CLAVE: Aquí guardamos quién inició sesión
    private Empleado usuarioActivo;

    // CONSTRUCTOR: Obligamos a que nos pasen el usuario al crear el menú
    public MenuPrincipal(Empleado usuario) {
        this.usuarioActivo = usuario;
    }

    public void mostrarMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean continuar = true;
        int opcion = 0;

        while (continuar) {
            System.out.println("\n--- Menú Principal ---");
            System.out.println("Usuario: " + usuarioActivo.getNombre()); // Mostramos quién es

            // Visualmente mostramos el candado (Opcional)
            if (esAdmin()) {
                System.out.println("1. Gestionar Empleados (ADMIN)");
            } else {
                System.out.println("1. 🔒 Gestionar Empleados (Bloqueado)");
            }

            System.out.println("2. Gestionar Clientes");
            System.out.println("3. Gestionar Préstamos (Registrar y Simular)");
            System.out.println("4. Gestionar Pagos (Registrar Abono)");
            System.out.println("5. Reportes");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = scanner.nextInt();
                scanner.nextLine(); // Consumir el salto de línea

                switch (opcion) {
                    case 1:
                        System.out.println("Gestión de Empleados seleccionada.");
                        MenuEmpleado menuEmp = new MenuEmpleado();
                        menuEmp.mostrarMenuEmpleado();
                        break;
                    case 2:
                        System.out.println("Gestión de Clientes seleccionada.");
                        MenuCliente menuCli = new MenuCliente();
                        menuCli.mostarrMenuCliente();
                        break;
                    case 3:
                        // Sebas
                        System.out.println("Gestión de Préstamos seleccionada.");
                        MenuPrestamos menuPres = new MenuPrestamos();
                        menuPres.mostrarMenuPrestamo();
                        break;
                    case 4:
                        // Sebas
                        System.out.println("Gestión de Pagos seleccionada.");
                        MenuPago menuPago = new MenuPago();
                        menuPago.mostrarMenuPago();
                        break;
                    case 5:
                        System.out.println("Módulo de Reportes seleccionado.");
                        MenuReportes menuRep = new MenuReportes();
                        menuRep.mostrarMenu();
                        break;
                    case 6:
                        System.out.println("Saliendo del sistema. ¡Hasta luego!");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Opción no válida. Por favor, intente de nuevo.");
                }
            } catch (InputMismatchException e) {
                System.out.println("❌ Error: Ingresaste una letra o símbolo. Por favor ingresa un NÚMERO.");

                // ¡¡MUY IMPORTANTE!!: Limpiar el "basurero" del scanner
                scanner.nextLine();
            }

        }
    }
    // Método auxiliar para no repetir la validación del correo
    private boolean esAdmin() {
        // Opción A: Validar por correo exacto (Como pediste)
        return usuarioActivo.getCorreo().equalsIgnoreCase("admin@admin.com");
        
        // Opción B (Más profesional): Validar por Rol
        // return usuarioActivo.getRol().equalsIgnoreCase("ADMIN");
    }
}
