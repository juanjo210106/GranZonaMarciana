package com.granzonamarciana.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.granzonamarciana.entity.SolicitudParticipacion;
import com.granzonamarciana.entity.EstadoSolicitud;

import java.util.List;

@Dao
public interface SolicitudParticipacionDao {

    @Insert
    void insertar(SolicitudParticipacion solicitud);

    @Update
    void actualizar(SolicitudParticipacion solicitud);

    @Delete
    void eliminar(SolicitudParticipacion solicitud);

    // Para que el administrador vea todas
    @Query("SELECT * FROM solicitud_participacion")
    List<SolicitudParticipacion> obtenerTodas();

    @Query("SELECT * FROM solicitud_participacion WHERE id = :id")
    SolicitudParticipacion obtenerPorId(int id);

    // Requisito: Consultar solicitudes por edición (útil para el administrador)
    @Query("SELECT * FROM solicitud_participacion WHERE idEdicion = :idEdicion")
    List<SolicitudParticipacion> obtenerPorEdicion(int idEdicion);

    // Requisito: Consultar solicitudes de un concursante específico
    @Query("SELECT * FROM solicitud_participacion WHERE idConcursante = :idConcursante")
    List<SolicitudParticipacion> obtenerPorConcursante(int idConcursante);

    @Query("SELECT * FROM solicitud_participacion WHERE estado = :estado")
    List<SolicitudParticipacion> obtenerPorEstado(EstadoSolicitud estado);
}
