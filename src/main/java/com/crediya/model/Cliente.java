package com.crediya.model;

// 1. 'extends' significa que Cliente HEREDA de Persona
public class Cliente extends Persona {
    
    // Solo declaramos lo que NO está en Persona
    private String telefono;

    public Cliente() {
        super(); // Llama al constructor vacío de Persona
    }

    public Cliente(int id, String nombre, String documento, String correo, String telefono) {
        // 2. 'super(...)' envía los datos comunes a la clase padre para que ella los guarde
        super(id, nombre, documento, correo);
        
        // Nosotros solo nos encargamos del dato específico
        this.telefono = telefono;
    }

    // Solo necesitamos Getters/Setters de telefono
    // (Los de nombre, documento, etc., ya los heredamos "gratis")
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    @Override
    public String toString() {
        return "Cliente [id=" + id + ", nombre=" + nombre + ", telefono=" + telefono + "]";
    }
}