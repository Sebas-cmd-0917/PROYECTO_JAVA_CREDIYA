package com.crediya.service;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDate;

import com.crediya.data.repositories.PrestamoDAOImpl;
import com.crediya.model.Prestamo;
import com.crediya.repository.PrestamoRepository;

public class PrestamoService {
    private final PrestamoRepository prestamoRepository = new PrestamoDAOImpl();
    private final CalculadoraPrestamosService calculadoraPrestamosService = new CalculadoraPrestamosService();

    public void registrarPrestamo(int clienteId, int empleadoId, double monto, double interes, int cuotas){
        try {
            if (monto <= 0) throw new Exception("El monto debe ser positivo");

            //model
            Prestamo prestamo = new Prestamo();
            prestamo.setClienteId(clienteId);
            prestamo.setEmpleadoId(empleadoId);
            prestamo.setMonto(monto);
            prestamo.setInteres(interes);
            prestamo.setCuotas(cuotas);
            prestamo.setFechaInicio(LocalDate.now());
            prestamo.setEstado("PENDIENTE");// ESTADO INICIAL

            prestamoRepository.registrarPrestamo(prestamo);

            guardarEnArchivoTxt(prestamo);

        } catch (Exception e) {
            System.out.println("❌ Error en el servicio de préstamos: " + e.getMessage());
        }
    }

    private void guardarEnArchivoTxt(Prestamo p){
        double cuotaMensual = calculadoraPrestamosService.calcularCuotaMensual(p.getMonto(), p.getInteres(), p.getCuotas());
        double totalPagar = cuotaMensual * p.getCuotas();

        String linea = String.format("%d;%.2f;%.2f;%.2f;%s;%s", p.getClienteId(), p.getMonto(), p.getInteres(), totalPagar, p.getEstado(), p.getFechaInicio());
        try (FileWriter fw = new FileWriter("prestamos.txt", true);
            PrintWriter pw = new PrintWriter(fw)){
                pw.println(linea);
                System.out.println("📁 Respaldo guardado en 'prestamos.txt'");
            
        } catch (Exception e) {
            System.out.println("⚠ No se pudo guardar en el archivo de texto: " + e.getMessage());
        }

    }

    public void finalizarPrestamo(int prestamoId){
        prestamoRepository.actualizarEstado(prestamoId, "PAGADO");

    }

}
    

