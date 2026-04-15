package com.proyecto.miexmi;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MenuHistoProfe extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Conectamos esta clase con el XML
        setContentView(R.layout.activity_historial_profesional);

        // 1. Enlazamos las tarjetas del XML con variables en Java
        CardView cardSubDestinos = findViewById(R.id.cardSubDestinos);
        CardView cardSubMisiones = findViewById(R.id.cardSubMisiones);
        CardView cardSubEmpleos = findViewById(R.id.cardSubEmpleos);
        CardView cardSubComisiones = findViewById(R.id.cardSubComisiones);
        CardView cardSubEvaluacion = findViewById(R.id.cardSubEvaluacion);
        CardView cardSubHps = findViewById(R.id.cardSubHps);
        CardView cardSubRelacion = findViewById(R.id.cardSubRelacion);
        CardView cardSubSituacion = findViewById(R.id.cardSubSituacion);




        // 2. Programamos el clic
        cardSubMisiones.setOnClickListener(v -> {
            Intent intent = new Intent(MenuHistoProfe.this, Misiones.class);
            startActivity(intent);
        });

        cardSubDestinos.setOnClickListener(v -> {
            Intent intent = new Intent(MenuHistoProfe.this, Destinos.class);
            startActivity(intent);
        });
        cardSubEmpleos.setOnClickListener(v -> {
            Intent intent = new Intent(MenuHistoProfe.this, Empleos.class);
            startActivity(intent);
        });

        cardSubComisiones.setOnClickListener(v -> {
            Intent intent = new Intent(MenuHistoProfe.this, Comisiones.class);
            startActivity(intent);
        });

        cardSubRelacion.setOnClickListener(v -> {
            Intent intent = new Intent(MenuHistoProfe.this, RelacionesAdmin.class);
            startActivity(intent);
        });

        cardSubSituacion.setOnClickListener(v -> {
            Intent intent = new Intent(MenuHistoProfe.this, SituacionesAdmin.class);
            startActivity(intent);
        });


        // Botón volver
        Utilidades.configurarBotonVolver(this, R.id.btnVolverHistorial);
    }
}