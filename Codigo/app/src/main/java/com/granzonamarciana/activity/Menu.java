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
        configurarBotonesPorRol();
    }

    private void actualizarDatosSesion() {
        // Invitado es el valor por defecto si no hay sesión
        username = sharedPreferences.getString("username", "Invitado");
        rol = sharedPreferences.getString("rol", "");

        TextView tvBienvenido = findViewById(R.id.tvBienvenido);
        if (tvBienvenido != null) {
            tvBienvenido.setText(getString(R.string.txt_hola, username));
        }
    }

    private void configurarBotonesPorRol() {
        boolean esAdmin = rol.equals(TipoRol.ADMINISTRADOR.toString());
        boolean esConcursante = rol.equals(TipoRol.CONCURSANTE.toString());
        boolean esEspectador = rol.equals(TipoRol.ESPECTADOR.toString());
        boolean estaLogueado = !rol.isEmpty();

        // --- 1. ACCESO PÚBLICO (Invitados y todos los demás) ---
        // Consultar noticias
        findViewById(R.id.btnVerNoticias).setOnClickListener(v ->
                startActivity(new Intent(Menu.this, ListNoticia.class)));

        // Consultar ediciones (Usamos btnInscribirse que ya existe en tu XML)
        View btnInscribirse = findViewById(R.id.btnInscribirse);
        if (btnInscribirse != null) {
            // Visible para Invitados (ver), Concursantes (inscribirse) y Espectadores (ver)
            btnInscribirse.setVisibility(esAdmin ? GONE : VISIBLE);
            btnInscribirse.setOnClickListener(v ->
                    startActivity(new Intent(Menu.this, ListEdicionInscribible.class)));
        }

        // --- 2. ACTORES REGISTRADOS (Cualquier rol logueado) ---
        int visLogueado = estaLogueado ? VISIBLE : GONE;

        // Editar perfil y cerrar sesión
        configurarVisibilidad(R.id.btnEditarPerfil, visLogueado, FormUsuario.class);
        configurarVisibilidad(R.id.btnCerrarSesion, visLogueado, null);

        // Consultar galas y puntuaciones
        configurarVisibilidad(R.id.btnMisPuntuaciones, visLogueado, ListPuntuacion.class);

        // --- 3. ROLES ESPECÍFICOS ---
        // Espectador: Puntuar en galas
        View btnVotar = findViewById(R.id.btnVotarGalas);
        if (btnVotar != null) {
            btnVotar.setVisibility(esEspectador ? VISIBLE : GONE);
            btnVotar.setOnClickListener(v -> startActivity(new Intent(Menu.this, ListGalaVotable.class)));
        }

        // --- 4. SECCIÓN ADMINISTRADOR ---
        gestionarSeccionAdmin(esAdmin);
    }

    private void gestionarSeccionAdmin(boolean esAdmin) {
        int visAdmin = esAdmin ? VISIBLE : GONE;

        // Etiquetas de sección
        View tvAdminLabel = findViewById(R.id.tvAdminLabel);
        View viewDivider = findViewById(R.id.viewDivider);
        if (tvAdminLabel != null) tvAdminLabel.setVisibility(visAdmin);
        if (viewDivider != null) viewDivider.setVisibility(visAdmin);

        // Gestión de sistema
        configurarVisibilidad(R.id.btnGestionarEdiciones, visAdmin, ListEdicion.class);
        configurarVisibilidad(R.id.btnGestionarGalas, visAdmin, ListGala.class);
        configurarVisibilidad(R.id.btnGestionarSolicitudes, visAdmin, ListSolicitud.class);
        configurarVisibilidad(R.id.btnUsuarios, visAdmin, ListUsuario.class);
    }

    private void configurarVisibilidad(int idBtn, int visibilidad, Class<?> destino) {
        View btn = findViewById(idBtn);
        if (btn != null) {
            btn.setVisibility(visibilidad);
            if (visibilidad == VISIBLE) {
                if (idBtn == R.id.btnCerrarSesion) {
                    btn.setOnClickListener(v -> cerrarSesion());
                } else if (destino != null) {
                    btn.setOnClickListener(v -> startActivity(new Intent(Menu.this, destino)));
                }
            }
        }
    }

    private void cerrarSesion() {
        sharedPreferences.edit().clear().apply();
        Intent intent = new Intent(Menu.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        actualizarDatosSesion();
        configurarBotonesPorRol();
    }
}