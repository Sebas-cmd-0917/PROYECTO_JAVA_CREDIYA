package com.crediya.service;

import java.util.List;

import com.crediya.data.repositories.ClienteDAOImpl;
import com.crediya.model.Cliente;

public class GestorClienteSrvice {

    private ClienteDAOImpl clienteRepo = new ClienteDAOImpl();

    public void registrarCliente(Cliente cliente) throws Exception {
        // 1. VALIDACIÓN BÁSICA: Objeto Nulo
        if (cliente == null) {
            throw new Exception("Error: No se han recibido datos del cliente.");
        }

        // 2. VALIDACIÓN DE DUPLICADOS (¡LA MÁS IMPORTANTE!)
        // Antes de guardar, consultamos a la BD si ese documento ya existe.
        Cliente existente = clienteRepo.buscarPorDocumentoCliente(cliente.getDocumento());

        if (existente != null) {
            throw new Exception("Error: Ya existe un cliente registrado con el documento " + cliente.getDocumento());
            
        }

        // 3. VALIDACIÓN DE NOMBRE
        if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
            throw new Exception("El nombre del cliente es obligatorio.");
        }

        if (cliente.getNombre().length() < 3) {
            throw new Exception("El nombre es muy corto (mínimo 3 letras).");
        }

        // 4. VALIDACIÓN DE DOCUMENTO (Formato)
        // Regex: \\d+ significa "solo números", {5,15} longitudes permitidas
        if (cliente.getDocumento() == null || !cliente.getDocumento().matches("\\d{5,15}")) {
            throw new Exception("El documento debe ser numérico y tener entre 5 y 15 dígitos.");
        }

        // 5. VALIDACIÓN DE CORREO
        if (cliente.getCorreo() == null || !cliente.getCorreo().contains("@") || !cliente.getCorreo().contains(".")) {
            throw new Exception("El correo electrónico no tiene un formato válido (ejemplo@mail.com).");
        }

        // 6. VALIDACIÓN DE TELÉFONO
        if (cliente.getTelefono() == null || !cliente.getTelefono().matches("\\d{7,12}")) {
            throw new Exception("El teléfono debe contener solo números (entre 7 y 12 dígitos).");
        }

        // --- SI PASA TODAS LAS BARRERAS ANTERIORES, GUARDAMOS ---
        clienteRepo.guardarCliente(cliente);
    }

    public List<Cliente> obtenerTodos() {
        return clienteRepo.listarTodosClientes();
    }

    // --- MÉTODO PARA MODIFICAR ---
    public void actualizarCliente(int id, String nuevoNombre, String nuevoDoc, String nuevoCorreo, String nuevoTel)
            throws Exception {

        // 1. Validar que el cliente exista
        // (Necesitas un método buscarPorId en tu DAO, si no lo tienes, usa
        // buscarPorDocumento o itera la lista)
        // Por ahora, asumiremos que validamos existencia en el menú o agregas
        // buscarPorId en el DAO.

        // 2. Validaciones de Reglas de Negocio
        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
            throw new Exception("El nombre no puede estar vacío.");
        }

        if (!nuevoDoc.matches("\\d{5,}")) {
            throw new Exception("El documento debe ser numérico y tener longitud válida.");
        }

        if (nuevoCorreo != null && !nuevoCorreo.contains("@")) {
            throw new Exception("El correo electrónico no es válido.");
        }

        if (!nuevoTel.matches("\\d{7,10}")) {
            throw new Exception("El teléfono debe contener entre 7 y 10 números.");
        }

        // 3. Preparar el objeto
        Cliente clienteAActualizar = new Cliente();
        clienteAActualizar.setId(id);
        clienteAActualizar.setNombre(nuevoNombre);
        clienteAActualizar.setDocumento(nuevoDoc);
        clienteAActualizar.setCorreo(nuevoCorreo);
        clienteAActualizar.setTelefono(nuevoTel);

        // 4. Llamar al DAO
        clienteRepo.actualizarCliente(clienteAActualizar);
    }

    // --- MÉTODO PARA ELIMINAR ---
    public void borrarCliente(int idCliente) throws Exception {
        if (idCliente <= 0) {
            throw new Exception("El ID del cliente no es válido.");
        }

        // Aquí podrías validar si el cliente tiene préstamos activos antes de borrarlo
        // if (tieneDeudas(idCliente)) throw new Exception("No se puede borrar, tiene
        // deudas.");

        clienteRepo.eliminarCliente(idCliente);
    }

    // Método auxiliar para buscar cliente (puedes agregarlo a tu DAO para hacerlo
    // mejor)
    public Cliente buscarClientePorId(int id) {
        List<Cliente> todos = clienteRepo.listarTodosClientes();
        for (Cliente c : todos) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }
}
