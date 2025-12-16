package com.crediya.util;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ArchivoTextoUtil {

    // Archivo unificado para ambos
    private static final String ARCHIVO_REGISTROS = "registros_usuarios.txt";

    /**
     * Guarda un registro con formato de ficha estilizada en el archivo unificado.
     */
    public static void guardarRegistroBonito(String tipoUsuario, String[] etiquetas, String[] valores) {
        try (FileWriter fw = new FileWriter(ARCHIVO_REGISTROS, true);
             PrintWriter pw = new PrintWriter(fw)) {
            
            String fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            pw.println("╔════════════════════════════════════════════════╗");
            // Centrar el título (aprox)
            pw.printf("║ %-46s ║%n", "NUEVO REGISTRO: " + tipoUsuario.toUpperCase());
            pw.println("╠════════════════════════════════════════════════╣");
            pw.printf("║ %-15s : %-28s ║%n", "FECHA REGISTRO", fechaHora);
            pw.println("╟────────────────────────────────────────────────╢");
            
            // Imprimir cada dato alineado
            for (int i = 0; i < etiquetas.length; i++) {
                // %-15s = Etiqueta a la izquierda (15 espacios)
                // %-28s = Valor a la izquierda (28 espacios)
                pw.printf("║ %-15s : %-28s ║%n", etiquetas[i], valores[i]);
            }
            
            pw.println("╚════════════════════════════════════════════════╝");
            pw.println(""); // Espacio vacío entre registros

        } catch (IOException e) {
            System.err.println("⚠️ Error guardando en el archivo unificado: " + e.getMessage());
        }
    }
}