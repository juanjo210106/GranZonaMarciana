package com.granzonamarciana.entity;

import androidx.room.Entity;

import com.granzonamarciana.entity.Actor;
import com.granzonamarciana.entity.TipoRol;

import java.time.LocalDate;

@Entity(tableName = "usuario")
public class Usuario extends Actor {

    public Usuario() {
        super();
    }

    public Usuario(String dni, String nombre, String apellido1, String apellido2, String telefono,
                   String correo, String poblacion, String provincia, LocalDate fechaNacimiento,
                   String password, TipoRol rol) {
        super(dni, nombre, apellido1, apellido2, telefono, correo, poblacion, provincia, fechaNacimiento, password, rol);
    }
}