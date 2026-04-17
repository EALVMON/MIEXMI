package com.proyecto.miexmi;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class ArmasParticulares extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Conectamos la clase con su XML
        setContentView(R.layout.activity_armas_particulares);

        // Configuramos el botón volver
        Utilidades.configurarBotonVolver(this, R.id.btnVolverArmas);
    }
}