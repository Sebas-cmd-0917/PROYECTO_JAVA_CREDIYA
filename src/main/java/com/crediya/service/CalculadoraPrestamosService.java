package com.crediya.service;

public class CalculadoraPrestamosService {
    public double calcularCuotaMensual(double monto, double interesesPorcentaje, int cuotas){
        double interesDecimal = interesesPorcentaje / 100;
        double totalInteres = monto * interesDecimal;
        double totalAPagar = monto + totalInteres;
        return totalAPagar / cuotas;
    }

    public String imprimirTablaAmortizacion(double monto, double interes, int cuotas){
        StringBuilder sBuilder = new StringBuilder();
        double cuota = calcularCuotaMensual(monto, interes, cuotas);
        sBuilder.append("--- Tabla de Amortización Proyectada ---");
        sBuilder.append("\nMonto: " + monto + " | Interés: " + interes + "%");
        for (int i = 1; i <= cuotas; i++) {
            sBuilder.append("\nCuota " + i + ": $" + String.format("%.2f", cuota));
        }
        return sBuilder.toString();
    }
    
}
