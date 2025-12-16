package com.crediya.ui;

import java.util.Scanner;
import com.crediya.model.Empleado;

import com.crediya.service.GestorEmpleadoService;

import java.util.InputMismatchException;
import java.util.List;

public class MenuEmpleado {
    Scanner scanner = new Scanner(System.in);
    GestorEmpleadoService servicioEmpleado = new GestorEmpleadoService();

    public void mostrarMenuEmpleado() {
        while (true) {
            System.out.println("\n===== 👔 MENÚ DE EMPLEADOS =====");
            System.out.println("1. 🆕 Registrar Empleado");
            System.out.println("2. ✏️  Modificar Empleado");
            System.out.println("3. 🗑️  Eliminar Empleado");
            System.out.println("4. 📋 Listar Empleados");
            System.out.println("0. 🔙 Volver");
            System.out.print("👉 Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            try {
                switch (opcion) {
                    case 1:
                        System.out.println("Crear Empleado seleccionado.");
                        crearEmpleado();
                        break;
                    case 2:
                        System.out.println("Modificar Empleado seleccionado.");
                        modificar();
                        break;
                    case 3:
                        System.out.println("Gestión de Préstamos seleccionada.");
                        eliminar();
                        break;
                    case 4:
                        System.out.println("\n--- LISTA DE EMPLEADOS ---");
                        listarEmpleados();
                        break;

                    case 0:
                        System.out.println("Saliendo del menu empleados!");
                        return;
                    default:
                        System.out.println("Opción no válida. Por favor, intente de nuevo.");
                }
            } catch (InputMismatchException e) {
                System.out.println("❌ Error: Ingresaste una letra o símbolo. Por favor ingresa un NÚMERO.");
                scanner.nextLine();
            }

        }
    }

    private void crearEmpleado() {
        // Lógica para crear empleado
        try {
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

            Empleado nuevoEmp = new Empleado(0, nom, doc, rol, correo, sal);
            servicioEmpleado.registrarEmpleado(nuevoEmp);
            System.out.println("✔ Empleado registrado correctamente.");
        } catch (Exception e) {
            System.out.println("❌ Error al registrar empleado: " + e.getMessage());
            scanner.nextLine();
        }
    }

    private void listarEmpleados() {
        // Lógica para listar empleados
        List<Empleado> lista = servicioEmpleado.obtenerTodos();
        if (lista.isEmpty()) {
            System.out.println("No hay empleados registrados.");
        } else {
            // 1. IMPRIMIR ENCABEZADOS DE LA TABLA
            // %-5s = Columna de Texto alineado a la Izquierda de 5 espacios
            // %-20s = Columna de Texto alineado a la Izquierda de 20 espacios
            System.out.printf("%-5s %-20s %-15s %-15s %-25s %-15s\n",
                    "ID", "NOMBRE", "DOC", "ROL", "CORREO", "SALARIO");

            System.out.println(
                    "----------------------------------------------------------------------------------------------------");

            // 2. IMPRIMIR CADA FILA CON EL MISMO FORMATO
            for (Empleado e : lista) {
                System.out.printf("%-5d %-20s %-15s %-15s %-25s $%,.2f\n",
                        e.getId(), // %d para números enteros
                        e.getNombre(), // %s para texto
                        e.getDocumento(),
                        e.getRol(),
                        e.getCorreo(),
                        e.getSalario()); // %,.2f para dinero (con comas y 2 decimales)

            }
            System.out.println(
                    "----------------------------------------------------------------------------------------------------");

        }
    }

    private void modificar() {
        listarEmpleados();
        System.out.print("\nIngrese ID del empleado a modificar: ");

        try {
            int id = Integer.parseInt(scanner.nextLine());
            Empleado actual = servicioEmpleado.buscarPorId(id);

            if (actual == null) {
                System.out.println("❌ Empleado no encontrado.");
                return;
            }

            System.out.println("--- EDICIÓN (Enter para mantener actual) ---");

            String nuevoNombre = pedirTexto("Nombre (" + actual.getNombre() + "): ", actual.getNombre());
            String nuevoDoc = pedirTexto("Documento (" + actual.getDocumento() + "): ", actual.getDocumento());
            String nuevoRol = pedirTexto("Rol (" + actual.getRol() + "): ", actual.getRol());
            String nuevoCorreo = pedirTexto("Correo (" + actual.getCorreo() + "): ", actual.getCorreo());

            // Lógica especial para el salario (Double)
            System.out.print("Salario ($" + actual.getSalario() + "): ");
            String inputSalario = scanner.nextLine();
            double nuevoSalario = inputSalario.isEmpty() ? actual.getSalario() : Double.parseDouble(inputSalario);

            servicioEmpleado.actualizarEmpleado(id, nuevoNombre, nuevoDoc, nuevoRol, nuevoCorreo, nuevoSalario);

        } catch (NumberFormatException e) {
            System.out.println("❌ Error: ID o Salario inválidos.");
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    private void eliminar() {
        listarEmpleados();
        System.out.print("\nIngrese ID del empleado a eliminar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());

            // Confirmación
            System.out.print("⚠️ ¿Seguro que desea eliminar este empleado? (SI/NO): ");
            if (scanner.nextLine().equalsIgnoreCase("SI")) {
                servicioEmpleado.borrarEmpleado(id);
            } else {
                System.out.println("Operación cancelada.");
            }
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    // Método auxiliar para textos
    private String pedirTexto(String mensaje, String valorActual) {
        System.out.print(mensaje);
        String input = scanner.nextLine();
        return input.isEmpty() ? valorActual : input;
    }

}
