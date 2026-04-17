package com.proyecto.miexmi;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class TitulosCiviles extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Conectamos esta clase con su XML
        setContentView(R.layout.activity_titulos_civiles);

        // Configuramos el botón volver
        Utilidades.configurarBotonVolver(this, R.id.btnVolverTitulos);
    }
}
