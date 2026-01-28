package com.granzonamarciana.service;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.granzonamarciana.dao.EdicionDao;
import com.granzonamarciana.database.DatabaseHelper;
import com.granzonamarciana.entity.Edicion;

import java.util.List;

public class EdicionService {
    private EdicionDao edicionDao;

    public EdicionService(Context context) {
        DatabaseHelper db = DatabaseHelper.getInstance(context);
        edicionDao = db.edicionDao();
    }

    public void insertarEdcicion(final Edicion edicion) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                edicionDao.insertarEdicion(edicion);
            }
        }).start();
    }

    public void actualizarEdicion(final Edicion edicion) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                edicionDao.actualizarEdicion(edicion);
            }
        }).start();
    }

    public void eliminarEdicion(final Edicion edicion) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                edicionDao.eliminarEdicion(edicion);
            }
        }).start();
    }

    // Operaciones con LiveData (LECTURA)
    public LiveData<List<Edicion>> listarEdiciones() {
        return edicionDao.listarEdiciones();
    }

    public LiveData<Edicion> buscarEdicionPorId(int id) {
        return edicionDao.buscarEdicionPorId(id);
    }
}
