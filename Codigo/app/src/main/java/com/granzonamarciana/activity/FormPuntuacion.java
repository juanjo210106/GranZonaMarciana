package com.granzonamarciana.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.granzonamarciana.R;
import com.granzonamarciana.entity.Actor;
import com.granzonamarciana.entity.Gala;
import com.granzonamarciana.entity.Puntuacion;
import com.granzonamarciana.entity.TipoRol;
import com.granzonamarciana.service.ActorService;
import com.granzonamarciana.service.GalaService;
import com.granzonamarciana.service.PuntuacionService;

import java.time.LocalDateTime;

public class FormPuntuacion extends AppCompatActivity {

    private PuntuacionService puntuacionService;
    private ActorService actorService;
    private GalaService galaService;

    private int idGala, idEspectador;
    private TextInputEditText etValor, etIdConcursante;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_puntuacion);

        // 1. Inicializar Servicios
        puntuacionService = new PuntuacionService(this);
        actorService = new ActorService(this);
        galaService = new GalaService(this);

        // 2. Recuperar datos
        idGala = getIntent().getIntExtra("idGala", -1);
        idEspectador = getSharedPreferences("granzonaUser", MODE_PRIVATE).getInt("id", -1);

        etValor = findViewById(R.id.etValorVoto);
        etIdConcursante = findViewById(R.id.etIdConcursanteVoto);
        MaterialButton btnGuardar = findViewById(R.id.btnGuardarVoto);

        // 3. Listener
        btnGuardar.setOnClickListener(v -> procesarVoto());
    }

    private void procesarVoto() {
        String valorStr = etValor.getText().toString().trim();
        String idConcursanteStr = etIdConcursante.getText().toString().trim();

        if (TextUtils.isEmpty(valorStr) || TextUtils.isEmpty(idConcursanteStr)) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        int valor;
        try {
            valor = Integer.parseInt(valorStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "El valor debe ser numérico", Toast.LENGTH_SHORT).show();
            return;
        }

        // RNF: Valor entre 1 y 10
        if (valor < 1 || valor > 10) {
            etValor.setError("La puntuación debe estar entre 1 y 10");
            Toast.makeText(this, "La puntuación debe estar entre 1 y 10", Toast.LENGTH_LONG).show();
            return;
        }

        int idConcursante;
        try {
            idConcursante = Integer.parseInt(idConcursanteStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "El ID del concursante debe ser numérico", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validamos paso a paso (Cadena de validaciones)
        validarExistenciaConcursante(valor, idConcursante);
    }

    // Paso 1: ¿Existe el concursante Y tiene rol CONCURSANTE?
    private void validarExistenciaConcursante(int valor, int idConcursante) {
        actorService.buscarPorId(idConcursante).observe(this, new Observer<Actor>() {
            @Override
            public void onChanged(Actor actor) {
                // Removemos el observer para que solo se ejecute una vez
                actorService.buscarPorId(idConcursante).removeObserver(this);

                if (actor == null) {
                    Toast.makeText(FormPuntuacion.this,
                            "Error: No existe ningún usuario con ID " + idConcursante,
                            Toast.LENGTH_LONG).show();
                } else if (actor.getRol() != TipoRol.CONCURSANTE) {
                    Toast.makeText(FormPuntuacion.this,
                            "Error: El usuario con ID " + idConcursante + " no es un concursante. Es un " + actor.getRol(),
                            Toast.LENGTH_LONG).show();
                } else {
                    // El actor existe y es concursante, continuamos
                    validarFechaGala(valor, idConcursante);
                }
            }
        });
    }

    // Paso 2: RNF ¿La gala está dentro del plazo de 24h?
    private void validarFechaGala(int valor, int idConcursante) {
        galaService.buscarGalaPorId(idGala).observe(this, new Observer<Gala>() {
            @Override
            public void onChanged(Gala gala) {
                galaService.buscarGalaPorId(idGala).removeObserver(this);

                if (gala == null) {
                    Toast.makeText(FormPuntuacion.this, "Error: Gala no encontrada", Toast.LENGTH_SHORT).show();
                    return;
                }

                // RNF: Solo se puede votar en las 24 horas posteriores al inicio de la gala
                LocalDateTime fechaLimite = gala.getFechaRealizacion().plusHours(24);
                LocalDateTime ahora = LocalDateTime.now();

                if (ahora.isBefore(gala.getFechaRealizacion())) {
                    Toast.makeText(FormPuntuacion.this,
                            "Error: La gala aún no ha comenzado. Comienza el " +
                                    gala.getFechaRealizacion().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if (ahora.isAfter(fechaLimite)) {
                    Toast.makeText(FormPuntuacion.this,
                            "Error: El periodo de votación (24h) ha finalizado. Finalizó el " +
                                    fechaLimite.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                            Toast.LENGTH_LONG).show();
                    return;
                }

                // Si la fecha es correcta, pasamos a validar duplicados
                validarVotoUnico(valor, idConcursante);
            }
        });
    }

    // Paso 3: RNF ¿Ya he votado a este concursante en esta gala?
    private void validarVotoUnico(int valor, int idConcursante) {
        puntuacionService.buscarVoto(idEspectador, idGala, idConcursante).observe(this, new Observer<Puntuacion>() {
            @Override
            public void onChanged(Puntuacion votoExistente) {
                puntuacionService.buscarVoto(idEspectador, idGala, idConcursante).removeObserver(this);

                if (votoExistente != null) {
                    Toast.makeText(FormPuntuacion.this,
                            "Error: Ya has puntuado a este participante en esta gala con " + votoExistente.getValor() + " puntos.",
                            Toast.LENGTH_LONG).show();
                } else {
                    // Si no hay voto previo, guardamos
                    guardarPuntuacionFinal(valor, idConcursante);
                }
            }
        });
    }

    private void guardarPuntuacionFinal(int valor, int idConcursante) {
        Puntuacion p = new Puntuacion();
        p.setValor(valor);
        p.setIdEspectador(idEspectador);
        p.setIdConcursante(idConcursante);
        p.setIdGala(idGala);

        puntuacionService.insertarPuntuacion(p);
        Toast.makeText(this, "¡Voto registrado correctamente! Has dado " + valor + " puntos.", Toast.LENGTH_SHORT).show();
        finish();
    }
}