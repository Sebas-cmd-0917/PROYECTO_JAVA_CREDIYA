package com.crediya.data.entities;

import java.time.LocalDate;


public class PagoEntity {
    private int id;
    private int prestamoId;
    private LocalDate fechaPago;
    private double monto;

    
    public PagoEntity() {
    }


    public PagoEntity(int id, int prestamoId, LocalDate fechaPago, double monto) {
        this.id = id;
        this.prestamoId = prestamoId;
        this.fechaPago = fechaPago;
        this.monto = monto;
    }


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public int getPrestamoId() {
        return prestamoId;
    }


    public void setPrestamoId(int prestamoId) {
        this.prestamoId = prestamoId;
    }


    public LocalDate getFechaPago() {
        return fechaPago;
    }


    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }


    public double getMonto() {
        return monto;
    }


    public void setMonto(double monto) {
        this.monto = monto;
    }

    
    


}
