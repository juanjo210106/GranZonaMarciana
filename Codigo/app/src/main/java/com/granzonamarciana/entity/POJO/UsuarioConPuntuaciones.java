package com.granzonamarciana.entity.POJO;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.granzonamarciana.entity.Puntuacion;
import com.granzonamarciana.entity.Usuario;

import java.util.List;

public class UsuarioConPuntuaciones {
    @Embedded
    public Usuario usuario;

    @Relation(
            parentColumn = "id",        // ID del Usuario (PK)
            entityColumn = "idEspectador"
    )
    public List<Puntuacion> puntuaciones;
}