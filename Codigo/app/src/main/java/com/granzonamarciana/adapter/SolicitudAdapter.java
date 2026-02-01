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

import java.util.List;

public class SolicitudAdapter extends ArrayAdapter<Solicitud> {

    public SolicitudAdapter(@NonNull Context context, @NonNull List<Solicitud> solicitudes) {
        super(context, 0, solicitudes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Solicitud s = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_solicitud, parent, false);
        }

        TextView tvEstado = convertView.findViewById(R.id.tvEstadoSolicitud);
        TextView tvMensaje = convertView.findViewById(R.id.tvDescripcionSolicitud);

        if (s != null) {
            tvEstado.setText("ESTADO: " + s.getEstado().toString());
            tvMensaje.setText(s.getMensaje());
        }

        return convertView;
    }
}