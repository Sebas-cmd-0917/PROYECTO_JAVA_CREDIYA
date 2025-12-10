package com.crediya.dao;

import java.util.List;
import com.crediya.model.Empleado;

public interface EmpleadoDAO {

    void guardarEmpleado(Empleado empleado);
    List<Empleado> listarTodosEmpleados();//lista los empleados
}
    
