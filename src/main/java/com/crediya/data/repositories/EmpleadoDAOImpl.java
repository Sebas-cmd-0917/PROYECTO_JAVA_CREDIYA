package com.crediya.data.repositories;
import java.util.ArrayList;
import java.util.List;

import com.crediya.config.ConexionDB;
import com.crediya.model.Empleado;
import java.sql.Connection;
import java.sql.PreparedStatement;
import com.crediya.repository.EmpleadoRepository;

public class EmpleadoDAOImpl implements EmpleadoRepository{
   
    @Override
    public void guardarEmpleado(Empleado empleado) {
        String sql = "INSERT INTO empleados (nombre, documento, rol, correo, salario) VALUES (?, ?, ?, ?, ?)";

        try(Connection dbConexion = ConexionDB.getConnection()) {

            PreparedStatement stmt = dbConexion.prepareStatement(sql);
            stmt.setString(1, empleado.getNombre());
            stmt.setString(2, empleado.getDocumento());
            stmt.setString(3, empleado.getRol()); 
            stmt.setString(4, empleado.getCorreo());
            stmt.setDouble(5, empleado.getSalario());

            stmt.executeUpdate();
            System.out.println("Empleado guardado exitosamente.");
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al guardar el empleado.");
        }
    }
 
    @Override
    public List<Empleado> listarTodosEmpleados() {
        List<Empleado> listarEmpleados = new ArrayList<>();
        String sql = "SELECT * FROM empleados";
        try (Connection dbConexion = ConexionDB.getConnection()) {
            PreparedStatement stmt = dbConexion.prepareStatement(sql);
            var rs = stmt.executeQuery();
            while (rs.next()) {
                Empleado emp = new Empleado();
                emp.setId(rs.getInt("id"));
                emp.setNombre(rs.getString("nombre"));
                emp.setDocumento(rs.getString("documento"));
                emp.setRol(rs.getString("rol"));
                emp.setCorreo(rs.getString("correo"));
                emp.setSalario(rs.getDouble("salario"));
                listarEmpleados.add(emp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al listar los empleados.");
        }
        return listarEmpleados;
    }
}

