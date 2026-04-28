package com.proyecto.miexmi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor; // Añadido para poder leer la base de datos
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.nio.charset.StandardCharsets;

public class MenuPrincipal extends AppCompatActivity {


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
}