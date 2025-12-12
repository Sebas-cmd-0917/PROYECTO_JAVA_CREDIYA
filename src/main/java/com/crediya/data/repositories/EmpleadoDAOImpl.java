package com.crediya.data.repositories;

import com.crediya.config.ConexionDB; // O ConexionDB según como lo llamó tu compañero
import com.crediya.data.entities.ClienteEntity;
import com.crediya.data.entities.EmpleadoEntity; // Importamos la Entidad
import com.crediya.data.mapper.EmpeladoMapper;   // Importamos el Mapper
import com.crediya.model.Cliente;
import com.crediya.model.Empleado;
import com.crediya.repository.EmpleadoRepository; // O EmpleadoRepository

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAOImpl implements EmpleadoRepository {

    // Instanciamos el traductor
    private EmpeladoMapper mapper = new EmpeladoMapper();

    @Override
    public void guardarEmpleado(Empleado empleadoModel) {
        String sql = "INSERT INTO empleados (nombre, documento, rol, correo, salario) VALUES (?, ?, ?, ?, ?)";

        // 1. CONVERSIÓN: Usamos el Mapper para volverlo Entidad
        EmpleadoEntity entity = mapper.toEntity(empleadoModel);

        try (Connection conexion = ConexionDB.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            // 2. Usamos los datos de la ENTITY (no del modelo directo)
            ps.setString(1, entity.getNombre());
            ps.setString(2, entity.getDocumento());
            ps.setString(3, entity.getRol());
            ps.setString(4, entity.getCorreo());
            ps.setDouble(5, entity.getSalario());

            ps.executeUpdate();
            System.out.println("✅ Empleado guardado (Arquitectura Capas).");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Empleado> listarTodosEmpleados() {
        List<Empleado> listaEmpleados= new ArrayList<>();
        String sql = "SELECT * FROM empleados";

        try (Connection conexion = ConexionDB.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // A. Leemos datos de SQL y creamos la ENTITY
                EmpleadoEntity entity = new EmpleadoEntity();
                entity.setId(rs.getInt("id"));
                entity.setNombre(rs.getString("nombre"));
                entity.setDocumento(rs.getString("documento"));
                entity.setRol(rs.getString("rol"));
                entity.setCorreo(rs.getString("correo"));
                entity.setSalario(rs.getDouble("salario"));

                // B. CONVERSIÓN: Entity -> Model (Usando el Mapper)
                Empleado modelo = mapper.toDomain(entity);
                // Asegúrate de pasar el ID si toDomain no lo hizo
                modelo.setId(entity.getId()); 

                // C. Agregamos a la lista de negocio
                listaEmpleados.add(modelo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaEmpleados;
    }

    public Empleado buscarPorDocumentoEmpleado(String documento){
        String sqlzo = "SELECT id, nombre, documento, rol, correo, salario FROM clientes documento = ?";
        Empleado empleadoEncontrado = null;

        try (Connection db = ConexionDB.getConnection();
            PreparedStatement stmt = db.prepareStatement(sqlzo)){
                
                stmt.setString(1, documento);
                ResultSet result = stmt.executeQuery();

                if (result.next()){
                    EmpleadoEntity entity = new EmpleadoEntity();
                    entity.setId(result.getInt("id"));
                    entity.setNombre(result.getString("nombre"));
                    entity.setDocumento(result.getString("documento"));
                    entity.setRol(result.getString("rol"));
                    entity.setCorreo(result.getString("correo"));
                    entity.setSalario(result.getDouble("salario"));
                    
                    empleadoEncontrado = mapper.toDomain(entity);
                    
                }else{
                    System.out.println("Empleado no encontrado ");
                }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    };

}