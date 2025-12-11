package com.crediya.data.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.crediya.config.ConexionDB;
import com.crediya.data.mapper.ClienteMapper;
import com.crediya.model.Cliente;
import com.crediya.repository.ClienteRepository;
import com.crediya.data.entities.ClienteEntity;

public class ClienteDAOImpl implements ClienteRepository {

    private ClienteMapper mapper = new ClienteMapper();

    @Override
    public void guardarCliente(Cliente clienteModel) {
        String sql = "INSERT INTO clientes (nombre, documento, correo, telefono) VALUES (?, ?, ?, ?)";

        ClienteEntity entity = mapper.toEntity(clienteModel);

        try(Connection dbConexion = ConexionDB.getConnection()) {

            PreparedStatement stmt = dbConexion.prepareStatement(sql);
            stmt.setString(1, entity.getNombre());
            stmt.setString(2, entity.getDocumento());; 
            stmt.setString(3, entity.getCorreo());
            stmt.setString(4, entity.getTelefono());
            stmt.executeUpdate();
            System.out.println("Cliente guardado exitosamente.");
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al guardar el cliente.");
        }
    }

    @Override
    public List<Cliente> listarTodosClientes() {
        List<Cliente> listarClientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes";
        try (Connection dbConexion = ConexionDB.getConnection()) {
            PreparedStatement stmt = dbConexion.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ClienteEntity entity = new ClienteEntity();
                entity.setId(rs.getInt("id"));
                entity.setNombre(rs.getString("nombre"));
                entity.setDocumento(rs.getString("documento"));
                entity.setCorreo(rs.getString("correo"));
                entity.setTelefono(rs.getString("telefono"));
                
                Cliente modelCliente = mapper.toDomain(entity);
                listarClientes.add(modelCliente);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al listar los clientes.");
        }
        return listarClientes;
    }
}
