package com.granzonamarciana.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.granzonamarciana.R;
import com.granzonamarciana.entity.Solicitud;
import com.granzonamarciana.service.ActorService;

import java.util.List;

public class SolicitudAdapter extends ArrayAdapter<Solicitud> {

    private final ActorService actorService;

    public SolicitudAdapter(@NonNull Context context, @NonNull List<Solicitud> solicitudes) {
        super(context, 0, solicitudes);
        this.actorService = new ActorService(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_solicitud, parent, false);
        }

        Solicitud solicitud = getItem(position);

        // Referencias a los IDs exactos de tu nuevo item_solicitud.xml
        TextView tvEstadoSolicitud = convertView.findViewById(R.id.tvEstadoSolicitud);
        TextView tvFechaSolicitud = convertView.findViewById(R.id.tvFechaSolicitud);
        TextView tvDescripcionSolicitud = convertView.findViewById(R.id.tvDescripcionSolicitud);

        if (solicitud != null) {
            // 1. Descripción/Mensaje de la propuesta
            tvDescripcionSolicitud.setText(solicitud.getMensaje());

            // 2. "Fecha" o Identificador (Como no hay fecha en la entidad, usamos el ID)
            tvFechaSolicitud.setText("Referencia ID: " + solicitud.getId());

            // 3. Estado y Nombre del Concursante
            // Ponemos el estado inicialmente
            tvEstadoSolicitud.setText("Estado: " + solicitud.getEstado().toString());

            // Buscamos el nombre para que el Admin sepa quién es el solicitante
            actorService.buscarPorId(solicitud.getIdConcursante()).observeForever(actor -> {
                if (actor != null) {
                    String nombre = actor.getNombre() + " " + actor.getApellido1();
                    tvEstadoSolicitud.setText(solicitud.getEstado().toString() + " - " + nombre);
                }
            });

            // Colores según el estado para la gestión visual del Admin
            switch (solicitud.getEstado()) {
                case ACEPTADA:
                    tvEstadoSolicitud.setTextColor(getContext().getColor(android.R.color.holo_green_dark));
                    break;
                case RECHAZADA:
                    tvEstadoSolicitud.setTextColor(getContext().getColor(android.R.color.holo_red_dark));
                    break;
                default:
                    tvEstadoSolicitud.setTextColor(getContext().getColor(android.R.color.black));
                    break;
            }
        }

        return convertView;
    }
}