package com.proyecto.miexmi;

import android.content.Intent;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MenuOtrosRegi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Conectamos esta clase con el XML (activity_otros_registros.xml)
        setContentView(R.layout.activity_otros_registros);

        // 1. Enlazamos las tarjetas del XML con variables en Java
        CardView cardSubArmas = findViewById(R.id.cardSubArmas);


        // 2. Programamos el clic de cada tarjeta

        cardSubArmas.setOnClickListener(v -> {
            Intent intent = new Intent(MenuOtrosRegi.this, ArmasParticulares.class);
            startActivity(intent);
        });

        // Boton volver llamamos a la funcion de la clase Utilidades
        Utilidades.configurarBotonVolver(this, R.id.btnVolverOtros);
    }
}
