package com.crediya.dao;

import java.util.List;
import com.crediya.model.Prestamo;

public interface PrestamoDAO {
    void registrarPrestamo(Prestamo prestamo);
    List<Prestamo> listarPorCliente(int clienteId);
    Prestamo obtenerPorId(int id); //verificar pagos
} 
    
