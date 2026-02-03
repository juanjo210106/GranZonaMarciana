package com.granzonamarciana.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.granzonamarciana.entity.Gala;

import java.util.List;

@Dao
public interface GalaDao {
    // Regsitrar una nueva gala
    @Insert
    void insertarGala(Gala g);

    // Modificar datos de una gala
    @Update
    void actualizarGala(Gala g);

    // Eliminar una gala del registro
    @Delete
    void eliminarGala(Gala g);

    // Obtener todas las galas registradas en el sistema
    @Query("SELECT * FROM gala")
    LiveData<List<Gala>> listarGalas();

    // Obtener los detalles de una gala específica mediante su ID
    @Query("SELECT * FROM gala WHERE id = :id")
    LiveData<Gala> buscarGalaPorId(int id);

    // Consultar las galas pertenecientesa una edición concreta
    // Este método establece la realación lógica entre ediciones y sus eventos
    @Query("SELECT * FROM gala WHERE idEdicion = :idEdicion")
    LiveData<List<Gala>> listarGalasPorEdicion(int idEdicion);
}