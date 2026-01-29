package com.granzonamarciana.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.google.android.material.button.MaterialButton;
import com.granzonamarciana.R;
import com.granzonamarciana.adapter.GalaAdapter;
import com.granzonamarciana.entity.Edicion;
import com.granzonamarciana.entity.Gala;
import com.granzonamarciana.service.GalaService;

import java.util.ArrayList;
import java.util.List;

public class ListGala extends AppCompatActivity {

    private GalaService galaService;
    private GalaAdapter galaAdapter;
    private Edicion edicionActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_gala);

        galaService = new GalaService(ListGala.this);

        // Recuperamos la edición para saber qué galas mostrar (Estilo maestro)
        edicionActual = (Edicion) getIntent().getSerializableExtra("edicion");

        ListView lvGalas = findViewById(R.id.lvGalas);
        TextView tvSinGalas = findViewById(R.id.tvSinGalas);
        MaterialButton btnCrearGala = findViewById(R.id.btnCrearGala);
        MaterialButton btnVolver = findViewById(R.id.btnVolver);

        // Se muestra si no hay galas (Estilo maestro)
        lvGalas.setEmptyView(tvSinGalas);

        galaAdapter = new GalaAdapter(ListGala.this, new ArrayList<>());
        lvGalas.setAdapter(galaAdapter);

        // Listar galas filtradas por la edición actual
        if (edicionActual != null) {
            galaService.listarGalasPorEdicion(edicionActual.getId()).observe(this, new Observer<List<Gala>>() {
                @Override
                public void onChanged(List<Gala> galas) {
                    galaAdapter.clear();
                    galaAdapter.addAll(galas);
                    galaAdapter.notifyDataSetChanged();
                }
            });
        }

        // Editar una gala existente
        lvGalas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Gala galaSeleccionada = (Gala) parent.getItemAtPosition(position);
                Intent i = new Intent(ListGala.this, FormGala.class);
                i.putExtra("gala", galaSeleccionada);
                i.putExtra("edicion", edicionActual); // Pasamos también la edición para validar fechas
                startActivity(i);
            }
        });

        // Crear una nueva gala
        btnCrearGala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListGala.this, FormGala.class);
                i.putExtra("edicion", edicionActual); // Pasamos la edición para saber a cuál pertenece
                startActivity(i);
            }
        });

        // Volver
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}