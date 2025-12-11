package com.crediya.data.repositories;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.crediya.config.ConexionDB;
import com.crediya.data.entities.PagoEntity;
import com.crediya.data.mapper.PagoMapper;
import com.crediya.model.Pago;
import com.crediya.repository.PagoRepository;

public class PagoDAOImpl implements PagoRepository{
    private final PagoMapper mapper = new PagoMapper();

    @Override
    public void registrarPago(Pago pago){
        String sqlzo = "INSERT INTO pagos (prestamos_id, fecha_pago, monto) VALUES (?, ?, ?)";
        PagoEntity pagoEntity = mapper.toEntity(pago);

        try (Connection db = ConexionDB.getConnection();
            PreparedStatement stmt = db.prepareStatement(sqlzo)) {

                stmt.setInt(1, pagoEntity.getPrestamoId());
                stmt.setDate(2, Date.valueOf(pagoEntity.getFechaPago()));
                stmt.setDouble(3, pagoEntity.getMonto());

                stmt.executeUpdate();
                System.out.println("Pago registrado exitosamente. ");
            
        } catch (SQLException e) {
            e.printStackTrace();
            
        }
    }

    @Override
    public List<Pago> ListarPagosPorPrestamo(int prestamoId) {
        List<Pago> lista = new ArrayList<>();
        String sqlzo = "SELECT id, prestamo_id, fecha_pago, monto FROM pagos WHERE prestamo_id = ?";

        try (Connection db = ConexionDB.getConnection();
             PreparedStatement stmt = db.prepareStatement(sqlzo)) {

            stmt.setInt(1, prestamoId);
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                // CORRECCIÓN 2: Usamos la ENTIDAD para recibir los datos de la BD
                PagoEntity entity = new PagoEntity();
                entity.setId(result.getInt("id"));
                entity.setPrestamoId(result.getInt("prestamo_id"));
                entity.setFechaPago(result.getDate("fecha_pago").toLocalDate());
                entity.setMonto(result.getDouble("monto"));

                // CORRECCIÓN 3: Usamos el MAPPER para convertir a Modelo de Negocio
                Pago pagoModel = mapper.toDomain(entity);
                
                lista.add(pagoModel);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
