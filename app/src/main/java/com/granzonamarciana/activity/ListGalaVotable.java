package com.granzonamarciana.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.granzonamarciana.R;
import com.granzonamarciana.adapter.GalaAdapter;
import com.granzonamarciana.entity.Gala;
import com.granzonamarciana.service.GalaService;
import java.util.ArrayList;

public class ListGalaVotable extends AppCompatActivity {

    private GalaService galaService;
    private GalaAdapter galaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_gala_votable);

        // Configurar ListView y Adapter (reutilizamos tu GalaAdapter)
        ListView lv = findViewById(R.id.lvGalasVotables);
        galaAdapter = new GalaAdapter(this, new ArrayList<>());
        lv.setAdapter(galaAdapter);

        // Cargar galas desde el service
        galaService = new GalaService(this);
        galaService.listarGalas().observe(this, galas -> {
            galaAdapter.clear();
            if (galas != null) {
                galaAdapter.addAll(galas);
            }
            galaAdapter.notifyDataSetChanged();
        });

        // EVENTO CLAVE: Al pulsar, vamos al formulario de votación
        lv.setOnItemClickListener((parent, view, position, id) -> {
            Gala galaSeleccionada = galaAdapter.getItem(position);

            Intent i = new Intent(ListGalaVotable.this, FormPuntuacion.class);
            // Pasamos el ID para que el formulario sepa a qué gala votar
            i.putExtra("idGala", galaSeleccionada.getId());
            startActivity(i);
        });

        findViewById(R.id.btnVolverSeleccion).setOnClickListener(v -> finish());
    }
}