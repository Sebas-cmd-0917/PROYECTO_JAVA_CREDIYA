package com.crediya.repository;

import java.util.List;
import com.crediya.model.Pago;

public interface PagoRepository {
    void registrarPago(Pago pago);
    List<Pago> ListarPagosPorPrestamo(int prestamoId);
    List<Pago> HistoricoDePagos();
    void modificarPago(Pago pago);
    void eliminarPago(int idPago);
}

