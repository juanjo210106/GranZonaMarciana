package com.granzonamarciana.service;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.granzonamarciana.dao.AdministradorDao;
import com.granzonamarciana.database.DatabaseHelper;
import com.granzonamarciana.entity.Administrador;

import java.util.List;

public class AdministradorService {

    private AdministradorDao administradorDao;

    public AdministradorService(Context context) {
        DatabaseHelper db = DatabaseHelper.getInstance(context);
        administradorDao = db.administradorDao();
    }

    public void insertarAdministrador(final Administrador administrador) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                administradorDao.insertarAdministrador(administrador);
            }
        }).start();
    }

    public void actualizarAdministrador(final Administrador administrador) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                administradorDao.actualizarAdministrador(administrador);
            }
        }).start();
    }

    public void eliminarAdministrador(final Administrador administrador) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                administradorDao.eliminarAdministrador(administrador);
            }
        }).start();
    }

    public LiveData<List<Administrador>> listarAdministradores() {
        return administradorDao.listarAdministradores();
    }

    public LiveData<Administrador> buscarAdministradorPorId(int id) {
        return administradorDao.buscarAdministradorPorId(id);
    }

    public LiveData<Administrador> buscarAdministradorPorUsername(String username) {
        return administradorDao.buscarAdministradorPorUsername(username);
    }
}