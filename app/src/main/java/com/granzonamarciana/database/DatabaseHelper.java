package com.granzonamarciana.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

// Importa TODAS las entidades aquí
import com.granzonamarciana.entity.Actor;
import com.granzonamarciana.entity.Edicion;
import com.granzonamarciana.entity.Gala;
import com.granzonamarciana.entity.Noticia;
import com.granzonamarciana.entity.Puntuacion;
import com.granzonamarciana.entity.Solicitud;

// Declaración de los DAOs
import com.granzonamarciana.dao.*;

@Database(entities = {
        Actor.class,
        Edicion.class,
        Gala.class,
        Solicitud.class,
        Noticia.class,
        Puntuacion.class
}, version = 1, exportSchema = false)
@TypeConverters({LocalDateConverter.class, LocalDateTimeConverter.class})
public abstract class DatabaseHelper extends RoomDatabase {

    // Instancia BD Singleton
    private static DatabaseHelper instanciaBD;

    public static synchronized DatabaseHelper getInstance(Context c) {
        if (instanciaBD == null) {
            instanciaBD = Room.databaseBuilder(c.getApplicationContext(),
                            DatabaseHelper.class, "granzonamarciana")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instanciaBD;
    }

    // Definición de métodos abstractos para los DAOs
    public abstract ActorDao actorDao();
    public abstract EdicionDao edicionDao();
    public abstract GalaDao galaDao();
    public abstract SolicitudDao solicitudDao();
    public abstract NoticiaDao noticiaDao();
    public abstract PuntuacionDao puntuacionDao();
}