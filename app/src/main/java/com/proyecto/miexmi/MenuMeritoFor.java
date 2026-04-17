package com.proyecto.miexmi;

import android.content.Intent;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MenuMeritoFor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Conectamos esta clase con el XML (activity_meritos_formacion.xml)
        setContentView(R.layout.activity_meritos_formacion);


        // 1. Enlazamos las tarjetas del XML con variables en Java
        CardView cardSubTrienios = findViewById(R.id.cardSubTrienios);
        CardView cardSubRecompensas = findViewById(R.id.cardSubRecompensas);
        CardView cardSubDistintivos = findViewById(R.id.cardSubDistintivos);
        CardView cardSubAptitudes = findViewById(R.id.cardSubAptitudes);
        CardView cardSubCursosMilitares = findViewById(R.id.cardSubCursosMilitares);
        CardView cardSubTitulosCiviles = findViewById(R.id.cardSubTitulosCiviles);



        // 2. Programamos el clic de cada tarjeta
        cardSubTrienios.setOnClickListener(v -> {
            Intent intent = new Intent(MenuMeritoFor.this, Trienios.class);
            startActivity(intent);
        });
        cardSubRecompensas.setOnClickListener(v -> {
            Intent intent = new Intent(MenuMeritoFor.this, Recompensas.class);
            startActivity(intent);
        });
        cardSubDistintivos.setOnClickListener(v -> {
            Intent intent = new Intent(MenuMeritoFor.this, Distintivos.class);
            startActivity(intent);
        });
        cardSubAptitudes.setOnClickListener(v -> {
            Intent intent = new Intent(MenuMeritoFor.this, Aptitudes.class);
            startActivity(intent);
        });

        cardSubCursosMilitares.setOnClickListener(v -> {
            Intent intent = new Intent(MenuMeritoFor.this, CursosMilitares.class);
            startActivity(intent);
        });

        cardSubTitulosCiviles.setOnClickListener(v -> {
            Intent intent = new Intent(MenuMeritoFor.this, TitulosCiviles.class);
            startActivity(intent);
        });








        // Boton volver llamamos a la funcion de la clase Utilidades
        Utilidades.configurarBotonVolver(this, R.id.btnVolverMeritos);

    }
}
