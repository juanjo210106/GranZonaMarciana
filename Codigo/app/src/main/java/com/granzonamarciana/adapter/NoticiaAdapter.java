package com.granzonamarciana.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
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
import com.squareup.picasso.Picasso;

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
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_noticia, parent, false);
            holder = new ViewHolder();
            holder.imgNoticia = convertView.findViewById(R.id.imgNoticia);
            holder.tvTitulo = convertView.findViewById(R.id.tvTituloNoticia);
            holder.tvCuerpo = convertView.findViewById(R.id.tvCuerpoNoticia);
            holder.imgEliminar = convertView.findViewById(R.id.imgEliminarNoticia);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Noticia noticia = getItem(position);

        if (noticia != null) {
            holder.tvTitulo.setText(noticia.getCabecera());
            holder.tvCuerpo.setText(noticia.getCuerpo());

            // Cargar imagen de la noticia con Picasso
            String urlImagen = noticia.getUrlImagen();
            if (!TextUtils.isEmpty(urlImagen)) {
                Picasso.get()
                        .load(urlImagen)
                        .placeholder(android.R.drawable.ic_menu_report_image) // Imagen mientras carga
                        .error(android.R.drawable.ic_menu_gallery) // Imagen si falla
                        .resize(150, 150) // Redimensionar para optimizar
                        .centerCrop()
                        .into(holder.imgNoticia);
            } else {
                // Si no hay URL, mostrar icono por defecto
                holder.imgNoticia.setImageResource(android.R.drawable.ic_menu_report_image);
            }

            // Gestión del botón eliminar (solo para Admin)
            if (rolUsuario.equals(TipoRol.ADMINISTRADOR.toString())) {
                holder.imgEliminar.setVisibility(View.VISIBLE);
                holder.imgEliminar.setOnClickListener(v -> {
                    if (deleteListener != null) {
                        deleteListener.onNoticiaDelete(noticia);
                    }
                });
            } else {
                holder.imgEliminar.setVisibility(View.GONE);
            }
        }

        return convertView;
    }

    // Patrón ViewHolder para optimizar el rendimiento
    static class ViewHolder {
        ImageView imgNoticia;
        TextView tvTitulo;
        TextView tvCuerpo;
        ImageView imgEliminar;
    }
}