package com.crediya.ui;

import java.util.List;
import java.util.Scanner;

import com.crediya.model.Prestamo;
import com.crediya.service.GestorPrestamos;

public class MenuExamen {
    GestorPrestamos prestamoService = new GestorPrestamos();

    public void mostrarMenuPrestamo() {

        Scanner scanner = new Scanner(System.in);

        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n===== 💰 MENÚ DE PRÉSTAMOS =====");
            System.out.println("1. 🆕 Prestamos Vencidos");
            System.out.println("2. 🧮 Prestamos Activos");
            System.out.println("3. 📋 Listar Préstamos");
            System.out.println("4. 🔎 Reporte Prestamos");
            System.out.println("5. 🔎 Gestionar Prestamos");
            System.out.println("0. 🔙 Volver");
            System.out.print("👉 Seleccione una opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine(); // limpiar buffer

            switch (opcion) {
                case 1:
                    imprimirPrestamos("CLIENTES EN MORA", prestamoService.obtenerPrestamosMorExam());
                    break;
                case 2:
                    imprimirPrestamos("PRÉSTAMOS ACTIVOS", prestamoService.obtenerPrestamoActExam());
                    break;
                case 3:
                    ListarLosPrestamos();

                    break;
                case 4:
                    imprimirPretsamosCantidades("Registro de prestamos", prestamoService.obtenerPrestamoActExam(),
                            prestamoService.obtenerPrestamoActExam());
                    break;
                case 5:
                    System.out.println("--------------------------------");
                    System.out.println("Gestión de Préstamos seleccionada.");
                    System.out.println("--------------------------------");
                    MenuPrestamos menuPres = new MenuPrestamos();
                    menuPres.mostrarMenuPrestamo();
                    break;

                case 0:
                    System.out.println("Volviendo al menú principal...");
                    break;
                default:
                    System.out.println("❌ Opción inválida");
            }
        }
    }

    public void ListarLosPrestamos() {
        List<Prestamo> lista = prestamoService.obtenerTodosLosPrestamos();
        if (lista.isEmpty()) {
            System.out.println("No hay préstamos registrados.");
        } else {
            // 1. IMPRIMIR ENCABEZADOS DE LA TABLA
            // %-5s = Columna de Texto alineado a la Izquierda de 5 espacios
            // %-20s = Columna de Texto alineado a la Izquierda de 20 espacios
            System.out.printf("%-5s %-10s %-15s %-10s %-15s %-10s %-10s %-15s\n",
                    "#", "ID_CLI", "NOMBRE CLIENTE", "NUM_DOC", "$ MONTO", "INTERÉS", "CUOTAS", "EMPLEADO");

            System.out.println(
                    "-------------------------------------------------------------------------------------------------------------");

            // 2. FILAS (Aquí está la corrección clave)
            for (Prestamo p : lista) {
                System.out.printf("%-5d %-10d %-15s %-10s $%,-14.0f %-10.1f %-10d %-15s\n",
                        p.getId(), // %d para enteros (ID)
                        p.getClienteId(), // %d para enteros
                        p.getNombreCliente(), // %s para texto
                        p.getNumDocumento(), // %s para texto
                        p.getMonto(), // Usamos $%,-14.0f para dinero
                        p.getInteres(), // %.1f para 1 decimal
                        p.getCuotas(), // %d para enteros
                        p.getNombreEmpleado());// %s para texto
            }
            System.out.println(
                    "-------------------------------------------------------------------------------------------------------------");
        }
    }

    private void imprimirPrestamos(String titulo, List<Prestamo> lista) {
        System.out.println("\n--- " + titulo + " ---");

        if (lista.isEmpty()) {
            System.out.println("No hay registros en este momento.");
            return;
        }

        System.out.printf("%-5s %-20s %-12s %-15s %-10s %-12s\n",
                "ID", "CLIENTE", "DOC", "MONTO", "INTERES", "ESTADO");
        System.out.println("-------------------------------------------------------------------------------");

        for (Prestamo p : lista) {
            System.out.printf("%-5d %-20s %-12s $%,-14.0f %-9.1f %-12s\n",
                    p.getId(),
                    // Validamos null por si el JOIN no trajo nombre (aunque debería)
                    (p.getNombreCliente() != null ? p.getNombreCliente() : "N/A"),
                    (p.getNumDocumento() != null ? p.getNumDocumento() : "N/A"),
                    p.getMonto(),
                    p.getInteres(),
                    p.getEstado());
        }
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("TOTAL REGISTROS: " + lista.size());
    }

    private void imprimirPretsamosCantidades(String titulo, List<Prestamo> listaAct, List<Prestamo> listaVen) {

        if (listaAct.isEmpty()) {
            System.out.println("No hay registros para este reporte.");
            return;
        } else {
            System.out.println("  número total de préstamos activos, vencidos, y el monto total adeudado");
            int cantActivos = prestamoService.obtenerPrestamoActExam().size();
            System.out.println(" TOTAL PRESTAMOS ACTIVOS: " + cantActivos);
        }

        if (listaVen.isEmpty()) {
            System.out.println("No hay registros para este reporte.");
            return;
        } else {
            int cantVencidos = prestamoService.obtenerPrestamosMorExam().size();
            System.out.println(" TOTAL PRESTAMOS VENCIDOS: " + cantVencidos);
        }

        List<Prestamo> listaTotal = prestamoService.obtenerTodosLosPrestamos();
        System.out.println("TOTAL REGISTROS: " + listaTotal.size());

    }

}
