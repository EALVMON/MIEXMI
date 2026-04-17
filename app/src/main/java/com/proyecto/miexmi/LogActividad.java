package com.proyecto.miexmi;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class LogActividad extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_actividad);

        // Configurar botón volver
        Utilidades.configurarBotonVolver(this, R.id.btnVolverLog);
    }
}
