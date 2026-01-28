package com.granzonamarciana.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.granzonamarciana.entity.Solicitud;

import java.util.List;

@Dao
public interface SolicitudDao {
    @Insert
    void insertarSolicitud(Solicitud solicitud);

    @Update
    void actualizarSolicitud(Solicitud solicitud);

    @Delete
    void eliminarSolicitud(Solicitud solicitud);

    // Ver las solicitudes de una edición específica
    @Query("SELECT * FROM solicitud WHERE idEdicion = :idEdicion")
    LiveData<List<Solicitud>> listarSolicitudesPorEdicion(int idEdicion);

    // Ver si un usuario ya tiene solicitud en esa edición
    @Query("SELECT * FROM solicitud WHERE idConcursante = :idUsuario AND idEdicion = :idEdicion")
    LiveData<Solicitud> buscarSolicitudDeUsuario(int idUsuario, int idEdicion);

    // Listar solo las pendientes
    @Query("SELECT * FROM solicitud WHERE estado = 'PENDIENTE'")
    LiveData<List<Solicitud>> listarSolicitudesPEndientes();
}
