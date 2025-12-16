package com.crediya.ui;

import java.util.List;
import java.util.Scanner;

import com.crediya.model.Prestamo;
import com.crediya.service.ReporteService;

public class MenuReportes {
    private final ReporteService reporteService = new ReporteService();
    private final Scanner scanner = new Scanner(System.in);

    public void mostrarMenu() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n===== 📊 MÓDULO DE REPORTES E INDICADORES =====");
            System.out.println("1. ⏳ Reporte de Préstamos ACTIVOS (Pendientes)");
            System.out.println("2. 🚨 Reporte de Clientes en MORA (Riesgo)");
            System.out.println("3. ✅ Histórico de Préstamos PAGADOS");
            System.out.println("4. 📈 Resumen Financiero General");
            System.out.println("0. 🔙 Volver al Menú Principal");
            System.out.print("👉 Seleccione una opción: ");
            try {
                opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {
                    case 1:
                        imprimirTabla("PRÉSTAMOS ACTIVOS", reporteService.obtenerPrestamosActivos());
                        break;
                    case 2:
                        imprimirTabla("⚠️ CLIENTES EN MORA ⚠️", reporteService.obtenerPrestamosEnMora());
                        break;
                    case 3:
                        imprimirTabla("HISTÓRICO PAGADO", reporteService.obtenerPrestamosPagados());
                        break;
                    case 4:
                        mostrarResumenFinanciero();
                        break;
                    case 0:
                        System.out.println("Volviendo al menú principal...");
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Error: Ingrese un número válido.");
            }
        }
    }

    // Método auxiliar para no repetir código de impresión de tablas
    private void imprimirTabla(String titulo, List<Prestamo> lista) {
        System.out.println("\n--- " + titulo + " ---");

        if (lista.isEmpty()) {
            System.out.println("No hay registros para este reporte.");
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

    private void mostrarResumenFinanciero() {
        double totalPrestado = reporteService.calcularTotalDineroPrestado();
        double totalInteres = reporteService.calcularTotalInteresProyectado();
        double proyeccionTotal = totalPrestado + totalInteres;

        System.out.println("\n===== 💰 RESUMEN FINANCIERO GENERAL =====");
        System.out.printf("Dinero Total Prestado (Capital):   $%,20.2f\n", totalPrestado);
        System.out.printf("Intereses Proyectados (Ganancia):  $%,20.2f\n", totalInteres);
        System.out.println("-------------------------------------------------------");
        System.out.printf("Cartera Total (Capital + Interés): $%,20.2f\n", proyeccionTotal);
        System.out.println("=======================================================");
    }
}
