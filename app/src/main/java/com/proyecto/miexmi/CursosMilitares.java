package com.proyecto.miexmi;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class CursosMilitares extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Conectamos la clase con su XML
        setContentView(R.layout.activity_cursos_militares);

        // Activamos el botón volver
        Utilidades.configurarBotonVolver(this, R.id.btnVolverCursos);
    }
}