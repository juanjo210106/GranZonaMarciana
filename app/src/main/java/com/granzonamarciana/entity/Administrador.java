package com.granzonamarciana.entity;

import androidx.room.Entity;

@Entity(tableName = "administrador")
public class Administrador extends Actor {

    public Administrador(String username, String password, TipoRol rol, String nombre,
                         String apellido1, String apellido2, String correo,
                         String telefono, String urlImagen) {

        super(username, password, rol, nombre, apellido1, apellido2, correo, telefono, urlImagen);
    }
}