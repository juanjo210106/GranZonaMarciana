package com.granzonamarciana.service;

import android.content.Context;
import androidx.lifecycle.LiveData;
import com.granzonamarciana.dao.PuntuacionDao;
import com.granzonamarciana.database.DatabaseHelper;
import com.granzonamarciana.entity.Puntuacion;

public class PuntuacionService {
    private PuntuacionDao puntuacionDao;

    public PuntuacionService(Context context) {
        DatabaseHelper db = DatabaseHelper.getInstance(context);
        puntuacionDao = db.puntuacionDao();
    }

    public void votar(final Puntuacion puntuacion) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                puntuacionDao.insertarPuntuacion(puntuacion);
            }
        }).start();
    }

    // Para saber si YO (Espectador) ya he votado a este concursante en esta gala
    public LiveData<Puntuacion> miVoto(int idEspectador, int idGala, int idConcursante) {
        return puntuacionDao.buscarVoto(idEspectador, idGala, idConcursante);
    }

    // Para sacar la nota media del concursante
    public LiveData<Float> obtenerMedia(int idConcursante, int idGala) {
        return puntuacionDao.obtenerMediaPuntuacion(idConcursante, idGala);
    }
}