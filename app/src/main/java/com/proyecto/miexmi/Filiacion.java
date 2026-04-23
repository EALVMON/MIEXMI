package com.proyecto.miexmi;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class Filiacion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filiacion);

        // Configuramos el botón de volver
        Utilidades.configurarBotonVolver(this, R.id.btnVolverFiliacion);

        // RECUPERAMOS LA SESIÓN usando la función que nos hemos creado en Utilidades.java
        int idUsuarioActual = Utilidades.obtenerUsuarioActual(this);

        if (idUsuarioActual == -1) {
            Toast.makeText(this, "Error de sesión", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // ENLAZAMOS LOS ELEMENTOS DEL XML
        TextInputEditText etNombre = findViewById(R.id.etNombreFiliacion);
        TextInputEditText etApellidos = findViewById(R.id.etApellidosFiliacion);
        TextInputEditText etTmi = findViewById(R.id.etTmiFiliacion);
        TextInputEditText etEscalafon = findViewById(R.id.etEscalafonFiliacion);
        TextInputEditText etFechaIncorp = findViewById(R.id.etFechaIncorpFiliacion);

        Button btnGuardar = findViewById(R.id.btnGuardarFiliacion);
        Button btnLimpiar = findViewById(R.id.btnLimpiarFiliacion);

        // --- CONFIGURACIÓN DEL CALENDARIO PARA LA FECHA ---
        // Lo hacemos usando nuestra función maestra para que la fecha siempre tenga
        // el formato perfecto y el código quede súper limpio.
        Utilidades.configurarCalendario(this, etFechaIncorp);

        // CARGAMOS LOS DATOS SI YA EXISTEN EN LA BASE DE DATOS
        try (ExpedienteHelper dbHelper = new ExpedienteHelper(this)) {
            Cursor cursor = dbHelper.obtenerFiliacion(idUsuarioActual);

            // Como sabemos que el cursor nunca es nulo, vamos directos al grano
            if (cursor.moveToFirst()) {
                // Sacamos los datos de las columnas y los escribimos en los campos de texto
                etNombre.setText(cursor.getString(cursor.getColumnIndexOrThrow("Nombre")));
                etApellidos.setText(cursor.getString(cursor.getColumnIndexOrThrow("Apellidos")));
                etTmi.setText(cursor.getString(cursor.getColumnIndexOrThrow("TMI")));
                etFechaIncorp.setText(cursor.getString(cursor.getColumnIndexOrThrow("Fech_Incorp")));

                // El escalafón es un número, lo convertimos a texto para poder mostrarlo
                int escalafonGuardado = cursor.getInt(cursor.getColumnIndexOrThrow("Nun_Escalafon"));
                if (escalafonGuardado != 0) {
                    etEscalafon.setText(String.valueOf(escalafonGuardado));
                }
            }

            // Cerramos el cursor directamente
            cursor.close();
        }

        // ACCIÓN DEL BOTÓN LIMPIAR
        btnLimpiar.setOnClickListener(v -> {
            etNombre.setText("");
            etApellidos.setText("");
            etTmi.setText("");
            etEscalafon.setText("");
            etFechaIncorp.setText("");
        });

        // ACCIÓN DEL BOTÓN GUARDAR
        btnGuardar.setOnClickListener(v -> {

            // Recogemos los textos (con la protección contra nulos)
            String nombre = etNombre.getText() != null ? etNombre.getText().toString().trim() : "";
            String apellidos = etApellidos.getText() != null ? etApellidos.getText().toString().trim() : "";
            String tmi = etTmi.getText() != null ? etTmi.getText().toString().trim() : "";
            String fecha = etFechaIncorp.getText() != null ? etFechaIncorp.getText().toString().trim() : "";
            String escalafonStr = etEscalafon.getText() != null ? etEscalafon.getText().toString().trim() : "";

            // Validación: Obligamos a poner al menos Nombre y Apellidos
            if (nombre.isEmpty() || apellidos.isEmpty()) {
                Toast.makeText(this, "El nombre y apellidos son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            // Convertimos el Escalafón a número entero (si está vacío, le ponemos un 0)
            int escalafon = 0;
            if (!escalafonStr.isEmpty()) {
                try {
                    escalafon = Integer.parseInt(escalafonStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "El escalafón debe ser un número válido", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            // Guardamos en la Base de Datos
            try (ExpedienteHelper dbHelper = new ExpedienteHelper(this)) {
                boolean exito = dbHelper.guardarFiliacion(idUsuarioActual, nombre, apellidos, tmi, fecha, escalafon);

                if (exito) {
                    Toast.makeText(this, "Datos guardados correctamente", Toast.LENGTH_SHORT).show();
                    finish(); // Cerramos y volvemos a la pantalla anterior
                } else {
                    Toast.makeText(this, "Error al guardar los datos", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}