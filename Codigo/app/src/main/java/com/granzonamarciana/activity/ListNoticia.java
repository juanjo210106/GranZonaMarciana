package com.granzonamarciana.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.granzonamarciana.R;
import com.granzonamarciana.adapter.NoticiaAdapter;
import com.granzonamarciana.entity.Noticia;
import com.granzonamarciana.entity.TipoRol;
import com.granzonamarciana.service.NoticiaService;

import java.util.ArrayList;

// Implementamos el Listener del adaptador para el borrado
public class ListNoticia extends AppCompatActivity implements NoticiaAdapter.OnNoticiaDeleteListener {

    private NoticiaService noticiaService;
    private NoticiaAdapter noticiaAdapter;
    private String rolSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_noticia);

        noticiaService = new NoticiaService(this);

        SharedPreferences prefs = getSharedPreferences("granzonaUser", MODE_PRIVATE);
        rolSesion = prefs.getString("rol", "");

        ListView lvNoticias = findViewById(R.id.lvNoticias);
        TextView tvSinNoticias = findViewById(R.id.tvSinNoticias);
        MaterialButton btnCrearNoticia = findViewById(R.id.btnCrearNoticia);
        MaterialButton btnVolver = findViewById(R.id.btnVolver);

        if (!rolSesion.equals(TipoRol.ADMINISTRADOR.toString())) {
            btnCrearNoticia.setVisibility(View.GONE);
        }

        lvNoticias.setEmptyView(tvSinNoticias);

        // SOLUCIÓN AL ERROR: Pasamos 'this' como tercer argumento (el listener)
        noticiaAdapter = new NoticiaAdapter(this, new ArrayList<>(), this);
        lvNoticias.setAdapter(noticiaAdapter);

        noticiaService.listarNoticias().observe(this, noticias -> {
            noticiaAdapter.clear();
            if (noticias != null) {
                noticiaAdapter.addAll(noticias);
            }
            noticiaAdapter.notifyDataSetChanged();
        });

        lvNoticias.setOnItemClickListener((parent, view, position, id) -> {
            Noticia noticiaSeleccionada = (Noticia) parent.getItemAtPosition(position);
            Intent i = new Intent(ListNoticia.this, FormNoticia.class);
            i.putExtra("noticia", noticiaSeleccionada);
            startActivity(i);
        });

        btnCrearNoticia.setOnClickListener(v -> startActivity(new Intent(this, FormNoticia.class)));
        btnVolver.setOnClickListener(v -> finish());
    }

    // Método que se ejecuta cuando se pulsa la papelera en el adaptador
    @Override
    public void onNoticiaDelete(Noticia noticia) {
        noticiaService.eliminarNoticia(noticia);
        Toast.makeText(this, "Noticia eliminada", Toast.LENGTH_SHORT).show();
    }
}