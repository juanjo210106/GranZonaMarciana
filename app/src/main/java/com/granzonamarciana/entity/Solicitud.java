package com.granzonamarciana.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "solicitud",
        foreignKeys = {
            @ForeignKey(entity = Actor.class, parentColumns = "id", childColumns = "idConcursante", onDelete = ForeignKey.CASCADE),
            @ForeignKey(entity = Edicion.class, parentColumns = "id", childColumns = "idEdicion", onDelete = ForeignKey.CASCADE)
        })
public class Solicitud extends DomainEntity {
    private int idConcursante;
    private int idEdicion;
    private String mensaje;
    private String estado; // PENDIENTE, RECHAZADA, ACEPTADA

    public Solicitud() {
    }

    public Solicitud(int idConcursante, int idEdicion, String mensaje, String estado) {
        this.idConcursante = idConcursante;
        this.idEdicion = idEdicion;
        this.mensaje = mensaje;
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

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
