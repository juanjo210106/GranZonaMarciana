package com.granzonamarciana.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.granzonamarciana.R;
import com.granzonamarciana.entity.Noticia;
import com.granzonamarciana.service.NoticiaService;

import java.time.LocalDateTime;

public class FormNoticia extends AppCompatActivity {
    private NoticiaService noticiaService;
    private Noticia noticiaExistente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_noticia);

        noticiaService = new NoticiaService(FormNoticia.this);

        EditText etCabecera = findViewById(R.id.etCabeceraNoticia);
        EditText etCuerpo = findViewById(R.id.etCuerpoNoticia);
        EditText etUrlImagen = findViewById(R.id.etUrlImagenNoticia);
        Button btnGuardar = findViewById(R.id.btnGuardarNoticia);
        TextView tvHeader = findViewById(R.id.tvHeaderNoticia);

        // Se rellenan campos si es edición (Estilo maestro)
        noticiaExistente = (Noticia) getIntent().getSerializableExtra("noticia");
        if (noticiaExistente != null) {
            etCabecera.setText(noticiaExistente.getCabecera());
            etCuerpo.setText(noticiaExistente.getCuerpo());
            etUrlImagen.setText(noticiaExistente.getUrlImagen());
            tvHeader.setText(getString(R.string.titulo_editar_noticia));
        }

        // Guardar o Actualizar
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cabecera = etCabecera.getText().toString();
                String cuerpo = etCuerpo.getText().toString();
                String url = etUrlImagen.getText().toString();

                if (cabecera.isEmpty() || cuerpo.isEmpty()) {
                    Toast.makeText(FormNoticia.this, getString(R.string.error_noticia_vacia), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (noticiaExistente != null) {
                    noticiaExistente.setCabecera(cabecera);
                    noticiaExistente.setCuerpo(cuerpo);
                    noticiaExistente.setUrlImagen(url);
                    noticiaService.actualizarNoticia(noticiaExistente);
                    Toast.makeText(FormNoticia.this, getString(R.string.msg_noticia_actualizada), Toast.LENGTH_SHORT).show();
                } else {
                    Noticia nuevaNoticia = new Noticia(
                            LocalDateTime.now(), // Fecha automática (Estilo maestro)
                            cabecera,
                            cuerpo,
                            url
                    );
                    noticiaService.insertarNoticia(nuevaNoticia);
                    Toast.makeText(FormNoticia.this, getString(R.string.msg_noticia_creada), Toast.LENGTH_SHORT).show();
                }

                finish(); // Volver al listado
            }
        });

        // Botón Volver
        Button btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}