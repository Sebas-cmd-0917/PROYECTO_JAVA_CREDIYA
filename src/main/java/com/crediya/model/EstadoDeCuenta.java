package com.crediya.model;

import java.util.List;

public class EstadoDeCuenta {
    private Prestamo prestamo;
    private List<PagoExamen> listaPagos;
    private double deudaTotalInicial;
    private double totalPagado;
    private double saldoFinal;

    // Constructor
    public EstadoDeCuenta(Prestamo p, List<PagoExamen> pagos, double deuda, double pagado, double saldo) {
        this.prestamo = p;
        this.listaPagos = pagos;
        this.deudaTotalInicial = deuda;
        this.totalPagado = pagado;
        this.saldoFinal = saldo;
    }

    // Getters
    public Prestamo getPrestamo() { return prestamo; }
    public List<PagoExamen> getListaPagos() { return listaPagos; }
    public double getDeudaTotalInicial() { return deudaTotalInicial; }
    public double getTotalPagado() { return totalPagado; }
    public double getSaldoFinal() { return saldoFinal; }
}
