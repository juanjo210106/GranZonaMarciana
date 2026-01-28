package com.granzonamarciana.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.granzonamarciana.entity.Espectador;

import java.util.List;

@Dao
public interface EspectadorDao {

    @Insert
    void insertar(Espectador espectador);

    @Update
    void actualizar(Espectador espectador);

    @Delete
    void eliminar(Espectador espectador);

    @Query("SELECT * FROM espectador")
    List<Espectador> obtenerTodos();

    @Query("SELECT * FROM espectador WHERE id = :id")
    Espectador obtenerPorId(int id);

    @Query("SELECT * FROM espectador WHERE username = :username AND password = :password")
    Espectador login(String username, String password);
}