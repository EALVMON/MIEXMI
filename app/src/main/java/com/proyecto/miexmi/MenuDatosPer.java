package com.proyecto.miexmi;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MenuDatosPer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Conectamos esta clase con el XML (activity_datos_personales.xml)
        setContentView(R.layout.activity_datos_personales);

        // programamos el Boton volver
                // 1. Enlazamos el botón de volver
             ImageButton btnVolver = findViewById(R.id.btnVolverDatos);

                // 2. Programamos la acción de volver atrás
                btnVolver.setOnClickListener(v -> {
                // La instrucción finish() destruye esta pantalla y nos devuelve a la anterior (MenuPrincipal)
                finish();
                });
    }
}