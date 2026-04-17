package com.proyecto.miexmi;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class EvaluacionAscenso extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Conectamos esta clase con su XML
        setContentView(R.layout.activity_evaluacion_ascenso);

        // Configuramos el botón volver
        Utilidades.configurarBotonVolver(this, R.id.btnVolverEvaluacion);
    }
}
