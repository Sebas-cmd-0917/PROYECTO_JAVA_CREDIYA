package com.crediya.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.crediya.config.ConexionDB;
import com.crediya.dao.PagoDAO;
import com.crediya.model.Pago;

public class PagoDAOImpl implements PagoDAO{

    @Override
    public void registrarPago(Pago pago){
        String sqlzo = "INSERT INTO pagos (prestamos_id, fecha_pago, monto) VALUES (?, ?, ?)";

        try (Connection db = ConexionDB.getConnection();
            PreparedStatement stmt = db.prepareStatement(sqlzo)) {

                stmt.setInt(1, pago.getPrestamoId());
                stmt.setDate(2, Date.valueOf(pago.getFechaPago()));
                stmt.setDouble(3, pago.getMonto());

                stmt.executeUpdate();
                System.out.println("Pago registrado exitosamente. ");
            
        } catch (SQLException e) {
            e.printStackTrace();
            
        }
    }

    @Override
    public List<Pago> ListarPagosPorPrestamo(int prestamoId){
        List<Pago> lista = new ArrayList<>();
        String sqlzo = "SELECT prestamo_id, fecha_pago, monto ";

            try (Connection db = ConexionDB.getConnection();
                PreparedStatement stmt = db.prepareStatement(sqlzo)) {
            
            stmt.setInt(1, prestamoId);
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                Pago p = new Pago(
                    result.getInt("prestamo_id"),
                    result.getDate("fecha_pago").toLocalDate(),
                    result.getDouble("monto")
                );
                p.setId(result.getInt("id"));
                lista.add(p);
            }
                    

            } catch (SQLException e) {
                e.printStackTrace();
            
        }
        return lista;
    }
    
}
