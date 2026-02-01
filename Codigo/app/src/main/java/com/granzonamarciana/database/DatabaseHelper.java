package com.granzonamarciana.database;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.granzonamarciana.entity.Usuario;
import com.granzonamarciana.entity.Administrador;
import com.granzonamarciana.entity.Concursante;
import com.granzonamarciana.entity.Espectador;
import com.granzonamarciana.entity.Edicion;
import com.granzonamarciana.entity.Gala;
import com.granzonamarciana.entity.Noticia;
import com.granzonamarciana.entity.Puntuacion;
import com.granzonamarciana.entity.Solicitud;
import com.granzonamarciana.entity.TipoRol;

import com.granzonamarciana.dao.UsuarioDao;
import com.granzonamarciana.dao.AdministradorDao;
import com.granzonamarciana.dao.ConcursanteDao;
import com.granzonamarciana.dao.EspectadorDao;
import com.granzonamarciana.dao.EdicionDao;
import com.granzonamarciana.dao.GalaDao;
import com.granzonamarciana.dao.NoticiaDao;
import com.granzonamarciana.dao.PuntuacionDao;
import com.granzonamarciana.dao.SolicitudDao;

import org.mindrot.jbcrypt.BCrypt;
import java.util.concurrent.Executors;

@Database(entities = {
        Usuario.class,
        Administrador.class,
        Concursante.class,
        Espectador.class,
        Edicion.class,
        Gala.class,
        Noticia.class,
        Puntuacion.class,
        Solicitud.class
}, version = 4)
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
                            // Precarga del Administrador Inicial (admin / admin123)
                            Executors.newSingleThreadExecutor().execute(() -> {
                                // Encriptamos la contraseña "admin123"
                                String passHash = BCrypt.hashpw("admin123", BCrypt.gensalt());

                                // Creamos el objeto administrador
                                Usuario adminInicial = new Usuario(
                                        "admin",
                                        passHash,
                                        TipoRol.ADMINISTRADOR,
                                        "Admin",
                                        "Principal",
                                        "Sistema",
                                        "admin@granzona.com",
                                        "600000000",
                                        "" // URL imagen vacía
                                );

                                // Insertamos a través del DAO de Usuario para que el Login lo detecte
                                instanciaBD.usuarioDao().insertarUsuario(adminInicial);
                            });
                        }
                    })
                    .build();
        }
        return instanciaBD;
    }

    // Métodos abstractos para acceder a los DAOs
    public abstract UsuarioDao usuarioDao();
    public abstract AdministradorDao administradorDao();
    public abstract ConcursanteDao concursanteDao();
    public abstract EspectadorDao espectadorDao();
    public abstract EdicionDao edicionDao();
    public abstract GalaDao galaDao();
    public abstract NoticiaDao noticiaDao();
    public abstract PuntuacionDao puntuacionDao();
    public abstract SolicitudDao solicitudDao();
}