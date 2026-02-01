package com.granzonamarciana.entity;

import androidx.room.Entity;
import androidx.room.TypeConverters;

import com.granzonamarciana.database.LocalDateTimeConverter;

import java.time.LocalDateTime;

@Entity(tableName = "noticia")
@TypeConverters({LocalDateTimeConverter.class})
public class Noticia extends DomainEntity {

    private LocalDateTime fechaPublicacion;
    private String cabecera;
    private String cuerpo;
    private String urlImagen;


    // Cosntructor vacío
    public Noticia() {

    }

    // Constructor
    public Noticia(LocalDateTime fechaPublicacion, String cabecera, String cuerpo, String urlImagen) {
        this.fechaPublicacion = fechaPublicacion;
        this.cabecera = cabecera;
        this.cuerpo = cuerpo;
        this.urlImagen = urlImagen;
    }

    // Getters y Setters
    public LocalDateTime getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(LocalDateTime fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public String getCabecera() {
        return cabecera;
    }

    public void setCabecera(String cabecera) {
        this.cabecera = cabecera;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }
}




