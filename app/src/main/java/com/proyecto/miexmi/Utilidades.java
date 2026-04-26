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

    // ========================================================================
    // === CONFIGURAR DESPLEGABLE DE TRIENIOS (GRUPOS DE CLASIFICACIÓN)     ===
    // ========================================================================
    public static void configurarDesplegableTrienios(android.content.Context context, android.widget.AutoCompleteTextView autoCompleteTextView) {
        String[] trienios = new String[]{
                "A1", "A2", "B", "C1", "C2", "AP"
        };
        android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, trienios);
        autoCompleteTextView.setAdapter(adapter);
    }

    // ========================================================================
    // === CONFIGURAR DESPLEGABLE DE RECOMPENSAS MILITARES                  ===
    // ========================================================================
    public static void configurarDesplegableRecompensas(android.content.Context context, android.widget.AutoCompleteTextView autoCompleteTextView) {
        String[] recompensas = new String[]{
                // Máximas recompensas
                "Cruz Laureada de San Fernando", "Medalla Militar", "Cruz Laureada de San Fernando Colectiva",
                "Medalla Militar Colectiva",
                // Cruces al mérito
                "Cruz al Mérito Militar (Distintivo Rojo)", "Cruz al Mérito Militar (Distintivo Azul)",
                "Cruz al Mérito Militar (Distintivo Amarillo)", "Cruz al Mérito Militar (Distintivo Blanco)",
                "Cruz al Mérito Naval (Distintivo Rojo)", "Cruz al Mérito Naval (Distintivo Azul)",
                "Cruz al Mérito Naval (Distintivo Amarillo)", "Cruz al Mérito Naval (Distintivo Blanco)",
                "Cruz al Mérito Aeronáutico (Distintivo Rojo)", "Cruz al Mérito Aeronáutico (Distintivo Azul)",
                "Cruz al Mérito Aeronáutico (Distintivo Amarillo)", "Cruz al Mérito Aeronáutico (Distintivo Blanco)",
                // Menciones
                "Mención Honorífica Individual", "Mención Honorífica Colectiva",
                "Citación como distinguido en la Orden General",
                // Recompensas colectivas
                "Corbata de la Orden de San Fernando", "Placa de la Orden de San Fernando",
                // Constancia en el servicio
                "Cruz a la Constancia en el Servicio (Bronce)", "Cruz a la Constancia en el Servicio (Plata)",
                "Cruz a la Constancia en el Servicio (Oro)",
                // Órdenes militares
                "Orden de San Fernando", "Orden de San Hermenegildo (Cruz)", "Orden de San Hermenegildo (Encomienda)",
                "Orden de San Hermenegildo (Placa)", "Orden de San Hermenegildo (Gran Cruz)",
                // Medalla nacional de operaciones
                "Medalla de las Operaciones - Bosnia-Herzegovina", "Medalla de las Operaciones - Kosovo",
                "Medalla de las Operaciones - Afganistán", "Medalla de las Operaciones - Líbano", "Medalla de las Operaciones - Irak", "Medalla de las Operaciones - Mali", "Medalla de las Operaciones - Somalia", "Medalla de las Operaciones - Océano Índico", "Medalla de las Operaciones - Letonia", "Medalla de las Operaciones - Operación Balmis",
                // OTAN
                "OTAN Medal - IFOR (Bosnia)", "OTAN Medal - SFOR (Bosnia)", "OTAN Medal - KFOR (Kosovo)",
                "OTAN Medal - ISAF (Afganistán)", "OTAN Medal - Resolute Support (Afganistán)",
                // Unión Europea
                "UE Medalla - EUFOR Althea (Bosnia)", "UE Medalla - EUTM Mali", "UE Medalla - Atalanta",
                "UE Medalla - EUTM Somalia",
                // ONU
                "ONU Medalla - UNPROFOR (Bosnia)", "ONU Medalla - UNIFIL (Líbano)", "ONU Medalla - MINURSO (Sahara)",
                //Coaliciones / otras
                "Medalla de la Coalición - Global War on Terrorism"
        };
        android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, recompensas);
        autoCompleteTextView.setAdapter(adapter);
    }

    // ========================================================================
    // === CONFIGURAR DESPLEGABLE DE DISTINTIVOS                            ===
    // ========================================================================
    public static void configurarDesplegableDistintivos(android.content.Context context, android.widget.AutoCompleteTextView autoCompleteTextView) {
        String[] distintivos = new String[]{
                // DESTINO
                "FAMET", "Montaña", "Operaciones Especiales", "La Legión", "Paracaidista (BRIPAC)",
                "Infantería", "Caballería", "Artillería", "Ingenieros", "Transmisiones",
                // APTITUD
                "Aptitud Paracaidista", "Aptitud Montaña", "Buceador de Combate", "Lanzador Paracaidista",
                "EOD / TEDAX Militar", "Tirador Selecto", "Tripulante de Aeronaves",
                // CURSOS
                "Curso de Estado Mayor", "Curso de Operaciones Especiales", "Curso de Helicópteros",
                "Observador Avanzado", "Curso NBQ",
                // PERMANENCIA
                "Permanencia Operaciones Especiales", "Permanencia Paracaidista", "Permanencia La Legión",
                // FUNCIÓN
                "Profesor", "Instructor", "Mando de Unidad", "Estado Mayor",
                // OPERACIONES
                "Participación en Misión Internacional"
        };
        android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, distintivos);
        autoCompleteTextView.setAdapter(adapter);
    }

    // ========================================================================
    // === CONFIGURAR DESPLEGABLE DE CURSOS MILITARES                       ===
    // ========================================================================
    public static void configurarDesplegableCursosMilitares(android.content.Context context, android.widget.AutoCompleteTextView autoCompleteTextView) {
        String[] cursos = new String[]{
                "Curso de Estado Mayor", "Curso de Operaciones Especiales", "Curso Paracaidista",
                "Curso de Montaña", "Curso de Buceador de Combate", "Curso EOD / TEDAX",
                "Curso NBQ", "Curso de Tirador Selecto", "Curso de Observador Avanzado",
                "Curso de Helicópteros", "Curso de Piloto Militar", "Curso de Tripulante de Aeronaves",
                "Curso de Transmisiones", "Curso de Inteligencia Militar", "Curso de Guerra Electrónica",
                "Curso de Ciberdefensa", "Curso de Logística Militar", "Curso de Sanidad Militar",
                "Curso de Instructor Militar", "Curso de Combate en Zona Urbana", "Curso SERE",
                "Curso de Policía Militar", "Curso de Conducción Táctica", "Curso de Explosivos",
                "Curso de Defensa Personal Militar", "Curso de Idiomas"
        };
        android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, cursos);
        autoCompleteTextView.setAdapter(adapter);
    }

    // ========================================================================
    // === CONFIGURAR DESPLEGABLE DE IDIOMAS                                ===
    // ========================================================================
    public static void configurarDesplegableIdiomas(android.content.Context context, android.widget.AutoCompleteTextView autoCompleteTextView) {
        String[] idiomas = new String[]{
                "Inglés", "Francés", "Portugués", "Árabe", "Ruso",
                "Chino", "Alemán", "Italiano", "Rumano", "Japonés"
        };
        android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, idiomas);
        autoCompleteTextView.setAdapter(adapter);
    }

    // ========================================================================
    // === CONFIGURAR DESPLEGABLE DE CARNETS MILITARES                      ===
    // ========================================================================
    public static void configurarDesplegableCarnets(android.content.Context context, android.widget.AutoCompleteTextView autoCompleteTextView) {
        String[] carnets = new String[]{
                "AM", "A1", "A2", "A", "B", "BE", "C1", "C", "C1E", "CE", "D1", "D", "D1E", "DE", "BTP","F"
        };
        android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, carnets);
        autoCompleteTextView.setAdapter(adapter);
    }

    // ========================================================================
    // === CONFIGURAR DESPLEGABLE DE RESULTADOS TCGF                        ===
    // ========================================================================
    public static void configurarDesplegableAptoTcgf(android.content.Context context, android.widget.AutoCompleteTextView autoCompleteTextView) {
        String[] opciones = new String[]{
                "Apto", "No Apto", "No presentado", "No Apto Reco Medico"
        };
        android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, opciones);
        autoCompleteTextView.setAdapter(adapter);
    }

}