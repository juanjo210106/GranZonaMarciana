package com.granzonamarciana.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.granzonamarciana.entity.Administrador;

import java.util.List;

@Dao
public interface AdministradorDao {

    @Insert
    void insertar(Administrador administrador);

    @Update
    void actualizar(Administrador administrador);

    @Delete
    void eliminar(Administrador administrador);

    @Query("SELECT * FROM administrador")
    List<Administrador> obtenerTodos();

    @Query("SELECT * FROM administrador WHERE id = :id")
    Administrador obtenerPorId(int id);

    @Query("SELECT * FROM administrador WHERE username = :username AND password = :password")
    Administrador login(String username, String password);
}
