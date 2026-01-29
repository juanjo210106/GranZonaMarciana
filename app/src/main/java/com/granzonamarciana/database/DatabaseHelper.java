package com.granzonamarciana.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

// Entidades (Importamos Usuario en lugar de las clases individuales)
import com.granzonamarciana.entity.Usuario;
import com.granzonamarciana.entity.Edicion;
import com.granzonamarciana.entity.Gala;
import com.granzonamarciana.entity.Noticia;
import com.granzonamarciana.entity.Puntuacion;
import com.granzonamarciana.entity.Solicitud;

// DAOs
import com.granzonamarciana.dao.UsuarioDao;
import com.granzonamarciana.dao.EdicionDao;
import com.granzonamarciana.dao.GalaDao;
import com.granzonamarciana.dao.NoticiaDao;
import com.granzonamarciana.dao.PuntuacionDao;
import com.granzonamarciana.dao.SolicitudDao;

@Database(entities = {
        Usuario.class,    // Entidad única para Admin, Concursante y Espectador
        Edicion.class,
        Gala.class,
        Noticia.class,
        Puntuacion.class,
        Solicitud.class
}, version = 1)
@TypeConverters({LocalDateConverter.class, LocalDateTimeConverter.class, EstadoSolicitudConverter.class})
public abstract class DatabaseHelper extends RoomDatabase {

    private static DatabaseHelper instanciaBD;

    public static synchronized DatabaseHelper getInstance(Context c) {
        if (instanciaBD == null) {
            instanciaBD = Room.databaseBuilder(c.getApplicationContext(),
                            DatabaseHelper.class, "granzonamarciana")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()         // Permite consultas rápidas en el hilo principal
                    .build();
        }
        return instanciaBD;
    }

    // Métodos abstractos para acceder a los DAOs
    public abstract UsuarioDao usuarioDao();
    public abstract EdicionDao edicionDao();
    public abstract GalaDao galaDao();
    public abstract NoticiaDao noticiaDao();
    public abstract PuntuacionDao puntuacionDao();
    public abstract SolicitudDao solicitudDao();
}