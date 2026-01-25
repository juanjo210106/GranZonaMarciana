package com.granzonamarciana.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;

import java.time.LocalDateTime;

@Entity(tableName = "gala",
    foreignKeys = @ForeignKey(entity = Edicion.class, parentColumns = "id", childColumns = "idEdicion", onDelete = ForeignKey.CASCADE))
public class Gala extends DomainEntity {
    private int idEdicion;
    private LocalDateTime fechaEmision; // LocalDateTime para controlar las 24h exactas
    private String descripcion;

    public Gala() {
    }

    public Gala(int idEdicion, LocalDateTime fechaEmision, String descripcion) {
        this.idEdicion = idEdicion;
        this.fechaEmision = fechaEmision;
        this.descripcion = descripcion;
    }

    public int getIdEdicion() {
        return idEdicion;
    }

    public void setIdEdicion(int idEdicion) {
        this.idEdicion = idEdicion;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
