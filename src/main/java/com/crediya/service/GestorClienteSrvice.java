package com.crediya.service;

import java.util.List;

import com.crediya.data.repositories.ClienteDAOImpl;
import com.crediya.model.Cliente;

public class GestorClienteSrvice {

    private ClienteDAOImpl clienteRepo = new ClienteDAOImpl();

    public void registrarCliente(Cliente cliente) {
        // Aquí podrías poner validaciones antes de guardar
        // Ej: if (empleado.getSalario() < 0) { Error }
        
        clienteRepo.guardarCliente(cliente);
    }

    public List<Cliente> obtenerTodos() {
        return clienteRepo.listarTodosClientes();
    }
}
