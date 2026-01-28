package com.granzonamarciana.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.granzonamarciana.entity.Administrador;
import com.granzonamarciana.entity.Concursante;
import com.granzonamarciana.entity.Espectador;
import com.granzonamarciana.entity.Edicion;
import com.granzonamarciana.entity.Gala;
import com.granzonamarciana.entity.Noticia;
import com.granzonamarciana.entity.Puntuacion;
import com.granzonamarciana.entity.Solicitud;

// Importamos los DAOs
import com.granzonamarciana.dao.*;


@Database(entities = {
        Administrador.class,
        Concursante.class,
        Espectador.class,
        Edicion.class,
        Gala.class,
        Noticia.class,
        Puntuacion.class,
        Solicitud.class
}, version = 1)
@TypeConverters({LocalDateConverter.class, LocalDateTimeConverter.class})
public abstract class DatabaseHelper extends RoomDatabase {

    private static DatabaseHelper instanciaBD;

    public static synchronized DatabaseHelper getInstance(Context c) {
        if (instanciaBD == null) {
            instanciaBD = Room.databaseBuilder(c.getApplicationContext(),
                            DatabaseHelper.class, "granzonamarciana")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries() // IMPORTANTE: Estilo de tu maestro
                    .build();
        }
        return instanciaBD;
    }

    // Métodos abstractos para los DAOs (nombres exactos)
    public abstract AdministradorDao administradorDao();
    public abstract ConcursanteDao concursanteDao();
    public abstract EspectadorDao espectadorDao();
    public abstract EdicionDao edicionDao();
    public abstract GalaDao galaDao();
    public abstract NoticiaDao noticiaDao();
    public abstract PuntuacionDao puntuacionDao();
    public abstract SolicitudDao solicitudDao();
}