package com.proyecto.miexmi;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class Comisiones extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Conectamos esta clase con el XML
        setContentView(R.layout.activity_comisiones);

        // Boton volver llamamos a la funcion de la clase Utilidades
        Utilidades.configurarBotonVolver(this, R.id.btnVolverComisiones);

    }
}
