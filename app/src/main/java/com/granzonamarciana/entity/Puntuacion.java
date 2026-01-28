package com.granzonamarciana.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.granzonamarciana.entity.DomainEntity;

@Entity(tableName = "puntuacion")
public class Puntuacion extends DomainEntity {

    private int valor;

    // Relaciones mediante IDs (Estilo de tu maestro en Anuncio.java)
    @ColumnInfo(name = "idEspectador")
    private int idEspectador;

    @ColumnInfo(name = "idConcursante")
    private int idConcursante;

    @ColumnInfo(name = "idGala")
    private int idGala;

    // Constructor completo
    public Puntuacion(int valor, int idEspectador, int idConcursante, int idGala) {
        this.valor = valor;
        this.idEspectador = idEspectador;
        this.idConcursante = idConcursante;
        this.idGala = idGala;
    }
}
