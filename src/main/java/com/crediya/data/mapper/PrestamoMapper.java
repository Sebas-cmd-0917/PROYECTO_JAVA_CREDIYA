package com.crediya.data.mapper;

import com.crediya.data.entities.PrestamoEntity;
import com.crediya.model.Prestamo;

public class PrestamoMapper {
    public Prestamo toDomain(PrestamoEntity entity){
        if (entity == null) return null;
        return new Prestamo(
            entity.getId(),
            entity.getClienteId(),
            entity.getEmpleadoId(),
            entity.getMonto(),
            entity.getInteres(),
            entity.getCuotas(),
            entity.getFechaInicio(),
            entity.getEstado()
        );
          
    }

    public PrestamoEntity toEntity(Prestamo domain){
        if (domain == null) return null;
        return new PrestamoEntity(
            domain.getId(),
            domain.getClienteId(),
            domain.getEmpleadoId(),
            domain.getMonto(),
            domain.getInteres(),
            domain.getCuotas(),
            domain.getFechaInicio(),
            domain.getEstado()
        );

    }
    
}
