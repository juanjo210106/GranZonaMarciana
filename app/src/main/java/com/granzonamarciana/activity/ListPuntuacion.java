package com.granzonamarciana.activity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import com.granzonamarciana.R;
import com.granzonamarciana.adapter.PuntuacionAdapter;
import com.granzonamarciana.entity.Puntuacion;
import com.granzonamarciana.service.PuntuacionService;
import java.util.ArrayList;
import java.util.List;

public class ListPuntuacion extends AppCompatActivity {

    private PuntuacionService puntuacionService;
    private PuntuacionAdapter puntuacionAdapter;
    private int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_puntuacion);

        // 1. Recuperar sesión (Estilo Maestro)
        idUsuario = getSharedPreferences("granzonaUser", MODE_PRIVATE).getInt("id", -1);

        // 2. Vincular UI
        ListView lv = findViewById(R.id.lvPuntuaciones);
        TextView tvVacio = findViewById(R.id.tvSinPuntuaciones);
        lv.setEmptyView(tvVacio);

        // 3. Configurar el adaptador que ya tienes
        puntuacionAdapter = new PuntuacionAdapter(this, new ArrayList<>());
        lv.setAdapter(puntuacionAdapter);

        // 4. Cargar datos
        puntuacionService = new PuntuacionService(this);

        // Aquí usamos el método del service para ver las puntuaciones
        // Si no tienes uno específico para el usuario, usamos el general por ahora:
        puntuacionService.listarPuntuaciones().observe(this, new Observer<List<Puntuacion>>() {
            @Override
            public void onChanged(List<Puntuacion> puntuaciones) {
                puntuacionAdapter.clear();
                if (puntuaciones != null) {
                    puntuacionAdapter.addAll(puntuaciones);
                }
                puntuacionAdapter.notifyDataSetChanged();
            }
        });

        findViewById(R.id.btnVolverPuntuacion).setOnClickListener(v -> finish());
    }
}