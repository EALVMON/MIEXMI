package com.proyecto.miexmi;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class PruebasFisicas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Conectamos la clase con su XML
        setContentView(R.layout.activity_tcgf);

        // Configuramos el botón volver
        Utilidades.configurarBotonVolver(this, R.id.btnVolverTcgf);
    }
}