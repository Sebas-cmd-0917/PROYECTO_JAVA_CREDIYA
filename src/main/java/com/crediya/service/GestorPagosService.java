package com.crediya.service;

import com.crediya.dao.PagoDAO;
import com.crediya.dao.PrestamoDAO;
import com.crediya.dao.impl.PagoDAOImpl;
import com.crediya.dao.impl.PrestamoDAOImpl;\
import com.crediya.model.Pago;
import com.crediya.model.Prestamo;

public class GestorPagosService {
    private PagoDAO pagodDAO = new PagoDAOImpl();
    private PrestamoDAO prestamoDAO = new PrestamoDAOImpl();

    public void registrarAbono(Pago nuevoPago){

    Prestamo prestamo = prestamoDAO.obtenerPorId(nuevoPago.getPrestamoid());

    if (prestamo == null ) {
        System.out.println("Error: El prestamo no existe. ");
        
    }
    }
}
