package com.proyecto.miexmi;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class CarnetConducir extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carnet);

        // Usamos la utilidad para el botón de retroceso
        Utilidades.configurarBotonVolver(this, R.id.btnVolverCarnet);
    }
}