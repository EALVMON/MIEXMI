package com.proyecto.miexmi;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ConsultaExpedienteActivity extends AppCompatActivity {

    private TextView tvNombre, tvDni, tvContenido;
    private com.google.android.material.textfield.TextInputEditText etFiltroBod, etFiltroFecha;
    private Button btnBuscar;

    private ExpedienteHelper dbHelper;
    private int idUsuarioActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_expediente);

        // 1. Configuramos el botón volver usando tu clase Utilidades
        Utilidades.configurarBotonVolver(this, R.id.btnVolverConsulta);

        // 2. Iniciamos base de datos y recuperamos la sesión
        dbHelper = new ExpedienteHelper(this);
        idUsuarioActual = Utilidades.obtenerUsuarioActual(this);

        if (idUsuarioActual == -1) {
            Toast.makeText(this, "Error de sesión", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 3. Enlazamos los elementos de la pantalla (XML) con Java
        tvNombre = findViewById(R.id.tvNombreConsulta);
        tvDni = findViewById(R.id.tvDniConsulta);
        tvContenido = findViewById(R.id.tvContenidoExpediente);
        etFiltroBod = findViewById(R.id.etFiltroBod);
        etFiltroFecha = findViewById(R.id.etFiltroFecha);
        btnBuscar = findViewById(R.id.btnBuscarFiltro);

        // 4. Activamos el calendario maestro en el campo de fecha usando Utilidades
        Utilidades.configurarCalendario(this, etFiltroFecha);

        // 5. Llenamos la cabecera azul y cargamos el expediente entero por defecto
        cargarDatosCabecera();
        cargarExpediente("", "");

        // 6. LÓGICA DEL BOTÓN DE BÚSQUEDA (PU.10)
        btnBuscar.setOnClickListener(v -> {
            String bod = etFiltroBod.getText() != null ? etFiltroBod.getText().toString().trim() : "";
            String fecha = etFiltroFecha.getText() != null ? etFiltroFecha.getText().toString().trim() : "";

            // Refrescamos la pantalla pasando los filtros
            cargarExpediente(bod, fecha);

            // Ocultamos el teclado numérico para que el usuario pueda ver los resultados
            try {
                Utilidades.ocultarTeclado(this, etFiltroBod);
            } catch (Exception ignored) {}
        });
    }

    // ====================================================================
    // === MÉTODOS PRIVADOS DE LÓGICA                                   ===
    // ====================================================================

    private void cargarDatosCabecera() {
        // Obtenemos el DNI
        String dni = dbHelper.obtenerDniPorId(idUsuarioActual);
        tvDni.setText("DNI: " + (dni != null ? dni : "Desconocido"));

        // Obtenemos Nombre y Apellidos
        Cursor c = dbHelper.obtenerFiliacion(idUsuarioActual);
        if (c.moveToFirst()) {
            String nombreFull = c.getString(c.getColumnIndexOrThrow("Nombre")) + " " +
                    c.getString(c.getColumnIndexOrThrow("Apellidos"));
            tvNombre.setText(nombreFull);
        } else {
            tvNombre.setText("Sin Filiación Registrada");
        }
        c.close();
    }

    // Método único para mostrar el resumen (PU.09) y también para buscar (PU.10)
    private void cargarExpediente(String bod, String fecha) {
        String textoExpediente = dbHelper.obtenerResumenExpediente(idUsuarioActual, bod, fecha);

        if (!bod.isEmpty() || !fecha.isEmpty()) {
            tvContenido.setText("=== RESULTADOS DEL FILTRO ===\n\n" + textoExpediente);
        } else {
            tvContenido.setText(textoExpediente);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close(); // Cerramos la base de datos para no dejar conexiones "fantasma"
    }
}