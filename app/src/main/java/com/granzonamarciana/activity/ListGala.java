package com.granzonamarciana.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.granzonamarciana.R;
import com.granzonamarciana.adapter.GalaAdapter;
import com.granzonamarciana.entity.Gala;
import com.granzonamarciana.entity.TipoRol;
import com.granzonamarciana.service.GalaService;

import java.util.ArrayList;
import java.util.List;

public class ListGala extends AppCompatActivity {

    private GalaService galaService;
    private GalaAdapter galaAdapter;
    private String rol;
    private int idEdicionFiltro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_gala);

        // 1. Inicializar servicios y datos de sesión
        galaService = new GalaService(this);
        rol = getSharedPreferences("granzonaUser", MODE_PRIVATE).getString("rol", "");

        // Comprobar si venimos filtrando por una edición específica
        idEdicionFiltro = getIntent().getIntExtra("idEdicion", -1);

        // 2. Referencias de la UI
        ListView lvGalas = findViewById(R.id.lvGalas);
        TextView tvSinGalas = findViewById(R.id.tvSinGalas);
        TextView tvTitulo = findViewById(R.id.tvTituloGalas);
        MaterialButton btnCrearGala = findViewById(R.id.btnCrearGala);
        MaterialButton btnVolver = findViewById(R.id.btnVolver);

        // 3. Configurar el Adaptador
        galaAdapter = new GalaAdapter(this, new ArrayList<>());
        lvGalas.setAdapter(galaAdapter);
        lvGalas.setEmptyView(tvSinGalas);

        // 4. Lógica de visibilidad para el botón "Añadir" (Solo Admin)
        if (!rol.equals(TipoRol.ADMINISTRADOR.toString())) {
            btnCrearGala.setVisibility(View.GONE);
        }

        // 5. Cargar Datos (Filtrados o Generales)
        cargarDatos();

        // 6. Evento de clic en la lista (Menú de opciones)
        lvGalas.setOnItemClickListener((parent, view, position, id) -> {
            Gala galaSeleccionada = (Gala) parent.getItemAtPosition(position);
            mostrarMenuOpciones(galaSeleccionada);
        });

        // 7. Botones de acción
        btnCrearGala.setOnClickListener(v -> {
            Intent intent = new Intent(this, FormGala.class);
            if (idEdicionFiltro != -1) intent.putExtra("idEdicion", idEdicionFiltro);
            startActivity(intent);
        });

        btnVolver.setOnClickListener(v -> finish());
    }

    private void cargarDatos() {
        if (idEdicionFiltro != -1) {
            // Listar galas de una edición concreta
            galaService.listarGalasPorEdicion(idEdicionFiltro).observe(this, this::actualizarLista);
        } else {
            // Listar todas las galas del sistema
            galaService.listarGalas().observe(this, this::actualizarLista);
        }
    }

    private void actualizarLista(List<Gala> galas) {
        galaAdapter.clear();
        if (galas != null) {
            galaAdapter.addAll(galas);
        }
        galaAdapter.notifyDataSetChanged();
    }

    /**
     * Muestra un diálogo con opciones según el rol del usuario
     */
    private void mostrarMenuOpciones(Gala gala) {
        List<String> opciones = new ArrayList<>();

        // Definir opciones según ROL
        if (rol.equals(TipoRol.ADMINISTRADOR.toString())) {
            opciones.add("Editar Gala");
            opciones.add("Ver Resultados Globales");
        } else {
            opciones.add("Ver Resultados de la Gala");
        }

        // Convertir lista a Array para el Diálogo
        String[] opsArray = opciones.toArray(new String[0]);

        new AlertDialog.Builder(this, R.style.Theme_GranZonaMarciana_Dialog)
                .setTitle("Gala #" + gala.getId())
                .setItems(opsArray, (dialog, which) -> {
                    if (rol.equals(TipoRol.ADMINISTRADOR.toString())) {
                        if (which == 0) {
                            // Admin -> Editar
                            Intent i = new Intent(this, FormGala.class);
                            i.putExtra("gala", gala);
                            startActivity(i);
                        } else {
                            // Admin -> Resultados
                            verPuntuaciones(gala.getId());
                        }
                    } else {
                        // Usuario Normal -> Solo tiene la opción "Ver Resultados" (pos 0)
                        verPuntuaciones(gala.getId());
                    }
                })
                .setNegativeButton(R.string.btn_cancelar, null)
                .show();
    }

    private void verPuntuaciones(int idGala) {
        Intent intent = new Intent(this, ListPuntuacion.class);
        intent.putExtra("idGala", idGala);
        startActivity(intent);
    }
}