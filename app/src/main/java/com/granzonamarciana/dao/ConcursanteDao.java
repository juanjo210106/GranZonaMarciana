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

    @Insert
    void insertarConcursante(Concursante c);

    @Update
    void actualizarConcursante(Concursante c);

    @Delete
    void eliminarConcursante(Concursante c);

    @Query("SELECT * FROM concursante")
    LiveData<List<Concursante>> listarConcursantes();

    @Query("SELECT * FROM concursante WHERE id = :id")
    LiveData<Concursante> buscarConcursantePorId(int id);

    @Query("SELECT * FROM concursante WHERE username = :username")
    LiveData<Concursante> buscarConcursantePorUsername(String username);
}