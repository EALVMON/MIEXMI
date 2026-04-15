package com.proyecto.miexmi;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

public class MenuOtrosRegi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Conectamos esta clase con el XML (activity_otros_registros.xml)
        setContentView(R.layout.activity_otros_registros);


        // Boton volver llamamos a la funcion de la clase Utilidades
        Utilidades.configurarBotonVolver(this, R.id.btnVolverOtros);
    }
}
