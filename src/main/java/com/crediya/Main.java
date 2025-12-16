package com.crediya;

import java.util.Scanner;

import com.crediya.model.Empleado;
import com.crediya.service.GestorEmpleadoService;
import com.crediya.ui.MenuPrincipal;

public class Main {
    public static void main(String[] args) {
       Scanner scanner = new Scanner(System.in);
        GestorEmpleadoService authService = new GestorEmpleadoService();
        
        while (true) {
            System.out.println("========================================");
            System.out.println("==== BIENVENIDO A CREDIYA MAYSE ========");
            System.out.println("========================================");

            System.out.println("--------- INICIO DE SESIÓN ------------");
            System.out.println("Por favor, ingrese sus credenciales.");
            System.out.println("---------------------------------------");
            System.out.print("Correo: ");
            String correo = scanner.nextLine();
            
            try {
                // 1. Esto busca el empleado en la BD. Si existe, te devuelve el objeto lleno.
                Empleado usuarioEncontrado = authService.iniciarSesion(correo);
                
                // 2. ¡AQUÍ ESTÁ LA CLAVE! Pasas el objeto real, NO null.
                MenuPrincipal menu = new MenuPrincipal(usuarioEncontrado);
                menu.mostrarMenu();
                
                // Si sale del menú, rompe el ciclo
                break;
                
            } catch (Exception e) {
                System.out.println("❌ " + e.getMessage());
            }
        }
    }
}