package com.granzonamarciana.service;

import android.content.Context;
import androidx.lifecycle.LiveData;

import com.granzonamarciana.dao.GalaDao;
import com.granzonamarciana.database.DatabaseHelper;
import com.granzonamarciana.entity.Gala;

import java.util.List;

public class GalaService {

    private GalaDao galaDao;

    public GalaService(Context context) {
        DatabaseHelper db = DatabaseHelper.getInstance(context);
        galaDao = db.galaDao();
    }

    public void insertarGala(final Gala gala) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                galaDao.insertarGala(gala);
            }
        }).start();
    }

    public void actualizarGala(final Gala gala) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                galaDao.actualizarGala(gala);
            }
        }).start();
    }

    public void eliminarGala(final Gala gala) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                galaDao.eliminarGala(gala);
            }
        }).start();
    }

    public LiveData<List<Gala>> listarGalas() {
        return galaDao.listarGalas();
    }

    public LiveData<Gala> buscarGalaPorId(int id) {
        return galaDao.buscarGalaPorId(id);
    }

    public LiveData<List<Gala>> listarGalasPorEdicion(int idEdicion) {
        return galaDao.listarGalasPorEdicion(idEdicion);
    }
}