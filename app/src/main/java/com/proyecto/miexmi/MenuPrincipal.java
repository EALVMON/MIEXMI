package com.proyecto.miexmi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor; // Añadido para poder leer la base de datos
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MenuPrincipal extends AppCompatActivity {

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

        // Programamos clic temporal para Exportar (Aún no tiene pantalla)
        cardExportar.setOnClickListener(v -> {
            Toast.makeText(this, "Módulo de Exportación en construcción", Toast.LENGTH_SHORT).show();
        });
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
}