package com.granzonamarciana.entity;

import androidx.room.Entity;

@Entity(tableName = "espectador")
public class Espectador extends Actor {

    public Espectador() {

    }

    public Espectador(String username, String password, TipoRol rol, String nombre,
                      String apellido1, String apellido2, String correo,
                      String telefono, String urlImagen) {

        super(username, password, rol, nombre, apellido1, apellido2, correo, telefono, urlImagen);
    }
}