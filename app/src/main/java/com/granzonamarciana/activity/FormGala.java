package com.granzonamarciana.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.granzonamarciana.R;
import com.granzonamarciana.entity.Edicion;
import com.granzonamarciana.entity.Gala;
import com.granzonamarciana.service.EdicionService;
import com.granzonamarciana.service.GalaService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;

public class FormGala extends AppCompatActivity {
    private GalaService galaService;
    private EdicionService edicionService;
    private Gala galaExistente;
    private List<Edicion> listaEdiciones;
    private Edicion edicionSeleccionada;
    private AutoCompleteTextView autoCompleteEdiciones;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_gala);

        galaService = new GalaService(this);
        edicionService = new EdicionService(this);

        // Referencias del XML (Cambiado Spinner por AutoCompleteTextView)
        autoCompleteEdiciones = findViewById(R.id.autoCompleteEdiciones);
        TextInputEditText etFechaRealizacion = findViewById(R.id.etFechaRealizacion);
        MaterialButton btnGuardarGala = findViewById(R.id.btnGuardarGala);
        MaterialButton btnVolver = findViewById(R.id.btnVolver);
        TextView tvHeaderGala = findViewById(R.id.tvHeaderGala);

        // 1. Cargar Ediciones en el desplegable moderno
        cargarEdiciones();

        // 2. ¿Modo Editar o Crear?
        galaExistente = (Gala) getIntent().getSerializableExtra("gala");
        if (galaExistente != null) {
            tvHeaderGala.setText("Editar Gala");
            etFechaRealizacion.setText(galaExistente.getFechaRealizacion().format(formatter));
            // La edición se autoselecciona dentro de cargarEdiciones()
        }

        // 3. Configurar el clic para abrir Calendario + Reloj
        etFechaRealizacion.setOnClickListener(v -> mostrarDateTimePicker(etFechaRealizacion));

        // 4. Lógica de Guardado
        btnGuardarGala.setOnClickListener(v -> {
            String fechaTexto = etFechaRealizacion.getText().toString();

            if (fechaTexto.isEmpty() || edicionSeleccionada == null) {
                Toast.makeText(this, "Selecciona edición y fecha", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                LocalDateTime fechaFinal = LocalDateTime.parse(fechaTexto, formatter);

                if (galaExistente != null) {
                    // EDITAR GALA
                    galaExistente.setFechaRealizacion(fechaFinal);
                    galaExistente.setIdEdicion(edicionSeleccionada.getId()); // Usando idEdicion
                    galaService.actualizarGala(galaExistente);
                    Toast.makeText(this, "Gala actualizada", Toast.LENGTH_SHORT).show();
                } else {
                    // CREAR GALA NUEVA
                    Gala nuevaGala = new Gala(fechaFinal, edicionSeleccionada.getId());
                    // Asegúrate de que tu constructor de Gala asigne el idEdicion
                    galaService.insertarGala(nuevaGala);
                    Toast.makeText(this, "Gala creada con éxito", Toast.LENGTH_SHORT).show();
                }
                finish();
            } catch (Exception e) {
                Toast.makeText(this, "Error en el formato de fecha", Toast.LENGTH_SHORT).show();
            }
        });

        btnVolver.setOnClickListener(v -> finish());
    }

    private void cargarEdiciones() {
        edicionService.listarEdiciones().observe(this, ediciones -> {
            if (ediciones != null) {
                this.listaEdiciones = ediciones;

                // Adaptador para el AutoCompleteTextView (Material Dropdown)
                ArrayAdapter<Edicion> adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1, listaEdiciones);

                autoCompleteEdiciones.setAdapter(adapter);

                // Detectar selección
                autoCompleteEdiciones.setOnItemClickListener((parent, view, position, id) -> {
                    edicionSeleccionada = (Edicion) parent.getItemAtPosition(position);
                });

                // Si estamos editando, buscamos y seleccionamos la edición actual
                if (galaExistente != null) {
                    for (Edicion e : listaEdiciones) {
                        if (e.getId() == galaExistente.getIdEdicion()) {
                            autoCompleteEdiciones.setText(e.toString(), false);
                            edicionSeleccionada = e;
                            break;
                        }
                    }
                }
            }
        });
    }

    private void mostrarDateTimePicker(TextInputEditText et) {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, day) -> {

            new TimePickerDialog(this, (viewTime, hour, minute) -> {
                String fechaFormateada = String.format("%02d/%02d/%04d %02d:%02d", day, month + 1, year, hour, minute);
                et.setText(fechaFormateada);
            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();

        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }
}