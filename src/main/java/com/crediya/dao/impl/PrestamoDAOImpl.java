package com.crediya.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.crediya.config.ConexionDB;
import com.crediya.dao.PrestamoDAO;
import com.crediya.model.Prestamo;

public class PrestamoDAOImpl implements PrestamoDAO {

    @Override
    public void registrarPrestamo(Prestamo p) {
        String sqlzo = "INSERT INTO prestamos (cliente_id, empleado_id, monto, interes, cuotas, fecha_inicio, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection db = ConexionDB.getConnection(); 
             PreparedStatement stmt = db.prepareStatement(sqlzo)) {

                stmt.setInt(1, p.getClienteId());
                stmt.setInt(2, p.getEmpleadoId());
                stmt.setDouble(3, p.getMonto());
                stmt.setDouble(4, p.getInteres());
                stmt.setInt(5, p.getCuotas());
                // Convertimos LocalDate de Java a Date de SQL
                stmt.setDate(6, Date.valueOf(p.getFechaInicio())); 
                stmt.setString(7, p.getEstado());

                stmt.executeUpdate();
                System.out.println("Préstamo registrado exitosamente.");


        } catch (SQLException e) {
            System.out.println("Error al registrar préstamo: " + e.getMessage());
            e.printStackTrace();
        }

    }

    public List<Prestamo> listarPorCliente(int clienteId) {
        List<Prestamo> lista = new ArrayList<>();
        String sqlzo = "SELECT cliente_id, empleado_id, monto, interes, cuotas, fecha_inicio, estado FROM prestamos WHERE cliente_id = ?";

        try (Connection db = ConexionDB.getConnection();
        PreparedStatement stmt = db.prepareStatement(sqlzo)) {

            stmt.setInt(1, clienteId);
            ResultSet result = stmt.executeQuery(); 

            while (result.next()) {
                Prestamo p = new Prestamo(
                    result.getInt("id"),
                    result.getInt("cliente_id"),
                    result.getInt("empleado_id"),
                    result.getDouble("monto"),
                    result.getDouble("interes"),
                    result.getInt("cuotas"),
                    result.getDate("fecha_inicio").toLocalDate(), // De SQL a Java
                    result.getString("estado")
                );
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }   

    @Override
    public Prestamo obtenerPorId(int id) {
        String sqlzo = "SELECT * FROM prestamos WHERE id = ?";
        try (Connection db = ConexionDB.getConnection();
            PreparedStatement stmt = db.prepareStatement(sqlzo)){

        stmt.setInt(1, id);
        ResultSet result = stmt.executeQuery();

        if (result.next()) {
            return new Prestamo(
                result.getInt("id"),
                result.getInt("cliente_id"),
                result.getInt("empleado_id"),
                result.getDouble("monto"),
                result.getDouble("interes"),
                result.getInt("cuotas"),
                result.getDate("fecha_inicio").toLocalDate(),
                result.getString("estado")
            );    
        }
    }catch (SQLException e){
        e.printStackTrace();
    }
    return null;
    }
}
