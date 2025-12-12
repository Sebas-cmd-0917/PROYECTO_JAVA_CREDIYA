package com.crediya.repository;

import java.util.List;

import com.crediya.model.Prestamo;

public interface PrestamoRepository {
    void registrarPrestamo(Prestamo prestamo);
    List<Prestamo> listarPorCliente(int clienteId);
   // List<Prestamo> listarPrestamos();
    Prestamo obtenerPorId(int id);//verificar pagos

    void actualizarEstado(int prestamo_id, String nuevoEstado); 
}
