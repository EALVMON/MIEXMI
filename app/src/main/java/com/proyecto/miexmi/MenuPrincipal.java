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

        //  Enlazamos el texto de la cabecera donde mostraremos el usuario
        TextView tvNombreMilitar = findViewById(R.id.tvNombreMilitar);

        // RECUPERAMOS LA SESIÓN (El ID del usuario que hizo login)
        SharedPreferences prefs = getSharedPreferences("SesionApp", Context.MODE_PRIVATE);
        int idUsuarioActual = prefs.getInt("ID_USUARIO_ACTUAL", -1);

        // Si hay un usuario logueado (el ID es diferente de -1)
        if (idUsuarioActual != -1) {

            // 1. Inicializamos la conexión con la Base de Datos como variable LOCAL
            // Usamos un bloque try-with-resources para que se cierre sola al terminar
            try (ExpedienteHelper dbHelper = new ExpedienteHelper(this)) {

                // --- NUEVA LÓGICA PLAN A (Nombre) y PLAN B (DNI) ---
                // Intentamos buscar sus datos en la tabla FILIACION primero
                Cursor cursorFilia = dbHelper.obtenerFiliacion(idUsuarioActual);

                if (cursorFilia.moveToFirst()) {
                    // PLAN A: Si ya rellenó sus datos, mostramos Nombre y Apellidos
                    String nombre = cursorFilia.getString(cursorFilia.getColumnIndexOrThrow("Nombre"));
                    String apellidos = cursorFilia.getString(cursorFilia.getColumnIndexOrThrow("Apellidos"));
                    tvNombreMilitar.setText(getString(R.string.nombre_completo, nombre, apellidos));
                } else {
                    // PLAN B: Si no tiene filiación, buscamos el DNI en la base de datos
                    String dniUsuario = dbHelper.obtenerDniPorId(idUsuarioActual);

                    if (dniUsuario != null) {
                        // Mostramos el DNI en la pantalla usando el recurso String
                        tvNombreMilitar.setText(getString(R.string.usuario_conectado, dniUsuario));
                    }
                }

                cursorFilia.close(); // Siempre cerramos el cursor al terminar
            }

        } else {
            // Si por algún error no hay sesión (alguien intentó saltarse el Login),
            // lo devolvemos al Login por seguridad.
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


    // Este metodo se ejecuta automáticamente cuando vuelves al Menú Principal
    // desde otra pantalla (por ejemplo, después de guardar la Filiación).
    // Lo usamos para refrescar la pantalla y que el nombre se actualice al instante.
    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }
}