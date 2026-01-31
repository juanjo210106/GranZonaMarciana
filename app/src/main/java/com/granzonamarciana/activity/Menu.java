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

        // Cargar sesión de granzonaUser
        sharedPreferences = getSharedPreferences("granzonaUser", MODE_PRIVATE);

        // Inicializamos los datos del usuario
        actualizarDatosSesion();

        // Inicializar lógica de cada botón (IDs actualizados al XML actual)
        botonNoticias();
        botonInscribirse(); // Para espectadores
        botonVotar();       // Para concursantes/espectadores
        botonEditarPerfil();

        // Sección Administración
        botonEdiciones();
        botonSolicitudes();
        botonUsuarios();
        botonCrearAdmin();

        // Salida
        botonCerrarSesion();
    }

    @Override
    protected void onResume() {
        super.onResume();
        actualizarDatosSesion();
    }

    // Carga los datos y actualiza el saludo
    private void actualizarDatosSesion() {
        username = sharedPreferences.getString("username", "Invitado");
        rol = sharedPreferences.getString("rol", "");
        TextView tvBienvenido = findViewById(R.id.tvBienvenido);
        tvBienvenido.setText("Hola, " + username);
    }

    // Ver Noticias: Común para todos
    private void botonNoticias() {
        MaterialButton btn = findViewById(R.id.btnVerNoticias);
        btn.setOnClickListener(v -> startActivity(new Intent(Menu.this, ListNoticia.class)));
    }

    // Inscribirse: Solo ESPECTADOR
    private void botonInscribirse() {
        MaterialButton btn = findViewById(R.id.btnInscribirse);
        if (btn == null) return; // Por seguridad si no está en el XML

        if (!rol.equals(TipoRol.ESPECTADOR.toString())) {
            btn.setVisibility(GONE);
        } else {
            btn.setVisibility(VISIBLE);
            btn.setOnClickListener(v -> startActivity(new Intent(Menu.this, FormSolicitud.class)));
        }
    }

    // Votar Galas: No Admins
    private void botonVotar() {
        MaterialButton btn = findViewById(R.id.btnVotarGalas);
        if (rol.equals(TipoRol.ADMINISTRADOR.toString()) || rol.isEmpty()) {
            btn.setVisibility(GONE);
        } else {
            btn.setVisibility(VISIBLE);
            btn.setOnClickListener(v -> startActivity(new Intent(Menu.this, ListGalaVotable.class)));
        }
    }

    // Editar Perfil: Cualquiera con cuenta
    private void botonEditarPerfil() {
        MaterialButton btn = findViewById(R.id.btnEditarPerfil);
        if (rol.isEmpty()) {
            btn.setVisibility(GONE);
        } else {
            btn.setVisibility(VISIBLE);
            btn.setOnClickListener(v -> startActivity(new Intent(Menu.this, FormUsuario.class)));
        }
    }

    // --- SECCIÓN ADMINISTRACIÓN ---

    private void botonEdiciones() {
        MaterialButton btn = findViewById(R.id.btnGestionarEdiciones);
        if (!rol.equals(TipoRol.ADMINISTRADOR.toString())) {
            btn.setVisibility(GONE);
        } else {
            btn.setVisibility(VISIBLE);
            btn.setOnClickListener(v -> startActivity(new Intent(Menu.this, ListEdicion.class)));
        }
    }

    private void botonSolicitudes() {
        MaterialButton btn = findViewById(R.id.btnGestionarSolicitudes);
        if (btn == null) return;

        if (!rol.equals(TipoRol.ADMINISTRADOR.toString())) {
            btn.setVisibility(GONE);
        } else {
            btn.setVisibility(VISIBLE);
            btn.setOnClickListener(v -> startActivity(new Intent(Menu.this, ListSolicitud.class)));
        }
    }

    private void botonUsuarios() {
        MaterialButton btn = findViewById(R.id.btnUsuarios);
        if (!rol.equals(TipoRol.ADMINISTRADOR.toString())) {
            btn.setVisibility(GONE);
        } else {
            btn.setVisibility(VISIBLE);
            btn.setOnClickListener(v -> startActivity(new Intent(Menu.this, ListUsuario.class)));
        }
    }

    private void botonCrearAdmin() {
        MaterialButton btn = findViewById(R.id.btnCrearAdmin);
        if (!rol.equals(TipoRol.ADMINISTRADOR.toString())) {
            btn.setVisibility(GONE);
        } else {
            btn.setVisibility(VISIBLE);
            btn.setOnClickListener(v -> startActivity(new Intent(Menu.this, FormAdmin.class)));
        }
    }

    // Cerrar Sesión
    private void botonCerrarSesion() {
        MaterialButton btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(v -> {
            sharedPreferences.edit().clear().apply();
            Intent intent = new Intent(Menu.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}