package com.granzonamarciana;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.granzonamarciana.activity.RegistroActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Carga el diseño del login (asegúrate de tener activity_main.xml con el botón)
        setContentView(R.layout.activity_main);

        // Buscamos el botón de ir al registro
        Button btnIrRegistro = findViewById(R.id.btnIrARegistro);

        if (btnIrRegistro != null) {
            btnIrRegistro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // ¡EL PUENTE! Lanzamos la actividad de registro directamente
                    Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
}