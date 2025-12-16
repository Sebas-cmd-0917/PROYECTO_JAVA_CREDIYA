package com.crediya.ui;

import java.util.Scanner;
import com.crediya.service.GestorClienteSrvice;
import com.crediya.model.Cliente;
import java.util.List;

public class MenuCliente {
    Scanner scanner = new Scanner(System.in);
    GestorClienteSrvice servicioCliente = new GestorClienteSrvice();

    public void mostarrMenuCliente() {
        while (true) {
            System.out.println("\n===== 📌 MENÚ DE CLIENTES =====");
            System.out.println("1. Crear Cliente");
            System.out.println("2. Modificar Cliente");
            System.out.println("3. Eliminar Cliente");
            System.out.println("4. Listar Clientes");
            System.out.println("0. Salir");
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
                    modificarCliente();
                    break;
                case 3:
                    System.out.println("Eliminar Cliente seleccionado.");
                    eliminarCliente();
                    // Lógica para eliminar cliente
                    break;
                case 4:
                    System.out.println("\n--- LISTA DE CLIENTES ---");
                    listarClientes();
                    break;
                case 0:
                    System.out.println("Saliendo del menu clientes!");
                    return;
                default:
                    break;
            }

        }
    }

    public void crearCliente() {
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

            Cliente nuevoCliente = new Cliente(0, nom, doc, correo, telefono);
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

            System.out.println(
                    "----------------------------------------------------------------------------------------");

            for (Cliente clte : clientes) {
                System.out.printf("%-5d %-20s %-15s %-25s %-15s\n",
                        clte.getId(), // %d para números enteros
                        clte.getNombre(), // %s para texto
                        clte.getDocumento(),
                        clte.getCorreo(),
                        clte.getTelefono()); // %,.2f para dinero (con comas y 2 decimales)

            }
            System.out.println(
                    "----------------------------------------------------------------------------------------");
        }
    }

    // --- MENÚ MODIFICAR (CON TRUCO DE ENTER) ---
    private void modificarCliente() {
        listarClientes(); // Mostrar lista para ver el ID
        System.out.print("\nIngrese el ID del cliente a modificar: ");

        try {
            int id = Integer.parseInt(scanner.nextLine());
            Cliente actual = servicioCliente.buscarClientePorId(id);

            if (actual == null) {
                System.out.println("❌ Cliente no encontrado.");
                return;
            }

            System.out.println("--- EDICIÓN (Presione ENTER para mantener el valor actual) ---");

            String nuevoNombre = pedirDato("Nombre (" + actual.getNombre() + "): ", actual.getNombre());
            String nuevoDoc = pedirDato("Documento (" + actual.getDocumento() + "): ", actual.getDocumento());
            String nuevoCorreo = pedirDato("Correo (" + actual.getCorreo() + "): ", actual.getCorreo());
            String nuevoTel = pedirDato("Teléfono (" + actual.getTelefono() + "): ", actual.getTelefono());

            servicioCliente.actualizarCliente(id, nuevoNombre, nuevoDoc, nuevoCorreo, nuevoTel);
            // El mensaje de éxito ya sale del DAO, o puedes ponerlo aquí

        } catch (NumberFormatException e) {
            System.out.println("❌ El ID debe ser un número.");
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    // --- MENÚ ELIMINAR (CON CONFIRMACIÓN) ---
    private void eliminarCliente() {
        listarClientes();
        System.out.print("\nIngrese el ID del cliente a eliminar: ");

        try {
            int id = Integer.parseInt(scanner.nextLine());
            Cliente aBorrar = servicioCliente.buscarClientePorId(id);

            if (aBorrar == null) {
                System.out.println("❌ El cliente no existe.");
                return;
            }

            // Confirmación de seguridad
            System.out.println("⚠️ ¿Está seguro de eliminar a " + aBorrar.getNombre() + "?");
            System.out.print("Escriba 'SI' para confirmar: ");
            String confirmacion = scanner.nextLine();

            if (confirmacion.equalsIgnoreCase("SI")) {
                servicioCliente.borrarCliente(id);
            } else {
                System.out.println("Operación cancelada.");
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ El ID debe ser un número.");
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    // Método Helper para la lógica de "Presionar Enter"
    private String pedirDato(String mensaje, String valorActual) {
        System.out.print(mensaje);
        String entrada = scanner.nextLine();
        if (entrada.trim().isEmpty()) {
            return valorActual; // Si no escribe nada, devuelve el viejo
        }
        return entrada; // Si escribe algo, devuelve lo nuevo
    }
}
