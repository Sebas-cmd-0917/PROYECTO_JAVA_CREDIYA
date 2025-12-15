package com.crediya.ui;

import java.util.Scanner;
import com.crediya.service.GestorClienteSrvice;
import com.crediya.model.Cliente;
import java.util.List;

public class MenuCliente {
    Scanner scanner = new Scanner(System.in);
    GestorClienteSrvice servicioCliente = new GestorClienteSrvice();

    public void mostarrMenuCliente(){
        while (true) {
             System.out.println("--- Menú Clientes ---");
            System.out.println("1. Crear Cliente");
            System.out.println("2. Modificar Cliente");
            System.out.println("3. Eliminar Cliente");
            System.out.println("4. Listar Clientes");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    crearCliente();
                    break;
                case 2:
                    System.out.println("Modificar Cliente seleccionado.");
                    // Lógica para modificar cliente
                    break;
                case 3:
                    System.out.println("Eliminar Cliente seleccionado.");
                    // Lógica para eliminar cliente     
                    break;
                case 4:
                    System.out.println("\n--- LISTA DE CLIENTES ---");
                    listarClientes();
                    break;
                case 5:
                    System.out.println("Saliendo del menu clientes!");
                    MenuPrincipal miMenu = new MenuPrincipal();
                    miMenu.mostrarMenu();
                    return;
                default:
                    break;
            }
            
        }
    }

    public void crearCliente(){
         try {
                    System.out.println("\n--- REGISTRAR CLIENTE ---");
                    System.out.print("Nombre: ");
                    String nom = scanner.nextLine();
                    System.out.print("Documento: ");
                    String doc = scanner.nextLine();
                    System.out.print("Correo: ");
                    String correo = scanner.nextLine();
                    System.out.print("Telefono: ");
                    String telefono = scanner.nextLine();

                    Cliente nuevoCliente = new Cliente( 0, nom, doc, correo, telefono);
                    servicioCliente.registrarCliente(nuevoCliente);
                    System.out.println("✔ Cliente registrado correctamente.");
                } catch (Exception e) {
                    System.out.println("Error al registrar cliente: " + e.getMessage());
                    scanner.nextLine();
                }
    }

    private void listarClientes() {
        List<Cliente> clientes = servicioCliente.obtenerTodos();
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
        } else {
            System.out.printf("%-5s %-20s %-15s %-25s %-15s\n", 
                                          "ID", "NOMBRE", "DOC", "CORREO", "TELEFONO");
                        
                        System.out.println("----------------------------------------------------------------------------------------");

            for (Cliente clte : clientes) {
                System.out.printf("%-5d %-20s %-15s %-25s %-15s\n", 
                clte.getId(),            // %d para números enteros
                clte.getNombre(),        // %s para texto
                clte.getDocumento(),
                clte.getCorreo(),
                clte.getTelefono());      // %,.2f para dinero (con comas y 2 decimales)
                System.out.println("----------------------------------------------------------------------------------------");


            }
        }
    }
}
    
