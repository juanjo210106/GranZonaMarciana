package com.granzonamarciana.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.granzonamarciana.R;
import com.granzonamarciana.entity.EstadoSolicitud;
import com.granzonamarciana.entity.Solicitud;
import com.granzonamarciana.service.SolicitudService;

public class FormSolicitud extends AppCompatActivity {

    private SolicitudService solicitudService;
    private TextInputEditText etMensaje;
    private int idUsuario;
    private int idEdicionRecibida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_solicitud);

        // Inicializar Servicios y recuperar ID de Edición del Intent
        solicitudService = new SolicitudService(this);
        idEdicionRecibida = getIntent().getIntExtra("idEdicion", -1);

        // Recuperar ID del Usuario de la sesión
        idUsuario = getSharedPreferences("granzonaUser", MODE_PRIVATE).getInt("id", -1);

        // Vincular UI (Usando los IDs de tu XML)
        etMensaje = findViewById(R.id.etDescripcionInscripcion);
        MaterialButton btnEnviar = findViewById(R.id.btnEnviarSolicitud);
        MaterialButton btnCancelar = findViewById(R.id.btnCancelarSolicitud);

        // Seguridad: Si no hay edición, no podemos inscribirnos
        if (idEdicionRecibida == -1) {
            Toast.makeText(this, "Error: No se ha seleccionado una edición válida", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        //  Lógica de Envío
        btnEnviar.setOnClickListener(v -> {
            String mensaje = etMensaje.getText().toString().trim();

            if (TextUtils.isEmpty(mensaje)) {
                // Usamos tu string: Por favor, rellena todos los campos obligatorios
                Toast.makeText(this, getString(R.string.error_faltan_datos), Toast.LENGTH_SHORT).show();
                return;
            }

            // CREAR OBJETO SOLICITUD
            Solicitud s = new Solicitud();
            s.setMensaje(mensaje);
            s.setEstado(EstadoSolicitud.PENDIENTE);
            s.setIdConcursante(idUsuario); // Mapeo del ID de usuario actual
            s.setIdEdicion(idEdicionRecibida); // ID que viene de la pantalla anterior

            // Guardar en la base de datos
            solicitudService.insertarSolicitud(s);

            // Usamos tu string: Usuario registrado correctamente (o podrías añadir uno específico para solicitud)
            Toast.makeText(this, "Solicitud enviada con éxito", Toast.LENGTH_SHORT).show();
            finish();
        });

        // Botón Cancelar
        btnCancelar.setOnClickListener(v -> finish());
    }
}