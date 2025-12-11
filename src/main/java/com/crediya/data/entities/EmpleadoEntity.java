package com.crediya.data.entities;

public class EmpleadoEntity {

    // Estos atributos deben llamarse EXACTAMENTE igual a tu idea de la tabla
    private int id;
    private String nombre;
    private String documento;
    private String rol;
    private String correo;
    private double salario;

    public EmpleadoEntity() { }

    // Constructor completo
    public EmpleadoEntity(int id, String nombre, String documento, String rol, String correo, double salario) {
        this.id = id;
        this.nombre = nombre;
        this.documento = documento;
        this.rol = rol;
        this.correo = correo;
        this.salario = salario;
    }

    // --- GETTERS Y SETTERS (Obligatorios) ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public double getSalario() { return salario; }
    public void setSalario(double salario) { this.salario = salario; }
}

