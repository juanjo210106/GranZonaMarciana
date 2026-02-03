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
    // Insertar una nueva noticia
    @Insert
    void insertarNoticia(Noticia n);

    // Actualizar el contenido de una noticia
    @Update
    void actualizarNoticia(Noticia n);

    // ELiminar una noticia
    @Delete
    void eliminarNoticia(Noticia n);

    // Listar todas las noticias ordenadas por fecha de publicación ( la mas reciente primero)
    // Esencial para mantener el feed de actualidad actualizado
    @Query("SELECT * FROM noticia ORDER BY fechaPublicacion DESC")
    LiveData<List<Noticia>> listarNoticias();

    // Obtener los detalles completos de una noticia específica mediante su ID
    @Query("SELECT * FROM noticia WHERE id = :id")
    LiveData<Noticia> buscarNoticiaPorId(int id);
}
