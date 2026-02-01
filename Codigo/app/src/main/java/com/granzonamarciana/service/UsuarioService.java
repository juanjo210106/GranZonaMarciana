package com.granzonamarciana.service;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.granzonamarciana.dao.UsuarioDao;
import com.granzonamarciana.database.DatabaseHelper;
import com.granzonamarciana.entity.POJO.UsuarioConPuntuaciones;
import com.granzonamarciana.entity.POJO.UsuarioConSolicitudes;
import com.granzonamarciana.entity.Usuario;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UsuarioService {

    private final UsuarioDao usuarioDao;
    private final ExecutorService executor;

    public UsuarioService(Context context) {
        DatabaseHelper db = DatabaseHelper.getInstance(context);
        this.usuarioDao = db.usuarioDao();
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void insertarUsuario(Usuario u) {
        executor.execute(() -> usuarioDao.insertarUsuario(u));
    }

    public void actualizarUsuario(Usuario u) {
        executor.execute(() -> usuarioDao.actualizarUsuario(u));
    }

    public void eliminarUsuario(Usuario u) {
        executor.execute(() -> usuarioDao.eliminarUsuario(u));
    }

    public LiveData<List<Usuario>> listarUsuarios() {
        return usuarioDao.listarUsuarios();
    }

    public LiveData<Usuario> buscarUsuarioPorId(int idUsuario) {
        return usuarioDao.buscarUsuarioPorId(idUsuario);
    }

    public LiveData<Usuario> buscarUsuarioPorUsername(String username) {
        return usuarioDao.buscarUsuarioPorUsername(username);
    }

    public LiveData<Usuario> buscarUsuarioPorCorreo(String correo) {
        return usuarioDao.buscarUsuarioPorCorreo(correo);
    }

    // Método síncrono para comprobaciones rápidas si fuera necesario
    public Usuario loginUsuario(String username) {
        return usuarioDao.loginUsuario(username);
    }


    public LiveData<UsuarioConSolicitudes> buscarUsuarioConSolicitudesPorId(int idUsuario) {
        return usuarioDao.buscarUsuarioConSolicitudesPorId(idUsuario);
    }

    public LiveData<UsuarioConPuntuaciones> buscarUsuarioConPuntuacionesPorId(int idUsuario) {
        return usuarioDao.buscarUsuarioConPuntuacionesPorId(idUsuario);
    }
}