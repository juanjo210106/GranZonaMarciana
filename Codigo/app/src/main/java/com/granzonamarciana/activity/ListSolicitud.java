package com.granzonamarciana.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.granzonamarciana.R;
import com.granzonamarciana.adapter.SolicitudAdapter;
import com.granzonamarciana.entity.EstadoSolicitud;
import com.granzonamarciana.entity.Solicitud;
import com.granzonamarciana.service.SolicitudService;

import java.util.ArrayList;

public class ListSolicitud extends AppCompatActivity {

    private SolicitudService solicitudService;
    private SolicitudAdapter solicitudAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_solicitud);

        // 1. Inicializar componentes de la UI
        ListView lvSolicitudes = findViewById(R.id.lvSolicitudes);
        TextView tvSinSolicitudes = findViewById(R.id.tvSinSolicitudes);

        // 2. Configurar el adaptador
        solicitudAdapter = new SolicitudAdapter(this, new ArrayList<>());
        lvSolicitudes.setAdapter(solicitudAdapter);

        // El EmptyView muestra tvSinSolicitudes si la lista está vacía
        lvSolicitudes.setEmptyView(tvSinSolicitudes);

        // 3. Inicializar servicio y observar los datos
        solicitudService = new SolicitudService(this);

        // Requisito: Consultar solicitudes de participación
        solicitudService.listarSolicitudes().observe(this, solicitudes -> {
            solicitudAdapter.clear();
            if (solicitudes != null) {
                solicitudAdapter.addAll(solicitudes);
            }
            solicitudAdapter.notifyDataSetChanged();
        });

        // 4. Gestión de solicitudes al pulsar
        lvSolicitudes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gestionarSolicitud(solicitudAdapter.getItem(position));
            }
        });

        // 5. Botón Volver (Asegúrate de que el ID sea btnVolverSolicitud en tu XML)
        View btnVolver = findViewById(R.id.btnVolverSolicitud);
        if (btnVolver != null) {
            btnVolver.setOnClickListener(v -> finish());
        }
    }

    // Mostramos diálogo para aprobar o rechazar la solicitud
    private void gestionarSolicitud(Solicitud s) {
        if (s == null) return;

        // Solo permitimos gestionar si todavía está PENDIENTE
        if (s.getEstado() != EstadoSolicitud.PENDIENTE) {
            Toast.makeText(this, "Esta solicitud ya ha sido procesada (" + s.getEstado() + ")", Toast.LENGTH_SHORT).show();
            return;
        }

        // Creamos el diálogo
        AlertDialog dialog = new AlertDialog.Builder(this, R.style.Theme_GranZonaMarciana_Dialog)
                .setTitle("Gestionar Solicitud")
                .setMessage("¿Deseas aceptar a este usuario como concursante?")
                .setPositiveButton("ACEPTAR", (d, which) -> {
                    s.setEstado(EstadoSolicitud.ACEPTADA);
                    solicitudService.actualizarSolicitud(s);
                    Toast.makeText(this, "Solicitud Aceptada", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("RECHAZAR", (d, which) -> {
                    s.setEstado(EstadoSolicitud.RECHAZADA);
                    solicitudService.actualizarSolicitud(s);
                    Toast.makeText(this, "Solicitud Rechazada", Toast.LENGTH_SHORT).show();
                })
                .setNeutralButton("CANCELAR", null)
                .create();

        dialog.show();

        // Color verde a las alertas (que si no me da TOC que salga morado)
        int colorPrincipal = ContextCompat.getColor(this, R.color.color_principal_oscuro);
        if (dialog.getButton(AlertDialog.BUTTON_POSITIVE) != null) {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(colorPrincipal);
        }
        if (dialog.getButton(AlertDialog.BUTTON_NEGATIVE) != null) {
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(colorPrincipal);
        }
        if (dialog.getButton(AlertDialog.BUTTON_NEUTRAL) != null) {
            dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(colorPrincipal);
        }
    }
}