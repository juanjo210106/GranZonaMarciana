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
import com.granzonamarciana.entity.Usuario;

import java.util.List;

public class UsuarioAdapter extends ArrayAdapter<Usuario> {

    public UsuarioAdapter(@NonNull Context context, @NonNull List<Usuario> usuarios) {
        super(context, 0, usuarios);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Usuario u = getItem(position);

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_usuario, parent, false);
        }

        TextView tvNombreActor = convertView.findViewById(R.id.tvNombreActor);
        TextView tvUsername = convertView.findViewById(R.id.tvUsername);
        TextView tvCorreo = convertView.findViewById(R.id.tvCorreo);
        TextView tvTelefono = convertView.findViewById(R.id.tvTelefono); // Cambiado por tus requisitos

        // Construimos el nombre completo usando tus atributos
        String nombreCompleto = u.getNombre() + " " + u.getApellido1();
        if (u.getApellido2() != null && !u.getApellido2().isEmpty()) {
            nombreCompleto += " " + u.getApellido2();
        }
        tvNombreActor.setText(nombreCompleto);

        tvUsername.setText(u.getUsername());
        tvCorreo.setText(u.getCorreo());
        tvTelefono.setText("Tel: " + u.getTelefono());

        return convertView;
    }
}