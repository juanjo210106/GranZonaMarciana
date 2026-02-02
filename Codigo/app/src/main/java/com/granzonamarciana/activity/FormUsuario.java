package com.granzonamarciana.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.granzonamarciana.R;
import com.granzonamarciana.entity.Actor;
import com.granzonamarciana.entity.TipoRol;
import com.granzonamarciana.service.ActorService;

import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;

public class FormUsuario extends AppCompatActivity {

    private ActorService actorService;
    private EditText etNombreUsuario, etContrasena, etNombre, etApellido1, etApellido2, etCorreo, etTelefono, etUrlImagen;
    private AutoCompleteTextView autoCompleteRol;
    private Actor actorCargado;

    private int idSesion;
    private String rolSesion;
    private int idActorGestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_usuario);

        actorService = new ActorService(this);
        inicializarCampos();

        // 1. Obtener datos de la sesión actual
        idSesion = getSharedPreferences("granzonaUser", MODE_PRIVATE).getInt("id", -1);
        rolSesion = getSharedPreferences("granzonaUser", MODE_PRIVATE).getString("rol", "");

        // 2. Comprobar si venimos de la lista de usuarios (Gestión de Admin)
        idActorGestion = getIntent().getIntExtra("id_actor_gestion", -1);

        configurarSelectorRoles();

        // 3. Lógica de Carga de Datos
        if (idActorGestion != -1) {
            // Caso: El Admin está consultando a otro usuario
            cargarUsuarioParaFormulario(idActorGestion);
        } else if (idSesion != -1) {
            // Caso: El usuario está editando su propio perfil
            cargarUsuarioParaFormulario(idSesion);
            // Al editar el perfil propio, bloqueamos el cambio de rol por seguridad
            autoCompleteRol.setEnabled(false);
        }

        findViewById(R.id.btnVolver).setOnClickListener(v -> finish());
        findViewById(R.id.btnGuardar).setOnClickListener(v -> procesarFormulario());
    }

    private void inicializarCampos() {
        etNombreUsuario = findViewById(R.id.etNombreUsuario);
        etContrasena = findViewById(R.id.etContrasena);
        etNombre = findViewById(R.id.etNombre);
        etApellido1 = findViewById(R.id.etApellido1);
        etApellido2 = findViewById(R.id.etApellido2);
        etCorreo = findViewById(R.id.etCorreo);
        etTelefono = findViewById(R.id.etTelefono);
        etUrlImagen = findViewById(R.id.etUrlImagen);
        autoCompleteRol = findViewById(R.id.autoCompleteRol);
    }

    private void configurarSelectorRoles() {
        List<String> roles = new ArrayList<>();
        // Por defecto, cualquier actor no autenticado puede ser Espectador o Concursante
        roles.add(TipoRol.ESPECTADOR.toString());
        roles.add(TipoRol.CONCURSANTE.toString());

        // CAMBIO: Solo un Administrador puede asignar el rol de Administrador
        if (rolSesion.equals(TipoRol.ADMINISTRADOR.toString())) {
            roles.add(TipoRol.ADMINISTRADOR.toString());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, roles);
        autoCompleteRol.setAdapter(adapter);
    }

    private void cargarUsuarioParaFormulario(int id) {
        actorService.buscarPorId(id).observe(this, actor -> {
            if (actor != null) {
                actorCargado = actor;
                etNombreUsuario.setText(actor.getUsername());
                etNombre.setText(actor.getNombre());
                etApellido1.setText(actor.getApellido1());
                etApellido2.setText(actor.getApellido2());
                etCorreo.setText(actor.getCorreo());
                etTelefono.setText(actor.getTelefono());
                etUrlImagen.setText(actor.getUrlImagen());
                autoCompleteRol.setText(actor.getRol().toString(), false);
            }
        });
    }

    private void procesarFormulario() {
        String username = etNombreUsuario.getText().toString().trim();
        String passRaw = etContrasena.getText().toString().trim();
        String rolSeleccionado = autoCompleteRol.getText().toString();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(rolSeleccionado)) {
            Toast.makeText(this, "Usuario y Rol son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        // Si es edición y no se escribe pass, mantenemos la anterior. Si es nuevo, ciframos.
        String passwordFinal;
        if (actorCargado != null && TextUtils.isEmpty(passRaw)) {
            passwordFinal = actorCargado.getPassword();
        } else {
            passwordFinal = BCrypt.hashpw(passRaw, BCrypt.gensalt());
        }

        if (actorCargado != null) {
            // ACTUALIZAR
            actorCargado.setUsername(username);
            actorCargado.setPassword(passwordFinal);
            actorCargado.setNombre(etNombre.getText().toString().trim());
            actorCargado.setApellido1(etApellido1.getText().toString().trim());
            actorCargado.setApellido2(etApellido2.getText().toString().trim());
            actorCargado.setCorreo(etCorreo.getText().toString().trim());
            actorCargado.setTelefono(etTelefono.getText().toString().trim());
            actorCargado.setUrlImagen(etUrlImagen.getText().toString().trim());
            actorCargado.setRol(TipoRol.valueOf(rolSeleccionado));

            actorService.actualizarActor(actorCargado);

            // CAMBIO: Si es el perfil propio, actualizar SharedPreferences
            if (idSesion == actorCargado.getId()) {
                getSharedPreferences("granzonaUser", MODE_PRIVATE)
                        .edit()
                        .putString("username", username)
                        .putString("rol", rolSeleccionado)
                        .apply();
            }

            Toast.makeText(this, "Cambios guardados", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            // CREAR NUEVO
            Actor nuevo = new Actor(
                    username, passwordFinal, TipoRol.valueOf(rolSeleccionado),
                    etNombre.getText().toString().trim(),
                    etApellido1.getText().toString().trim(),
                    etApellido2.getText().toString().trim(),
                    etCorreo.getText().toString().trim(),
                    etTelefono.getText().toString().trim(),
                    etUrlImagen.getText().toString().trim()
            );
            actorService.insertarActor(nuevo);
            Toast.makeText(this, "Usuario creado con éxito", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}