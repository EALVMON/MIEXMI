package com.proyecto.miexmi;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class CeeFundamental extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Conectamos la clase con su XML
        setContentView(R.layout.activity_cee_fundamental);

        // Activamos el botón volver
        Utilidades.configurarBotonVolver(this, R.id.btnVolverCEEF);
    }
}