package com.crediya.repository;

import java.util.List;
import com.crediya.model.PagoExamen;

public interface PagoRepository {
    void registrarPago(PagoExamen pago);

    List<PagoExamen> ListarPagosPorPrestamo(int prestamoId);

    List<PagoExamen> HistoricoDePagos();

    boolean modificarPago(PagoExamen pago);

    boolean eliminarPago(int idPago);

    List<PagoExamen> listarPorDocumento(String documentoCliente);
}
