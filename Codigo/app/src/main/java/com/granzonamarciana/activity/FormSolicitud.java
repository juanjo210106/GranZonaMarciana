package com.granzonamarciana.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.granzonamarciana.R;
import com.granzonamarciana.entity.Edicion;
import com.granzonamarciana.entity.EstadoSolicitud;
import com.granzonamarciana.entity.Solicitud;
import com.granzonamarciana.entity.TipoRol;
import com.granzonamarciana.service.ActorService;
import com.granzonamarciana.service.EdicionService;
import com.granzonamarciana.service.SolicitudService;

import java.util.ArrayList;
import java.util.List;

public class FormSolicitud extends AppCompatActivity {

    private SolicitudService solicitudService;
    private ActorService actorService;
    private EdicionService edicionService; // Necesario para RNF de cupos

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
        edicionService = new EdicionService(this);

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

        // 4. Configurar UI según Rol
        if (idSolicitudGestion != -1 && rolSesion.equals(TipoRol.ADMINISTRADOR.toString())) {
            // MODO ADMIN: Gestionar solicitud existente
            btnEnviar.setVisibility(View.GONE);
            layoutAccionesAdmin.setVisibility(View.VISIBLE);
            etMensaje.setEnabled(false);
            cargarSolicitudParaGestion();
        } else {
            // MODO CONCURSANTE: Nueva solicitud
            btnEnviar.setVisibility(View.VISIBLE);
            layoutAccionesAdmin.setVisibility(View.GONE);
            if (idEdicionRecibida == -1) {
                finish();
            }
        }

        // 5. Listeners
        btnEnviar.setOnClickListener(v -> enviarNuevaSolicitud());

        // RNF: Lógica de aprobación con control de cupo
        btnAprobar.setOnClickListener(v -> validarCupoYAprobar());

        btnRechazar.setOnClickListener(v -> actualizarEstadoSolicitud(EstadoSolicitud.RECHAZADA));
        btnCancelar.setOnClickListener(v -> finish());
    }

    private void cargarSolicitudParaGestion() {
        solicitudService.buscarSolicitudPorId(idSolicitudGestion).observe(this, solicitud -> {
            if (solicitud != null) {
                solicitudCargada = solicitud;
                etMensaje.setText(solicitud.getMensaje());

                // Mostrar nombre del concursante
                actorService.buscarPorId(solicitud.getIdConcursante()).observe(this, actor -> {
                    if (actor != null) {
                        tvInstrucciones.setText("Candidato: " + actor.getNombre() + " " + actor.getApellido1());
                    }
                });
            }
        });
    }

    private void enviarNuevaSolicitud() {
        String mensaje = etMensaje.getText().toString().trim();
        if (TextUtils.isEmpty(mensaje)) {
            Toast.makeText(this, "Escribe una descripción", Toast.LENGTH_SHORT).show();
            return;
        }

        Solicitud s = new Solicitud();
        s.setMensaje(mensaje);
        s.setEstado(EstadoSolicitud.PENDIENTE);
        s.setIdConcursante(idUsuarioSesion);
        s.setIdEdicion(idEdicionRecibida);

        solicitudService.insertarSolicitud(s);
        Toast.makeText(this, "Solicitud enviada", Toast.LENGTH_SHORT).show();
        finish();
    }

    // --- LÓGICA RNF: Control de Cupos ---
    private void validarCupoYAprobar() {
        if (solicitudCargada == null) return;

        // Paso 1: Obtener la Edición para saber el límite (maxParticipantes)
        // NOTA: Verifica si tu método en EdicionService es 'buscarEdicionPorId' o 'buscarPorId'
        edicionService.buscarEdicionPorId(solicitudCargada.getIdEdicion()).observe(this, new Observer<Edicion>() {
            @Override
            public void onChanged(Edicion edicion) {
                // Removemos observer para que no se ejecute de nuevo al cambiar datos
                edicionService.buscarEdicionPorId(solicitudCargada.getIdEdicion()).removeObserver(this);

                if (edicion != null) {
                    contarYProcesar(edicion);
                }
            }
        });
    }

    private void contarYProcesar(Edicion edicion) {
        // Paso 2: Listar todas las solicitudes de esta edición
        solicitudService.listarSolicitudesPorEdicion(edicion.getId()).observe(this, new Observer<List<Solicitud>>() {
            @Override
            public void onChanged(List<Solicitud> solicitudes) {
                solicitudService.listarSolicitudesPorEdicion(edicion.getId()).removeObserver(this);

                int aceptadas = 0;
                List<Solicitud> pendientes = new ArrayList<>();

                for (Solicitud s : solicitudes) {
                    if (s.getEstado() == EstadoSolicitud.ACEPTADA) {
                        aceptadas++;
                    } else if (s.getEstado() == EstadoSolicitud.PENDIENTE && s.getId() != solicitudCargada.getId()) {
                        pendientes.add(s);
                    }
                }

                // Verificamos si ya está lleno
                if (aceptadas >= edicion.getMaxParticipantes()) {
                    Toast.makeText(FormSolicitud.this, "Error: Cupo completo (" + edicion.getMaxParticipantes() + ")", Toast.LENGTH_LONG).show();
                } else {
                    // Hay hueco: Aprobamos la actual
                    actualizarEstadoSolicitud(EstadoSolicitud.ACEPTADA);

                    // RNF: Si al aceptar esta se llena el cupo, rechazamos el resto
                    if (aceptadas + 1 == edicion.getMaxParticipantes()) {
                        rechazarRestantes(pendientes);
                        Toast.makeText(FormSolicitud.this, "Cupo cerrado. Se rechazaron " + pendientes.size() + " solicitudes restantes.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void rechazarRestantes(List<Solicitud> pendientes) {
        for (Solicitud s : pendientes) {
            s.setEstado(EstadoSolicitud.RECHAZADA);
            solicitudService.actualizarSolicitud(s);
        }
    }
    // ------------------------------------

    private void actualizarEstadoSolicitud(EstadoSolicitud nuevoEstado) {
        if (solicitudCargada != null) {
            solicitudCargada.setEstado(nuevoEstado);
            solicitudService.actualizarSolicitud(solicitudCargada);
            if(nuevoEstado == EstadoSolicitud.RECHAZADA) {
                Toast.makeText(this, "Solicitud rechazada", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Solicitud aprobada", Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }
}