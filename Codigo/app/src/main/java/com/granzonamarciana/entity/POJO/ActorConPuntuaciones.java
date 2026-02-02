package com.granzonamarciana.entity.POJO;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.granzonamarciana.entity.Actor;
import com.granzonamarciana.entity.Puntuacion;

import java.util.List;

public class ActorConPuntuaciones {
    @Embedded
    public Actor actor;

    @Relation(
            parentColumn = "id",            // ID del Actor (PK en la tabla Actor)
            entityColumn = "idEspectador"    // FK en la tabla Puntuacion que apunta al Actor
    )
    public List<Puntuacion> puntuaciones;
}