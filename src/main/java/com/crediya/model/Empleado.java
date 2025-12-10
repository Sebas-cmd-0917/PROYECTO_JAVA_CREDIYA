package com.crediya.model;

public class Empleado {
    private int id;
    private String nombre;
    private String documento;
    private String rol;
    private String correo;
    private double salario;

    public Empleado(){

    };

    public Empleado(String nombre, String documento, String rol, String correo, double salario) {
        this.nombre = nombre;
        this.documento = documento;
        this.rol = rol;
        this.correo = correo;
        this.salario = salario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    @Override
    public String toString() {
        return "Empleado{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", documento='" + documento + '\'' +
                ", rol='" + rol + '\'' +
                ", correo='" + correo + '\'' +
                ", salario=" + salario +
                '}';
    }


}