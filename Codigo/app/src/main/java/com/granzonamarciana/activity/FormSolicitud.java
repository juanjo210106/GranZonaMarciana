package com.granzonamarciana.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.granzonamarciana.R;
import com.granzonamarciana.entity.EstadoSolicitud;
import com.granzonamarciana.entity.Solicitud;
import com.granzonamarciana.entity.TipoRol;
import com.granzonamarciana.service.ActorService;
import com.granzonamarciana.service.SolicitudService;

public class FormSolicitud extends AppCompatActivity {

    private SolicitudService solicitudService;
    private ActorService actorService;
    private TextInputEditText etMensaje;
    private TextView tvInstrucciones;
    private LinearLayout layoutAccionesAdmin;

    private int idUsuarioSesion;
    private String rolSesion;
    private int idEdicionRecibida;
    private int idSolicitudGestion;
    private Solicitud solicitudCargada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_solicitud);

        // 1. Inicializar Servicios
        solicitudService = new SolicitudService(this);
        actorService = new ActorService(this);

        // 2. Recuperar datos del Intent y Sesión
        idEdicionRecibida = getIntent().getIntExtra("idEdicion", -1);
        idSolicitudGestion = getIntent().getIntExtra("idSolicitud", -1);
        idUsuarioSesion = getSharedPreferences("granzonaUser", MODE_PRIVATE).getInt("id", -1);
        rolSesion = getSharedPreferences("granzonaUser", MODE_PRIVATE).getString("rol", "");

        // 3. Vincular UI
        etMensaje = findViewById(R.id.etDescripcionInscripcion);
        tvInstrucciones = findViewById(R.id.tvInstrucciones);
        layoutAccionesAdmin = findViewById(R.id.layoutAccionesAdmin);
        MaterialButton btnEnviar = findViewById(R.id.btnEnviarSolicitud);
        MaterialButton btnAprobar = findViewById(R.id.btnAprobarSolicitud);
        MaterialButton btnRechazar = findViewById(R.id.btnRechazarSolicitud);
        MaterialButton btnCancelar = findViewById(R.id.btnCancelarSolicitud);

        // 4. Configurar visibilidad según el rol y modo
        if (idSolicitudGestion != -1 && rolSesion.equals(TipoRol.ADMINISTRADOR.toString())) {
            // MODO ADMINISTRADOR: Gestión de solicitud existente
            btnEnviar.setVisibility(View.GONE);
            layoutAccionesAdmin.setVisibility(View.VISIBLE);
            etMensaje.setEnabled(false); // El admin solo lee la propuesta
            cargarSolicitudParaGestion();
        } else {
            // MODO CONCURSANTE: Crear nueva solicitud
            btnEnviar.setVisibility(View.VISIBLE);
            layoutAccionesAdmin.setVisibility(View.GONE);

            if (idEdicionRecibida == -1) {
                Toast.makeText(this, "Error: Edición no seleccionada", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        // 5. Lógica de botones
        btnEnviar.setOnClickListener(v -> enviarNuevaSolicitud());
        btnAprobar.setOnClickListener(v -> actualizarEstadoSolicitud(EstadoSolicitud.ACEPTADA));
        btnRechazar.setOnClickListener(v -> actualizarEstadoSolicitud(EstadoSolicitud.RECHAZADA));
        btnCancelar.setOnClickListener(v -> finish());
    }

    private void cargarSolicitudParaGestion() {
        solicitudService.buscarPorId(idSolicitudGestion).observe(this, solicitud -> {
            if (solicitud != null) {
                solicitudCargada = solicitud;
                etMensaje.setText(solicitud.getMensaje());

                // Consultar quién es el concursante para mostrarlo al Admin
                actorService.buscarPorId(solicitud.getIdConcursante()).observe(this, actor -> {
                    if (actor != null) {
                        tvInstrucciones.setText("Propuesta de: " + actor.getNombre() + " " + actor.getApellido1());
                    }
                });
            }
        });
    }

    private void enviarNuevaSolicitud() {
        String mensaje = etMensaje.getText().toString().trim();
        if (TextUtils.isEmpty(mensaje)) {
            Toast.makeText(this, getString(R.string.error_faltan_datos), Toast.LENGTH_SHORT).show();
            return;
        }

        Solicitud s = new Solicitud();
        s.setMensaje(mensaje);
        s.setEstado(EstadoSolicitud.PENDIENTE);
        s.setIdConcursante(idUsuarioSesion);
        s.setIdEdicion(idEdicionRecibida);

        solicitudService.insertarSolicitud(s);
        Toast.makeText(this, "Solicitud enviada correctamente", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void actualizarEstadoSolicitud(EstadoSolicitud nuevoEstado) {
        if (solicitudCargada != null) {
            solicitudCargada.setEstado(nuevoEstado);
            solicitudService.actualizarSolicitud(solicitudCargada);
            String msg = nuevoEstado == EstadoSolicitud.ACEPTADA ? "Solicitud Aprobada" : "Solicitud Rechazada";
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}