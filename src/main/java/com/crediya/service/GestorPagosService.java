package com.crediya.service;

import java.util.List;

import com.crediya.dao.PagoDAO;
import com.crediya.dao.PrestamoDAO;
import com.crediya.dao.impl.PagoDAOImpl;
import com.crediya.dao.impl.PrestamoDAOImpl;
import com.crediya.model.Pago;
import com.crediya.model.Prestamo;

public class GestorPagosService {
    private PagoDAO pagoDAO = new PagoDAOImpl();
    private PrestamoDAO prestamoDAO = new PrestamoDAOImpl();

    public void registrarAbono(Pago nuevoPago){
    //Obtener el préstamo
    Prestamo prestamo = prestamoDAO.obtenerPorId(nuevoPago.getPrestamoId());

    if (prestamo == null ) {
        System.out.println("Error: El prestamo no existe. ");
        return;
    }
    //Calcular deuda total (Capital + Interes)
    double totalDeuda = prestamo.getMonto() + (prestamo.getMonto() * (prestamo.getInteres() / 100));
    
    //Calcular cuánto ha pagado hasta ahora
    List<Pago> pagosPrevios = pagoDAO.ListarPagosPorPrestamo(prestamo.getClienteId());
    double totalPagado = 0;

    for (Pago p : pagosPrevios) {
        totalPagado += p.getMonto();
    }

    double saldoPendiente = totalDeuda - totalPagado;

    if (nuevoPago.getMonto() > saldoPendiente){
        System.out.println("Error: El abono excede el saldo pendiente. Saldo actual: " + saldoPendiente);
    }else {
        pagoDAO.registrarPago(nuevoPago);
        System.out.println("Abono registrado. Nuevo saldo: " + (saldoPendiente - nuevoPago.getMonto()));
    }
    }
}
