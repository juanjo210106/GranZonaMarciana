package com.granzonamarciana.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.granzonamarciana.entity.Administrador;

import java.util.List;

@Dao
public interface AdministradorDao {
    // Insertar un Administrador
    @Insert
    void insertarAdministrador(Administrador a);

    // Actualizar un Administrador
    @Update
    void actualizarAdministrador(Administrador a);

    // Eliminar un Administrador
    @Delete
    void eliminarAdministrador(Administrador a);

    // Listar todos los administradores registrados en el sistema
    @Query("SELECT * FROM administrador")
    LiveData<List<Administrador>> listarAdministradores();

    // Buscar un administrador por su ID único para cargar su perfil
    @Query("SELECT * FROM administrador WHERE id = :id")
    LiveData<Administrador> buscarAdministradorPorId(int id);

    // Buscar un administrador por su nombre de usuario (útil para el login o comprobaciones de duplicados)
    @Query("SELECT * FROM administrador WHERE username = :username")
    LiveData<Administrador> buscarAdministradorPorUsername(String username);
}
