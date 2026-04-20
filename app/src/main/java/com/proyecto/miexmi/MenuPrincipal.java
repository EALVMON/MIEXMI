package com.proyecto.miexmi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MenuPrincipal extends AppCompatActivity {

    // Metodo principal que se ejecuta al abrir la pantalla
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Conectamos esta clase con el XML (activity_menu_principal.xml)
        setContentView(R.layout.activity_menu_principal);

        // Enlazamos las tarjetas y botones del XML con variables en Java
        CardView cardDatosPersonales = findViewById(R.id.cardDatosPersonales);
        CardView cardHistorialProfesional = findViewById(R.id.cardHistorialProfesional);
        CardView cardMeritosFormacion = findViewById(R.id.cardMeritosFormacion);
        CardView cardRegistrosAdmin = findViewById(R.id.cardRegistrosAdmin);
        CardView cardExportar = findViewById(R.id.cardExportar);
        CardView cardActividad = findViewById(R.id.cardActividad);

        // Enlazamos el botón de la "Rosca" de Ajustes/Seguridad
        ImageButton btnAjustes = findViewById(R.id.btnAjustesSeguridad);

        // Programamos el clic para la "Rosca" de Ajustes/Seguridad
        btnAjustes.setOnClickListener(v -> {
            Intent intent = new Intent(MenuPrincipal.this, Seguridad.class);
            startActivity(intent);
        });

        // Programamos el clic para "Datos Personales"
        cardDatosPersonales.setOnClickListener(v -> {
            Intent intent = new Intent(MenuPrincipal.this, MenuDatosPer.class);
            startActivity(intent);
        });

        // Programamos el clic para "Historial Profesional"
        cardHistorialProfesional.setOnClickListener(v -> {
            Intent intent = new Intent(MenuPrincipal.this, MenuHistoProfe.class);
            startActivity(intent);
        });

        // Programamos el clic para "Méritos y Formación"
        cardMeritosFormacion.setOnClickListener(v -> {
            Intent intent = new Intent(MenuPrincipal.this, MenuMeritoFor.class);
            startActivity(intent);
        });

        // Programamos el clic para "Otros Registros"
        cardRegistrosAdmin.setOnClickListener(v -> {
            Intent intent = new Intent(MenuPrincipal.this, MenuOtrosRegi.class);
            startActivity(intent);
        });

        // Programamos el clic para "Actividad de la App" (Lleva al Log)
        cardActividad.setOnClickListener(v -> {
            Intent intent = new Intent(MenuPrincipal.this, LogActividad.class);
            startActivity(intent);
        });

        // Programamos clic temporal para Exportar (Aún no tiene pantalla)
        cardExportar.setOnClickListener(v -> {
            Toast.makeText(this, "Módulo de Exportación en construcción", Toast.LENGTH_SHORT).show();
        });

    }
}