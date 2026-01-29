package com.granzonamarciana.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.granzonamarciana.R;
import com.granzonamarciana.entity.Noticia;
import com.granzonamarciana.service.NoticiaService;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NoticiaAdapter extends ArrayAdapter<Noticia> {
    private NoticiaService noticiaService;

    public NoticiaAdapter(@NonNull Context context, @NonNull List<Noticia> noticias) {
        super(context, 0, noticias);
        noticiaService = new NoticiaService(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Noticia noticia = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_noticia, parent, false);
        }

        ImageView imgNoticia = convertView.findViewById(R.id.imgNoticia);
        TextView tvTituloNoticia = convertView.findViewById(R.id.tvTituloNoticia);
        TextView tvCuerpoNoticia = convertView.findViewById(R.id.tvCuerpoNoticia);
        ImageView imgEliminarNoticia = convertView.findViewById(R.id.imgEliminarNoticia);

        if (noticia != null) {
            tvTituloNoticia.setText(noticia.getCabecera());
            tvCuerpoNoticia.setText(noticia.getCuerpo());

            if (noticia.getUrlImagen() != null && !noticia.getUrlImagen().isEmpty()) {
                Picasso.get().load(noticia.getUrlImagen()).into(imgNoticia);
            }

            imgEliminarNoticia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    noticiaService.eliminarNoticia(noticia);
                }
            });
        }

        return convertView;
    }
}