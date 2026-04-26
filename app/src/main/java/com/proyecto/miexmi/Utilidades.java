package com.proyecto.miexmi;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ImageButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Calendar; // Importación para manejar fechas
import java.util.Locale;   // Importación para el formato del texto

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


     // Funcion Recupera el ID del usuario que tiene la sesión iniciada.

    public static int obtenerUsuarioActual(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("SesionApp", Context.MODE_PRIVATE);
        return prefs.getInt("ID_USUARIO_ACTUAL", -1);
    }


     // FUNCIÓN MAESTRA DE CALENDARIO YA QUE LA VAMOS A UTILIZAR EN VAROS MODULOS LA PETICION DE FEHCA
    public static void configurarCalendario(Context context, TextInputEditText editText) {
        editText.setOnClickListener(v -> {
            Calendar calendario = Calendar.getInstance();
            int anio = calendario.get(Calendar.YEAR);
            int mes = calendario.get(Calendar.MONTH);
            int dia = calendario.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(context, (view, year, month, dayOfMonth) -> {
                // Formateamos la fecha siempre igual: dd/mm/yyyy
                String fechaSeleccionada = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, (month + 1), year);
                editText.setText(fechaSeleccionada);
            }, anio, mes, dia);

            dialog.show();
        });
    }

    // ========================================================================
    // === CONFIGURAR MENÚ DESPLEGABLE DE EMPLEOS (EJÉRCITO DE TIERRA)      ===
    // ========================================================================
    public static void configurarDesplegableEmpleos(android.content.Context context, android.widget.AutoCompleteTextView autoCompleteTextView) {
        String[] escalafonTierra = new String[]{
                "Soldado", "Cabo", "Cabo 1º", "Cabo Mayor",
                "Sargento", "Sargento 1º", "Brigada", "Subteniente", "Suboficial Mayor",
                "Alférez", "Teniente", "Capitán", "Comandante", "Teniente Coronel", "Coronel",
                "General de Brigada", "General de División", "Teniente General"
        };

        android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(
                context,
                android.R.layout.simple_dropdown_item_1line,
                escalafonTierra
        );
        autoCompleteTextView.setAdapter(adapter);
    }

    // ========================================================================
    // === CONFIGURAR DESPLEGABLE DE HABILITACIONES DE SEGURIDAD (HPS)      ===
    // ====================================================================
    public static void configurarDesplegableHPS(android.content.Context context, android.widget.AutoCompleteTextView autoCompleteTextView) {
        String[] habilitaciones = new String[]{
                "Nacional-Alto Secreto", "Nacional-Secreto", "Nacional-Confidencial", "Nacional-Difusión Limitada",
                "NATO -COSMIC TOP SECRET", "NATO SECRET", "NATO CONFIDENTIAL", "NATO RESTRICTED",
                "EU TOP SECRET", "EU SECRET", "EU CONFIDENTIAL", "EU RESTRICTED"
        };
        android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, habilitaciones);
        autoCompleteTextView.setAdapter(adapter);
    }
}