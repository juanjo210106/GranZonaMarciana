package com.granzonamarciana.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.granzonamarciana.R;
import com.granzonamarciana.entity.EstadoSolicitud;
import com.granzonamarciana.entity.Solicitud;
import com.granzonamarciana.service.SolicitudService;

public class FormSolicitud extends AppCompatActivity {

    private SolicitudService solicitudService;
    private EditText etMensaje;
    private int idUsuario;
    private int idEdicionActual = 1; // Deberías obtener la edición activa, ponemos 1 por defecto

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_solicitud);

        etMensaje = findViewById(R.id.etDescripcionInscripcion);
        idUsuario = getSharedPreferences("granzonaUser", MODE_PRIVATE).getInt("id", -1);
        solicitudService = new SolicitudService(this);

        findViewById(R.id.btnEnviarSolicitud).setOnClickListener(v -> {
            String mensaje = etMensaje.getText().toString().trim();

            if (TextUtils.isEmpty(mensaje)) {
                Toast.makeText(this, "Escribe un mensaje para tu solicitud", Toast.LENGTH_SHORT).show();
                return;
            }

            // CREAR SOLICITUD
            Solicitud s = new Solicitud();
            s.setMensaje(mensaje);
            s.setEstado(EstadoSolicitud.PENDIENTE);
            s.setIdConcursante(idUsuario); // Mapeamos idUsuario a idConcursante
            s.setIdEdicion(idEdicionActual);

            solicitudService.insertarSolicitud(s);
            Toast.makeText(this, "Solicitud enviada", Toast.LENGTH_SHORT).show();
            finish();
        });

        findViewById(R.id.btnCancelarSolicitud).setOnClickListener(v -> finish());
    }
}