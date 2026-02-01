package com.granzonamarciana.service;

import android.content.Context;
import androidx.lifecycle.LiveData;

import com.granzonamarciana.dao.SolicitudDao;
import com.granzonamarciana.database.DatabaseHelper;
import com.granzonamarciana.entity.Solicitud;

import java.util.List;

public class SolicitudService {

    private SolicitudDao solicitudDao;

    public SolicitudService(Context context) {
        DatabaseHelper db = DatabaseHelper.getInstance(context);
        solicitudDao = db.solicitudDao();
    }

    public void insertarSolicitud(final Solicitud solicitud) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                solicitudDao.insertarSolicitud(solicitud);
            }
        }).start();
    }

    public void actualizarSolicitud(final Solicitud solicitud) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                solicitudDao.actualizarSolicitud(solicitud);
            }
        }).start();
    }

    public void eliminarSolicitud(final Solicitud solicitud) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                solicitudDao.eliminarSolicitud(solicitud);
            }
        }).start();
    }

    public LiveData<List<Solicitud>> listarSolicitudes() {
        return solicitudDao.listarSolicitudes();
    }

    public LiveData<Solicitud> buscarSolicitudPorId(int id) {
        return solicitudDao.buscarSolicitudPorId(id);
    }

    public LiveData<List<Solicitud>> listarSolicitudesPorEdicion(int idEdicion) {
        return solicitudDao.listarSolicitudesPorEdicion(idEdicion);
    }

    public LiveData<Solicitud> buscarSolicitudDeUsuario(int idConcursante, int idEdicion) {
        return solicitudDao.buscarSolicitudDeUsuario(idConcursante, idEdicion);
    }

    public LiveData<List<Solicitud>> listarSolicitudesPendientes() {
        return solicitudDao.listarSolicitudesPendientes();
    }
}