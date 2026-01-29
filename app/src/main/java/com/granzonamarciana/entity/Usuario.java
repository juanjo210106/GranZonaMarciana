package com.granzonamarciana.entity;

import androidx.room.Entity;

@Entity(tableName = "usuario")
public class Usuario extends Actor {

    // Constructor vacío requerido por Room
    public Usuario() {
        super();
    }

    // Constructor principal que mapea los campos de tu clase Actor
    public Usuario(String username, String password, TipoRol rol, String nombre,
                   String apellido1, String apellido2, String correo,
                   String telefono, String urlImagen) {
        super(username, password, rol, nombre, apellido1, apellido2, correo, telefono, urlImagen);
    }
}