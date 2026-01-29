package com.granzonamarciana.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.granzonamarciana.R;
import com.granzonamarciana.entity.TipoRol;
import com.granzonamarciana.entity.Usuario;
import com.granzonamarciana.service.UsuarioService;

import org.mindrot.jbcrypt.BCrypt;

public class FormUsuario extends AppCompatActivity {

    private UsuarioService usuarioService;
    private EditText etNombreUsuario, etContrasena, etNombre, etApellido1, etApellido2, etCorreo, etTelefono, etUrlImagen;
    private Usuario usuario;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_usuario);

        inicializarCampos();

        // Lógica del maestro para recuperar la sesión
        id = getSharedPreferences("granzonaUser", MODE_PRIVATE).getInt("id", -1);

        usuarioService = new UsuarioService(this);

        if (id != -1) {
            usuarioService.buscarUsuarioPorId(id).observe(this, new Observer<Usuario>() {
                @Override
                public void onChanged(Usuario u) {
                    if (u != null) {
                        usuario = u;
                        cargarDatosEnFormulario(usuario);
                    }
                }
            });
        }

        findViewById(R.id.btnVolver).setOnClickListener(v -> finish());
        findViewById(R.id.btnGuardar).setOnClickListener(v -> crearActualizarUsuario());
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
    }

    private void cargarDatosEnFormulario(Usuario u) {
        etNombreUsuario.setText(u.getUsername());
        etNombre.setText(u.getNombre());
        etApellido1.setText(u.getApellido1());
        etApellido2.setText(u.getApellido2());
        etCorreo.setText(u.getCorreo());
        etTelefono.setText(u.getTelefono());
        etUrlImagen.setText(u.getUrlImagen());
    }

    private void crearActualizarUsuario() {
        if (TextUtils.isEmpty(etNombreUsuario.getText()) ||
                TextUtils.isEmpty(etContrasena.getText()) ||
                TextUtils.isEmpty(etNombre.getText()) ||
                TextUtils.isEmpty(etCorreo.getText()) ||
                TextUtils.isEmpty(etTelefono.getText())) {

            Toast.makeText(this, "Por favor completa todos los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cifrado de contraseña con BCrypt igual que el maestro
        String passwordCifrada = BCrypt.hashpw(
                etContrasena.getText().toString().trim(),
                BCrypt.gensalt()
        );

        if (id != -1 && usuario != null) {
            // MODO ACTUALIZAR (Perfil)
            usuario.setUsername(etNombreUsuario.getText().toString().trim());
            usuario.setPassword(passwordCifrada);
            usuario.setNombre(etNombre.getText().toString().trim());
            usuario.setApellido1(etApellido1.getText().toString().trim());
            usuario.setApellido2(etApellido2.getText().toString().trim());
            usuario.setCorreo(etCorreo.getText().toString().trim());
            usuario.setTelefono(etTelefono.getText().toString().trim());
            usuario.setUrlImagen(etUrlImagen.getText().toString().trim());

            usuarioService.actualizarUsuario(usuario);
            Toast.makeText(this, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show();

            getSharedPreferences("granzonaUser", MODE_PRIVATE)
                    .edit()
                    .putString("username", usuario.getUsername())
                    .apply();

            finish();
            return;
        }

        // MODO CREAR (Nuevo Registro)
        // Por defecto, un nuevo registro es ESPECTADOR
        Usuario nuevoUsuario = new Usuario(
                etNombreUsuario.getText().toString().trim(),
                passwordCifrada,
                TipoRol.ESPECTADOR,
                etNombre.getText().toString().trim(),
                etApellido1.getText().toString().trim(),
                etApellido2.getText().toString().trim(),
                etCorreo.getText().toString().trim(),
                etTelefono.getText().toString().trim(),
                etUrlImagen.getText().toString().trim()
        );

        usuarioService.insertarUsuario(nuevoUsuario);
        Toast.makeText(this, "Registro realizado con éxito", Toast.LENGTH_SHORT).show();
        finish();
    }
}