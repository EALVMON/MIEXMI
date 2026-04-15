package com.proyecto.miexmi;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class SituacionesAdmin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Conectamos esta clase con su XML específico
        setContentView(R.layout.activity_situaciones_admin);

        // Configuramos el botón volver usando tu clase de utilidades
        Utilidades.configurarBotonVolver(this, R.id.btnVolverSituaciones);
    }
}
