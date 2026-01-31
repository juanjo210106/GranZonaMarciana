package com.granzonamarciana.service;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.granzonamarciana.dao.ConcursanteDao;
import com.granzonamarciana.database.DatabaseHelper;
import com.granzonamarciana.entity.Concursante;

import java.util.List;

public class ConcursanteService {
    private ConcursanteDao concursanteDao;

    public ConcursanteService(Context context) {
        DatabaseHelper db = DatabaseHelper.getInstance(context);
        concursanteDao = db.concursanteDao();
    }

    public void insertarConcursante(final Concursante concursante) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                concursanteDao.insertarConcursante(concursante);
            }
        }).start();
    }

    public LiveData<Concursante> buscarPorUsername(String username) {
        return concursanteDao.buscarConcursantePorUsername(username);
    }

    public LiveData<List<Concursante>> listarTodos() {
        return concursanteDao.listarConcursantes();
    }

    public LiveData<Concursante> buscarPorId(int id) {
        return concursanteDao.buscarConcursantePorId(id);
    }
}

