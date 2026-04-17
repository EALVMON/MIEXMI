package com.proyecto.miexmi;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class Distintivos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Conectamos esta clase con su XML específico
        setContentView(R.layout.activity_distintivos);

        // Configuramos el botón volver usando la clase de utilidades
        Utilidades.configurarBotonVolver(this, R.id.btnVolverDistintivos);
    }
}