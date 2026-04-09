package com.proyecto.miexmi;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

// Creamos la clase LoginActivity que HEREDA de AppCompatActivity
public class MenuPrincipal extends AppCompatActivity {


    // Metodo principal que se ejecuta al abrir la pantalla
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Conectamos esta clase con el XML (activity_nemu_principal.xml)
        setContentView(R.layout.activity_menu_principal);
    }
}