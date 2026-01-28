package com.granzonamarciana.dao;

import androidx.lifecycle.LiveData;
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
    void insertarEspectador(Espectador e);

    @Update
    void actualizarEspectador(Espectador e);

    @Delete
    void eliminarEspectador(Espectador e);

    @Query("SELECT * FROM espectador")
    LiveData<List<Espectador>> listarEspectadores();

    @Query("SELECT * FROM espectador WHERE id = :id")
    LiveData<Espectador> buscarEspectadorPorId(int id);

    @Query("SELECT * FROM espectador WHERE username = :username")
    LiveData<Espectador> buscarEspectadorPorUsername(String username);
}