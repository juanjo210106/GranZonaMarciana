package com.granzonamarciana.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.google.android.material.button.MaterialButton;
import com.granzonamarciana.R;
import com.granzonamarciana.adapter.EdicionAdapter;
import com.granzonamarciana.entity.Edicion;
import com.granzonamarciana.service.EdicionService;

import java.util.ArrayList;
import java.util.List;

public class ListEdicion extends AppCompatActivity {

    private EdicionService edicionService;
    private EdicionAdapter edicionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_edicion);

        edicionService = new EdicionService(this);

        ListView lvEdiciones = findViewById(R.id.lvEdiciones);
        TextView tvSinEdiciones = findViewById(R.id.tvSinEdiciones);
        MaterialButton btnCrearEdicion = findViewById(R.id.btnCrearEdicion);
        MaterialButton btnVolver = findViewById(R.id.btnVolver);

        lvEdiciones.setEmptyView(tvSinEdiciones);

        edicionAdapter = new EdicionAdapter(this, new ArrayList<>());
        lvEdiciones.setAdapter(edicionAdapter);

        edicionService.listarEdiciones().observe(this, new Observer<List<Edicion>>() {
            @Override
            public void onChanged(List<Edicion> ediciones) {
                edicionAdapter.clear();
                edicionAdapter.addAll(ediciones);
                edicionAdapter.notifyDataSetChanged();
            }
        });

        lvEdiciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Edicion edicion = (Edicion) parent.getItemAtPosition(position);
                Intent i = new Intent(ListEdicion.this, FormEdicionActivity.class);
                i.putExtra("idEdicion", edicion.getId());
                startActivity(i);
            }
        });

        btnCrearEdicion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListEdicion.this, FormEdicionActivity.class);
                startActivity(i);
            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}