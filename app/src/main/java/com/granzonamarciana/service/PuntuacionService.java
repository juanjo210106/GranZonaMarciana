package com.granzonamarciana.service;

import android.content.Context;
import androidx.lifecycle.LiveData;
import com.granzonamarciana.dao.PuntuacionDao;
import com.granzonamarciana.database.DatabaseHelper;
import com.granzonamarciana.entity.Puntuacion;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PuntuacionService {

    private final PuntuacionDao puntuacionDao;
    private final ExecutorService executor;

    public PuntuacionService(Context context) {
        DatabaseHelper db = DatabaseHelper.getInstance(context);
        puntuacionDao = db.puntuacionDao();
        // Usamos Executor al estilo maestro para operaciones en segundo plano
        this.executor = Executors.newSingleThreadExecutor();
    }

    // Cambiado de 'votar' a 'insertarPuntuacion' para coincidir con la actividad
    public void insertarPuntuacion(final Puntuacion puntuacion) {
        executor.execute(() -> puntuacionDao.insertarPuntuacion(puntuacion));
    }

    public LiveData<List<Puntuacion>> listarPuntuaciones() {
        return puntuacionDao.listarPuntuaciones();
    }

    public LiveData<List<Puntuacion>> listarPuntuacionesPorEspectador(int idEspectador) {
        return puntuacionDao.listarPuntuacionesPorEspectador(idEspectador);
    }

    // Para saber si el Espectador ya ha votado
    public LiveData<Puntuacion> buscarVoto(int idEspectador, int idGala, int idConcursante) {
        return puntuacionDao.buscarVoto(idEspectador, idGala, idConcursante);
    }

    // Para sacar la nota media del concursante
    public LiveData<Float> obtenerMedia(int idConcursante, int idGala) {
        return puntuacionDao.obtenerMediaPuntuacion(idConcursante, idGala);
    }

    public LiveData<List<Puntuacion>> listarPuntuacionesPorGala(int idGala) {
        return puntuacionDao.listarPuntuacionesPorGala(idGala);
    }
}