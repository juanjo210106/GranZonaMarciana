package com.granzonamarciana.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "actores")
public class Actor implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    // Datos personales
    public String nombreUsuario;
    public String contrasena;
    public String nombre;
    public String apellidos;
    public String email;
    public String telefono;

    // Imágenes como enlaces
    public String imagenUrl;

    // Roles: "ADMIN", "CONCURSANTE", "ESPECTADOR"
    public String rol;

    // Constructor vacío
    public Actor() {
    }

    // Constructor completo
    public Actor(String nombreUsuario, String contrasena, String nombre, String apellidos, String email, String telefono, String imagenUrl, String rol) {
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.telefono = telefono;
        this.imagenUrl = imagenUrl;
        this.rol = rol;
    }

    // Getters y Setters
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
