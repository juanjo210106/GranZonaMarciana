package com.granzonamarciana.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.granzonamarciana.entity.Noticia;

import java.util.List;

@Dao
public interface NoticiaDao {

    @Insert
    void insertarNoticia(Noticia n);

    @Update
    void actualizarNoticia(Noticia n);

    @Delete
    void eliminarNoticia(Noticia n);

    @Query("SELECT * FROM noticia ORDER BY fechaPublicacion DESC")
    LiveData<List<Noticia>> listarNoticias();

    @Query("SELECT * FROM noticia WHERE id = :id")
    LiveData<Noticia> buscarNoticiaPorId(int id);
}
