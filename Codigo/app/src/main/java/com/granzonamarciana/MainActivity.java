package com.granzonamarciana;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.granzonamarciana.activity.FormUsuario;
import com.granzonamarciana.activity.Menu;
import com.granzonamarciana.entity.TipoRol;
import com.granzonamarciana.entity.Usuario;
import com.granzonamarciana.service.UsuarioService;

import org.mindrot.jbcrypt.BCrypt;

public class MainActivity extends AppCompatActivity {

    private UsuarioService usuarioService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        comprobarSiEstaLogueado();

        usuarioService = new UsuarioService(this);

        EditText etUsername = findViewById(R.id.etUsername);
        EditText etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnCrearUsuario = findViewById(R.id.btnCrearUsuario);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Usuario y/o contraseña vacíos", Toast.LENGTH_LONG).show();
                } else {
                    loginUsuario(username, password);
                }
            }
        });

        btnCrearUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ir a registro
                startActivity(new Intent(MainActivity.this, FormUsuario.class));
            }
        });
    }

    private void loginUsuario(final String username, final String password) {
        // Buscamos en la tabla usuario (donde están todos los roles)
        usuarioService.buscarUsuarioPorUsername(username).observe(this, new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                if (usuario != null && BCrypt.checkpw(password, usuario.getPassword())) {
                    // Éxito: Guardamos sesión y vamos al Menu
                    guardarUsuarioLogueado(usuario.getId(), usuario.getUsername(), usuario.getRol());
                    startActivity(new Intent(MainActivity.this, Menu.class));
                    finish(); // Cerramos login
                } else {
                    Toast.makeText(MainActivity.this, "Usuario y/o contraseña incorrectos", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void guardarUsuarioLogueado(int id, String username, TipoRol rol) {
        SharedPreferences sharedPreferences = getSharedPreferences("granzonaUser", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("id", id);
        editor.putString("username", username);
        editor.putString("rol", rol.toString());

        editor.apply();
    }

    private void comprobarSiEstaLogueado() {
        SharedPreferences sharedPreferences = getSharedPreferences("granzonaUser", MODE_PRIVATE);
        if (sharedPreferences.contains("id")) {
            startActivity(new Intent(MainActivity.this, Menu.class));
            finish();
        }
    }
}