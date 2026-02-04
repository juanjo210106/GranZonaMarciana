package com.granzonamarciana.database;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.granzonamarciana.entity.Actor;
import com.granzonamarciana.entity.Edicion;
import com.granzonamarciana.entity.Gala;
import com.granzonamarciana.entity.Noticia;
import com.granzonamarciana.entity.Puntuacion;
import com.granzonamarciana.entity.Solicitud;

import com.granzonamarciana.dao.ActorDao;
import com.granzonamarciana.dao.EdicionDao;
import com.granzonamarciana.dao.GalaDao;
import com.granzonamarciana.dao.NoticiaDao;
import com.granzonamarciana.dao.PuntuacionDao;
import com.granzonamarciana.dao.SolicitudDao;

@Database(entities = {
        Actor.class,
        Edicion.class,
        Gala.class,
        Noticia.class,
        Puntuacion.class,
        Solicitud.class
}, version = 7)  // IMPORTANTE: Incrementar versión
@TypeConverters({LocalDateConverter.class, LocalDateTimeConverter.class, EstadoSolicitudConverter.class})
public abstract class DatabaseHelper extends RoomDatabase {

    private static DatabaseHelper instanciaBD;

    public static synchronized DatabaseHelper getInstance(Context c) {
        if (instanciaBD == null) {
            instanciaBD = Room.databaseBuilder(c.getApplicationContext(),
                            DatabaseHelper.class, "granzonamarciana")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .addCallback(new RoomDatabase.Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                        }
                    })
                    .build();
        }
        return instanciaBD;
    }

    // Métodos abstractos para acceder a los DAOs
    public abstract ActorDao actorDao();
    public abstract EdicionDao edicionDao();
    public abstract GalaDao galaDao();
    public abstract NoticiaDao noticiaDao();
    public abstract PuntuacionDao puntuacionDao();
    public abstract SolicitudDao solicitudDao();
}