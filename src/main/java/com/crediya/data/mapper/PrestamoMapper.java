package com.crediya.data.mapper;

import com.crediya.data.entities.PrestamoEntity;
import com.crediya.model.EstadoPrestamo;
import com.crediya.model.Prestamo;

public class PrestamoMapper {
    public Prestamo toDomain(PrestamoEntity entity){
        if (entity == null) return null;
        EstadoPrestamo estadoEnum = EstadoPrestamo.valueOf(entity.getEstado());
        return new Prestamo(
            entity.getId(),
            entity.getClienteId(),
            entity.getEmpleadoId(),
            entity.getMonto(),
            entity.getInteres(),
            entity.getCuotas(),
            entity.getFechaInicio(),
            estadoEnum
        );
          
    }

    public PrestamoEntity toEntity(Prestamo domain){
        if (domain == null) return null;

        String estadoString = domain.getEstado().toString(); 
        return new PrestamoEntity(
            domain.getId(),
            domain.getClienteId(),
            domain.getEmpleadoId(),
            domain.getMonto(),
            domain.getInteres(),
            domain.getCuotas(),
            domain.getFechaInicio(),
            estadoString
        );

    }
    
}
