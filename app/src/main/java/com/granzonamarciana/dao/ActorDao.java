package com.granzonamarciana.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.granzonamarciana.entity.Actor;

import java.util.List;

@Dao
public interface ActorDao {
    @Insert
    void insertarActor(Actor actor);

    @Update
    void atualizarActor(Actor actor);

    @Delete
    void eliminarActor(Actor actor);

    // Para realizar el Login: Buscamos por nombre de usuario
    @Query("SELECT * FROM actor WHERE nombreUsuario = :nombreUsuario")
    LiveData<Actor> buscarPorNombreUsuario(String nombreUsuario);

    // Esto es para listar concursantes o espectadores según el rol
    @Query("SELECT * FROM actor WHERE rol = :rol")
    LiveData<List<Actor>> listarActoresPorRol(String rol);

    @Query("SELECT * FROM actor WHERE id = :id")
    LiveData<Actor> buscarPorId(int id);

    // Para listar todos (admin)
    @Query("SELECT * FROM actor")
    LiveData<List<Actor>> listarTodos();
}
