package com.granzonamarciana.entity.POJO;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.granzonamarciana.entity.Solicitud;
import com.granzonamarciana.entity.Usuario;

import java.util.List;

public class UsuarioConSolicitudes {
    @Embedded
    public Usuario usuario;

    @Relation(
            parentColumn = "id",        // ID en la tabla Usuario
            entityColumn = "idConcursante" // DEBE COINCIDIR con el campo de tu entidad Solicitud
    )
    public List<Solicitud> solicitudes;
}