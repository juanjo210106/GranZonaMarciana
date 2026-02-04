package com.granzonamarciana.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.granzonamarciana.R;
import com.granzonamarciana.entity.Actor;
import com.granzonamarciana.entity.TipoRol;
import com.granzonamarciana.service.ActorService;

import com.google.android.material.textfield.TextInputEditText;

import org.mindrot.jbcrypt.BCrypt;

public class FormAdmin extends AppCompatActivity {

    private ActorService actorService;
    private TextInputEditText etUsername, etContrasena, etNombre, etApellido1, etApellido2, etCorreo, etTelefono, etUrlImagen;
    private Actor administrador;
    private boolean editando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_admin);

        actorService = new ActorService(this);
        inicializarCampos();

        editando = getIntent().getBooleanExtra("editando", false);

        if (editando) {
            int id = getSharedPreferences("granzonaUser", MODE_PRIVATE).getInt("id", -1);
            if (id != -1) {
                actorService.buscarPorId(id).observe(this, a -> {
                    if (a != null && a.getRol() == TipoRol.ADMINISTRADOR) {
                        administrador = a;
                        cargarDatosEnFormulario(administrador);
                    }
                });
            }
        }

        findViewById(R.id.btnVolver).setOnClickListener(v -> finish());
        findViewById(R.id.btnGuardar).setOnClickListener(v -> crearActualizarAdmin());
    }

    private void inicializarCampos() {
        etUsername = findViewById(R.id.etNombreUsuario);
        etContrasena = findViewById(R.id.etContraseña);
        etNombre = findViewById(R.id.etNombre);
        etApellido1 = findViewById(R.id.etApellido1);
        etApellido2 = findViewById(R.id.etApellido2);
        etCorreo = findViewById(R.id.etCorreo);
        etTelefono = findViewById(R.id.etTelefono);
        etUrlImagen = findViewById(R.id.etUrlImagen);
    }

    private void cargarDatosEnFormulario(Actor a) {
        etUsername.setText(a.getUsername());
        etNombre.setText(a.getNombre());
        etApellido1.setText(a.getApellido1());
        etApellido2.setText(a.getApellido2());
        etCorreo.setText(a.getCorreo());
        etTelefono.setText(a.getTelefono());
        etUrlImagen.setText(a.getUrlImagen());
    }

    private void crearActualizarAdmin() {
        String user = etUsername.getText().toString().trim();
        String pass = etContrasena.getText().toString().trim();
        String nom = etNombre.getText().toString().trim();
        String ape1 = etApellido1.getText().toString().trim();
        String ape2 = etApellido2.getText().toString().trim();
        String mail = etCorreo.getText().toString().trim();
        String tel = etTelefono.getText().toString().trim();
        String img = etUrlImagen.getText().toString().trim();

        if (TextUtils.isEmpty(user) || TextUtils.isEmpty(nom) || TextUtils.isEmpty(ape1) ||
                TextUtils.isEmpty(mail) || TextUtils.isEmpty(tel)) {
            Toast.makeText(this, "Completa los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        if (editando && administrador != null) {
            // EDITAR ADMINISTRADOR EXISTENTE
            administrador.setUsername(user);

            // Solo actualizar contraseña si se ingresó una nueva
            if (!TextUtils.isEmpty(pass)) {
                String passwordCifrada = BCrypt.hashpw(pass, BCrypt.gensalt());
                administrador.setPassword(passwordCifrada);
            }

            administrador.setNombre(nom);
            administrador.setApellido1(ape1);
            administrador.setApellido2(ape2);
            administrador.setCorreo(mail);
            administrador.setTelefono(tel);
            administrador.setUrlImagen(img);

            actorService.actualizarActor(administrador);
            Toast.makeText(this, "Perfil actualizado", Toast.LENGTH_SHORT).show();
        } else {
            // CREAR NUEVO ADMINISTRADOR
            if (TextUtils.isEmpty(pass)) {
                Toast.makeText(this, "La contraseña es obligatoria para crear un administrador", Toast.LENGTH_SHORT).show();
                return;
            }

            String passwordCifrada = BCrypt.hashpw(pass, BCrypt.gensalt());
            Actor nuevo = new Actor(user, passwordCifrada, TipoRol.ADMINISTRADOR,
                    nom, ape1, ape2, mail, tel, img);

            actorService.insertarActor(nuevo);
            Toast.makeText(this, "Administrador creado correctamente", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}