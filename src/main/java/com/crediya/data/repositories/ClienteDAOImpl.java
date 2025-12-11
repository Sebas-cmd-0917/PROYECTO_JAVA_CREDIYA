package com.crediya.data.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import com.crediya.config.ConexionDB;
import com.crediya.model.Cliente;
import com.crediya.repository.ClienteRepository;

public class ClienteDAOImpl implements ClienteRepository {
    @Override
    public void guardarCliente(Cliente cliente) {
        String sql = "INSERT INTO clientes (nombre, documento, correo, telefono) VALUES (?, ?, ?, ?)";

        try(Connection dbConexion = ConexionDB.getConnection()) {

            PreparedStatement stmt = dbConexion.prepareStatement(sql);
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getDocumento());; 
            stmt.setString(3, cliente.getCorreo());
            stmt.setString(4, cliente.getTelefono());
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
            var rs = stmt.executeQuery();
            while (rs.next()) {
                Cliente clte = new Cliente();
                clte.setId(rs.getInt("id"));
                clte.setNombre(rs.getString("nombre"));
                clte.setDocumento(rs.getString("documento"));
                clte.setCorreo(rs.getString("correo"));
                clte.setTelefono(rs.getString("telefono"));
                listarClientes.add(clte);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al listar los clientes.");
        }
        return listarClientes;
    }
}
