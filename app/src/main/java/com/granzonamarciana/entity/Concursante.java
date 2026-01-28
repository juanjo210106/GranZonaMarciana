package com.granzonamarciana.entity;


import androidx.room.Entity;

@Entity(tableName = "concursante")
public class Concursante extends Actor {

    // Constructor vacío para Room
    public Concursante() {
    }

    // Constructor completo sincronizado con el nuevo Actor
    public Concursante(String username, String password, TipoRol rol, String nombre,
                       String apellido1, String apellido2, String correo,
                       String telefono, String urlImagen) {

        super(username, password, rol, nombre, apellido1, apellido2, correo, telefono, urlImagen);
    }
}
