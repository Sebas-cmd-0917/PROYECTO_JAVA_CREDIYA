package com.crediya;

import java.sql.Connection;

import com.crediya.config.ConexionDB;

public class Main {
    public static void main(String[] args) {
        System.out.println("¡Bienvenido a CrediYa!");
        System.out.println("Iniciando prueba de conexión...");
        
        // Llamamos al método de conexión
        Connection con = ConexionDB.getConnection();
        
        if (con != null) {
            System.out.println("El sistema está listo para recibir datos.");
        } else {
            System.out.println("El sistema falló al conectar.");
        }
    }
}