package com.granzonamarciana.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.granzonamarciana.entity.Usuario;
import com.granzonamarciana.entity.Administrador;
import com.granzonamarciana.entity.Concursante; // Añadido
import com.granzonamarciana.entity.Espectador;   // Añadido
import com.granzonamarciana.entity.Edicion;
import com.granzonamarciana.entity.Gala;
import com.granzonamarciana.entity.Noticia;
import com.granzonamarciana.entity.Puntuacion;
import com.granzonamarciana.entity.Solicitud;

import com.granzonamarciana.dao.UsuarioDao;
import com.granzonamarciana.dao.AdministradorDao;
import com.granzonamarciana.dao.ConcursanteDao; // Añadido
import com.granzonamarciana.dao.EspectadorDao;  // Añadido
import com.granzonamarciana.dao.EdicionDao;
import com.granzonamarciana.dao.GalaDao;
import com.granzonamarciana.dao.NoticiaDao;
import com.granzonamarciana.dao.PuntuacionDao;
import com.granzonamarciana.dao.SolicitudDao;

@Database(entities = {
        Usuario.class,
        Administrador.class,
        Concursante.class,   // 1. Registrar entidad Concursante
        Espectador.class,    // 2. Registrar entidad Espectador
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
                    .allowMainThreadQueries()
                    .build();
        }
        return instanciaBD;
    }

    // Métodos abstractos para acceder a los DAOs
    public abstract UsuarioDao usuarioDao();
    public abstract AdministradorDao administradorDao();
    public abstract ConcursanteDao concursanteDao(); // 3. Añadir este método para ConcursanteService
    public abstract EspectadorDao espectadorDao();   // 4. Añadir este método para EspectadorService
    public abstract EdicionDao edicionDao();
    public abstract GalaDao galaDao();
    public abstract NoticiaDao noticiaDao();
    public abstract PuntuacionDao puntuacionDao();
    public abstract SolicitudDao solicitudDao();
}