package com.granzonamarciana.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.granzonamarciana.entity.Concursante;

import java.util.List;

@Dao
public interface ConcursanteDao {
    // Insertar un nuevo concursante en la base de datos
    @Insert
    void insertarConcursante(Concursante c);

    // Actualizar la información de un concursante ya existente
    @Update
    void actualizarConcursante(Concursante c);

    // Eliminar un concursante del sistema
    @Delete
    void eliminarConcursante(Concursante c);

    // Obtener la lista completa de concursantes inscritos
    @Query("SELECT * FROM concursante")
    LiveData<List<Concursante>> listarConcursantes();

    // Buscar un concursante por su ID único para gestionar su participación
    @Query("SELECT * FROM concursante WHERE id = :id")
    LiveData<Concursante> buscarConcursantePorId(int id);

    // Buscar concursante por nombre de usuario (Validaciones y perfiles públicos)
    @Query("SELECT * FROM concursante WHERE username = :username")
    LiveData<Concursante> buscarConcursantePorUsername(String username);
}