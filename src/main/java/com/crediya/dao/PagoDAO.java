package com.crediya.dao;

import java.util.List;
import com.crediya.model.Pago;


public interface PagoDAO {
    void registrarPago(Pago pago);
    List<Pago> ListarPagosPorPrestamo(int prestamoId); //calcular cuánto se ha pagado
}


