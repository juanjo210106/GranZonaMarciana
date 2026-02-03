package com.granzonamarciana.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.granzonamarciana.entity.Espectador;

import java.util.List;

@Dao
public interface EspectadorDao {
    // Registrar un nuevo espectador en el sistema
    @Insert
    void insertarEspectador(Espectador e);

    // Actualizar los datos del perfil de un espectador
    @Update
    void actualizarEspectador(Espectador e);

    // Dar de baja a un espectador
    @Delete
    void eliminarEspectador(Espectador e);

    // Obtener la lista de todos los espectadores registrados
    @Query("SELECT * FROM espectador")
    LiveData<List<Espectador>> listarEspectadores();

    // Localizar a un espectador por su id
    @Query("SELECT * FROM espectador WHERE id = :id")
    LiveData<Espectador> buscarEspectadorPorId(int id);

    // Buscar a un espectador por su nombre de usuario para procesos de autenticación
    @Query("SELECT * FROM espectador WHERE username = :username")
    LiveData<Espectador> buscarEspectadorPorUsername(String username);
}