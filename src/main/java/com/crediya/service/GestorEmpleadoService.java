package com.crediya.service;

import java.util.List;

import com.crediya.data.repositories.EmpleadoDAOImpl;
import com.crediya.model.Empleado;

public class GestorEmpleadoService {
    // El servicio tiene a su cargo al repositorio
    private EmpleadoDAOImpl empleadoRepo = new EmpleadoDAOImpl();

    public void registrarEmpleado(Empleado empleado) {
        // Aquí podrías poner validaciones antes de guardar
        // Ej: if (empleado.getSalario() < 0) { Error }
        
        empleadoRepo.guardarEmpleado(empleado);
    }

    public List<Empleado> obtenerTodos() {
        return empleadoRepo.listarTodosEmpleados();
    }
}
