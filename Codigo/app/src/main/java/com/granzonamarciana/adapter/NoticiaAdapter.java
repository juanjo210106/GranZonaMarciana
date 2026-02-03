package com.granzonamarciana.adapter;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.granzonamarciana.entity.TipoRol;

import java.util.List;

public class NoticiaAdapter extends ArrayAdapter<Noticia> {

    private final String rolUsuario;
    private final OnNoticiaDeleteListener deleteListener;

    // Interfaz para comunicar el borrado a la Activity
    public interface OnNoticiaDeleteListener {
        void onNoticiaDelete(Noticia noticia);
    }

    public NoticiaAdapter(@NonNull Context context, @NonNull List<Noticia> noticias, OnNoticiaDeleteListener listener) {
        super(context, 0, noticias);
        this.deleteListener = listener;

        // Recuperamos el rol para saber si mostramos la papelera
        SharedPreferences sp = context.getSharedPreferences("granzonaUser", Context.MODE_PRIVATE);
        this.rolUsuario = sp.getString("rol", "");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_noticia, parent, false);
        }

        Noticia noticia = getItem(position);

        ImageView imgNoticia = convertView.findViewById(R.id.imgNoticia);
        TextView tvTitulo = convertView.findViewById(R.id.tvTituloNoticia);
        TextView tvCuerpo = convertView.findViewById(R.id.tvCuerpoNoticia);
        ImageView imgEliminar = convertView.findViewById(R.id.imgEliminarNoticia);

        if (noticia != null) {
            tvTitulo.setText(noticia.getCabecera());
            tvCuerpo.setText(noticia.getCuerpo());

            if (rolUsuario.equals(TipoRol.ADMINISTRADOR.toString())) {
                imgEliminar.setVisibility(View.VISIBLE);
                imgEliminar.setOnClickListener(v -> {
                    if (deleteListener != null) {
                        deleteListener.onNoticiaDelete(noticia);
                    }
                });
            } else {
                imgEliminar.setVisibility(View.GONE);
            }

            imgNoticia.setImageResource(android.R.drawable.ic_menu_report_image);
        }

        return convertView;
    }
}