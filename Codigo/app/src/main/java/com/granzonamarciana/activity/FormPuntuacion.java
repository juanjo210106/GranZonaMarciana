package com.granzonamarciana.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.granzonamarciana.R;
import com.granzonamarciana.entity.Gala;
import com.granzonamarciana.entity.Puntuacion;
import com.granzonamarciana.service.ConcursanteService;
import com.granzonamarciana.service.GalaService;
import com.granzonamarciana.service.PuntuacionService;

import java.time.LocalDateTime;

public class FormPuntuacion extends AppCompatActivity {

    private PuntuacionService puntuacionService;
    private ConcursanteService concursanteService;
    private GalaService galaService; // Necesario para validar fecha (RNF)

    private int idGala, idEspectador;
    private TextInputEditText etValor, etIdConcursante;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_puntuacion);

        // 1. Inicializar Servicios
        puntuacionService = new PuntuacionService(this);
        concursanteService = new ConcursanteService(this);
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

        // RNF: Valor entre 1 y 5
        if (valor < 1 || valor > 5) {
            etValor.setError("La puntuación debe estar entre 1 y 5");
            return;
        }

        int idConcursante = Integer.parseInt(idConcursanteStr);

        // Validamos paso a paso (Cadena de validaciones)
        validarExistenciaConcursante(valor, idConcursante);
    }

    // Paso 1: ¿Existe el concursante?
    private void validarExistenciaConcursante(int valor, int idConcursante) {
        concursanteService.buscarPorId(idConcursante).observe(this, concursante -> {
            // No eliminamos observer aquí porque buscarPorId suele devolver un LiveData continuo,
            // pero para una acción puntual como guardar, idealmente observaríamos una vez.
            // En este flujo simple, asumimos que 'concursante' llega una vez.

            if (concursante == null) {
                Toast.makeText(this, "Error: El concursante no existe", Toast.LENGTH_SHORT).show();
            } else {
                validarFechaGala(valor, idConcursante);
            }
        });
    }

    // Paso 2: RNF ¿La gala está dentro del plazo de 24h?
    private void validarFechaGala(int valor, int idConcursante) {
        galaService.buscarGalaPorId(idGala).observe(this, new Observer<Gala>() {
            @Override
            public void onChanged(Gala gala) {
                galaService.buscarGalaPorId(idGala).removeObserver(this); // Stop observing

                if (gala == null) {
                    Toast.makeText(FormPuntuacion.this, "Error: Gala no encontrada", Toast.LENGTH_SHORT).show();
                    return;
                }

                LocalDateTime fechaLimite = gala.getFechaRealizacion().plusHours(24);
                if (LocalDateTime.now().isAfter(fechaLimite)) {
                    Toast.makeText(FormPuntuacion.this, "El periodo de votación (24h) ha finalizado.", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(FormPuntuacion.this, "Ya has puntuado a este participante en esta gala.", Toast.LENGTH_LONG).show();
                } else {
                    // Si no hay voto previo, guardamos.
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
        Toast.makeText(this, "¡Voto registrado!", Toast.LENGTH_SHORT).show();
        finish();
    }
}