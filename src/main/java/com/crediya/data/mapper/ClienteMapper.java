package com.crediya.data.mapper;

import com.crediya.model.Cliente;
import com.crediya.data.entities.ClienteEntity;

public class ClienteMapper {
    // Convierte de la Base de Datos (Entity) -> Al Negocio (Model)
    public Cliente toDomain(ClienteEntity entity){
        if (entity == null) return null;
        return new Cliente(
            entity.getId(),
            entity.getNombre(), 
            entity.getDocumento(),
            entity.getCorreo(),
            entity.getTelefono()
        );
       
    }

    // Convierte del Negocio (Model) -> A la Base de Datos (Entity)
    public ClienteEntity toEntity(Cliente domain){
        if (domain == null) return null;
        return new ClienteEntity(
            domain.getId(),
            domain.getNombre(),
            domain.getDocumento(),
            domain.getCorreo(),
            domain.getTelefono()
        );
    }
}
