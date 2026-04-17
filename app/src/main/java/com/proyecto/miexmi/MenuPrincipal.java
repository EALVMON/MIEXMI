package com.proyecto.miexmi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

// Creamos la clase LoginActivity que HEREDA de AppCompatActivity
public class MenuPrincipal extends AppCompatActivity {


    // Metodo principal que se ejecuta al abrir la pantalla
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Conectamos esta clase con el XML (activity_nemu_principal.xml)
        setContentView(R.layout.activity_menu_principal);

        // 1. Enlazamos las tarjetas del XML con variables en Java
        CardView cardDatosPersonales = findViewById(R.id.cardDatosPersonales);
        CardView cardHistorialProfesional = findViewById(R.id.cardHistorialProfesional);
        CardView cardMeritosFormacion = findViewById(R.id.cardMeritosFormacion);
        CardView cardRegistrosAdmin = findViewById(R.id.cardRegistrosAdmin);
        CardView cardExportar = findViewById(R.id.cardExportar);
        CardView cardActividad = findViewById(R.id.cardActividad);

        // 2. Programamos el clic para "Datos Personales"
        cardDatosPersonales.setOnClickListener(v -> {
            Intent intent = new Intent(MenuPrincipal.this, MenuDatosPer.class);
            startActivity(intent);
        });

        // 3. Programamos el clic para "Historial Profesional"
        cardHistorialProfesional.setOnClickListener(v -> {

            Intent intent = new Intent(MenuPrincipal.this, MenuHistoProfe.class);
             startActivity(intent);

        });

        // 4. Programamos el clic para "Méritos y Formación"
        cardMeritosFormacion.setOnClickListener(v -> {
            Intent intent = new Intent(MenuPrincipal.this, MenuMeritoFor.class);
            startActivity(intent);

        });

        // 5. Programamos el clic para "Otros Registros"
        cardRegistrosAdmin.setOnClickListener(v -> {
            Intent intent = new Intent(MenuPrincipal.this, MenuOtrosRegi.class);
            startActivity(intent);

        });

        // 6. Programamos clics temporales para las que aún no tienen pantalla
        cardExportar.setOnClickListener(v -> {
            Intent intent = new Intent(MenuPrincipal.this, LogActividad.class);
            startActivity(intent);

        });

        cardActividad.setOnClickListener(v -> {
            Toast.makeText(this, "Módulo de Actividad en construcción", Toast.LENGTH_SHORT).show();
        });
    }
}