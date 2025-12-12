package com.crediya.data.repositories;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.crediya.config.ConexionDB;
import com.crediya.data.entities.PrestamoEntity;
import com.crediya.data.mapper.PrestamoMapper;
import com.crediya.model.Prestamo;
import com.crediya.repository.PrestamoRepository;

public class PrestamoDAOImpl implements PrestamoRepository {

    private final PrestamoMapper mapper = new PrestamoMapper();

    @Override
    public void registrarPrestamo(Prestamo p) {
        
        String sqlzo = "INSERT INTO prestamos (cliente_id, empleado_id, monto, interes, cuotas, fecha_inicio, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";
        // Convertir el Modelo (Negocio) a Entidad (Base de Datos)
        // Esto asegura que solo guardamos lo que la entidad permite
        PrestamoEntity prestamoEntity = mapper.toEntity(p);

        try (Connection db = ConexionDB.getConnection(); 
             PreparedStatement stmt = db.prepareStatement(sqlzo)) {

                // PASO 2: Usamos los datos DE LA ENTIDAD para el SQL
                stmt.setInt(1, prestamoEntity.getClienteId());
                stmt.setInt(2, prestamoEntity.getEmpleadoId());
                stmt.setDouble(3, prestamoEntity.getMonto());
                stmt.setDouble(4, prestamoEntity.getInteres());
                stmt.setInt(5, prestamoEntity.getCuotas());
                stmt.setDate(6, Date.valueOf(prestamoEntity.getFechaInicio())); 
                stmt.setString(7, prestamoEntity.getEstado());

                stmt.executeUpdate();
                System.out.println("Préstamo registrado exitosamente.");


        } catch (SQLException e) {
            System.out.println(" ❌ Error al registrar préstamo: " + e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    public List<Prestamo> listarPrestamos() {
        List<Prestamo> listaModelos = new ArrayList<>();
        String sqlzo = "SELECT p.id, p.cliente_id, p.empleado_id, p.monto, p.interes, p.cuotas, p.fecha_inicio, p.estado, " +
               "c.nombre AS nombre_cliente, c.documento AS doc_cliente, " +
               "e.nombre AS nombre_empleado " +
               "FROM prestamos p " +
               "INNER JOIN clientes c ON p.cliente_id = c.id " +
               "INNER JOIN empleados e ON p.empleado_id = e.id";

        try (Connection db = ConexionDB.getConnection();
        PreparedStatement stmt = db.prepareStatement(sqlzo)) {

            ResultSet result = stmt.executeQuery(); 

            while (result.next()) {
                PrestamoEntity entity = new PrestamoEntity();
                    entity.setId(result.getInt("id"));
                    entity.setClienteId(result.getInt("cliente_id"));
                    entity.setEmpleadoId(result.getInt("empleado_id"));
                    entity.setMonto(result.getDouble("monto"));
                    entity.setInteres(result.getDouble("interes"));
                    entity.setCuotas(result.getInt("cuotas"));
                    entity.setFechaInicio(result.getDate("fecha_inicio").toLocalDate());
                    entity.setEstado(result.getString("estado"));
                // Convertir la ENTIDAD a MODELO DE NEGOCIO
                // El resto del programa solo entenderá 'Prestamo', no 'PrestamoEntity'
                Prestamo model = mapper.toDomain(entity);
                

                model.setNombreCliente(result.getString("nombre_cliente"));
                model.setNumDocumento(result.getString("doc_cliente"));
                model.setNombreEmpleado(result.getString("nombre_empleado"));

                listaModelos.add(model);
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaModelos;
    }

    @Override
    public List<Prestamo> obtenerPrestamoPorDocumento(String documento) {
        List<Prestamo> listaPreDoc = new ArrayList<>();
        String sqlzo = "SELECT p.id, p.cliente_id, p.empleado_id, p.monto, p.interes, p.cuotas, p.fecha_inicio, p.estado " +
                       "FROM prestamos p " +
                       "JOIN clientes c ON p.cliente_id = c.id " +
                       "WHERE c.documento = ?";

        try (Connection db = ConexionDB.getConnection();
             PreparedStatement stmt = db.prepareStatement(sqlzo)) {

            stmt.setString(1, documento);
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                PrestamoEntity entity = new PrestamoEntity();
                entity.setId(result.getInt("id"));
                entity.setClienteId(result.getInt("cliente_id"));
                entity.setEmpleadoId(result.getInt("empleado_id"));
                entity.setMonto(result.getDouble("monto"));
                entity.setInteres(result.getDouble("interes"));
                entity.setCuotas(result.getInt("cuotas"));
                entity.setFechaInicio(result.getDate("fecha_inicio").toLocalDate());
                entity.setEstado(result.getString("estado"));

                // Convertir la ENTIDAD a MODELO DE NEGOCIO
                Prestamo model = mapper.toDomain(entity);
                listaPreDoc.add(model);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaPreDoc;
    }

    @Override
    public List<Prestamo> listarPorCliente(int clienteId) {
        List<Prestamo> listaModelos = new ArrayList<>();
        String sqlzo = "SELECT cliente_id, empleado_id, monto, interes, cuotas, fecha_inicio estado FROM prestamos WHERE cliente_id = ?";

        try (Connection db = ConexionDB.getConnection();
        PreparedStatement stmt = db.prepareStatement(sqlzo)) {

            stmt.setInt(1, clienteId);
            ResultSet result = stmt.executeQuery(); 

            while (result.next()) {
                PrestamoEntity entity = new PrestamoEntity();
                    entity.setId(result.getInt("id"));
                    entity.setClienteId(result.getInt("cliente_id"));
                    entity.setEmpleadoId(result.getInt("empleado_id"));
                    entity.setMonto(result.getDouble("monto"));
                    entity.setInteres(result.getDouble("interes"));
                    entity.setCuotas(result.getInt("cuotas"));
                    entity.setFechaInicio(result.getDate("fecha_inicio").toLocalDate());
                    entity.setEstado(result.getString("estado"));
                // Convertir la ENTIDAD a MODELO DE NEGOCIO
                // El resto del programa solo entenderá 'Prestamo', no 'PrestamoEntity'
                Prestamo model = mapper.toDomain(entity);
                listaModelos.add(model);
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaModelos;
    }   

    @Override
    public Prestamo obtenerPorId(int id) {
        String sqlzo = "SELECT id, cliente_id, empleado_id, monto, interes, cuotas, fecha_inicio, estado FROM prestamos WHERE id = ?";
        try (Connection db = ConexionDB.getConnection();
            PreparedStatement stmt = db.prepareStatement(sqlzo)){

        stmt.setInt(1, id);
        ResultSet result = stmt.executeQuery();

        if (result.next()) {
            PrestamoEntity entity = new PrestamoEntity();
                entity.setId(result.getInt("id"));
                entity.setClienteId(result.getInt("cliente_id"));
                entity.setEmpleadoId(result.getInt("empleado_id"));
                entity.setMonto(result.getDouble("monto"));
                entity.setInteres(result.getDouble("interes"));
                entity.setCuotas(result.getInt("cuotas"));
                entity.setFechaInicio(result.getDate("fecha_inicio").toLocalDate());
                entity.setEstado(result.getString("estado"));
                return mapper.toDomain(entity);  
        }
    }catch (SQLException e){
        e.printStackTrace();
    }
    return null;
    }

    @Override
    public void actualizarEstado(int prestamoId, String nuevoEstado){
        String sqlzo = "UPDATE prestamos SET estado = ? WHERE id = ?";

        try (Connection db = ConexionDB.getConnection();
            PreparedStatement stmt = db.prepareStatement(sqlzo)){

                stmt.setString(1, nuevoEstado);
                stmt.setInt(1, prestamoId);

                int filas = stmt.executeUpdate();
                if (filas > 0) {
                    System.out.println("✅ Estado del préstamo actualizado a: \" + nuevoEstado");
                }else {
                    System.out.println("⚠ No se encontró el préstamo con ID: " + prestamoId);
                }

        }catch(SQLException e){
            System.out.println("❌ Error al actualizar estado: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
