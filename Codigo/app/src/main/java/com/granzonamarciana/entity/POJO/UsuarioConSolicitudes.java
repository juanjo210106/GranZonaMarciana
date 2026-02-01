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
            entityColumn = "idConcursante"
    )
    public List<Solicitud> solicitudes;
}