package com.crediya.repository;

import java.util.List;

import com.crediya.model.Empleado;

public interface EmpleadoRepository {

     void guardarEmpleado(Empleado empleado);
    List<Empleado> listarTodosEmpleados();//lista los empleados
    Empleado buscarPorDocumentoEmpleado(String documento);
    void modificarEmpleado(Empleado empleado);
    void eliminarEmpleado(int idEmpleado);
}
