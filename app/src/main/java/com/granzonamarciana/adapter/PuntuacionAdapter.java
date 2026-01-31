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
        // 1. Obtener el objeto de datos para esta posición
        Puntuacion puntuacion = getItem(position);

        // 2. Comprobar si una vista existente está siendo reutilizada, de lo contrario inflar la vista
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_puntuacion, parent, false);
        }

        // 3. Buscar las vistas para la población de datos
        TextView tvValor = convertView.findViewById(R.id.tvValorPuntuacion);
        TextView tvDetalle = convertView.findViewById(R.id.tvDetalleVoto);

        // 4. Poblar los datos en las vistas usando los recursos de strings
        if (puntuacion != null) {
            // Usamos el string formateado "Puntos: %1$d"
            tvValor.setText(getContext().getString(R.string.txt_puntos, puntuacion.getValor()));

            // Usamos el string formateado "Concursante ID: %1$d | Espectador ID: %2$d"
            // Esto es muy útil para el Admin para rastrear quién votó
            tvDetalle.setText(getContext().getString(
                    R.string.label_voto_detalle,
                    puntuacion.getIdConcursante(),
                    puntuacion.getIdEspectador()
            ));
        }

        return convertView;
    }
}