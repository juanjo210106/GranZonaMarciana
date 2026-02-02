package com.granzonamarciana.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.google.android.material.button.MaterialButton;
import com.granzonamarciana.R;
import com.granzonamarciana.adapter.EdicionAdapter;
import com.granzonamarciana.entity.Edicion;
import com.granzonamarciana.entity.EstadoSolicitud;
import com.granzonamarciana.entity.TipoRol;
import com.granzonamarciana.service.ActorService;
import com.granzonamarciana.service.EdicionService;
import com.granzonamarciana.service.SolicitudService;

import java.util.ArrayList;
import java.util.List;

public class ListEdicion extends AppCompatActivity {

    private EdicionService edicionService;
    private SolicitudService solicitudService;
    private ActorService actorService;
    private EdicionAdapter edicionAdapter;
    private String rolSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_edicion);

        edicionService = new EdicionService(this);
        solicitudService = new SolicitudService(this);
        actorService = new ActorService(this);

        // Obtener rol de la sesión
        SharedPreferences prefs = getSharedPreferences("granzonaUser", MODE_PRIVATE);
        rolSesion = prefs.getString("rol", "");

        ListView lvEdiciones = findViewById(R.id.lvEdiciones);
        TextView tvSinEdiciones = findViewById(R.id.tvSinEdiciones);
        MaterialButton btnCrearEdicion = findViewById(R.id.btnCrearEdicion);
        MaterialButton btnVolver = findViewById(R.id.btnVolver);

        lvEdiciones.setEmptyView(tvSinEdiciones);

        edicionAdapter = new EdicionAdapter(this, new ArrayList<>());
        lvEdiciones.setAdapter(edicionAdapter);

        edicionService.listarEdiciones().observe(this, new Observer<List<Edicion>>() {
            @Override
            public void onChanged(List<Edicion> ediciones) {
                edicionAdapter.clear();
                edicionAdapter.addAll(ediciones);
                edicionAdapter.notifyDataSetChanged();
            }
        });

        // CAMBIO: Al hacer clic en una edición
        lvEdiciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Edicion edicion = (Edicion) parent.getItemAtPosition(position);

                // Si es admin, va al formulario de edición
                if (rolSesion.equals(TipoRol.ADMINISTRADOR.toString())) {
                    Intent i = new Intent(ListEdicion.this, FormEdicion.class);
                    i.putExtra("edicion", edicion);
                    startActivity(i);
                } else {
                    // CAMBIO: Para invitados y usuarios, mostrar info pública
                    mostrarDetalleEdicion(edicion);
                }
            }
        });

        // Botón crear - solo visible para admin
        if (rolSesion.equals(TipoRol.ADMINISTRADOR.toString())) {
            btnCrearEdicion.setVisibility(View.VISIBLE);
            btnCrearEdicion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(ListEdicion.this, FormEdicion.class);
                    startActivity(i);
                }
            });
        } else {
            btnCrearEdicion.setVisibility(View.GONE);
        }

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // NUEVO MÉTODO: Mostrar información de la edición en un diálogo
    private void mostrarDetalleEdicion(Edicion edicion) {
        // Construir mensaje con información de la edición
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("Fechas:\n");
        mensaje.append("Inicio: ").append(edicion.getFechaInicio()).append("\n");
        mensaje.append("Fin: ").append(edicion.getFechaFin()).append("\n\n");
        mensaje.append("Máximo de participantes: ").append(edicion.getMaxParticipantes()).append("\n\n");

        // Obtener participantes aceptados
        solicitudService.listarSolicitudesPorEdicion(edicion.getId()).observe(this, solicitudes -> {
            if (solicitudes != null) {
                List<String> participantes = new ArrayList<>();

                for (int i = 0; i < solicitudes.size(); i++) {
                    if (solicitudes.get(i).getEstado() == EstadoSolicitud.ACEPTADA) {
                        int idConcursante = solicitudes.get(i).getIdConcursante();
                        // Obtener nombre del participante
                        actorService.buscarPorId(idConcursante).observe(this, actor -> {
                            if (actor != null) {
                                String nombre = actor.getNombre() + " " + actor.getApellido1();
                                if (!participantes.contains(nombre)) {
                                    participantes.add(nombre);
                                }
                            }
                        });
                    }
                }

                mensaje.append("Participantes confirmados: ").append(participantes.size()).append("\n");
                if (!participantes.isEmpty()) {
                    mensaje.append("\nParticipantes:\n");
                    for (String p : participantes) {
                        mensaje.append("• ").append(p).append("\n");
                    }
                }

                // Mostrar diálogo con información
                new AlertDialog.Builder(this)
                        .setTitle("Edición " + edicion.getId())
                        .setMessage(mensaje.toString())
                        .setPositiveButton("Ver Galas", (dialog, which) -> {
                            // Ver galas de esta edición
                            Intent intent = new Intent(ListEdicion.this, ListGala.class);
                            intent.putExtra("idEdicion", edicion.getId());
                            startActivity(intent);
                        })
                        .setNegativeButton("Cerrar", null)
                        .show();
            }
        });
    }
}