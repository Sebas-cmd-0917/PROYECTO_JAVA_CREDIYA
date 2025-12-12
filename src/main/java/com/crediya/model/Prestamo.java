package com.crediya.model;

import java.time.LocalDate;

public class Prestamo {
    private int id;
    private int clienteId;
    private int empleadoId;
    private String nombreCliente;
    private String numDocumento;
    private String nombreEmpleado;
    private double monto;
    private double interes;
    private int cuotas;
    private LocalDate fechaInicio;
    private String estado;

    //Buena practica
    public Prestamo() {
    }


    //se usa para crear un préstamo NUEVO.
    public Prestamo(int clienteId, int empleadoId, double monto, double interes, int cuotas,
            LocalDate fechaInicio, String estado) {
        this.clienteId = clienteId;
        this.empleadoId = empleadoId;
        this.monto = monto;
        this.interes = interes;
        this.cuotas = cuotas;
        this.fechaInicio = fechaInicio;
        this.estado = estado;
    }

    //Con este se realiza las consultas, cuando ya este el id asignado
    public Prestamo(int id, int clienteId, int empleadoId, double monto, double interes, int cuotas,
            LocalDate fechaInicio, String estado) {
        this.id = id;
        this.clienteId = clienteId;
        this.empleadoId = empleadoId;
        this.monto = monto;
        this.interes = interes;
        this.cuotas = cuotas;
        this.fechaInicio = fechaInicio;
        this.estado = estado;
    }

    


    public String getNombreCliente() {
        return nombreCliente;
    }


    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }


    public String getNumDocumento() {
        return numDocumento;
    }


    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }


    public String getNombreEmpleado() {
        return nombreEmpleado;
    }


    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getClienteId() {
        return clienteId;
    }


    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }


    public int getEmpleadoId() {
        return empleadoId;
    }


    public void setEmpleadoId(int empleadoId) {
        this.empleadoId = empleadoId;
    }


    public double getMonto() {
        return monto;
    }


    public void setMonto(double monto) {
        this.monto = monto;
    }


    public double getInteres() {
        return interes;
    }


    public void setInteres(double interes) {
        this.interes = interes;
    }


    public int getCuotas() {
        return cuotas;
    }


    public void setCuotas(int cuotas) {
        this.cuotas = cuotas;
    }


    public LocalDate getFechaInicio() {
        return fechaInicio;
    }


    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }


    public String getEstado() {
        return estado;
    }


    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Prestamo [id=" + id + ", cliente=" + clienteId + ", monto=" + monto + ", estado=" + estado + "]";
    }


}


