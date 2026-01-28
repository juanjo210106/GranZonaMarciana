package com.granzonamarciana.dao;

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
    void insertar(Noticia noticia);

    @Update
    void actualizar(Noticia noticia);

    @Delete
    void eliminar(Noticia noticia);

    // Requisito: Consultar el listado de noticias del sistema
    @Query("SELECT * FROM noticia ORDER BY fechaPublicacion DESC")
    List<Noticia> obtenerTodas();

    // Requisito: Visualizar el detalle de cada noticia
    @Query("SELECT * FROM noticia WHERE id = :id")
    Noticia obtenerPorId(int id);
}
