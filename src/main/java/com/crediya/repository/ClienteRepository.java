package com.crediya.repository;

import java.util.List;
import com.crediya.model.Cliente;

public interface ClienteRepository {

    void guardarCliente(Cliente cliente);
    List<Cliente> listarTodosClientes();//lista los empleados
    Cliente buscarPorDocumentoCliente(String documento);
    void actualizarCliente(Cliente cliente);
    void eliminarCliente(int id);
}
