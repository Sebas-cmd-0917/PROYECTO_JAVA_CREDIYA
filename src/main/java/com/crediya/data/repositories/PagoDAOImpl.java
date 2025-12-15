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
        String sqlzo = "INSERT INTO pagos (prestamo_id, fecha_pago, monto) VALUES (?, ?, ?)";
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
    public List<Pago> HistoricoDePagos(){
        List<Pago> lista = new ArrayList<>();
        String sqlzo = "SELECT p.id, p.prestamo_id, p.fecha_pago, p.monto, " +
                   "c.nombre AS nombre_cliente " +
                   "FROM pagos p " +
                   "INNER JOIN prestamos pr ON p.prestamo_id = pr.id " +
                   "INNER JOIN clientes c ON pr.cliente_id = c.id";;

        try (Connection db = ConexionDB.getConnection();
             PreparedStatement stmt = db.prepareStatement(sqlzo)) {

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
                pagoModel.setNombreCliente(result.getString("nombre_cliente"));
                
                lista.add(pagoModel);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
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

    @Override
    public void modificarPago(Pago pago) {
        String sqlzo = "UPDATE pagos SET prestamo_id = ?, fecha_pago = ?, monto = ? WHERE id = ?";
        PagoEntity pagoEntity = mapper.toEntity(pago);  
        try (Connection db = ConexionDB.getConnection();
             PreparedStatement stmt = db.prepareStatement(sqlzo)) {

            stmt.setInt(1, pagoEntity.getPrestamoId());
            stmt.setDate(2, Date.valueOf(pagoEntity.getFechaPago()));
            stmt.setDouble(3, pagoEntity.getMonto());
            stmt.setInt(4, pagoEntity.getId());

            int filasActualizadas = stmt.executeUpdate();
            if (filasActualizadas > 0) {
                System.out.println("Pago modificado exitosamente.");
            } else {
                System.out.println("No se encontró el pago con ID: " + pagoEntity.getId());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eliminarPago(int idPago) {
        String sqlzo = "DELETE FROM pagos WHERE id = ?";
        try (Connection db = ConexionDB.getConnection();
             PreparedStatement stmt = db.prepareStatement(sqlzo)) {

            stmt.setInt(1, idPago);

            int filasEliminadas = stmt.executeUpdate();
            if (filasEliminadas > 0) {
                System.out.println("Pago eliminado exitosamente.");
            } else {
                System.out.println("No se encontró el pago con ID: " + idPago);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al eliminar el pago", e);
        }
    }
}
