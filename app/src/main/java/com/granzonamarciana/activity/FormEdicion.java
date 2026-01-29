package com.granzonamarciana.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.granzonamarciana.R;
import com.granzonamarciana.entity.Edicion;
import com.granzonamarciana.service.EdicionService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FormEdicion extends AppCompatActivity {
    private EdicionService edicionService;
    private Edicion edicionExistente;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_edicion);

        edicionService = new EdicionService(FormEdicion.this);

        EditText etFechaInicio = findViewById(R.id.etFechaInicio);
        EditText etFechaFin = findViewById(R.id.etFechaFin);
        EditText etMaxParticipantes = findViewById(R.id.etMaxParticipantes);
        Button btnGuardar = findViewById(R.id.btnGuardar);

        // Se rellenan campos del formulario si venimos de editar (Estilo maestro)
        edicionExistente = (Edicion) getIntent().getSerializableExtra("edicion");
        if (edicionExistente != null) {
            etFechaInicio.setText(edicionExistente.getFechaInicio().format(formatter));
            etFechaFin.setText(edicionExistente.getFechaFin().format(formatter));
            etMaxParticipantes.setText(String.valueOf(edicionExistente.getMaxParticipantes()));
            ((TextView) findViewById(R.id.tvTitulo)).setText("Editar Edición");
        }

        // Editar / Crear Edición
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etFechaInicio.getText().toString().isEmpty() ||
                        etFechaFin.getText().toString().isEmpty() ||
                        etMaxParticipantes.getText().toString().isEmpty()) {
                    Toast.makeText(FormEdicion.this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    LocalDate inicio = LocalDate.parse(etFechaInicio.getText().toString(), formatter);
                    LocalDate fin = LocalDate.parse(etFechaFin.getText().toString(), formatter);
                    int max = Integer.parseInt(etMaxParticipantes.getText().toString());

                    if (edicionExistente != null) {
                        edicionExistente.setFechaInicio(inicio);
                        edicionExistente.setFechaFin(fin);
                        edicionExistente.setMaxParticipantes(max);

                        edicionService.actualizarEdicion(edicionExistente);
                        Toast.makeText(FormEdicion.this, "Edición actualizada", Toast.LENGTH_SHORT).show();
                    } else {
                        Edicion nuevaEdicion = new Edicion(inicio, fin, max);

                        // Uso el nombre exacto de tu Service: 'insertarEdcicion'
                        edicionService.insertarEdcicion(nuevaEdicion);
                        Toast.makeText(FormEdicion.this, "Edición creada", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                } catch (Exception e) {
                    Toast.makeText(FormEdicion.this, "Formato de fecha incorrecto (dd/MM/yyyy)", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Volver
        Button btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}