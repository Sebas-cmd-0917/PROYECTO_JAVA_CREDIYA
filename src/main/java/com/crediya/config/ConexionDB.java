package com.crediya.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionDB {

    private static String URL;
    private static String USER;
    private static String PASSWORD;

    // Bloque estático: Se ejecuta una sola vez cuando el programa arranca
    static {
        Properties props = new Properties();
        try (InputStream input = ConexionDB.class.getClassLoader().getResourceAsStream("db.properties")) {
            
            if (input == null) {
                System.out.println("❌ Error: No se pudo encontrar el archivo db.properties");
            } else {
                // Cargamos los datos del archivo
                props.load(input);
                
                // Asignamos a las variables
                URL = props.getProperty("db.url");
                USER = props.getProperty("db.user");
                PASSWORD = props.getProperty("db.password");
            }

        } catch (IOException ex) {
            System.out.println("❌ Error leyendo configuración de BD: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Cargar Driver (opcional en versiones nuevas de Java, pero bueno mantenerlo)
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Usar las variables que leímos del archivo
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            
        } catch (ClassNotFoundException e) {
            System.out.println("❌ Falta el Driver MySQL JDBC.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("❌ Error de Conexión SQL: " + e.getMessage());
        }
        return connection;
    }
}