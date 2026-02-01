package com.granzonamarciana.service;

import android.content.Context;
import androidx.lifecycle.LiveData;

import com.granzonamarciana.dao.NoticiaDao;
import com.granzonamarciana.database.DatabaseHelper;
import com.granzonamarciana.entity.Noticia;

import java.util.List;

public class NoticiaService {

    private NoticiaDao noticiaDao;

    public NoticiaService(Context context) {
        DatabaseHelper db = DatabaseHelper.getInstance(context);
        noticiaDao = db.noticiaDao();
    }

    public void insertarNoticia(final Noticia noticia) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                noticiaDao.insertarNoticia(noticia);
            }
        }).start();
    }

    public void actualizarNoticia(final Noticia noticia) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                noticiaDao.actualizarNoticia(noticia);
            }
        }).start();
    }

    public void eliminarNoticia(final Noticia noticia) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                noticiaDao.eliminarNoticia(noticia);
            }
        }).start();
    }

    public LiveData<List<Noticia>> listarNoticias() {
        return noticiaDao.listarNoticias();
    }

    public LiveData<Noticia> buscarNoticiaPorId(int id) {
        return noticiaDao.buscarNoticiaPorId(id);
    }
}