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
            System.out.println("\n===== 🏦 MENÚ PRINCIPAL - CREDIYA =====");
            System.out.println("👤 Usuario: " + usuarioActivo.getNombre() + " | Rol: " + usuarioActivo.getRol());
            System.out.println("---------------------------------------");
            // Visualmente mostramos el candado (Opcional)
            if (esAdmin()) {
                System.out.println("1. 👔 Gestionar Empleados (ADMIN)");
            } else {
                System.out.println("1. 🔒 Gestionar Empleados (Bloqueado)");
            }

            System.out.println("2. 👥 Gestionar Clientes");
            System.out.println("3. 💰 Gestionar Préstamos");
            System.out.println("4. 💸 Gestionar Pagos");
            System.out.println("5. 📊 Reportes e Indicadores");
            System.out.println("6. 🚪 Salir del Sistema");
            System.out.println("---------------------------------------");
            System.out.print("👉 Seleccione una opción: ");

            try {
                opcion = scanner.nextInt();
                scanner.nextLine(); // Consumir el salto de línea

                switch (opcion) {
                    case 1:
                        if (!esAdmin()) {
                            System.out.println("❌ Acceso denegado. Módulo exclusivo para ADMIN.");
                            break;
                        } else {
                            System.out.println("Gestión de Empleados seleccionada ADMIN.");
                            MenuEmpleado menuEmp = new MenuEmpleado();
                            menuEmp.mostrarMenuEmpleado();
                            break;
                        }

                    case 2:
                        System.out.println("--------------------------------");
                        System.out.println("Gestión de Clientes seleccionada.");
                        System.out.println("--------------------------------");
                        MenuCliente menuCli = new MenuCliente();
                        menuCli.mostarrMenuCliente();
                        break;
                    case 3:
                        // Sebas
                        System.out.println("--------------------------------");
                        System.out.println("Gestión de Préstamos seleccionada.");
                        System.out.println("--------------------------------");
                        MenuPrestamos menuPres = new MenuPrestamos();
                        menuPres.mostrarMenuPrestamo();
                        break;
                    case 4:
                        // Sebas
                        System.out.println("--------------------------------");
                        System.out.println(" Gestión de Pagos seleccionada  ");
                        System.out.println("--------------------------------");

                        MenuPago menuPago = new MenuPago();
                        menuPago.mostrarMenuPago();
                        break;
                    case 5:
                        System.out.println("--------------------------------");
                        System.out.println("Módulo de Reportes seleccionado.");
                        System.out.println("--------------------------------");

                        MenuReportes menuRep = new MenuReportes();
                        menuRep.mostrarMenu();
                        break;
                    case 6:
                        System.out.println("Saliendo del sistema. ¡Hasta luego!");
                        scanner.close();
                        return;
                    default:
                        System.out.println("❌ Opción no válida. Por favor, intente de nuevo.");
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
