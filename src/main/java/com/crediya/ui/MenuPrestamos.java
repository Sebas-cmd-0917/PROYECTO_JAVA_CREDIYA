package com.crediya.ui;


import java.util.List;
import java.util.Scanner;

// Importaciones necesarias para buscar
import com.crediya.data.repositories.ClienteDAOImpl;
import com.crediya.data.repositories.EmpleadoDAOImpl; // <--- NUEVO
import com.crediya.model.Cliente;
import com.crediya.model.Empleado;
import com.crediya.repository.ClienteRepository;
import com.crediya.repository.EmpleadoRepository; // <--- NUEVO

import com.crediya.model.Prestamo;
import com.crediya.service.CalculadoraPrestamosService;
import com.crediya.service.GestorPagosService;
import com.crediya.service.PrestamoService;

public class MenuPrestamos {

    Scanner scanner = new Scanner(System.in);
    
    // 1. INICIALIZAMOS LOS REPOSITORIOS PARA PODER BUSCAR
    // Antes tenías "private final ... clienteRepo;" sin inicializar (daba error null)
    private ClienteRepository clienteRepository = new ClienteDAOImpl();
    private EmpleadoRepository empleadoRepository = new EmpleadoDAOImpl(); 

    GestorPagosService gestorPagosService = new GestorPagosService();
    PrestamoService prestamoService = new PrestamoService();

    CalculadoraPrestamosService calculadoraPrestamosService = new CalculadoraPrestamosService();

    public void mostrarMenuPrestamo() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n===== 📌 MENÚ DE PRÉSTAMOS =====");
            System.out.println("1. Registrar préstamo (BD + Archivo)");
            System.out.println("2. Simular préstamo");
            System.out.println("3. Finalizar préstamo (Cambiar a PAGADO)");
            System.out.println("4. Listar préstamos");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine(); // limpiar buffer

            switch (opcion) {
                case 1:
                    registrarPrestamo();
                    break;
                case 2:
                    simularPrestamo();
                    break;
                case 3:
                    cambiarEstadoPrestamo();
                    break;
                case 4:
                    listarPrestamos();
                    break;
                case 0:
                    System.out.println("Volviendo al menú principal...");
                    break;
                default:
                    System.out.println("❌ Opción inválida");
            }
        }
        
    }

    // 👉 OPCIÓN 1: Registrar préstamo (MODIFICADO POR DOCUMENTO)
    private void registrarPrestamo() {
        try {
            System.out.println("\n--- Registrar préstamo ---");

            // --- BUSCAR CLIENTE ---
            System.out.print("Ingrese Documento del Cliente: ");
            String docCliente = scanner.next(); // <--- Leemos String
        

            // --- BUSCAR EMPLEADO ---
            System.out.print("Ingrese Documento del Empleado: ");
            String docEmpleado = scanner.next();

            // --- PEDIR EL RESTO DE DATOS ---
            System.out.print("Monto: ");
            double monto = scanner.nextDouble();

            System.out.print("Interés (%): ");
            double interes = scanner.nextDouble();

            System.out.print("Cuotas: ");
            int cuotas = scanner.nextInt();
            scanner.nextLine();

            // Usamos los IDs que recuperamos de la búsqueda (cliente.getId())
            prestamoService.registrarPrestamo(docCliente,docEmpleado, monto, interes, cuotas);

        } catch (Exception e) {
            System.out.println("❌ Error al registrar préstamo: " + e.getMessage());
            scanner.nextLine();
        }
    }



     //listar prestammos

    public void listarPrestamos() {
        System.out.println("\n--- Lista de Préstamos ---");
        List <Prestamo> lista = prestamoService.obtenerTodos();
                if (lista.isEmpty()) {
                    System.out.println("No hay préstamos registrados.");
                } else {
                    // 1. IMPRIMIR ENCABEZADOS DE LA TABLA
                        // %-5s  = Columna de Texto alineado a la Izquierda de 5 espacios
                        // %-20s = Columna de Texto alineado a la Izquierda de 20 espacios
                        System.out.printf("%-5s %-10s %-15s %-10s %-10s %-10s %-10s %15s\n", 
                                          "#", "ID_CLI","NOMBRE CLIENTE","NUM_DOC",  "$ MONTO", "INTERÉS", "CUOTAS" ,"NOMBRE_EMPLEADO");
                        
                        System.out.println("---------------------------------------------------------------------------------------------------------------------------");

                        // 2. IMPRIMIR CADA FILA CON EL MISMO FORMATO
                        for (Prestamo p : lista) {
                            System.out.printf("%-5s %-10s %-15s %-10s %-10s %-10s %-10s %10s\n", 
                                    p.getId(),            // %d para números enteros
                                    p.getClienteId(), 
                                    p.getNombreCliente(),
                                    p.getNumDocumento(),
                                    p.getMonto(),
                                    p.getInteres(),
                                    p.getCuotas(),     // %,.2f para dinero (con comas y 2 decimales)
                                    p.getNombreEmpleado());    // %s para texto
                                    


            }
                        System.out.println("---------------------------------------------------------------------------------------------------------------------------");
            
        }
    }

    // 👉 OPCIÓN 2: Simular préstamo (MODIFICADO POR DOCUMENTO)
    private void simularPrestamo() {
        try {
            System.out.println("\n--- Simular préstamo ---");

            // Repetimos la lógica de búsqueda para obtener los IDs válidos
            System.out.print("Ingrese Documento del Cliente: ");
            String docCliente = scanner.next();

            System.out.print("Ingrese Documento del Empleado: ");
            String docEmpleado = scanner.next();
            Empleado empleado = empleadoRepository.buscarPorDocumentoEmpleado(docEmpleado);
            if (empleado == null) { System.out.println("❌ Empleado no encontrado."); return; }

            System.out.print("Monto: ");
            double monto = scanner.nextDouble();

            System.out.print("Interés (%): ");
            double interes = scanner.nextDouble();

            System.out.print("Cuotas: ");
            int cuotas = scanner.nextInt();
            scanner.nextLine();

            // Mostrar simulación
            System.out.println(calculadoraPrestamosService.imprimirTablaAmortizacion(monto, interes, cuotas));

            // Preguntar si quiere registrar
            System.out.print("\n¿Desea registrar este préstamo? (S/N): ");
            String confirmacion = scanner.nextLine();

            if (confirmacion.equalsIgnoreCase("S")) {
                // Usamos los IDs encontrados arriba
                prestamoService.registrarPrestamo(docCliente, docEmpleado, monto, interes, cuotas);
            } else {
                System.out.println("❌ Registro cancelado. Solo se realizó la simulación.");
            }

        } catch (Exception e) {
            System.out.println("❌ Error al simular préstamo: " + e.getMessage());
            scanner.nextLine();
        }
    }

    private void cambiarEstadoPrestamo() {
        // Aquí seguimos pidiendo ID del préstamo porque es único para el sistema
        System.out.print("\nIngrese el ID del préstamo a finalizar: ");
        int pId = scanner.nextInt();
        prestamoService.finalizarPrestamo(pId);
    }
}