package com.crediya.data.mapper;

import com.crediya.data.entities.EmpleadoEntity;
import com.crediya.model.Empleado;

public class EmpeladoMapper {
    // Convierte de la Base de Datos (Entity) -> Al Negocio (Model)
    public Empleado toDomain(EmpleadoEntity entity){
        if (entity == null) return null;
        return new Empleado(
            entity.getNombre(), 
            entity.getDocumento(),
            entity.getRol(),
            entity.getCorreo(),
            entity.getSalario()
        );
        // Nota: Si tu modelo Empleado tiene ID, asígnalo también: domain.setId(entity.getId());
    }

    // Convierte del Negocio (Model) -> A la Base de Datos (Entity)
    public EmpleadoEntity toEntity(Empleado domain){
        if (domain == null) return null;
        return new EmpleadoEntity(
            domain.getId(),
            domain.getNombre(),
            domain.getDocumento(),
            domain.getRol(),
            domain.getCorreo(),
            domain.getSalario()
        );
    }
}
