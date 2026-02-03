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
    // Insertar una nueva edición
    @Insert
    void insertarEdicion(Edicion edicion);

    // Actualizar datos de una edición
    @Update
    void actualizarEdicion(Edicion edicion);

    // Eliminasr una edición del histórico
    @Delete
    void eliminarEdicion(Edicion edicion);

    // Obtener todas las ediciones para mostrarlas en listas o seectores
    @Query("SELECT * FROM edicion")
    LiveData<List<Edicion>> listarEdiciones();

    // Obtener una edición específica por su ID
    @Query("SELECT * FROM edicion WHERE id = :id")
    LiveData<Edicion> buscarEdicionPorId(int id);
}
