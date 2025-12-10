package com.crediya.ui;

import java.time.LocalDate;
import java.util.Scanner;

import com.crediya.dao.PrestamoDAO;
import com.crediya.dao.impl.PrestamoDAOImpl;
import com.crediya.model.Pago;
import com.crediya.service.CalculadoraPrestamosService;
import com.crediya.service.GestorPagosService;

public class MenuPago {
    

    public void mostrarMenuPago(){
        Scanner scanner = new Scanner(System.in);
        PrestamoDAO prestamoDAO = new PrestamoDAOImpl();
        GestorPagosService gestorPagosService = new GestorPagosService();
        CalculadoraPrestamosService calculadoraPrestamosService = new CalculadoraPrestamosService();

        // Lógica para gestionar pagos
                try {
                        System.out.print("ID del Préstamo: ");
                        int pId = scanner.nextInt();
                        System.out.print("Monto a abonar: ");
                        double abono = scanner.nextDouble();
                        scanner.nextLine();

                        // Llama a Gestor de Pagos (que valida si la deuda no se excede)
                        Pago nuevoPago = new Pago(pId, LocalDate.now(), abono);
                        gestorPagosService.registrarAbono(nuevoPago);

                    } catch (Exception e) {
                        System.out.println("❌ Error: Datos inválidos.");
                        scanner.nextLine();
                    }
    }

}
