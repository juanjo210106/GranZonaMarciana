package com.granzonamarciana.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.granzonamarciana.entity.Edicion;

import java.util.List;

@Dao
public interface EdicionDao {
    @Insert
    void insertarEdicion(Edicion edicion);

    @Update
    void actualizarEdicion(Edicion edicion);

    @Delete
    void eliminarEdicion(Edicion edicion);

    @Query("SELECT * FROM edicion")
    LiveData<List<Edicion>> listarEdiciones();

    @Query("SELECT * FROM edicion WHERE id = :id")
    LiveData<Edicion> buscarEdicionPorId(int id);
}
