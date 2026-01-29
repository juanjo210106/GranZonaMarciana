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

        TextView tvTituloGala = convertView.findViewById(R.id.tvTituloGala);
        TextView tvFechaHoraGala = convertView.findViewById(R.id.tvFechaHoraGala);

        if (gala != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            tvTituloGala.setText(getContext().getString(R.string.txt_titulo_gala) + " #" + gala.getId());
            tvFechaHoraGala.setText(gala.getFechaRealizacion().format(formatter));
        }

        return convertView;
    }
}
