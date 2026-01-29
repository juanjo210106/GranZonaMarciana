package com.granzonamarciana.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.granzonamarciana.R;
import com.granzonamarciana.entity.Puntuacion;
import com.granzonamarciana.service.PuntuacionService;

public class FormPuntuacion extends AppCompatActivity {

    private PuntuacionService puntuacionService;
    private int idGala, idEspectador;
    private EditText etValor, etIdConcursante;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_puntuacion);

        puntuacionService = new PuntuacionService(this);

        // 1. Obtener IDs necesarios (Gala del intent, Espectador de la sesión)
        idGala = getIntent().getIntExtra("idGala", -1);
        idEspectador = getSharedPreferences("granzonaUser", MODE_PRIVATE).getInt("id", -1);

        // 2. Vincular campos del XML
        etValor = findViewById(R.id.etValorVoto);
        etIdConcursante = findViewById(R.id.etIdConcursanteVoto);

        // 3. Botón Guardar (Estilo Maestro: validación y service)
        findViewById(R.id.btnGuardarVoto).setOnClickListener(v -> {
            String valorStr = etValor.getText().toString().trim();
            String idConcursanteStr = etIdConcursante.getText().toString().trim();

            if (TextUtils.isEmpty(valorStr) || TextUtils.isEmpty(idConcursanteStr)) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // CREAR OBJETO PUNTUACIÓN (Usando tus campos: valor, idEspectador, idConcursante, idGala)
            Puntuacion p = new Puntuacion();
            p.setValor(Integer.parseInt(valorStr));
            p.setIdEspectador(idEspectador);
            p.setIdConcursante(Integer.parseInt(idConcursanteStr));
            p.setIdGala(idGala);

            puntuacionService.insertarPuntuacion(p);
            Toast.makeText(this, "¡Puntuación guardada!", Toast.LENGTH_SHORT).show();
            finish(); // Volver atrás
        });
    }
}