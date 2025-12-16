package com.crediya.service;

import java.util.List;

import com.crediya.data.repositories.EmpleadoDAOImpl;
import com.crediya.model.Empleado;
import com.crediya.util.ArchivoTextoUtil;
import com.crediya.util.Validador;

public class GestorEmpleadoService {
    
    private EmpleadoDAOImpl empleadoRepo = new EmpleadoDAOImpl();

    public void registrarEmpleado(Empleado empleado) throws Exception {
        validarDatos(empleado);
        
        Empleado existente = empleadoRepo.buscarPorDocumentoEmpleado(empleado.getDocumento());
        if(existente != null){
             throw new Exception("❌ Ya existe un empleado con el documento " + empleado.getDocumento());
        }

        empleadoRepo.guardarEmpleado(empleado);

        // 3. --- GUARDADO BONITO EN TXT UNIFICADO ---
        String[] etiquetas = {"ID (AUTO)", "NOMBRE", "DOCUMENTO", "ROL", "CORREO", "SALARIO"};
        String[] valores = {
            String.valueOf(empleado.getId()),
            empleado.getNombre(),
            empleado.getDocumento(),
            empleado.getRol(),
            empleado.getCorreo(),
            String.format("$%,.2f", empleado.getSalario()) // Formato moneda para que se vea bien
        };

        ArchivoTextoUtil.guardarRegistroBonito("EMPLEADO", etiquetas, valores);
        
    }

    public void actualizarEmpleado(int id, String nombre, String doc, String rol, String correo, double salario) throws Exception {
        if (id <= 0) throw new Exception("❌ ID inválido.");
        Empleado e = new Empleado(id, nombre, doc, rol, correo, salario);
        validarDatos(e);
        empleadoRepo.modificarEmpleado(e);
    }


    public List<Empleado> obtenerTodos() {
        return empleadoRepo.listarTodosEmpleados();
    }


    // --- ELIMINAR ---
    public void borrarEmpleado(int id) throws Exception {
        if (id <= 0) throw new Exception("ID inválido.");
        // Aquí podrías validar: si el empleado tiene préstamos asignados, no borrarlo.
        empleadoRepo.eliminarEmpleado(id);
    }



    public Empleado buscarPorId(int id) {
        return empleadoRepo.listarTodosEmpleados().stream()
                .filter(e -> e.getId() == id).findFirst().orElse(null);
    }

    private void validarDatos(Empleado e) throws Exception {
        if (!Validador.esTextoValido(e.getNombre(), 3)) throw new Exception("❌ Nombre inválido.");
        if (!Validador.esNumericoYLongitud(e.getDocumento(), 5, 15)) throw new Exception("❌ Documento inválido.");
        if (!Validador.esCorreoValido(e.getCorreo())) throw new Exception("❌ Correo inválido.");
        if (!Validador.esTextoValido(e.getRol(), 2)) throw new Exception("❌ Rol obligatorio.");
        if (e.getSalario() < 0) throw new Exception("❌ El salario no puede ser negativo.");
    }




   public Empleado iniciarSesion(String correo) throws Exception {
    // 1. Buscamos en la BD usando el método que creamos en el DAO
    Empleado empleadoEncontrado = empleadoRepo.buscarPorCorreo(correo);

    // 2. Si es null, significa que ese correo no existe
    if (empleadoEncontrado == null) {
        throw new Exception("Usuario no encontrado o correo incorrecto.");
    }

    // 3. Si existe, lo devolvemos al Main para que le de permiso
    return empleadoEncontrado;
}
}
