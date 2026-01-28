package com.granzonamarciana.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.granzonamarciana.entity.Concursante;

import java.util.List;

@Dao
public interface ConcursanteDao {

    @Insert
    void insertar(Concursante concursante);

    @Update
    void actualizar(Concursante concursante);

    @Delete
    void eliminar(Concursante concursante);

    @Query("SELECT * FROM concursante")
    List<Concursante> obtenerTodos();

    @Query("SELECT * FROM concursante WHERE id = :id")
    Concursante obtenerPorId(int id);

    @Query("SELECT * FROM concursante WHERE username = :username AND password = :password")
    Concursante login(String username, String password);
}