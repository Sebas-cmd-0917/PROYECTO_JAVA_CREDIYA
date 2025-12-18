package com.crediya.service;

import java.util.List;
import java.util.stream.Collectors;

import com.crediya.data.repositories.PrestamoDAOImpl;
import com.crediya.model.EstadoPrestamo;
import com.crediya.model.Prestamo;
import com.crediya.repository.PrestamoRepository;

public class GestorPrestamos {

    private final PrestamoRepository preRep = new PrestamoDAOImpl();
    
    public List<Prestamo> obtenerTodosLosPrestamos() {
        return preRep.listarPrestamosExamen();
    }


    //Para el examen prestamos activos
    public List<Prestamo> obtenerPrestamoActExam() {
        List<Prestamo> todos = preRep.listarPrestamos();
        
        return todos.stream()
                .filter(p -> p.getEstado() == EstadoPrestamo.PENDIENTE)
                .collect(Collectors.toList());
    }
    //vencidos
    public List<Prestamo> obtenerPrestamosMorExam() {
        List<Prestamo> todos = preRep.listarPrestamos();

        return todos.stream()
                .filter(p -> p.getEstado() == EstadoPrestamo.MORA)
                .collect(Collectors.toList());
    }
}
