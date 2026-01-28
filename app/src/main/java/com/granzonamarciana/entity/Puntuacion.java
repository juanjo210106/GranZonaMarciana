package com.granzonamarciana.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "puntuacion")
public class Puntuacion extends DomainEntity {

    private int valor;

    // Relaciones mediante IDs
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

    // Getters y Setters
    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public int getIdEspectador() {
        return idEspectador;
    }

    public void setIdEspectador(int idEspectador) {
        this.idEspectador = idEspectador;
    }

    public int getIdConcursante() {
        return idConcursante;
    }

    public void setIdConcursante(int idConcursante) {
        this.idConcursante = idConcursante;
    }

    public int getIdGala() {
        return idGala;
    }

    public void setIdGala(int idGala) {
        this.idGala = idGala;
    }
}