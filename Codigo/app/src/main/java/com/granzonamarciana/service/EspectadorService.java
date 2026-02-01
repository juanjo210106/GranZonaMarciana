package com.granzonamarciana.service;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.granzonamarciana.dao.EspectadorDao;
import com.granzonamarciana.database.DatabaseHelper;
import com.granzonamarciana.entity.Espectador;


public class EspectadorService {
    private EspectadorDao espectadorDao;

    public EspectadorService(Context context){
        DatabaseHelper db = DatabaseHelper.getInstance(context);
        espectadorDao = db.espectadorDao();
    }

    public void insertarEspectador(final Espectador espectador) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                espectadorDao.insertarEspectador(espectador);
            }
        }).start();
    }

    public LiveData<Espectador> buscarPorUsername(String username) {
        return espectadorDao.buscarEspectadorPorUsername(username);
    }
}
