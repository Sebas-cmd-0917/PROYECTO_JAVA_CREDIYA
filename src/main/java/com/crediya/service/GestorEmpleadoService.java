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

    // --- MODIFICAR ---
    public void actualizarEmpleado(int id, String nombre, String doc, String rol, String correo, double salario) throws Exception {
        if (id <= 0) throw new Exception("ID inválido.");
        
        validarDatos(nombre, doc, correo, salario);

        Empleado aActualizar = new Empleado();
        aActualizar.setId(id); // ¡Fundamental para el WHERE del SQL!
        aActualizar.setNombre(nombre);
        aActualizar.setDocumento(doc);
        aActualizar.setRol(rol);
        aActualizar.setCorreo(correo);
        aActualizar.setSalario(salario);

        empleadoRepo.modificarEmpleado(aActualizar);
    }

    // --- ELIMINAR ---
    public void borrarEmpleado(int id) throws Exception {
        if (id <= 0) throw new Exception("ID inválido.");
        // Aquí podrías validar: si el empleado tiene préstamos asignados, no borrarlo.
        empleadoRepo.eliminarEmpleado(id);
    }

    // --- HELPER: BUSCAR POR ID (Para el Menú) ---
    public Empleado buscarPorId(int id) {
        // Como tu DAO no tiene buscarPorId, lo simulamos filtrando la lista completa
        // (Lo ideal sería agregar buscarPorId en el DAO más adelante)
        List<Empleado> todos = empleadoRepo.listarTodosEmpleados();
        for (Empleado e : todos) {
            if (e.getId() == id) {
                return e;
            }
        }
        return null;
    }

    // --- VALIDACIONES COMUNES ---
    private void validarDatos(String nombre, String doc, String correo, double salario) throws Exception {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new Exception("El nombre es obligatorio.");
        }
        if (!doc.matches("\\d+")) {
            throw new Exception("El documento debe contener solo números.");
        }
        if (correo != null && !correo.contains("@")) {
            throw new Exception("Formato de correo inválido.");
        }
        if (salario < 0) {
            throw new Exception("El salario no puede ser negativo.");
        }
    }
}
