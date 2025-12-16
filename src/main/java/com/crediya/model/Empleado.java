package com.crediya.model;

public class Empleado extends Persona {

    // Atributos exclusivos de Empleado
    private String rol;
    private double salario;

    public Empleado() {
        super();
    }

    public Empleado(int id, String nombre, String documento, String rol, String correo, double salario) {
        // Enviamos los datos comunes al padre
        super(id, nombre, documento, correo);

        // Guardamos los propios
        this.rol = rol;
        this.salario = salario;
    }

    // Getters y Setters exclusivos
    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    @Override
    public String toString() {
        return "Empleado [nombre=" + nombre + ", rol=" + rol + "]";
    }
}