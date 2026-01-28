package com.granzonamarciana.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.granzonamarciana.R;
import com.granzonamarciana.entity.Concursante;
import com.granzonamarciana.entity.Espectador;
import com.granzonamarciana.entity.TipoRol;
import com.granzonamarciana.service.ConcursanteService;
import com.granzonamarciana.service.EspectadorService;

import org.mindrot.jbcrypt.BCrypt;

public class RegistroActivity extends AppCompatActivity {

    private EditText etUsername, etPassword, etNombre, etApellido1, etApellido2, etCorreo, etTelefono;
    private RadioGroup rgRol;
    private Button btnGuardar;

    // Servicios
    private ConcursanteService concursanteService;
    private EspectadorService espectadorService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Inicializamos los servicios
        concursanteService = new ConcursanteService(this);
        espectadorService = new EspectadorService(this);

        // Vinvulamos las vistas
        etUsername = findViewById(R.id.etNombreUsuario);
        etPassword = findViewById(R.id.etPassword);
        etNombre = findViewById(R.id.etNombre);
        etApellido1 = findViewById(R.id.etApellido1);
        etApellido2 = findViewById(R.id.etApellido2);
        etCorreo = findViewById(R.id.etCorreo);
        etTelefono = findViewById(R.id.etTelefono);
        rgRol = findViewById(R.id.rgRol);
        btnGuardar = findViewById(R.id.btnGuardarRegistro);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarUsuario();
            }
        });
    }

    private void registrarUsuario() {
        String user = etUsername.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();
        String nombre = etNombre.getText().toString().trim();
        String ap1 = etApellido1.getText().toString().trim();
        String ap2 = etApellido2.getText().toString().trim();
        String correo = etCorreo.getText().toString().trim();
        String tlf = etTelefono.getText().toString().trim();

        // Validaciones Básicas
        if (user.isEmpty() || pass.isEmpty() || nombre.isEmpty()) {
            Toast.makeText(this, "Rellena os campos oligatorios (Usuario, Pass, Nombre)", Toast.LENGTH_SHORT).show();
            return;
        }

        // Hash de contraseña (Seguridad)
        String hashedPass = BCrypt.hashpw(pass, BCrypt.gensalt());

        // Comprobamos que Rol ha seleccionado el usuario
        int selectedId = rgRol.getCheckedRadioButtonId();

        if (selectedId == R.id.rbConcursante) {
            // Guardamos como CONCURSANTE
            Concursante c = new Concursante(user, hashedPass, TipoRol.CONCURSANTE, nombre, ap1, ap2, correo, tlf, "");
            concursanteService.insertarConcursante(c);
            Toast.makeText(this, "Concursante registrado con éxito", Toast.LENGTH_SHORT).show();
        } else {
            // Guardamos como ESPECTADOR
            Espectador e = new Espectador(user, hashedPass, TipoRol.ESPECTADOR, nombre, ap1, ap2, correo, tlf, "");
            espectadorService.insertarEspectador(e);
            Toast.makeText(this, "Espectador registrado con éxito", Toast.LENGTH_SHORT).show();
        }

        // Cerramos la pantalla y volvemos al login
        finish();
    }
}