package com.granzonamarciana.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.granzonamarciana.entity.Puntuacion;

import java.util.List;

@Dao
public interface PuntuacionDao {

    @Insert
    void insertarPuntuacion(Puntuacion puntuacion);

    // Obtener todas las puntuaciones (útil para el admin)
    @Query("SELECT * FROM puntuacion")
    LiveData<List<Puntuacion>> listarPuntuaciones();

    // Obtener solo mis puntuaciones (usado en ListPuntuacion)
    @Query("SELECT * FROM puntuacion WHERE idEspectador = :idEspectador")
    LiveData<List<Puntuacion>> listarPuntuacionesPorEspectador(int idEspectador);

    // Ver todos los votos de una gala concreta
    @Query("SELECT * FROM puntuacion WHERE idGala = :idGala")
    LiveData<List<Puntuacion>> listarPuntuacionesPorGala(int idGala);

    // Comprobamos si un espectador ya votó a este concursante en esta gala
    @Query("SELECT * FROM puntuacion WHERE idEspectador = :idEspectador AND idGala = :idGala AND idConcursante = :idConcursante")
    LiveData<Puntuacion> buscarVoto(int idEspectador, int idGala, int idConcursante);

    // Calcular la media de un concursante en una gala (usa tu campo 'valor')
    @Query("SELECT AVG(valor) FROM puntuacion WHERE idConcursante = :idConcursante AND idGala = :idGala")
    LiveData<Float> obtenerMediaPuntuacion(int idConcursante, int idGala);
}