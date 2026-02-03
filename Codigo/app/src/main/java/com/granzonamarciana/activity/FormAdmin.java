package com.granzonamarciana.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.granzonamarciana.R;
import com.granzonamarciana.entity.Administrador;
import com.granzonamarciana.entity.TipoRol;
import com.granzonamarciana.service.AdministradorService;

import org.mindrot.jbcrypt.BCrypt;

public class FormAdmin extends AppCompatActivity {

    private AdministradorService administradorService;
    private EditText etUsername, etContrasena, etNombre, etApellido1, etApellido2, etCorreo, etTelefono, etUrlImagen;
    private Administrador administrador;
    private boolean editando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_admin);

        administradorService = new AdministradorService(this);
        inicializarCampos();

        editando = getIntent().getBooleanExtra("editando", false);

        if (editando) {
            // Se recupera el ID de la sesión "granzonaUser"
            int id = getSharedPreferences("granzonaUser", MODE_PRIVATE).getInt("id", -1);
            if (id != -1) {
                administradorService.buscarAdministradorPorId(id).observe(this, a -> {
                    if (a != null) {
                        administrador = a;
                        cargarDatosEnFormulario(administrador);
                    }
                });
            }
        }

        findViewById(R.id.btnVolver).setOnClickListener(v -> finish());
        findViewById(R.id.btnGuardar).setOnClickListener(v -> crearActualizarAdmin());
    }

    // Métodos para facilitarnos la vida
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

    private void cargarDatosEnFormulario(Administrador a) {
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

        String passwordCifrada = TextUtils.isEmpty(pass) ? null : BCrypt.hashpw(pass, BCrypt.gensalt());

        if (editando && administrador != null) {
            administrador.setUsername(user);
            if (passwordCifrada != null) administrador.setPassword(passwordCifrada);
            administrador.setNombre(nom);
            administrador.setApellido1(ape1);
            administrador.setApellido2(ape2);
            administrador.setCorreo(mail);
            administrador.setTelefono(tel);
            administrador.setUrlImagen(img);

            administradorService.actualizarAdministrador(administrador);
            Toast.makeText(this, "Perfil actualizado", Toast.LENGTH_SHORT).show();
        } else {
            // Usamos tu constructor de Administrador
            Administrador nuevo = new Administrador(user, passwordCifrada, TipoRol.ADMINISTRADOR,
                    nom, ape1, ape2, mail, tel, img);
            administradorService.insertarAdministrador(nuevo);
            Toast.makeText(this, "Administrador creado", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}