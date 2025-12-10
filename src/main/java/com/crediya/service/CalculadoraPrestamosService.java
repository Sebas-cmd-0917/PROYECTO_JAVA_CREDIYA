package com.crediya.service;

public class CalculadoraPrestamosService {
    public double calcularCuotaMensual(double monto, double interesesPorcentaje, int cuotas){
        double interesDecimal = interesesPorcentaje / 100;
        double totalInteres = monto * interesDecimal;
        double totalAPagar = monto + totalInteres;
        return totalAPagar / cuotas;
    }

    public void imprimirTablaAmortizacion(double monto, double interes, int cuotas){
        double cuota = calcularCuotaMensual(monto, interes, cuotas);
        System.out.println("--- Tabla de Amortización Proyectada ---");
        System.out.println("Monto: " + monto + " | Interés: " + interes + "%");
        for (int i = 1; i <= cuotas; i++) {
            System.out.println("Cuota " + i + ": $" + String.format("%.2f", cuota));
        }
    }
    
}
