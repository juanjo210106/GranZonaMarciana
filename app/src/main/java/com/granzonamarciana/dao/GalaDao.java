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

    @Insert
    void insertarGala(Gala g);

    @Update
    void actualizarGala(Gala g);

    @Delete
    void eliminarGala(Gala g);

    @Query("SELECT * FROM gala WHERE idEdicion = :idEdicion")
    LiveData<List<Gala>> listarGalasPorEdicion(int idEdicion);
}