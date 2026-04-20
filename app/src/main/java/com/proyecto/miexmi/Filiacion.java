package com.proyecto.miexmi;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class Filiacion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filiacion);

        Utilidades.configurarBotonVolver(this, R.id.btnVolverFiliacion);
    }
}
