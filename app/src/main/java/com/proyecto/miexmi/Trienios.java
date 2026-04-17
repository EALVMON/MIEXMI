package com.proyecto.miexmi;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class Trienios extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Conectamos esta clase con su XML específico
        setContentView(R.layout.activity_trienios);

        // Configuramos el botón volver usando la clase de utilidades
        Utilidades.configurarBotonVolver(this, R.id.btnVolverTrienios);
    }
}
