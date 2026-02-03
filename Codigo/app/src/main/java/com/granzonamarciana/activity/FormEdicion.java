package com.granzonamarciana.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.granzonamarciana.R;
import com.granzonamarciana.entity.Edicion;
import com.granzonamarciana.service.EdicionService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class FormEdicion extends AppCompatActivity {
    private EdicionService edicionService;
    private Edicion edicionExistente;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_edicion);

        edicionService = new EdicionService(FormEdicion.this);

        // Referencias actualizadas a TextInputEditText para evitar errores
        TextInputEditText etFechaInicio = findViewById(R.id.etFechaInicio);
        TextInputEditText etFechaFin = findViewById(R.id.etFechaFin);
        TextInputEditText etMaxParticipantes = findViewById(R.id.etMaxParticipantes);
        Button btnGuardar = findViewById(R.id.btnGuardar);
        Button btnVolver = findViewById(R.id.btnVolver);

        // LÓGICA DE CALENDARIO
        etFechaInicio.setOnClickListener(v -> mostrarCalendario(etFechaInicio));
        etFechaFin.setOnClickListener(v -> mostrarCalendario(etFechaFin));

        // Rellenar campos si venimos de editar
        edicionExistente = (Edicion) getIntent().getSerializableExtra("edicion");
        if (edicionExistente != null) {
            etFechaInicio.setText(edicionExistente.getFechaInicio().format(formatter));
            etFechaFin.setText(edicionExistente.getFechaFin().format(formatter));
            etMaxParticipantes.setText(String.valueOf(edicionExistente.getMaxParticipantes()));
            ((TextView) findViewById(R.id.tvTitulo)).setText("Editar Edición");
        }

        btnGuardar.setOnClickListener(v -> {
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
                    edicionService.insertarEdcicion(nuevaEdicion);
                    Toast.makeText(FormEdicion.this, "Edición creada", Toast.LENGTH_SHORT).show();
                }
                finish();
            } catch (Exception e) {
                Toast.makeText(FormEdicion.this, "Formato de fecha o datos incorrectos", Toast.LENGTH_SHORT).show();
            }
        });

        btnVolver.setOnClickListener(v -> finish());
    }

    // Método que abre el calendario
    private void mostrarCalendario(TextInputEditText editText) {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            // Formato de calendario
            String fecha = String.format("%02d/%02d/%d", dayOfMonth, (month + 1), year);
            editText.setText(fecha);
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }
}