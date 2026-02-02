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
import com.granzonamarciana.entity.Actor;

import java.util.List;

public class ActorAdapter extends ArrayAdapter<Actor> {

    public ActorAdapter(@NonNull Context context, @NonNull List<Actor> actores) {
        super(context, 0, actores);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Inflamos el layout item_usuario.xml si es necesario
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_usuario, parent, false);
        }

        // Obtenemos el actor de la posición actual
        Actor actor = getItem(position);

        // Referencias a los IDs exactos de tu item_usuario.xml
        TextView tvNombreActor = convertView.findViewById(R.id.tvNombreActor);
        TextView tvUsername = convertView.findViewById(R.id.tvUsername);
        TextView tvRol = convertView.findViewById(R.id.tvRol);
        TextView tvCorreo = convertView.findViewById(R.id.tvCorreo);
        TextView tvTelefono = convertView.findViewById(R.id.tvTelefono);

        if (actor != null) {
            // 1. Nombre completo (Nombre + Apellidos)
            String nombreCompleto = actor.getNombre() + " " + actor.getApellido1();
            if (actor.getApellido2() != null && !actor.getApellido2().isEmpty()) {
                nombreCompleto += " " + actor.getApellido2();
            }
            tvNombreActor.setText(nombreCompleto);

            // 2. Nombre de usuario
            tvUsername.setText(actor.getUsername());

            // 3. Rol del sistema (Concursante, Espectador, etc.)
            if (actor.getRol() != null) {
                tvRol.setText("Rol: " + actor.getRol().toString());
            }

            // 4. Correo electrónico
            tvCorreo.setText(actor.getCorreo());

            // 5. Teléfono de contacto
            tvTelefono.setText(actor.getTelefono());
        }

        return convertView;
    }
}