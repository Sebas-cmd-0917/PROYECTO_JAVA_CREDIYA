package com.crediya.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    //jdbc:mysql: esto se usa para decirle. ajava que jablaremos una base de datos mysql
    private static final String URL = "jdbc:mysql://localhost:3307/crediya_db";
    private static final String USER = "root"; 
    private static final String PASSWORD = "admin"; 

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Intentamos conectar
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            // System.out.println("✅ ¡Conexión a Base de Datos EXITOSA!");
            
        } catch (ClassNotFoundException e) {
            // System.out.println("❌ Error: No se encontró el Driver de MySQL (Falta el .jar).");
            e.printStackTrace();
        } catch (SQLException e) {
            // System.out.println("❌ Error de Conexión SQL: Revisa si XAMPP/MySQL está prendido.");4
            
            System.out.println("Mensaje: " + e.getMessage());
        }
        return connection;
    }
}
