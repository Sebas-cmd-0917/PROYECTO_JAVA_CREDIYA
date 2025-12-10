package com.crediya;

import java.sql.Connection;
import com.crediya.config.ConexionDB;
import com.crediya.ui.MenuPrincipal;

public class Main {
    public static void main(String[] args) {
        MenuPrincipal miMenu = new MenuPrincipal();
        miMenu.mostrarMenu();
    }
}