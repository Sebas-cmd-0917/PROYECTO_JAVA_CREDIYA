package com.crediya.data.mapper;

import com.crediya.data.entities.PagoEntity;
import com.crediya.model.PagoExamen;

public class PagoMapper {
    // De Entidad DB a Modelo Negocio
    public PagoExamen toDomain(PagoEntity entity){
        if (entity == null) return null;
        return new PagoExamen(
            entity.getId(),
            entity.getPrestamoId(),
            entity.getFechaPago(),
            entity.getMonto()
        );

    }

    public PagoEntity toEntity(PagoExamen domain){
        if (domain == null) return null;
        return new PagoEntity(
            domain.getId(),
            domain.getPrestamoId(),
            domain.getFechaPago(),
            domain.getMonto()
        );
    }
}
