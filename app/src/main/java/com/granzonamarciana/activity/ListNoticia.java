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
import com.granzonamarciana.adapter.NoticiaAdapter;
import com.granzonamarciana.entity.Noticia;
import com.granzonamarciana.service.NoticiaService;

import java.util.ArrayList;
import java.util.List;

public class ListNoticia extends AppCompatActivity {

    private NoticiaService noticiaService;
    private NoticiaAdapter noticiaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_noticia);

        noticiaService = new NoticiaService(ListNoticia.this);

        ListView lvNoticias = findViewById(R.id.lvNoticias);
        TextView tvSinNoticias = findViewById(R.id.tvSinNoticias);
        MaterialButton btnCrearNoticia = findViewById(R.id.btnCrearNoticia);
        MaterialButton btnVolver = findViewById(R.id.btnVolver);

        // Se configura la vista vacía (Estilo maestro)
        lvNoticias.setEmptyView(tvSinNoticias);

        noticiaAdapter = new NoticiaAdapter(ListNoticia.this, new ArrayList<>());
        lvNoticias.setAdapter(noticiaAdapter);

        // Observamos los cambios en la lista de noticias (Estilo maestro)
        noticiaService.listarNoticias().observe(this, new Observer<List<Noticia>>() {
            @Override
            public void onChanged(List<Noticia> noticias) {
                noticiaAdapter.clear();
                noticiaAdapter.addAll(noticias);
                noticiaAdapter.notifyDataSetChanged();
            }
        });

        // Click para editar una noticia
        lvNoticias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Noticia noticiaSeleccionada = (Noticia) parent.getItemAtPosition(position);
                Intent i = new Intent(ListNoticia.this, FormNoticia.class);
                i.putExtra("noticia", noticiaSeleccionada); // Pasamos el objeto completo (Estilo maestro)
                startActivity(i);
            }
        });

        // Click para crear nueva noticia
        btnCrearNoticia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListNoticia.this, FormNoticia.class);
                startActivity(i);
            }
        });

        // Botón volver
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}