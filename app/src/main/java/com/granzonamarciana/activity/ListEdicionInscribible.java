package com.granzonamarciana.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.granzonamarciana.R;
import com.granzonamarciana.adapter.EdicionAdapter;
import com.granzonamarciana.entity.Edicion;
import com.granzonamarciana.service.EdicionService;

import java.time.LocalDate;
import java.util.ArrayList;

public class ListEdicionInscribible extends AppCompatActivity {

    private EdicionService edicionService;
    private EdicionAdapter edicionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Reutilizamos el layout de listado de galas para ahorrar código, ya que es idéntico
        setContentView(R.layout.activity_list_gala);

        // 1. Personalizar la UI para "Inscripciones"
        TextView tvTitulo = findViewById(R.id.tvTituloGalas);
        tvTitulo.setText(R.string.titulo_elegir_edicion);

        // El botón de crear gala no tiene sentido aquí, lo ocultamos
        findViewById(R.id.btnCrearGala).setVisibility(View.GONE);

        // 2. Configurar Lista y Adaptador
        ListView lvEdiciones = findViewById(R.id.lvGalas);
        edicionAdapter = new EdicionAdapter(this, new ArrayList<>());
        lvEdiciones.setAdapter(edicionAdapter);

        // 3. Cargar datos desde el Service
        edicionService = new EdicionService(this);
        edicionService.listarEdiciones().observe(this, ediciones -> {
            edicionAdapter.clear();
            if (ediciones != null) {
                edicionAdapter.addAll(ediciones);
            }
            edicionAdapter.notifyDataSetChanged();
        });

        // 4. Lógica de selección y validación de fecha
        lvEdiciones.setOnItemClickListener((parent, view, position, id) -> {
            Edicion edicionSeleccionada = (Edicion) parent.getItemAtPosition(position);

            // COMPROBACIÓN: ¿La edición ya ha terminado?
            if (edicionSeleccionada.getFechaFin().isBefore(LocalDate.now())) {
                // Si la fecha de fin es anterior a hoy, bloqueamos
                Toast.makeText(this, R.string.error_edicion_finalizada, Toast.LENGTH_LONG).show();
            } else {
                // Si es válida, vamos al formulario de inscripción pasando la ID
                Intent intent = new Intent(this, FormSolicitud.class);
                intent.putExtra("idEdicion", edicionSeleccionada.getId());
                startActivity(intent);
                finish(); // Cerramos esta lista para que no pueda volver atrás fácilmente
            }
        });

        // 5. Botón Volver
        findViewById(R.id.btnVolver).setOnClickListener(v -> finish());
    }
}