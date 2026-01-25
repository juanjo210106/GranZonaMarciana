package com.granzonamarciana.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "actores")
public class Actor {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String nombreUsuario;
    public String contrasena;
    public String nombre;
    public String apellidos;
    public String email;
    public String telefono;

    // Las imágenes son enlaces
    public String imagenUrl;

    // Roles: "ADMIN", "CONCURSANTE", "ESPECTADOR"
    public String rol;

    public Actor() {
    }

    public Actor(String nombreUsuario, String contrasena, String nombre, String rol) {
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.nombre = nombre;
        this.rol = rol;
    }
}
