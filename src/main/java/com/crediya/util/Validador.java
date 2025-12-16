package com.crediya.util;

import java.util.regex.Pattern;

public class Validador {

    // Regex para validar correos reales (texto@dominio.algo)
    private static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    
    // Regex para verificar que sea solo números
    private static final String NUMERICO_REGEX = "\\d+";

    // Validar si un texto es un correo válido
    public static boolean esCorreoValido(String correo) {
        if (correo == null) return false;
        return Pattern.compile(EMAIL_REGEX).matcher(correo).matches();
    }

    // Validar si es número y cumple con el largo mínimo y máximo
    public static boolean esNumericoYLongitud(String str, int min, int max) {
        if (str == null) return false;
        if (!str.matches(NUMERICO_REGEX)) return false; 
        return str.length() >= min && str.length() <= max;
    }
    
    // Validar que un texto no esté vacío y tenga un largo mínimo
    public static boolean esTextoValido(String str, int minLength) {
        if (str == null || str.trim().isEmpty()) return false;
        return str.length() >= minLength;
    }
}