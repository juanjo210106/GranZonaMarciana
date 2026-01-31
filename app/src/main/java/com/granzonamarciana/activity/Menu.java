package com.granzonamarciana.activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
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

        actualizarDatosSesion();

        // --- BOTONES USUARIO ---
        botonNoticias();
        botonInscribirse();
        botonVotar();
        botonMisPuntuaciones(); // NUEVO
        botonEditarPerfil();

        // --- SECCIÓN ADMINISTRACIÓN ---
        gestionarVisibilidadAdmin();
        botonEdiciones();
        botonGestionarGalas();
        botonSolicitudes();
        botonUsuarios();

        botonCerrarSesion();
    }

    private void actualizarDatosSesion() {
        username = sharedPreferences.getString("username", "Invitado");
        rol = sharedPreferences.getString("rol", "");
        TextView tvBienvenido = findViewById(R.id.tvBienvenido);
        tvBienvenido.setText(getString(R.string.txt_hola, username));
    }

    private void gestionarVisibilidadAdmin() {
        TextView tvAdminLabel = findViewById(R.id.tvAdminLabel);
        View viewDivider = findViewById(R.id.viewDivider);

        // Solo visible si es ADMIN
        int visibilidad = rol.equals(TipoRol.ADMINISTRADOR.toString()) ? VISIBLE : GONE;

        if (tvAdminLabel != null) tvAdminLabel.setVisibility(visibilidad);
        if (viewDivider != null) viewDivider.setVisibility(visibilidad);
    }

    @Override
    protected void onResume() {
        super.onResume();
        actualizarDatosSesion();
        gestionarVisibilidadAdmin();
    }

    // --- LÓGICA DE BOTONES ---

    private void botonNoticias() {
        findViewById(R.id.btnVerNoticias).setOnClickListener(v ->
                startActivity(new Intent(Menu.this, ListNoticia.class)));
    }

    private void botonInscribirse() {
        MaterialButton btn = findViewById(R.id.btnInscribirse);
        if (!rol.equals(TipoRol.ESPECTADOR.toString())) {
            btn.setVisibility(GONE);
        } else {
            btn.setVisibility(VISIBLE);
            btn.setOnClickListener(v -> {
                startActivity(new Intent(Menu.this, ListEdicionInscribible.class));
            });
        }
    }

    private void botonVotar() {
        MaterialButton btn = findViewById(R.id.btnVotarGalas);
        // No tiene sentido que el admin vote
        if (rol.equals(TipoRol.ADMINISTRADOR.toString()) || rol.isEmpty()) btn.setVisibility(GONE);
        else btn.setOnClickListener(v -> startActivity(new Intent(Menu.this, ListGalaVotable.class)));
    }

    private void botonMisPuntuaciones() {
        MaterialButton btn = findViewById(R.id.btnMisPuntuaciones);
        // Si no está logueado, fuera
        if (rol.isEmpty()) btn.setVisibility(GONE);
        else btn.setOnClickListener(v -> startActivity(new Intent(Menu.this, ListPuntuacion.class)));
    }

    private void botonEditarPerfil() {
        MaterialButton btn = findViewById(R.id.btnEditarPerfil);
        if (rol.isEmpty()) btn.setVisibility(GONE);
        else btn.setOnClickListener(v -> startActivity(new Intent(Menu.this, FormUsuario.class)));
    }

    // --- MÉTODOS ADMIN (Todos se ocultan si no es ADMIN) ---

    private void botonEdiciones() {
        MaterialButton btn = findViewById(R.id.btnGestionarEdiciones);
        if (!rol.equals(TipoRol.ADMINISTRADOR.toString())) btn.setVisibility(GONE);
        else btn.setOnClickListener(v -> startActivity(new Intent(Menu.this, ListEdicion.class)));
    }

    private void botonGestionarGalas() {
        MaterialButton btn = findViewById(R.id.btnGestionarGalas);
        if (!rol.equals(TipoRol.ADMINISTRADOR.toString())) btn.setVisibility(GONE);
        else btn.setOnClickListener(v -> startActivity(new Intent(Menu.this, ListGala.class)));
    }

    private void botonSolicitudes() {
        MaterialButton btn = findViewById(R.id.btnGestionarSolicitudes);
        if (!rol.equals(TipoRol.ADMINISTRADOR.toString())) btn.setVisibility(GONE);
        else btn.setOnClickListener(v -> startActivity(new Intent(Menu.this, ListSolicitud.class)));
    }

    private void botonUsuarios() {
        MaterialButton btn = findViewById(R.id.btnUsuarios);
        if (!rol.equals(TipoRol.ADMINISTRADOR.toString())) btn.setVisibility(GONE);
        else btn.setOnClickListener(v -> startActivity(new Intent(Menu.this, ListUsuario.class)));
    }

    private void botonCerrarSesion() {
        findViewById(R.id.btnCerrarSesion).setOnClickListener(v -> {
            sharedPreferences.edit().clear().apply();
            Intent intent = new Intent(Menu.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}