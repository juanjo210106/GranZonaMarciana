package com.granzonamarciana.activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.granzonamarciana.MainActivity;
import com.granzonamarciana.R;
import com.granzonamarciana.entity.TipoRol;

public class Menu extends AppCompatActivity {
    private String username, rol;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        sharedPreferences = getSharedPreferences("granzonaUser", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "Invitado");
        rol = sharedPreferences.getString("rol", "");

        actualizarSaludo();

        // Inicializar lógica de botones
        botonNoticias();
        botonEdiciones();
        botonSolicitudes();
        botonVotar();
        botonInscribirse();
        botonUsuarios();
        botonEditarPerfil();
        botonCerrarSesion();
    }

    @Override
    protected void onResume() {
        super.onResume();
        actualizarSaludo();
    }

    private void actualizarSaludo() {
        username = sharedPreferences.getString("username", "Invitado");
        TextView tvBienvenido = findViewById(R.id.tvBienvenido);
        tvBienvenido.setText("Hola, " + username);
    }

    // Botón común para todos
    private void botonNoticias() {
        Button btn = findViewById(R.id.btnVerNoticias);
        btn.setOnClickListener(v -> {
            startActivity(new Intent(Menu.this, ListNoticia.class));
        });
    }

    // Gestionar Ediciones (Solo Admin)
    private void botonEdiciones() {
        Button btn = findViewById(R.id.btnGestionarEdiciones);
        if (!rol.equals(TipoRol.ADMINISTRADOR.toString())) {
            btn.setVisibility(GONE);
        } else {
            btn.setVisibility(VISIBLE);
            btn.setOnClickListener(v -> {
                startActivity(new Intent(Menu.this, ListEdicion.class));
            });
        }
    }

    // Gestionar Solicitudes (Solo Admin)
    private void botonSolicitudes() {
        Button btn = findViewById(R.id.btnGestionarSolicitudes);
        if (!rol.equals(TipoRol.ADMINISTRADOR.toString())) {
            btn.setVisibility(GONE);
        } else {
            btn.setVisibility(VISIBLE);
            btn.setOnClickListener(v -> {
                startActivity(new Intent(Menu.this, ListSolicitud.class));
            });
        }
    }

    // Votar Galas (Espectador y Concursante)
    private void botonVotar() {
        Button btn = findViewById(R.id.btnVotarGalas);
        if (rol.equals(TipoRol.ADMINISTRADOR.toString()) || rol.isEmpty()) {
            btn.setVisibility(GONE);
        } else {
            btn.setVisibility(VISIBLE);
            btn.setOnClickListener(v -> {
                startActivity(new Intent(Menu.this, ListGalaVotable.class));
            });
        }
    }

    // Inscribirse (Solo Espectador)
    private void botonInscribirse() {
        Button btn = findViewById(R.id.btnInscribirse);
        if (!rol.equals(TipoRol.ESPECTADOR.toString())) {
            btn.setVisibility(GONE);
        } else {
            btn.setVisibility(VISIBLE);
            btn.setOnClickListener(v -> {
                startActivity(new Intent(Menu.this, FormInscribirse.class));
            });
        }
    }

    // Lista de Usuarios (Solo Admin)
    private void botonUsuarios() {
        Button btn = findViewById(R.id.btnUsuarios);
        if (!rol.equals(TipoRol.ADMINISTRADOR.toString())) {
            btn.setVisibility(GONE);
        } else {
            btn.setVisibility(VISIBLE);
            btn.setOnClickListener(v -> {
                startActivity(new Intent(Menu.this, ListUsuario.class));
            });
        }
    }

    // Editar Perfil (Cualquier usuario logueado)
    private void botonEditarPerfil() {
        Button btn = findViewById(R.id.btnEditarPerfil);
        if (rol.isEmpty()) {
            btn.setVisibility(GONE);
        } else {
            btn.setVisibility(VISIBLE);
            btn.setOnClickListener(v -> {
                startActivity(new Intent(Menu.this, FormUsuario.class));
            });
        }
    }

    // Cerrar Sesión
    private void botonCerrarSesion() {
        Button btn = findViewById(R.id.btnCerrarSesion);
        btn.setOnClickListener(v -> {
            sharedPreferences.edit().clear().apply();
            Intent intent = new Intent(Menu.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}