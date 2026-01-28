package com.granzonamarciana.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.granzonamarciana.entity.Puntuacion;

@Dao
public interface PuntuacionDao {
    @Insert
    void insertarPuntuacion(Puntuacion puntuacion);

    // Comprobamos si un espectador ya votó a este concursante en esta gala
    // Con esto devolvemos la puntuación si existe, o null si no
    @Query("SELECT * FROM puntuacion WHERE idEspectador = :idEspectador AND idGala AND idConcursante = :idConcursante")
    LiveData<Puntuacion> buscarVoto(int idEspectador, int idGala, int idConcursante);

    // Calcular la media de un concursante en una gala
    @Query("SELECT AVG(valor) FROM puntuacion WHERE idConcursante = :idConcursante AND idGala = :idGala")
    LiveData<Float> obtenerMediaPuntuacion(int idConcursante, int idGala);
}
