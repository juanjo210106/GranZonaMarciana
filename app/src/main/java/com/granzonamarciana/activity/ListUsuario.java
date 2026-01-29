package com.granzonamarciana.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.granzonamarciana.R;
import com.granzonamarciana.adapter.UsuarioAdapter;
import com.granzonamarciana.entity.Usuario;
import com.granzonamarciana.service.UsuarioService;

import java.util.ArrayList;
import java.util.List;

public class ListUsuario extends AppCompatActivity {

    private UsuarioService usuarioService;
    private UsuarioAdapter usuarioAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_usuario);

        // Inicializar componentes de la UI
        ListView lvUsuarios = findViewById(R.id.lvUsuarios);
        TextView tvSinUsuarios = findViewById(R.id.tvSinUsuarios);
        Button btnVolver = findViewById(R.id.btnVolver);

        // Configurar el adaptador
        usuarioAdapter = new UsuarioAdapter(ListUsuario.this, new ArrayList<Usuario>());
        lvUsuarios.setAdapter(usuarioAdapter);

        // Se muestra automáticamente tvSinUsuarios si la lista está vacía
        lvUsuarios.setEmptyView(tvSinUsuarios);

        // Inicializar el servicio y observar los datos
        usuarioService = new UsuarioService(ListUsuario.this);

        usuarioService.listarUsuarios().observe(this, new Observer<List<Usuario>>() {
            @Override
            public void onChanged(List<Usuario> usuarios) {
                if (usuarios != null) {
                    usuarioAdapter.clear();
                    usuarioAdapter.addAll(usuarios);
                    usuarioAdapter.notifyDataSetChanged();
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