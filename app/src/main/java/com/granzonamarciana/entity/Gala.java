package com.granzonamarciana.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.TypeConverters;

import com.granzonamarciana.database.LocalDateTimeConverter;

import java.time.LocalDateTime;

@Entity(tableName = "gala")
@TypeConverters({LocalDateTimeConverter.class})
public class Gala extends DomainEntity {

    private LocalDateTime fechaRealizacion;

    @ColumnInfo(name = "idEdicion")
    private int idEdicion;

    public Gala() {
    }

    public Gala(LocalDateTime fechaRealizacion, int idEdicion) {
        this.fechaRealizacion = fechaRealizacion;
        this.idEdicion = idEdicion;
    }

    public LocalDateTime getFechaRealizacion() {
        return fechaRealizacion;
    }

    public void setFechaRealizacion(LocalDateTime fechaRealizacion) {
        this.fechaRealizacion = fechaRealizacion;
    }

    public int getIdEdicion() {
        return idEdicion;
    }

    public void setIdEdicion(int idEdicion) {
        this.idEdicion = idEdicion;
    }
}