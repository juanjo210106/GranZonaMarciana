package com.granzonamarciana.dao;

import androidx.lifecycle.LiveData;
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
    void insertarAdministrador(Administrador a);

    @Update
    void actualizarAdministrador(Administrador a);

    @Delete
    void eliminarAdministrador(Administrador a);

    @Query("SELECT * FROM administrador")
    LiveData<List<Administrador>> listarAdministradores();

    @Query("SELECT * FROM administrador WHERE id = :id")
    LiveData<Administrador> buscarAdministradorPorId(int id);

    @Query("SELECT * FROM administrador WHERE username = :username")
    LiveData<Administrador> buscarAdministradorPorUsername(String username);
}
