package com.crediya.service;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.List;

import com.crediya.data.repositories.EmpleadoDAOImpl;
import com.crediya.model.Empleado;
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
        guardarEnArchivoTxt(empleado);
    }

    public void actualizarEmpleado(int id, String nombre, String doc, String rol, String correo, double salario) throws Exception {
        if (id <= 0) throw new Exception("❌ ID inválido.");
        Empleado e = new Empleado(id, nombre, doc, rol, correo, salario);
        validarDatos(e);
        empleadoRepo.modificarEmpleado(e);
    }

    public void borrarEmpleado(int id) throws Exception {
        if (id <= 0) throw new Exception("❌ ID inválido.");
        empleadoRepo.eliminarEmpleado(id);
    }

    public List<Empleado> obtenerTodos() {
        return empleadoRepo.listarTodosEmpleados();
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

    private void guardarEnArchivoTxt(Empleado e) {
        try (FileWriter fw = new FileWriter("empleados.txt", true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(e.getId() + ";" + e.getNombre() + ";" + e.getDocumento() + ";" + e.getRol() + ";" + e.getCorreo() + ";" + e.getSalario());
        } catch (IOException ex) {
            System.out.println("⚠ Error guardando TXT.");
        }
    }
}