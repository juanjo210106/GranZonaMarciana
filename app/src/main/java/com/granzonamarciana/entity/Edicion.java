package com.granzonamarciana.entity;

import androidx.room.Entity;
import androidx.room.TypeConverters;

import com.granzonamarciana.database.LocalDateConverter;

import java.time.LocalDate;

@Entity(tableName = "edicion")
@TypeConverters({LocalDateConverter.class})
public class Edicion extends DomainEntity {

    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int maxParticipantes;
    
    public Edicion() {
    }

    public Edicion(LocalDate fechaInicio, LocalDate fechaFin, int maxParticipantes) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.maxParticipantes = maxParticipantes;
    }


    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getMaxParticipantes() {
        return maxParticipantes;
    }

    public void setMaxParticipantes(int maxParticipantes) {
        this.maxParticipantes = maxParticipantes;
    }
}