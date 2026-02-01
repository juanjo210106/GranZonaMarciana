package com.granzonamarciana.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.granzonamarciana.entity.POJO.UsuarioConPuntuaciones;
import com.granzonamarciana.entity.POJO.UsuarioConSolicitudes;
import com.granzonamarciana.entity.Usuario;

import java.util.List;

@Dao
public interface UsuarioDao {

    @Insert
    void insertarUsuario(Usuario u);

    @Update
    void actualizarUsuario(Usuario u);

    @Delete
    void eliminarUsuario(Usuario u);

    @Query("SELECT * FROM usuario")
    LiveData<List<Usuario>> listarUsuarios();

    @Query("SELECT * FROM usuario WHERE id = :idUsuario")
    LiveData<Usuario> buscarUsuarioPorId(int idUsuario);


    @Query("SELECT * FROM usuario WHERE username = :username")
    LiveData<Usuario> buscarUsuarioPorUsername(String username);

    @Query("SELECT * FROM usuario WHERE correo = :correo")
    LiveData<Usuario> buscarUsuarioPorCorreo(String correo);


    @Query("SELECT * FROM usuario WHERE username = :username LIMIT 1")
    Usuario loginUsuario(String username);

    @Transaction
    @Query("SELECT * FROM usuario WHERE id = :idUsuario")
    LiveData<UsuarioConSolicitudes> buscarUsuarioConSolicitudesPorId(int idUsuario);

    @Transaction
    @Query("SELECT * FROM usuario WHERE id = :idUsuario")
    LiveData<UsuarioConPuntuaciones> buscarUsuarioConPuntuacionesPorId(int idUsuario);
}
