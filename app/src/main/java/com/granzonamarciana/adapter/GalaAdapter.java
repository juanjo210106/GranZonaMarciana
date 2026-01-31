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
import com.granzonamarciana.entity.Gala;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class GalaAdapter extends ArrayAdapter<Gala> {

    // Formato de fecha para mostrar al usuario (ej: 31/01/2026 21:00)
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public GalaAdapter(@NonNull Context context, @NonNull List<Gala> galas) {
        super(context, 0, galas);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Gala gala = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_gala, parent, false);
        }

        TextView tvNombre = convertView.findViewById(R.id.tvNombreGala);
        TextView tvFecha = convertView.findViewById(R.id.tvFechaGala);

        if (gala != null) {
            // Usamos el string "Gala #ID"
            tvNombre.setText(getContext().getString(R.string.txt_gala_numero, gala.getId()));

            // Formateamos la fecha y usamos el string "Realización: ..."
            String fechaFormateada = gala.getFechaRealizacion().format(formatter);
            tvFecha.setText(getContext().getString(R.string.txt_fecha_realizacion, fechaFormateada));
        }

        return convertView;
    }
}