package com.granzonamarciana.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

        // Inicializar componentes de la UI
        ListView lvSolicitudes = findViewById(R.id.lvSolicitudes);
        TextView tvSinSolicitudes = findViewById(R.id.tvSinSolicitudes);

        // Configurar el adaptador
        solicitudAdapter = new SolicitudAdapter(this, new ArrayList<>());
        lvSolicitudes.setAdapter(solicitudAdapter);

        // El EmptyView muestra tvSinSolicitudes si la lista está vacía
        lvSolicitudes.setEmptyView(tvSinSolicitudes);

        // Inicializar servicio y observar los datos con LiveData
        solicitudService = new SolicitudService(this);
        solicitudService.listarSolicitudes().observe(this, solicitudes -> {
            solicitudAdapter.clear();
            if (solicitudes != null) {
                solicitudAdapter.addAll(solicitudes);
            }
            solicitudAdapter.notifyDataSetChanged();
        });

        // Lógica para gestionar la solicitud al pulsar en ella
        lvSolicitudes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gestionarSolicitud(solicitudAdapter.getItem(position));
            }
        });

        // Botón Volver
        findViewById(R.id.btnVolverSolicitud).setOnClickListener(v -> finish());
    }


    private void gestionarSolicitud(Solicitud s) {
        // Solo permitimos gestionar si todavía está PENDIENTE
        if (s.getEstado() != EstadoSolicitud.PENDIENTE) {
            Toast.makeText(this, "Esta solicitud ya ha sido procesada", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Gestionar Solicitud")
                .setMessage("¿Deseas aceptar a este usuario como concursante?")
                .setPositiveButton("ACEPTAR", (dialog, which) -> {
                    s.setEstado(EstadoSolicitud.ACEPTADA);
                    solicitudService.actualizarSolicitud(s);
                    Toast.makeText(this, "Solicitud Aceptada", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("RECHAZAR", (dialog, which) -> {
                    s.setEstado(EstadoSolicitud.RECHAZADA);
                    solicitudService.actualizarSolicitud(s);
                    Toast.makeText(this, "Solicitud Rechazada", Toast.LENGTH_SHORT).show();
                })
                .setNeutralButton("CANCELAR", null)
                .show();
    }
}