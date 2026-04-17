package com.proyecto.miexmi;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class TarjetaMilitar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Conectamos la clase con su XML
        setContentView(R.layout.activity_tmi);

        // Configuramos el botón volver
        Utilidades.configurarBotonVolver(this, R.id.btnVolverTmi);
    }
}
