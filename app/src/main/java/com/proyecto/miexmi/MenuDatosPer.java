package com.proyecto.miexmi;
import android.content.Intent;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MenuDatosPer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Conectamos esta clase con el XML (activity_datos_personales.xml)
        setContentView(R.layout.activity_datos_personales);

        // 1. Enlazamos las tarjetas del XML con variables en Java
        CardView CardSubcee = findViewById(R.id.cardSubCuerpoEscala);
        CardView CardSubtmi = findViewById(R.id.cardSubTmi);
        CardView cardSubFiliacion = findViewById(R.id.cardSubFiliacion);



        // 2. Programamos el clic de cada tarjeta
        CardSubcee.setOnClickListener(v -> {
            Intent intent = new Intent(MenuDatosPer.this, CeeFundamental.class);
            startActivity(intent);
        });

        CardSubtmi.setOnClickListener(v -> {
            Intent intent = new Intent(MenuDatosPer.this, TarjetaMilitar.class);
            startActivity(intent);
        });

        cardSubFiliacion.setOnClickListener(v -> {
            Intent intent = new Intent(MenuDatosPer.this, Filiacion.class);
            startActivity(intent);
        });




        // Boton volver llamamos a la funcion de la clase Utilidades
        Utilidades.configurarBotonVolver(this, R.id.btnVolverDatos);

    }
}