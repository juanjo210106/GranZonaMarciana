package com.granzonamarciana.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.granzonamarciana.R;
import com.granzonamarciana.adapter.PuntuacionAdapter;
import com.granzonamarciana.entity.Puntuacion;
import com.granzonamarciana.entity.TipoRol;
import com.granzonamarciana.service.PuntuacionService;

import java.util.ArrayList;
import java.util.List;

public class ListPuntuacion extends AppCompatActivity {

    private PuntuacionService puntuacionService;
    private PuntuacionAdapter puntuacionAdapter;
    private String rol;
    private int idUsuario, idGala;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_puntuacion);

        puntuacionService = new PuntuacionService(this);
        idUsuario = getSharedPreferences("granzonaUser", MODE_PRIVATE).getInt("id", -1);
        rol = getSharedPreferences("granzonaUser", MODE_PRIVATE).getString("rol", "");

        // El ID de la gala nos dirá qué queremos ver
        idGala = getIntent().getIntExtra("idGala", -1);

        TextView tvTitulo = findViewById(R.id.tvTituloPuntuaciones);
        TextView tvSinDatos = findViewById(R.id.tvSinPuntuaciones);
        TextView tvMedia = findViewById(R.id.tvMediaGala);
        MaterialCardView cardResumen = findViewById(R.id.cardResumen);
        ListView lv = findViewById(R.id.lvPuntuaciones);

        puntuacionAdapter = new PuntuacionAdapter(this, new ArrayList<>());
        lv.setAdapter(puntuacionAdapter);
        lv.setEmptyView(tvSinDatos);

        if (idGala != -1) {
            // CASO: Ver resultados de una gala (Abierto para Admin y Usuarios)
            tvTitulo.setText(getString(R.string.titulo_puntuaciones_admin));

            // Solo el Admin ve la tarjeta de la nota media (privilegio de admin)
            cardResumen.setVisibility(rol.equals(TipoRol.ADMINISTRADOR.toString()) ? View.VISIBLE : View.GONE);

            puntuacionService.listarPuntuacionesPorGala(idGala).observe(this, lista -> {
                actualizarLista(lista);
                if (rol.equals(TipoRol.ADMINISTRADOR.toString())) {
                    calcularYMostrarMedia(lista, tvMedia);
                }
            });

        } else {
            // CASO: El usuario viene desde el botón "Mis Puntuaciones" del Menú
            tvTitulo.setText(getString(R.string.titulo_mis_puntuaciones));
            cardResumen.setVisibility(View.GONE);

            puntuacionService.listarPuntuacionesPorEspectador(idUsuario).observe(this, this::actualizarLista);
        }

        findViewById(R.id.btnVolverPuntuacion).setOnClickListener(v -> finish());
    }

    private void actualizarLista(List<Puntuacion> lista) {
        puntuacionAdapter.clear();
        if (lista != null) {
            puntuacionAdapter.addAll(lista);
        }
        puntuacionAdapter.notifyDataSetChanged();
    }

    private void calcularYMostrarMedia(List<Puntuacion> lista, TextView tvMedia) {
        if (lista == null || lista.isEmpty()) {
            tvMedia.setText(getString(R.string.label_media_total, 0.0f));
            return;
        }
        float suma = 0;
        for (Puntuacion p : lista) suma += p.getValor();
        float media = suma / lista.size();
        tvMedia.setText(getString(R.string.label_media_total, media));
    }
}