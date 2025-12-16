package com.crediya.service;

import java.util.List;
import java.util.stream.Collectors;

import com.crediya.data.repositories.PrestamoDAOImpl;
import com.crediya.model.EstadoPrestamo;
import com.crediya.model.Prestamo;
import com.crediya.repository.PrestamoRepository;

public class ReporteService {
    // Inyectamos el repositorio de préstamos para obtener los datos
    private final PrestamoRepository prestamoRepo = new PrestamoDAOImpl();

    /**
     * REQUERIMIENTO: Consultar préstamos activos (PENDIENTE)
     * Uso de Stream API: .filter()
     */
    public List<Prestamo> obtenerPrestamosActivos() {
        List<Prestamo> todos = prestamoRepo.listarPrestamos();
        
        return todos.stream()
                .filter(p -> p.getEstado() == EstadoPrestamo.PENDIENTE)
                .collect(Collectors.toList());
    }

    /**
     * REQUERIMIENTO: Consultar préstamos vencidos/mora
     * Uso de Stream API: .filter()
     */
    public List<Prestamo> obtenerPrestamosEnMora() {
        List<Prestamo> todos = prestamoRepo.listarPrestamos();

        return todos.stream()
                .filter(p -> p.getEstado() == EstadoPrestamo.MORA)
                .collect(Collectors.toList());
    }

    /**
     * REQUERIMIENTO: Consultar historial de pagados
     */
    public List<Prestamo> obtenerPrestamosPagados() {
        List<Prestamo> todos = prestamoRepo.listarPrestamos();

        return todos.stream()
                .filter(p -> p.getEstado() == EstadoPrestamo.PAGADO)
                .collect(Collectors.toList());
    }

    /**
     * REQUERIMIENTO EXTRA: Resumen Financiero
     * Uso de Stream API: .mapToDouble() y .sum() para sumar montos
     */
    public double calcularTotalDineroPrestado() {
        return prestamoRepo.listarPrestamos().stream()
                .mapToDouble(Prestamo::getMonto)
                .sum();
    }

    // Calcula cuánto dinero debería entrar en intereses (Proyección)
    public double calcularTotalInteresProyectado() {
        return prestamoRepo.listarPrestamos().stream()
                .mapToDouble(p -> p.getMonto() * (p.getInteres() / 100))
                .sum();
    }
}
