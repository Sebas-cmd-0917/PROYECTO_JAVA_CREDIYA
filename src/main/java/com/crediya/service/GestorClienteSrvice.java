package com.crediya.service;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.List;

import com.crediya.data.repositories.ClienteDAOImpl;
import com.crediya.model.Cliente;
import com.crediya.util.Validador;

public class GestorClienteSrvice { 

    private ClienteDAOImpl clienteRepo = new ClienteDAOImpl();

    public void registrarCliente(Cliente cliente) throws Exception {
        // 1. Validaciones de formato (Negocio)
        validarDatosComunes(cliente);

        // 2. Validación de Duplicados (Regla de unicidad)
        Cliente existente = clienteRepo.buscarPorDocumentoCliente(cliente.getDocumento());
        if (existente != null) {
            throw new Exception("❌ Error: Ya existe un cliente con el documento " + cliente.getDocumento());
        }

        // 3. Persistencia
        clienteRepo.guardarCliente(cliente);
        guardarEnArchivoTxt(cliente);
    }

    public void actualizarCliente(int id, String nombre, String doc, String correo, String tel) throws Exception {
        if (id <= 0) throw new Exception("❌ ID inválido.");
        
        Cliente cliente = new Cliente(id, nombre, doc, correo, tel);
        validarDatosComunes(cliente);

        clienteRepo.actualizarCliente(cliente);
    }

    public void borrarCliente(int id) throws Exception {
        if (id <= 0) throw new Exception("❌ ID inválido.");
        clienteRepo.eliminarCliente(id);
    }

    public List<Cliente> obtenerTodos() {
        return clienteRepo.listarTodosClientes();
    }

    public Cliente buscarClientePorId(int id) {
        return clienteRepo.listarTodosClientes().stream()
                .filter(c -> c.getId() == id).findFirst().orElse(null);
    }

    // --- Lógica Privada ---
    private void validarDatosComunes(Cliente c) throws Exception {
        if (!Validador.esTextoValido(c.getNombre(), 3)) 
            throw new Exception("❌ El nombre es obligatorio (min 3 letras).");
        if (!Validador.esNumericoYLongitud(c.getDocumento(), 5, 15)) 
            throw new Exception("❌ Documento inválido (5-15 dígitos numéricos).");
        if (!Validador.esCorreoValido(c.getCorreo())) 
            throw new Exception("❌ Correo inválido (ej: usuario@mail.com).");
        if (!Validador.esNumericoYLongitud(c.getTelefono(), 7, 12)) 
            throw new Exception("❌ Teléfono inválido (7-12 dígitos numéricos).");
    }

    private void guardarEnArchivoTxt(Cliente c) {
        try (FileWriter fw = new FileWriter("clientes.txt", true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(c.getId() + ";" + c.getNombre() + ";" + c.getDocumento() + ";" + c.getCorreo() + ";" + c.getTelefono());
        } catch (IOException e) {
            System.out.println("⚠ No se pudo guardar en TXT.");
        }
    }
}