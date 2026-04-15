package com.proyecto.miexmi;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

public class MenuDatosPer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Conectamos esta clase con el XML (activity_datos_personales.xml)
        setContentView(R.layout.activity_datos_personales);

        // Boton volver llamamos a la funcion de la clase Utilidades
        Utilidades.configurarBotonVolver(this, R.id.btnVolverDatos);

    }
}