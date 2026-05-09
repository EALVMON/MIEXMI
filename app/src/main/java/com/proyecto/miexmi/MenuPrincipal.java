package com.proyecto.miexmi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor; // Añadido para poder leer la base de datos
import android.os.Bundle;
import android.os.Handler; // [NUEVO] Para gestionar el temporizador de inactividad
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.nio.charset.StandardCharsets;

public class MenuPrincipal extends AppCompatActivity {

    // VARIABLES PARA EL CIERRE AUTOMÁTICO POR INACTIVIDAD
    // Usamos un Handler para contar el tiempo en milisegundos
    private final Handler handlerInactividad = new Handler();
    private Runnable runnableInactividad;
    // Definimos el tiempo límite: 3 minutos (3 * 60 segundos * 1000 milisegundos)
    private final long TIEMPO_INACTIVIDAD = 3 * 60 * 1000;

    // Aquí guardamos el texto justo antes de meterlo en el archivo
    private String datosTemporalesParaGuardar = "";

    // Este metodo Se encarga de recibir la carpeta
    // que el usuario eligió y escribir los datos ahí.
    private final ActivityResultLauncher<Intent> lanzadorGuardarArchivo =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

                // Si el usuario aceptó y eligió una ruta válida...
                if (result.getResultCode() == RESULT_OK && result.getData() != null && result.getData().getData() != null) {
                    try {
                        // Abrimos un "túnel" de salida hacia el archivo recién creado en el móvil
                        java.io.OutputStream outputStream = getContentResolver().openOutputStream(result.getData().getData());

                        if (outputStream != null) {
                            // Escribimos los datos en formato UTF-8 (vital para tildes y eñes)
                            outputStream.write(datosTemporalesParaGuardar.getBytes(StandardCharsets.UTF_8));
                            outputStream.close();
                            Toast.makeText(this, "✅ Archivo guardado con éxito", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, "❌ Error al guardar el archivo", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    // Metodo principal que se ejecuta al abrir la pantalla
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Conectamos esta clase con el XML (activity_menu_principal.xml)
        setContentView(R.layout.activity_menu_principal);

        //CONFIGURACIÓN DE LA TAREA DE CIERRE AUTOMÁTICO
        // Se define qué pasará cuando el tiempo se agote
        runnableInactividad = () -> {
            // Borramos la sesión para obligar a loguearse de nuevo
            SharedPreferences prefsSalir = getSharedPreferences("SesionApp", Context.MODE_PRIVATE);
            prefsSalir.edit().clear().apply();

            // Mostramos aviso y redirigimos al Login
            Toast.makeText(MenuPrincipal.this, "Sesión cerrada por inactividad (3 min)", Toast.LENGTH_LONG).show();
            Intent intentInactividad = new Intent(MenuPrincipal.this, LoginActivity.class);
            startActivity(intentInactividad);
            finish();
        };

        // RECUPERAMOS LA SESIÓN (El ID del usuario que hizo login)
        SharedPreferences prefs = getSharedPreferences("SesionApp", Context.MODE_PRIVATE);
        int idUsuarioActual = prefs.getInt("ID_USUARIO_ACTUAL", -1);

        // Si por algún error no hay sesión (alguien intentó saltarse el Login),
        // lo devolvemos al Login por seguridad.
        if (idUsuarioActual == -1) {
            Toast.makeText(this, "Error de sesión. Vuelve a iniciar sesión.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish(); // Cerramos el MenuPrincipal
            return;   // Detenemos la ejecución del resto del código
        }

        // Enlazamos las tarjetas y botones del XML con variables en Java
        CardView cardDatosPersonales = findViewById(R.id.cardDatosPersonales);
        CardView cardHistorialProfesional = findViewById(R.id.cardHistorialProfesional);
        CardView cardMeritosFormacion = findViewById(R.id.cardMeritosFormacion);
        CardView cardRegistrosAdmin = findViewById(R.id.cardRegistrosAdmin);
        CardView cardExportar = findViewById(R.id.cardExportar);
        CardView cardActividad = findViewById(R.id.cardActividad);
        CardView cardConsultaExpediente = findViewById(R.id.cardConsultaExpediente);

        // Enlazamos el botón de la "Rosca" de Ajustes/Seguridad
        ImageButton btnAjustes = findViewById(R.id.btnAjustesSeguridad);

        // Programamos el clic para la "Rosca" de Ajustes/Seguridad
        btnAjustes.setOnClickListener(v -> {
            Intent intent = new Intent(MenuPrincipal.this, Seguridad.class);
            startActivity(intent);
        });

        // Enlazamos el nuevo botón de cerrar sesión
        ImageButton btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        // Programamos el clic para "Cerrar Sesión"
        btnCerrarSesion.setOnClickListener(v -> {
            // 1. Borramos la "memoria" de quién estaba logueado
            SharedPreferences prefsSalir = getSharedPreferences("SesionApp", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefsSalir.edit();
            editor.clear(); // Esto vacía el archivo SharedPreferences por completo
            editor.apply();

            // 2. Avisamos al usuario
            Toast.makeText(MenuPrincipal.this, "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show();

            // 3. Lo mandamos de vuelta al Login y cerramos el menú
            Intent intentSalir = new Intent(MenuPrincipal.this, LoginActivity.class);
            startActivity(intentSalir);
            finish();
        });

        // Programamos el clic para "Datos Personales"
        cardDatosPersonales.setOnClickListener(v -> {
            Intent intent = new Intent(MenuPrincipal.this, MenuDatosPer.class);
            startActivity(intent);
        });

        // Programamos el clic para "Historial Profesional"
        cardHistorialProfesional.setOnClickListener(v -> {
            Intent intent = new Intent(MenuPrincipal.this, MenuHistoProfe.class);
            startActivity(intent);
        });

        // Programamos el clic para "Méritos y Formación"
        cardMeritosFormacion.setOnClickListener(v -> {
            Intent intent = new Intent(MenuPrincipal.this, MenuMeritoFor.class);
            startActivity(intent);
        });

        // Programamos el clic para "Otros Registros"
        cardRegistrosAdmin.setOnClickListener(v -> {
            Intent intent = new Intent(MenuPrincipal.this, MenuOtrosRegi.class);
            startActivity(intent);
        });

        // Programamos el clic para "Actividad de la App" (Lleva al Log)
        cardActividad.setOnClickListener(v -> {
            Intent intent = new Intent(MenuPrincipal.this, LogActividad.class);
            startActivity(intent);
        });

        // Programamos clic para Exportar
        cardExportar.setOnClickListener(v -> mostrarMenuExportacion());

        // Programamos el clic para llevar al usuario al Consulta Expediente
        cardConsultaExpediente.setOnClickListener(v -> {
            Intent intent = new Intent(MenuPrincipal.this, ConsultaExpedienteActivity.class);
            startActivity(intent);
        });
    }

    // [ METODO PARA REINICIAR EL TIEMPO DE INACTIVIDAD
    private void reiniciarTemporizador() {
        handlerInactividad.removeCallbacks(runnableInactividad);
        handlerInactividad.postDelayed(runnableInactividad, TIEMPO_INACTIVIDAD);
    }

    // DETECTOR DE INTERACCIÓN DEL USUARIO
    // Este metodo de Android se lanza cada vez que alguien toca la pantalla
    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        // Si el usuario toca algo, le damos otros 3 minutos de tiempo
        reiniciarTemporizador();
    }

    // ========================================================================
    // === CICLO DE VIDA PARA REFRESCO DE PANTALLA                          ===
    // ========================================================================

    // Este metodo se ejecuta automáticamente cuando volvemos al Menú Principal
    // desde otra pantalla (por ejemplo, después de guardar la Filiación o el Empleo).

    @Override
    protected void onResume() {
        super.onResume();
        // Disparamos la actualización del Dashboard
        actualizarResumen();

        // [NUEVO] Al volver al menú, activamos la vigilancia de inactividad
        reiniciarTemporizador();

        // --- LANZAMOS PARA VER LAS CADUCIDADES ---
        SharedPreferences prefs = getSharedPreferences("SesionApp", Context.MODE_PRIVATE);
        int idUsuarioActual = prefs.getInt("ID_USUARIO_ACTUAL", -1);
        if (idUsuarioActual != -1) {
            verificarCaducidades(idUsuarioActual);
        }
    }

    // CONTROL CUANDO LA APP SE QUEDA EN SEGUNDO PLANO
    @Override
    protected void onPause() {
        super.onPause();
        // Paramos el temporizador para evitar que cierre la sesión mientras no usamos la app
        handlerInactividad.removeCallbacks(runnableInactividad);
    }

    // ========================================================================
    // === METODO PARA LLENAR EL TABLERO CON DATOS FRESCOS                ===
    // ========================================================================
    private void actualizarResumen() {
        // RECUPERAMOS LA SESIÓN de nuevo para saber a quién buscar en la Base de Datos
        SharedPreferences prefs = getSharedPreferences("SesionApp", Context.MODE_PRIVATE);
        int idUsuarioActual = prefs.getInt("ID_USUARIO_ACTUAL", -1);

        if (idUsuarioActual == -1) return; // Si no hay usuario, abortamos

        // Enlazamos el texto de la cabecera donde mostraremos el usuario
        TextView tvNombreMilitar = findViewById(R.id.tvNombreMilitar);

        // ---  ENLACES PARA EL DASHBOARD ---
        TextView tvDetalleEmpleo = findViewById(R.id.tvDetalleEmpleo);
        TextView tvDetalleDestino = findViewById(R.id.tvDetalleDestino);

        // Vaciamos los textos del panel por si el usuario borró sus datos
        tvDetalleEmpleo.setText("");
        tvDetalleDestino.setText("");

        // 1. Inicializamos la conexión con la Base de Datos como variable LOCAL
        // Usamos un bloque try-with-resources para que se cierre sola al terminar
        try (ExpedienteHelper dbHelper = new ExpedienteHelper(this)) {

            // Intentamos buscar sus datos en la tabla FILIACION primero
            Cursor cursorFilia = dbHelper.obtenerFiliacion(idUsuarioActual);

            if (cursorFilia.moveToFirst()) {
                //  Si ya rellenó sus datos, mostramos Nombre y Apellidos
                String nombre = cursorFilia.getString(cursorFilia.getColumnIndexOrThrow("Nombre"));
                String apellidos = cursorFilia.getString(cursorFilia.getColumnIndexOrThrow("Apellidos"));
                tvNombreMilitar.setText(getString(R.string.nombre_completo, nombre, apellidos));
            } else {
                // Si no tiene filiación, buscamos el DNI en la base de datos
                String dniUsuario = dbHelper.obtenerDniPorId(idUsuarioActual);

                if (dniUsuario != null) {
                    // Mostramos el DNI en la pantalla usando el recurso String
                    tvNombreMilitar.setText(getString(R.string.usuario_conectado, dniUsuario));
                }
            }
            cursorFilia.close(); // Siempre cerramos el cursor al terminar

            // --- LÓGICA PARA CARGAR EL ÚLTIMO EMPLEO ---
            // Llamamos a la base de datos
            Cursor cursorEmpleo = dbHelper.obtenerEmpleos(idUsuarioActual);

            // Si el cursor encuentra datos (true), leemos la primera fila (el último empleo)
            if (cursorEmpleo.moveToFirst()) {
                String ultimoEmpleo = cursorEmpleo.getString(cursorEmpleo.getColumnIndexOrThrow("Nom_Empleo"));
                tvDetalleEmpleo.setText(getString(R.string.detalle_empleo, ultimoEmpleo));
            }
            cursorEmpleo.close(); // Cerramos el cursor de empleos

            // --- LÓGICA PARA CARGAR EL ÚLTIMO DESTINO ---
            // Llamamos a la base de datos para obtener los destinos (ya vienen ordenados por id DESC)
            Cursor cursorDestino = dbHelper.obtenerDestinos(idUsuarioActual);

            // Si encontramos algún destino, cogemos el primero de la lista (que es el más reciente)
            if (cursorDestino.moveToFirst()) {
                String ultimoDestino = cursorDestino.getString(cursorDestino.getColumnIndexOrThrow("Nom_Destino"));
                tvDetalleDestino.setText(getString(R.string.detalle_destino, ultimoDestino));
            }
            cursorDestino.close(); // Cerramos el cursor de destinos
        }
    }

    // ====================================================================
    // === LÓGICA DE EXPORTACIÓN (CSV y JSON)  Busque algo por internet ===
    // ====================================================================

    // Este metodo crea y muestra la ventanita emergente (pop-up) para elegir el formato.
    private void mostrarMenuExportacion() {

        // 1. Creamos una lista con las dos opciones de texto que verá el usuario.
        String[] opciones = {"📊 Exportar a Excel (CSV)", "💾 Copia de Seguridad (JSON)"};

        // 2. Usamos 'AlertDialog.Builder', que es la herramienta de Android para crear pop-ups en pantalla.
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);

        // 3. Le ponemos el título principal a la ventanita.
        builder.setTitle("¿Cómo quieres exportar tus datos?");

        // 4. Añadimos los botones y le decimos qué hacer cuando el usuario toque uno.
        // 'which' nos dirá qué número ha tocado: 0 para la primera opción, 1 para la segunda.
        builder.setItems(opciones, (dialog, which) -> {

            // Conectamos con la base de datos y averiguamos el ID del usuario que tiene la sesión abierta.
            try (ExpedienteHelper dbHelper = new ExpedienteHelper(this)) {
                int idUsuario = Utilidades.obtenerUsuarioActual(this);

                if (which == 0) {
                    // --- OPCIÓN 0: EL USUARIO ELIGIÓ EXCEL (CSV) ---

                    // Pedimos a la base de datos el conjunto del texto en formato Excel y lo guardamos temporalmente.
                    datosTemporalesParaGuardar = dbHelper.exportarACsv(idUsuario);

                    // Llamamos a la pantalla de "Guardar como...".
                    // "text/csv" es el código que usa Android para saber que esto es una hoja de cálculo.
                    lanzarGuardarArchivo("text/csv", "Mi_Expediente_Militar.csv");

                } else {
                    // --- OPCIÓN 1: EL USUARIO ELIGIÓ COPIA DE SEGURIDAD (JSON) ---

                    // Pedimos a la base de datos el conjunto del texto en formato JSON y lo guardamos temporalmente.
                    datosTemporalesParaGuardar = dbHelper.exportarTodoAJson(idUsuario);

                    // Llamamos a la pantalla de "Guardar como...".
                    // "application/json" es el código para archivos de datos puros.
                    lanzarGuardarArchivo("application/json", "Backup_Expediente.json");
                }
            }
        });

        // 5. Finalmente, con el conjunto configurado, mostramos el pop-up en la pantalla.
        builder.show();
    }


    // Este metodo "despierta" el explorador de archivos nativo del móvil (el típico "Guardar como...").
    private void lanzarGuardarArchivo(String mimeType, String nombreArchivo) {

        // ACTION_CREATE_DOCUMENT es la orden al sistema operativo para crear un archivo nuevo.
        android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_CREATE_DOCUMENT);

        // CATEGORY_OPENABLE asegura que el archivo se guarde en una carpeta accesible (no en lugares ocultos del móvil).
        intent.addCategory(android.content.Intent.CATEGORY_OPENABLE);

        // Le decimos de qué tipo es (CSV o JSON) usando la variable mimeType que recibimos.
        intent.setType(mimeType);

        // Le sugerimos un nombre de archivo por defecto (ej: "Backup_Expediente.json").
        intent.putExtra(android.content.Intent.EXTRA_TITLE, nombreArchivo);

        // Usamos el lanzador moderno (definido al principio de la clase) en lugar del obsoleto startActivityForResult
        lanzadorGuardarArchivo.launch(intent);
    }

    // MEtodo privado que no devuelve nada (void). Recibe como dato el ID del usuario actual.
    // Su trabajo es revisar carnets, armas, TMI y HPS buscando caducidades próximas.
    private void verificarCaducidades(int idUsuario) {

        // --- 1. COMPROBACIÓN DEL MODO "NO MOLESTAR" ---

        // Abrimos la "memoria interna" del móvil (SharedPreferences) donde guardamos la sesión y ajustes.
        android.content.SharedPreferences prefs = getSharedPreferences("SesionApp", android.content.Context.MODE_PRIVATE);

        // Buscamos si hay una fecha guardada con el nombre "FECHA_ALERTA_ACEPTADA". Si no existe, devuelve vacío ("").
        String ultimaVezAceptado = prefs.getString("FECHA_ALERTA_ACEPTADA", "");

        // Creamos una herramienta para darle formato a la fecha de hoy (día/mes/año).
        java.text.SimpleDateFormat formatoFecha = new java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault());

        // Generamos un texto con la fecha exacta de hoy usando la herramienta anterior.
        String hoyStr = formatoFecha.format(new java.util.Date());

        // Si la última vez que el usuario le dio a "Entendido" fue hoy...
        if (ultimaVezAceptado.equals(hoyStr)) {
            // ... abortamos . 'return' hace que el código se detenga y salga de este metodo.
            return;
        }


        // --- 2. BÚSQUEDA DE CADUCIDADES EN LA BASE DE DATOS ---

        // Creamos un "Constructor de Textos" (StringBuilder) que es muy eficiente para ir pegando frases largas.
        StringBuilder mensajeAlerta = new StringBuilder();

        // Abrimos la base de datos de forma segura (con try) para que se cierre sola al terminar y no gaste batería.
        try (ExpedienteHelper dbHelper = new ExpedienteHelper(this)) {

            // --- A) REVISAR CARNETS ---
            android.database.Cursor cCarnet = dbHelper.obtenerCarnets(idUsuario);
            while (cCarnet.moveToNext()) {
                String fec = cCarnet.getString(cCarnet.getColumnIndexOrThrow("M_Carn_Fecha_Caducidad"));
                if (Utilidades.estaCercaDeCaducar(fec)) {
                    mensajeAlerta.append("• Carnet ").append(cCarnet.getString(cCarnet.getColumnIndexOrThrow("Tipo_Carnet")))
                            .append(" caduca el ").append(fec).append("\n");
                }
            }
            cCarnet.close();


            // --- B) REVISAR ARMAS PARTICULARES ---
            android.database.Cursor cArmas = dbHelper.obtenerArmas(idUsuario);
            while (cArmas.moveToNext()) {
                String fec = cArmas.getString(cArmas.getColumnIndexOrThrow("M_EArm_Fecha_Cad"));
                if (Utilidades.estaCercaDeCaducar(fec)) {
                    mensajeAlerta.append("• Arma ").append(cArmas.getString(cArmas.getColumnIndexOrThrow("Nom_Arma")))
                            .append(" caduca el ").append(fec).append("\n");
                }
            }
            cArmas.close();


            // --- C) REVISAR TMI (Tarjeta Militar) ---
            android.database.Cursor cTmi = dbHelper.obtenerTMIs(idUsuario);
            while (cTmi.moveToNext()) {
                String fec = cTmi.getString(cTmi.getColumnIndexOrThrow("M_Tmi_Fecha_Cadu"));
                if (Utilidades.estaCercaDeCaducar(fec)) {
                    mensajeAlerta.append("• TMI (").append(cTmi.getString(cTmi.getColumnIndexOrThrow("N_Tarjeta")))
                            .append(") caduca el ").append(fec).append("\n");
                }
            }
            cTmi.close();


            // --- D) REVISAR HPS (Habilitaciones de Seguridad) ---
            android.database.Cursor cHps = dbHelper.obtenerHps(idUsuario);
            while (cHps.moveToNext()) {
                String fec = cHps.getString(cHps.getColumnIndexOrThrow("Fecha_M_Caducidad"));
                if (Utilidades.estaCercaDeCaducar(fec)) {
                    mensajeAlerta.append("• HPS (").append(cHps.getString(cHps.getColumnIndexOrThrow("Nom_Habilitacion")))
                            .append(") caduca el ").append(fec).append("\n");
                }
            }
            cHps.close();
        }

        // --- 3. MOSTRAR EL POP-UP (SOLO SI HAY ALGO QUE AVISAR) ---
        if (mensajeAlerta.length() > 0) {

            // Le pasamos el texto limpio (sin .toString()) al nuevo metodo externo para que dibuje la ventana.
            mostrarDialogoCaducidad(mensajeAlerta.toString(), prefs, hoyStr);
        }
    }

    // ---  METODO  se encarga de dibujar la ventanita emergente ---
    private void mostrarDialogoCaducidad(String listaAlertas, SharedPreferences prefs, String hoyStr) {

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);

        builder.setTitle("⚠️ AVISO DE CADUCIDAD");

        // concatenamos el String que nos llega.
        builder.setMessage("Los siguientes elementos caducan en menos de 3 meses:\n\n" + listaAlertas);

        builder.setPositiveButton("Entendido", (dialog, which) -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("FECHA_ALERTA_ACEPTADA", hoyStr);
            editor.apply();
        });

        builder.setNegativeButton("Recordar más tarde", (dialog, which) -> dialog.dismiss());
        builder.setCancelable(false);
        builder.show();
    }
}