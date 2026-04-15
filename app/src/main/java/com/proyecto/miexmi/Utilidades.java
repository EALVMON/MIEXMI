package com.proyecto.miexmi;

import android.app.Activity;
import android.widget.ImageButton;

public class Utilidades {

    // Función estática: la podemos llamar desde cualquier parte sin crear un objeto "Utilidades"
    public static void configurarBotonVolver(Activity activity, int idBoton) {

        // 1. Buscamos el botón en la pantalla que nos pasen
        ImageButton btnVolver = activity.findViewById(idBoton);

        // 2. Si el botón existe, le ponemos la acción de cerrar la pantalla
        if (btnVolver != null) {
            btnVolver.setOnClickListener(v -> activity.finish());
        }
    }
}