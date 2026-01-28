package com.granzonamarciana.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.granzonamarciana.entity.Gala;

import java.util.List;

@Dao
public interface GalaDao {

    @Insert
    void insertar(Gala gala);

    @Update
    void actualizar(Gala gala);

    @Delete
    void eliminar(Gala gala);

    @Query("SELECT * FROM gala")
    List<Gala> obtenerTodas();

    @Query("SELECT * FROM gala WHERE id = :id")
    Gala obtenerPorId(int id);

    // Requisito de consultar el listado de galas de cada edición
    @Query("SELECT * FROM gala WHERE idEdicion = :idEdicion ORDER BY fechaRealizacion ASC")
    List<Gala> obtenerGalasPorEdicion(int idEdicion);
}