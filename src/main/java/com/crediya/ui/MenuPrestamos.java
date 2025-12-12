package com.crediya.ui;

import java.util.Scanner;

// Importaciones necesarias para buscar
import com.crediya.data.repositories.ClienteDAOImpl;
import com.crediya.data.repositories.EmpleadoDAOImpl; // <--- NUEVO
import com.crediya.model.Cliente;
import com.crediya.model.Empleado;
import com.crediya.repository.ClienteRepository;
import com.crediya.repository.EmpleadoRepository; // <--- NUEVO
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
    CalculadoraPrestamosService calculadoraPrestamosService = new CalculadoraPrestamosService();
    PrestamoService prestamoService = new PrestamoService();

    public void mostrarMenuPrestamo() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n===== 📌 MENÚ DE PRÉSTAMOS =====");
            System.out.println("1. Registrar préstamo (BD + Archivo)");
            System.out.println("2. Simular préstamo");
            System.out.println("3. Finalizar préstamo (Cambiar a PAGADO)");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine(); // limpiar buffer

            switch (opcion) {
                case 1: registrarPrestamo(); break;
                case 2: simularPrestamo(); break;
                case 3: cambiarEstadoPrestamo(); break;
                case 0: System.out.println("Volviendo al menú principal..."); break;
                default: System.out.println("❌ Opción inválida");
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