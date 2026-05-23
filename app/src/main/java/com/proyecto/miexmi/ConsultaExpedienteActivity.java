package com.proyecto.miexmi;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class ConsultaExpedienteActivity extends Temporizador {

    // ====================================================================
    // 1. DECLARACIÓN DE VARIABLES GLOBALES
    // ====================================================================
    private TextView tvNombre, tvDni, tvContenido;

    // la siguientes variables son una version mejorada del EditText (Material Design)
    private com.google.android.material.textfield.TextInputEditText etFiltroBod, etFiltroFecha;

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

        // si nos devuelve -1 obtenerUsuario se cierra y le da el siguiente mensaje
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
        Button btnBuscar = findViewById(R.id.btnBuscarFiltro);

        // 4. Activamos el calendario en el campo de fecha usando Utilidades
        Utilidades.configurarCalendario(this, etFiltroFecha);

        // 5. Llenamos la cabecera azul y cargamos el expediente entero por defecto, estos metodos
        // estan al final en metodos privados
        cargarDatosCabecera();
        cargarExpediente("", "");

        // 6. Código del botón de búsqueda
        btnBuscar.setOnClickListener(v -> {
            String bod = etFiltroBod.getText() != null ? etFiltroBod.getText().toString().trim() : "";
            String fecha = etFiltroFecha.getText() != null ? etFiltroFecha.getText().toString().trim() : "";

            // Refrescamos la pantalla pasando los filtros
            cargarExpediente(bod, fecha);

            // Ocultamos el teclado numérico para que el usuario pueda ver los resultados
            try {
                Utilidades.ocultarTeclado(this, etFiltroBod);
            } catch (Exception ignored) {
            }
        });
    }

    // ====================================================================
    // === MÉTODOS PRIVADOS                                             ===
    // ====================================================================

    private void cargarDatosCabecera() {
        // Obtenemos el DNI
        String dni = dbHelper.obtenerDniPorId(idUsuarioActual);


        String textoDni = dni != null ? dni : getString(R.string.desconocido);
        tvDni.setText(getString(R.string.formato_dni, textoDni));

        // Obtenemos Nombre y Apellidos
        Cursor c = dbHelper.obtenerFiliacion(idUsuarioActual);
        if (c.moveToFirst()) {
            String nombreFull = c.getString(c.getColumnIndexOrThrow("Nombre")) + " " +
                    c.getString(c.getColumnIndexOrThrow("Apellidos"));
            tvNombre.setText(nombreFull);
        } else {

            tvNombre.setText(getString(R.string.sin_filiacion));
        }
        c.close();
    }

    // Metodo único para mostrar el resumen y también para buscar
    private void cargarExpediente(String bod, String fecha) {
        String textoExpediente = dbHelper.obtenerResumenExpediente(idUsuarioActual, bod, fecha);

        if (!bod.isEmpty() || !fecha.isEmpty()) {

            tvContenido.setText(getString(R.string.resultados_filtro, textoExpediente));
        } else {
            tvContenido.setText(textoExpediente);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close(); // Cerramos la base de datos
    }
}