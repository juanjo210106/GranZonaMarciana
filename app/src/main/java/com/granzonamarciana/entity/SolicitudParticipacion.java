package com.granzonamarciana.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "solicitud_participacion")
public class SolicitudParticipacion extends DomainEntity {

    private String mensaje;
    private EstadoSolicitud estado;

    @ColumnInfo(name = "idConcursante")
    private int idConcursante;

    @ColumnInfo(name = "idEdicion")
    private int idEdicion;

    // Constructor vacío para Room
    public SolicitudParticipacion() {
    }

    // Constructor completo
    public SolicitudParticipacion(String mensaje, EstadoSolicitud estado, int idConcursante, int idEdicion) {
        this.mensaje = mensaje;
        this.estado = estado;
        this.idConcursante = idConcursante;
        this.idEdicion = idEdicion;
    }

    // Getters y Setters
    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public EstadoSolicitud getEstado() {
        return estado;
    }

    public void setEstado(EstadoSolicitud estado) {
        this.estado = estado;
    }

    public int getIdConcursante() {
        return idConcursante;
    }

    public void setIdConcursante(int idConcursante) {
        this.idConcursante = idConcursante;
    }

    public int getIdEdicion() {
        return idEdicion;
    }

    public void setIdEdicion(int idEdicion) {
        this.idEdicion = idEdicion;
    }
}

