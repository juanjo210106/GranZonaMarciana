package com.granzonamarciana.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.granzonamarciana.R;
import com.granzonamarciana.entity.Puntuacion;
import com.granzonamarciana.service.PuntuacionService;
import com.granzonamarciana.service.ConcursanteService;

public class FormPuntuacion extends AppCompatActivity {

    private PuntuacionService puntuacionService;
    private ConcursanteService concursanteService;
    private int idGala, idEspectador;
    private TextInputEditText etValor, etIdConcursante;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_puntuacion);

        puntuacionService = new PuntuacionService(this);
        concursanteService = new ConcursanteService(this);

        idGala = getIntent().getIntExtra("idGala", -1);
        idEspectador = getSharedPreferences("granzonaUser", MODE_PRIVATE).getInt("id", -1);

        etValor = findViewById(R.id.etValorVoto);
        etIdConcursante = findViewById(R.id.etIdConcursanteVoto);
        MaterialButton btnGuardar = findViewById(R.id.btnGuardarVoto);

        btnGuardar.setOnClickListener(v -> {
            String valorStr = etValor.getText().toString().trim();
            String idConcursanteStr = etIdConcursante.getText().toString().trim();

            if (TextUtils.isEmpty(valorStr) || TextUtils.isEmpty(idConcursanteStr)) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            int idConcursante = Integer.parseInt(idConcursanteStr);


            concursanteService.buscarPorId(idConcursante).observe(this, concursante -> {
                if (concursante == null) {
                    Toast.makeText(this, "Error: El ID de concursante no existe", Toast.LENGTH_LONG).show();
                } else {
                    // Si existe, procedemos a guardar
                    Puntuacion p = new Puntuacion();
                    p.setValor(Integer.parseInt(valorStr));
                    p.setIdEspectador(idEspectador);
                    p.setIdConcursante(idConcursante);
                    p.setIdGala(idGala);

                    puntuacionService.insertarPuntuacion(p);
                    Toast.makeText(this, "¡Voto registrado para " + concursante.getNombre() + "!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        });
    }
}