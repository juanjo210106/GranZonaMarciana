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
import com.granzonamarciana.entity.Puntuacion;

import java.util.List;

public class PuntuacionAdapter extends ArrayAdapter<Puntuacion> {

    public PuntuacionAdapter(@NonNull Context context, @NonNull List<Puntuacion> puntuaciones) {
        super(context, 0, puntuaciones);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // 1. Obtener el objeto para esta posición
        Puntuacion p = getItem(position);

        // 2. Inflar el layout si no se está reciclando (Estilo Maestro)
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_puntuacion, parent, false);
        }

        // 3. Vincular componentes del XML
        TextView tvValor = convertView.findViewById(R.id.tvValorPuntuacion);
        TextView tvDetalle = convertView.findViewById(R.id.tvDetalleVoto);

        // 4. Aplicar los datos de tu entidad
        if (p != null) {
            // USAMOS getValor() tal como está en tu entity
            tvValor.setText("Puntuación: " + p.getValor());

            // Mostramos los IDs relacionados
            String detalle = "Gala: " + p.getIdGala() + " | Concursante: " + p.getIdConcursante();
            tvDetalle.setText(detalle);
        }

        return convertView;
    }
}