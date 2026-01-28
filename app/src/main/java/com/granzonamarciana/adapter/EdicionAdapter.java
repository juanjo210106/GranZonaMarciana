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
import com.granzonamarciana.entity.Edicion;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class EdicionAdapter extends ArrayAdapter<Edicion> {

    public EdicionAdapter(@NonNull Context context, @NonNull List<Edicion> ediciones) {
        super(context, 0, ediciones);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Edicion edicion = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_edicion, parent, false);
        }

        TextView tvTituloEdicion = convertView.findViewById(R.id.tvTituloEdicion);
        TextView tvFechas = convertView.findViewById(R.id.tvFechas);
        TextView tvParticipantes = convertView.findViewById(R.id.tvParticipantes);

        if (edicion != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            String titulo = getContext().getString(R.string.txt_titulo_edicion);
            tvTituloEdicion.setText(titulo + " " + edicion.getId());

            String inicioLabel = getContext().getString(R.string.txt_inicio);
            String finLabel = getContext().getString(R.string.txt_fin);

            String inicio = edicion.getFechaInicio() != null ? edicion.getFechaInicio().format(formatter) : "";
            String fin = edicion.getFechaFin() != null ? edicion.getFechaFin().format(formatter) : "";

            tvFechas.setText(inicioLabel + ": " + inicio + " - " + finLabel + ": " + fin);

            String partLabel = getContext().getString(R.string.txt_max_participantes);
            tvParticipantes.setText(partLabel + ": " + edicion.getMaxParticipantes());
        }

        return convertView;
    }
}