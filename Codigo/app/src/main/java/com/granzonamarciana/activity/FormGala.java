package com.granzonamarciana.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.granzonamarciana.R;
import com.granzonamarciana.entity.Edicion;
import com.granzonamarciana.entity.Gala;
import com.granzonamarciana.service.EdicionService;
import com.granzonamarciana.service.GalaService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FormGala extends AppCompatActivity {

    private GalaService galaService;
    private EdicionService edicionService;

    // Componentes de la UI
    private TextInputEditText etFecha;
    private AutoCompleteTextView autoCompleteEdicion;
    private MaterialButton btnGuardar, btnVolver;

    // Variables de lógica
    private int idGalaGestion;
    private Gala galaCargada;
    private List<Edicion> listaEdiciones = new ArrayList<>();
    private Edicion edicionSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_gala);

        // 1. Inicializar servicios
        galaService = new GalaService(this);
        edicionService = new EdicionService(this);

        // 2. Recuperar ID si es edición
        idGalaGestion = getIntent().getIntExtra("idGala", -1);

        // 3. Vincular con XML y cargar datos
        inicializarVistas();
        cargarEdiciones();

        if (idGalaGestion != -1) {
            cargarDatosGala();
        }

        // 4. Listeners
        btnGuardar.setOnClickListener(v -> validarFechasYGuardar());
        btnVolver.setOnClickListener(v -> finish());

        configurarSelectorFecha();
    }

    private void inicializarVistas() {
        // Vinculación exacta con tus IDs del XML
        etFecha = findViewById(R.id.etFechaRealizacion);
        autoCompleteEdicion = findViewById(R.id.autoCompleteEdiciones);
        btnGuardar = findViewById(R.id.btnGuardarGala);
        btnVolver = findViewById(R.id.btnVolver);
    }

    private void configurarSelectorFecha() {
        etFecha.setOnClickListener(v -> {
            LocalDate hoy = LocalDate.now();
            // Abre un calendario
            new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                // Formato dd/MM/yyyy para mostrar en el campo
                String fechaStr = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
                etFecha.setText(fechaStr);
            }, hoy.getYear(), hoy.getMonthValue() - 1, hoy.getDayOfMonth()).show();
        });
    }

    private void cargarEdiciones() {
        edicionService.listarEdiciones().observe(this, ediciones -> {
            if (ediciones != null) {
                listaEdiciones = ediciones;
                List<String> nombres = new ArrayList<>();
                // Llenamos el dropdown con información útil
                for (Edicion e : ediciones) {
                    nombres.add("Edición " + e.getId() + " (Inicia: " + e.getFechaInicio() + ")");
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, nombres);
                autoCompleteEdicion.setAdapter(adapter);

                // Capturamos la selección
                autoCompleteEdicion.setOnItemClickListener((parent, view, position, id) -> {
                    edicionSeleccionada = listaEdiciones.get(position);
                });
            }
        });
    }

    private void cargarDatosGala() {
        galaService.buscarGalaPorId(idGalaGestion).observe(this, gala -> {
            if (gala != null) {
                galaCargada = gala;
                // Mostramos la fecha existente
                LocalDateTime fecha = gala.getFechaRealizacion();
                etFecha.setText(fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

                // Nota: Seleccionar automáticamente la edición en el dropdown es complejo
                // con AutoCompleteTextView sin un adaptador custom, así que lo dejamos limpio por ahora.
            }
        });
    }

    private void validarFechasYGuardar() {
        String fechaStr = etFecha.getText().toString();

        if (TextUtils.isEmpty(fechaStr) || edicionSeleccionada == null) {
            Toast.makeText(this, "Selecciona una fecha y una edición", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1. Convertir String a Fecha
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fechaPropuesta;
        try {
            fechaPropuesta = LocalDate.parse(fechaStr, dateFmt);
        } catch (Exception e) {
            Toast.makeText(this, "Formato de fecha inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        // Como no hay hora en el XML, definimos las 22:00 por defecto
        LocalTime horaDefecto = LocalTime.of(22, 0);
        LocalDateTime fechaGalaFinal = LocalDateTime.of(fechaPropuesta, horaDefecto);

        // 2. RNF: Validar que la fecha esté DENTRO de la edición seleccionada
        if (fechaPropuesta.isBefore(edicionSeleccionada.getFechaInicio())) {
            Toast.makeText(this, "Error: La gala es antes del inicio de la edición", Toast.LENGTH_LONG).show();
            return;
        }

        if (fechaPropuesta.isAfter(edicionSeleccionada.getFechaFin())) {
            Toast.makeText(this, "Error: La gala es después del fin de la edición", Toast.LENGTH_LONG).show();
            return;
        }

        // 3. Guardar en Base de Datos
        if (galaCargada != null) {
            // Actualizar existente
            galaCargada.setFechaRealizacion(fechaGalaFinal);
            galaCargada.setIdEdicion(edicionSeleccionada.getId());
            galaService.actualizarGala(galaCargada);
            Toast.makeText(this, "Gala actualizada correctamente", Toast.LENGTH_SHORT).show();
        } else {
            // Crear nueva
            Gala nueva = new Gala(fechaGalaFinal, edicionSeleccionada.getId());
            galaService.insertarGala(nueva);
            Toast.makeText(this, "Gala creada correctamente", Toast.LENGTH_SHORT).show();
        }

        finish(); // Volver atrás
    }
}