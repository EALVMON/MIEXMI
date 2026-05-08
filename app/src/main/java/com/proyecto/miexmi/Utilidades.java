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

    // ========================================================================
    // === SISTEMA DE SEGURIDAD: CIFRADO DE CONTRASEÑAS (SHA-256)           ===
    // ========================================================================

    // Metodo público y estático que recibe un texto normal (la contraseña que escribe el usuario)
    // y nos devuelve otro texto (la contraseña ya codificada ).
    public static String cifrarContrasena(String password) {

        // Abrimos un bloque 'try-catch' porque vamos a invocar a las herramientas criptográficas
        // de Android, y si el móvil es muy antiguo o falla, debemos capturar el error.
        try {

            // --- 1. PREPARAR LA CODIFICACION ---
            // 'MessageDigest' es la herramienta de Java para cifrar.
            // Le decimos que queremos usar el algoritmo "SHA-256" (uno de los más seguros del mundo).
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");

            // --- 2. CODIFICAR LA CONTRASEÑA ---
            // Primero, convertimos el texto de la contraseña a "Bytes" (ceros y unos) usando el formato UTF-8.
            // Luego, le pasamos esos bytes al metodo '.digest()', que es el que hace los cálculos matemáticos.
            // El resultado ('hash') es una matriz de bytes incomprensible (símbolos raros que no se pueden leer).
            byte[] hash = digest.digest(password.getBytes(java.nio.charset.StandardCharsets.UTF_8));

            // --- 3. CONVERTIR LOS BYTES A TEXTO LEGIBLE (HEXADECIMAL) ---
            // Como no podemos guardar "símbolos raros" en la base de datos, vamos a traducir
            // esos bytes a un formato llamado Hexadecimal (que solo usa números del 0 al 9 y letras de la A a la F).

            // Creamos un 'StringBuilder' (nuestro constructor de textos rápido) para ir uniendo las letras.
            StringBuilder hexString = new StringBuilder();

            // Iniciamos un bucle: vamos a coger la matriz de bytes ('hash') y analizarla byte por byte.
            for (byte b : hash) {

                // Un 'byte' en Java puede ser un número negativo.
                // El truco '(0xff & b)' le quita el signo negativo para convertirlo en un número positivo limpio.
                // Luego, 'Integer.toHexString' traduce ese número al formato Hexadecimal.
                String hex = Integer.toHexString(0xff & b);

                // A veces, la traducción da una sola letra (ej: "a").
                // Para que el cifrado sea uniforme y perfecto, si solo tiene 1 letra, le ponemos un "0" delante (ej: "0a").
                if (hex.length() == 1) {
                    hexString.append('0');
                }

                // Añadimos ese fragmento traducido a nuestra cadena de texto principal.
                hexString.append(hex);
            }

            // --- 4. DEVOLVER EL RESULTADO ---
            // Una vez terminado el bucle, convertimos el StringBuilder a un texto normal (String)
            // y lo devolvemos. Este texto será algo como: "03ac674216f3e15c761ee1a5e255f067..."
            return hexString.toString();

        } catch (Exception ex) {
            // --- 5. SI ALGO FALLA ---
            // Si el móvil no soporta SHA-256 (algo casi imposible hoy en día), capturamos el error.
            // Usamos el 'Log.e' de Android para escribir el error de forma segura en la consola interna,
            // sin que los hackers puedan verlo en la pantalla.
            android.util.Log.e("Utilidades", "Error crítico al cifrar la contraseña", ex);

            // Si el conjunto  falla, devolvemos la contraseña original para que la app no se bloquee por completo.
            return password;
        }
    }

    // ========================================================================
    // === COMPROBADOR DE CADUCIDAD (3 MESES)                               ===
    // ========================================================================

    // Metodo público (accesible desde cualquier lado) y estático (se puede usar sin crear un objeto Utilidades).
    // Devuelve un valor 'boolean' (true si caduca pronto, false si aún le queda tiempo o ya caducó).
    // Recibe como dato 'fechaStr', que es la fecha en formato texto (String) que sacamos de la base de datos.
    public static boolean estaCercaDeCaducar(String fechaStr) {

        // --- ESCUDO ANTI-NULOS ---
        // Si no le pasamos ninguna fecha (null) o el texto está vacío (""),
        // devolvemos 'false' directamente para evitar que la aplicación se "cuelgue".
        if (fechaStr == null || fechaStr.isEmpty()) return false;

        // Abrimos un bloque 'try-catch' porque trabajar con fechas introducidas por usuarios
        // es peligroso. Si el usuario escribió "hola" en vez de una fecha, esto podría explotar.
        try {

            // --- 1. CONVERTIR TEXTO A FECHA REAL ---

            // Creamos una herramienta ('SimpleDateFormat') que sabe traducir textos a fechas.
            // Le decimos el patrón exacto que usamos en España: "dd/MM/yyyy" (Día/Mes/Año).
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault());

            // Usamos la herramienta para parsear (traducir) el texto 'fechaStr' y convertirlo
            // en un objeto de tipo 'Date' (Fecha) real que Java pueda entender matemáticamente.
            java.util.Date fechaCaducidad = sdf.parse(fechaStr);

            // Creamos un nuevo objeto 'Date' vacío. Al crearlo así, Java le asigna
            // automáticamente la fecha y hora exacta de este mismo segundo (HOY).
            java.util.Date hoy = new java.util.Date();

            // Medida de seguridad extra: si la traducción de la fecha falló y es nula, salimos.
            if (fechaCaducidad == null) return false;


            // --- 2. CALCULAR LA CADUCIDAD DE LOS 3 MESES ---

            // La clase 'Calendar' es la calculadora de tiempo de Java. Nos permite sumar o restar días, meses o años.
            java.util.Calendar cal = java.util.Calendar.getInstance();

            // Le decimos a la calculadora que empiece a contar desde el día de HOY.
            cal.setTime(hoy);

            // Le SUMAMOS exactamente 3 MESES a la fecha de hoy.
            cal.add(java.util.Calendar.MONTH, 3);

            // Guardamos el resultado de esa suma matemática en una nueva variable llamada 'fechaLimite'.
            // Ejemplo: Si hoy es 1 de Enero, 'fechaLimite' será ahora el 1 de Abril.
            java.util.Date fechaLimite = cal.getTime();


            // --- 3. EL VEREDICTO FINAL ---

            // Hacemos dos preguntas cruciales para saber si disparamos la alerta:
            // Pregunta 1: ¿La fecha de caducidad es DESPUÉS (after) de hoy? (Para ignorar cosas que ya caducaron el año pasado).
            // Pregunta 2: ¿La fecha de caducidad es ANTES (before) de nuestra fecha límite de 3 meses?


            // Si ambas son ciertas, devuelve TRUE (¡Alerta!). Si alguna falla, devuelve FALSE (Todo en orden).
            return fechaCaducidad.after(hoy) && fechaCaducidad.before(fechaLimite);

        } catch (Exception e) {
            // Si durante el proceso hubo algún error (ej: el texto no era una fecha válida),
            // el código salta directamente aquí. Devolvemos 'false' para que no salte ninguna
            // alarma errónea y la aplicación siga funcionando sin cerrarse de golpe.
            return false;
        }
    }

    // Para poder verificar las claves que sean iguales y asi tambien ver que me funcionan los test
    public static boolean contrasenasCoinciden(String pass1, String pass2) {
        // Si alguna es nula, no coinciden. Si no, comprobamos si son iguales.
        if (pass1 == null || pass2 == null) return false;
        return pass1.equals(pass2);
    }
    // Para poder verificar la longitud de la contraesañ y asi tambien ver que me funcionan los test
    public static boolean esPasswordSegura(String password) {
        // Si la contraseña es nula o tiene menos de 8 caracteres, no es segura
        return password != null && password.length() >= 8;
    }

    // Añado este metodo para comprobar que los campos DNI o Clave no esten vacios
    //pongo este metodo en utilidades para poder despues realizar la pruebas unitarias
    // String... campos . El símbolo ... le dice a Java: "Prepárate, porque te voy a pasar una lista
    // de  textos, pero no sé cuántos serán: pueden ser 2, pueden ser 10 o puede ser 1". Se llama Variable Arguments
    public static boolean camposRellenos(String... campos) {
        for (String campo : campos) {
            if (campo == null || campo.trim().isEmpty()) {
                return false; // Si encuentra uno vacío, devuelve falso inmediatamente
            }
        }
        return true; // Todos los campos tienen texto
    }

}