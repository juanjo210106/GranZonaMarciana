package com.granzonamarciana.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.granzonamarciana.entity.Actor;
import com.granzonamarciana.entity.Noticia;
import com.granzonamarciana.entity.TipoRol;
import com.granzonamarciana.service.ActorService;
import com.granzonamarciana.service.NoticiaService;

import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Clase para poblar la base de datos con datos iniciales.
 * Se ejecuta solo la primera vez que se inicia la aplicación.
 */
public class PopulateDB {

    private static final String PREFS_NAME = "PopulateDBPrefs";
    private static final String KEY_DB_POPULATED = "db_populated";

    private final Context context;
    private final ActorService actorService;
    private final NoticiaService noticiaService;
    private final ExecutorService executor;

    public PopulateDB(Context context) {
        this.context = context;
        this.actorService = new ActorService(context);
        this.noticiaService = new NoticiaService(context);
        this.executor = Executors.newSingleThreadExecutor();
    }

    /**
     * Verifica si la base de datos ya fue poblada y la puebla si es necesario.
     */
    public void populateIfNeeded() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean isPopulated = prefs.getBoolean(KEY_DB_POPULATED, false);

        if (!isPopulated) {
            populateDatabase();
            // Marcar como poblada
            prefs.edit().putBoolean(KEY_DB_POPULATED, true).apply();
        }
    }

    /**
     * Puebla la base de datos con datos iniciales.
     */
    private void populateDatabase() {
        executor.execute(() -> {
            // Crear usuarios iniciales
            crearUsuariosIniciales();

            // Crear noticias iniciales
            crearNoticiasIniciales();
        });
    }

    /**
     * Crea los usuarios iniciales del sistema.
     */
    private void crearUsuariosIniciales() {
        // 1. Administrador del sistema
        Actor admin = new Actor(
                "admin",
                BCrypt.hashpw("admin123", BCrypt.gensalt()),
                TipoRol.ADMINISTRADOR,
                "Admin",
                "Principal",
                "Sistema",
                "admin@granzona.com",
                "600000000",
                "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=400&h=400&fit=crop"
        );
        actorService.insertarActor(admin);

        // 2. Usuario Concursante de ejemplo
        Actor concursante = new Actor(
                "patica",
                BCrypt.hashpw("patica123", BCrypt.gensalt()),
                TipoRol.CONCURSANTE,
                "José Antonio",
                "Patica",
                "López",
                "patica@granzona.com",
                "611111111",
                "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400&h=400&fit=crop"
        );
        actorService.insertarActor(concursante);

        // 3. Usuario Espectador de ejemplo
        Actor espectador = new Actor(
                "viewer",
                BCrypt.hashpw("viewer123", BCrypt.gensalt()),
                TipoRol.ESPECTADOR,
                "María",
                "García",
                "Fernández",
                "viewer@granzona.com",
                "622222222",
                "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=400&h=400&fit=crop"
        );
        actorService.insertarActor(espectador);
    }

    /**
     * Crea las noticias iniciales del sistema.
     */
    private void crearNoticiasIniciales() {
        // Noticia 1: Bienvenida
        Noticia noticia1 = new Noticia(
                LocalDateTime.now().minusDays(2),
                "¡Bienvenidos a Gran Zona Marciana!",
                "Comenzamos una nueva temporada llena de emoción, risas y sorpresas. " +
                        "Los concursantes están listos para competir por el gran premio. " +
                        "No te pierdas ningún detalle de esta increíble aventura marciana.",
                "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=800&h=600&fit=crop"
        );
        noticiaService.insertarNoticia(noticia1);

        // Noticia 2: Inicio de Galas
        Noticia noticia2 = new Noticia(
                LocalDateTime.now().minusDays(1),
                "Las Galas Comienzan Este Viernes",
                "Este viernes a las 22:00 horas arranca oficialmente Gran Zona Marciana. " +
                        "Los concursantes se enfrentarán a su primera prueba y los espectadores " +
                        "podrán comenzar a votar por sus favoritos. ¡No te lo pierdas!",
                "https://images.unsplash.com/photo-1492684223066-81342ee5ff30?w=800&h=600&fit=crop"
        );
        noticiaService.insertarNoticia(noticia2);

        // Noticia 3: Inscripciones Abiertas
        Noticia noticia3 = new Noticia(
                LocalDateTime.now(),
                "¡Inscripciones Abiertas para la Próxima Edición!",
                "¿Quieres ser parte de Gran Zona Marciana? Las inscripciones para la " +
                        "próxima edición ya están abiertas. Envía tu solicitud y cuéntanos por qué " +
                        "deberías ser el próximo concursante. ¡Mucha suerte!",
                "https://images.unsplash.com/photo-1540575467063-178a50c2df87?w=800&h=600&fit=crop"
        );
        noticiaService.insertarNoticia(noticia3);
    }

    /**
     * Método para resetear la base de datos (útil para desarrollo/testing).
     * ADVERTENCIA: Esto borrará todos los datos y repoblará la base de datos.
     */
    public void resetDatabase() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(KEY_DB_POPULATED, false).apply();

        // Aquí podrías añadir lógica para limpiar la base de datos si lo necesitas
        // DatabaseHelper.getInstance(context).clearAllTables();

        populateIfNeeded();
    }

    /**
     * Verifica si la base de datos ya fue poblada.
     */
    public boolean isPopulated() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_DB_POPULATED, false);
    }
}
