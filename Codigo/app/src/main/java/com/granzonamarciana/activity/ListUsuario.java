package com.granzonamarciana.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.granzonamarciana.R;
import com.granzonamarciana.adapter.ActorAdapter;
import com.granzonamarciana.entity.Actor;
import com.granzonamarciana.service.ActorService;

import java.util.ArrayList;
import java.util.List;

public class ListUsuario extends AppCompatActivity {

    private ActorService actorService;
    private ActorAdapter actorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_usuario);

        // Inicializar componentes de la UI (IDs de tu activity_list_usuario.xml)
        ListView lvUsuarios = findViewById(R.id.lvUsuarios);
        TextView tvSinUsuarios = findViewById(R.id.tvSinUsuarios);
        Button btnVolver = findViewById(R.id.btnVolver);

        // Configurar el adaptador con la lista vacía inicialmente
        actorAdapter = new ActorAdapter(this, new ArrayList<Actor>());
        lvUsuarios.setAdapter(actorAdapter);

        // Configurar la vista vacía (si no hay datos, sale el mensaje de error)
        lvUsuarios.setEmptyView(tvSinUsuarios);

        // Inicializar el servicio
        actorService = new ActorService(this);

        // Observar los datos: Listar todos los usuarios (espectadores y concursantes)
        actorService.listarUsuariosSistema().observe(this, new Observer<List<Actor>>() {
            @Override
            public void onChanged(List<Actor> actores) {
                if (actores != null) {
                    actorAdapter.clear();
                    actorAdapter.addAll(actores);
                    actorAdapter.notifyDataSetChanged();
                }
            }
        });

        // Consultar detalle: Al pulsar un usuario, el admin accede a su información
        lvUsuarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Actor seleccionado = actorAdapter.getItem(position);
                if (seleccionado != null) {
                    Intent intent = new Intent(ListUsuario.this, FormUsuario.class);
                    // Pasamos el ID para que el formulario sepa que es edición/consulta
                    intent.putExtra("id_actor_gestion", seleccionado.getId());
                    startActivity(intent);
                }
            }
        });

        // Configurar botón volver
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}