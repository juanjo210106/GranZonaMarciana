package com.granzonamarciana.adapter;

import android.content.Context;
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
import com.granzonamarciana.entity.Actor;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ActorAdapter extends ArrayAdapter<Actor> {

    public ActorAdapter(@NonNull Context context, @NonNull List<Actor> actores) {
        super(context, 0, actores);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_usuario, parent, false);
            holder = new ViewHolder();
            holder.imgPerfil = convertView.findViewById(R.id.imgPerfilUsuario);
            holder.tvNombreActor = convertView.findViewById(R.id.tvNombreActor);
            holder.tvUsername = convertView.findViewById(R.id.tvUsername);
            holder.tvRol = convertView.findViewById(R.id.tvRol);
            holder.tvCorreo = convertView.findViewById(R.id.tvCorreo);
            holder.tvTelefono = convertView.findViewById(R.id.tvTelefono);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Actor actor = getItem(position);

        if (actor != null) {
            // 1. Nombre completo (Nombre + Apellidos)
            String nombreCompleto = actor.getNombre() + " " + actor.getApellido1();
            if (actor.getApellido2() != null && !actor.getApellido2().isEmpty()) {
                nombreCompleto += " " + actor.getApellido2();
            }
            holder.tvNombreActor.setText(nombreCompleto);

            // 2. Nombre de usuario
            holder.tvUsername.setText(actor.getUsername());

            // 3. Rol del sistema (Concursante, Espectador, etc.)
            if (actor.getRol() != null) {
                holder.tvRol.setText("Rol: " + actor.getRol().toString());
            }

            // 4. Correo electrónico
            holder.tvCorreo.setText(actor.getCorreo());

            // 5. Teléfono de contacto
            holder.tvTelefono.setText(actor.getTelefono());

            // 6. Cargar imagen de perfil con Picasso
            String urlImagen = actor.getUrlImagen();
            if (!TextUtils.isEmpty(urlImagen)) {
                Picasso.get()
                        .load(urlImagen)
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(android.R.drawable.ic_menu_report_image)
                        .resize(150, 150)
                        .centerCrop()
                        .into(holder.imgPerfil);
            } else {
                holder.imgPerfil.setImageResource(android.R.drawable.ic_menu_report_image);
            }
        }

        return convertView;
    }

    static class ViewHolder {
        ImageView imgPerfil;
        TextView tvNombreActor;
        TextView tvUsername;
        TextView tvRol;
        TextView tvCorreo;
        TextView tvTelefono;
    }
}