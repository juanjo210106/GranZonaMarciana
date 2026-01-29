package com.granzonamarciana.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.granzonamarciana.R;
import com.granzonamarciana.entity.Edicion;
import com.granzonamarciana.entity.Gala;
import com.granzonamarciana.service.GalaService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class FormGala extends AppCompatActivity {
    private GalaService galaService;
    private Gala galaExistente;
    private Edicion edicionPertenece;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_gala);

        galaService = new GalaService(FormGala.this);

        EditText etFechaRealizacion = findViewById(R.id.etFechaRealizacion);
        Button btnGuardarGala = findViewById(R.id.btnGuardarGala);
        TextView tvHeaderGala = findViewById(R.id.tvHeaderGala);

        // Se rellenan campos del formulario si venimos de editar (Estilo maestro)
        galaExistente = (Gala) getIntent().getSerializableExtra("gala");
        edicionPertenece = (Edicion) getIntent().getSerializableExtra("edicion");

        if (galaExistente != null) {
            etFechaRealizacion.setText(galaExistente.getFechaRealizacion().format(formatter));
            tvHeaderGala.setText(getString(R.string.titulo_editar_gala));
        }

        // Selector de Fecha y Hora (Necesario para el campo LocalDateTime de Gala)
        etFechaRealizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDateTimePicker(etFechaRealizacion);
            }
        });

        // Editar / Crear Gala
        btnGuardarGala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etFechaRealizacion.getText().toString().isEmpty()) {
                    Toast.makeText(FormGala.this, getString(R.string.error_fecha_vacia), Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    LocalDateTime fechaGala = LocalDateTime.parse(etFechaRealizacion.getText().toString(), formatter);

                    // Validación: La gala debe estar en el rango de la edición
                    if (edicionPertenece != null) {
                        LocalDateTime inicioEd = edicionPertenece.getFechaInicio().atStartOfDay();
                        LocalDateTime finEd = edicionPertenece.getFechaFin().atTime(23, 59);

                        if (fechaGala.isBefore(inicioEd) || fechaGala.isAfter(finEd)) {
                            Toast.makeText(FormGala.this, getString(R.string.error_gala_fuera_rango), Toast.LENGTH_LONG).show();
                            return;
                        }
                    }

                    if (galaExistente != null) {
                        galaExistente.setFechaRealizacion(fechaGala);
                        galaService.actualizarGala(galaExistente);
                        Toast.makeText(FormGala.this, getString(R.string.msg_gala_actualizada), Toast.LENGTH_SHORT).show();
                    } else {
                        Gala nuevaGala = new Gala(fechaGala, edicionPertenece.getId());
                        galaService.insertarGala(nuevaGala);
                        Toast.makeText(FormGala.this, getString(R.string.msg_gala_creada), Toast.LENGTH_SHORT).show();
                    }
                    finish();
                } catch (Exception e) {
                    Toast.makeText(FormGala.this, getString(R.string.error_formato_fecha), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Volver (Estilo maestro)
        Button btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void mostrarDateTimePicker(final EditText et) {
        final Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, day) -> {
            new TimePickerDialog(this, (viewTime, hour, minute) -> {
                String fechaSel = String.format("%02d/%02d/%04d %02d:%02d", day, month + 1, year, hour, minute);
                et.setText(fechaSel);
            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }
}