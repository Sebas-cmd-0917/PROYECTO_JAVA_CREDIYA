package com.crediya.data.mapper;

import com.crediya.data.entities.PagoEntity;
import com.crediya.model.Pago;

public class PagoMapper {
    // De Entidad DB a Modelo Negocio
    public Pago toDomain(PagoEntity entity){
        if (entity == null) return null;
        return new Pago(
            entity.getId(),
            entity.getPrestamoId(),
            entity.getFechaPago(),
            entity.getMonto()
        );

    }

    public PagoEntity toEntity(Pago domain){
        if (domain == null) return null;
        return new PagoEntity(
            domain.getId(),
            domain.getPrestamoId(),
            domain.getFechaPago(),
            domain.getMonto()
        );
    }
}
