package com.granzonamarciana.service;

import android.content.Context;
import androidx.lifecycle.LiveData;
import com.granzonamarciana.dao.ActorDao;
import com.granzonamarciana.database.DatabaseHelper;
import com.granzonamarciana.entity.Actor;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ActorService {

    private final ActorDao actorDao;
    private final ExecutorService executor;

    public ActorService(Context context) {
        DatabaseHelper db = DatabaseHelper.getInstance(context);
        this.actorDao = db.actorDao();
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void insertarActor(Actor a) {
        executor.execute(() -> actorDao.insertarActor(a));
    }

    public void actualizarActor(Actor a) {
        executor.execute(() -> actorDao.actualizarActor(a));
    }

    public void eliminarActor(Actor a) {
        executor.execute(() -> actorDao.eliminarActor(a));
    }

    // Devuelve todos los usuarios (concursantes y espectadores) para la gestión del administrador
    public LiveData<List<Actor>> listarUsuariosSistema() {
        return actorDao.listarUsuariosSistema();
    }

    public LiveData<Actor> buscarPorId(int id) {
        return actorDao.buscarPorId(id);
    }

    public LiveData<Actor> buscarPorUsername(String username) {
        return actorDao.buscarPorUsername(username);
    }

    public LiveData<List<Actor>> listarTodos() {
        return actorDao.listarTodos();
    }

    // Para operaciones que requieran el objeto inmediatamente sin LiveData
    public Actor loginSync(String username) {
        return actorDao.loginSync(username);
    }
}