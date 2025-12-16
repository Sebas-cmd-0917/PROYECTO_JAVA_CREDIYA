package com.crediya.model;

public class Persona {
    // Usamos 'protected' en lugar de 'private' para que las clases hijas (Cliente/Empleado)
    // puedan acceder a estos datos directamente si lo necesitan.
    protected int id;
    protected String nombre;
    protected String documento;
    protected String correo;

    // Constructor vacío
    public Persona() {
    }

    // Constructor con los datos comunes
    public Persona(int id, String nombre, String documento, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.documento = documento;
        this.correo = correo;
    }

    // --- Getters y Setters (Comunes para todos) ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
}
