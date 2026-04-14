package com.proyecto.miexmi;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MenuOtrosRegi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Conectamos esta clase con el XML (activity_otros_registros.xml)
        setContentView(R.layout.activity_otros_registros);

        // Programamos el Boton volver
        // 1. Enlazamos el botón de volver
        ImageButton btnVolver = findViewById(R.id.btnVolverOtros);

        // 2. Programamos la acción de volver atrás
        btnVolver.setOnClickListener(v -> {
            // La instrucción finish() destruye esta pantalla y nos devuelve a la anterior (MenuPrincipal)
            finish();
        });
    }
}
