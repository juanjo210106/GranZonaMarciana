package com.granzonamarciana.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.granzonamarciana.R;
import com.granzonamarciana.entity.Noticia;
import com.granzonamarciana.entity.TipoRol;
import com.granzonamarciana.service.NoticiaService;

import java.time.LocalDateTime;

public class FormNoticia extends AppCompatActivity {
    private NoticiaService noticiaService;
    private Noticia noticiaExistente;
    private String rolSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_noticia);

        noticiaService = new NoticiaService(this);

        // Recuperar rol para seguridad
        SharedPreferences prefs = getSharedPreferences("granzonaUser", MODE_PRIVATE);
        rolSesion = prefs.getString("rol", "");

        EditText etCabecera = findViewById(R.id.etCabeceraNoticia);
        EditText etCuerpo = findViewById(R.id.etCuerpoNoticia);
        EditText etUrlImagen = findViewById(R.id.etUrlImagenNoticia);
        Button btnGuardar = findViewById(R.id.btnGuardarNoticia);
        Button btnVolver = findViewById(R.id.btnVolver);
        TextView tvHeader = findViewById(R.id.tvHeaderNoticia);

        noticiaExistente = (Noticia) getIntent().getSerializableExtra("noticia");

        // REQUISITO: Solo el Admin puede editar o guardar
        boolean esAdmin = rolSesion.equals(TipoRol.ADMINISTRADOR.toString());

        if (noticiaExistente != null) {
            etCabecera.setText(noticiaExistente.getCabecera());
            etCuerpo.setText(noticiaExistente.getCuerpo());
            etUrlImagen.setText(noticiaExistente.getUrlImagen());
            tvHeader.setText(esAdmin ? getString(R.string.titulo_editar_noticia) : "Detalle de Noticia");

            // Si no es admin, bloqueamos edición (Modo Visualización de Detalle)
            if (!esAdmin) {
                etCabecera.setEnabled(false);
                etCuerpo.setEnabled(false);
                etUrlImagen.setEnabled(false);
                btnGuardar.setVisibility(View.GONE);
            }
        } else if (!esAdmin) {
            // Un no-admin no debería poder intentar crear una noticia
            Toast.makeText(this, "Acceso denegado", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnGuardar.setOnClickListener(v -> {
            String cabecera = etCabecera.getText().toString().trim();
            String cuerpo = etCuerpo.getText().toString().trim();
            String url = etUrlImagen.getText().toString().trim();

            if (cabecera.isEmpty() || cuerpo.isEmpty()) {
                Toast.makeText(this, "Título y cuerpo obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            if (noticiaExistente != null) {
                noticiaExistente.setCabecera(cabecera);
                noticiaExistente.setCuerpo(cuerpo);
                noticiaExistente.setUrlImagen(url);
                noticiaService.actualizarNoticia(noticiaExistente);
                Toast.makeText(this, "Noticia actualizada", Toast.LENGTH_SHORT).show();
            } else {
                // RNF: La fecha se genera automáticamente al crear
                Noticia nueva = new Noticia(LocalDateTime.now(), cabecera, cuerpo, url);
                noticiaService.insertarNoticia(nueva);
                Toast.makeText(this, "Noticia creada", Toast.LENGTH_SHORT).show();
            }
            finish();
        });

        btnVolver.setOnClickListener(v -> finish());
    }
}