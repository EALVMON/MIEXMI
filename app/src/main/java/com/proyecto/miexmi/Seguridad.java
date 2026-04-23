package com.proyecto.miexmi;


import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// Importamos el campo de texto de Material Design que tienes en tu XML
import com.google.android.material.textfield.TextInputEditText;

public class Seguridad extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seguridad);

        // Configuramos el botón de volver usando la clase de Utilidades
        Utilidades.configurarBotonVolver(this, R.id.btnVolverSeguridad);

        // RECUPERAMOS LA SESIÓN
        int idUsuarioActual = Utilidades.obtenerUsuarioActual(this);

        if (idUsuarioActual == -1) {
            Toast.makeText(this, "Error de sesión", Toast.LENGTH_SHORT).show();
            finish();
            return; // Cortamos ejecución si no hay sesión
        }

        // ENLAZAMOS LOS ELEMENTOS DEL XML
        TextInputEditText etDni = findViewById(R.id.etDniSeguridad);
        TextInputEditText etPassActual = findViewById(R.id.etPassActual);
        TextInputEditText etPassNueva = findViewById(R.id.etPassNueva);
        TextInputEditText etPassRepetir = findViewById(R.id.etPassRepetir);
        Button btnGuardar = findViewById(R.id.btnGuardarSeguridad);
        Button btnLimpiar = findViewById(R.id.btnLimpiarSeguridad);

        // CARGAMOS EL DNI Y LO BLOQUEAMOS PARA QUE NO SE PUEDA EDITAR
        try (ExpedienteHelper dbHelper = new ExpedienteHelper(this)) {
            String dniUsuario = dbHelper.obtenerDniPorId(idUsuarioActual);
            if (dniUsuario != null) {
                etDni.setText(dniUsuario);
                etDni.setEnabled(false); // Lo pone en modo "solo lectura"
            }
        }

        // ACCIÓN DEL BOTÓN LIMPIAR
        btnLimpiar.setOnClickListener(v -> {
            etPassActual.setText("");
            etPassNueva.setText("");
            etPassRepetir.setText("");
            // El DNI lo dejamos intacto
        });

        // ACCIÓN DEL BOTÓN GUARDAR CAMBIOS
        btnGuardar.setOnClickListener(v -> {

            // Recogemos los textos le pongo un  anti-nulos
            String actual = etPassActual.getText() != null ? etPassActual.getText().toString().trim() : "";
            String nueva = etPassNueva.getText() != null ? etPassNueva.getText().toString().trim() : "";
            String repetir = etPassRepetir.getText() != null ? etPassRepetir.getText().toString().trim() : "";

            // Validación 1: Campos vacíos
            if (actual.isEmpty() || nueva.isEmpty() || repetir.isEmpty()) {
                Toast.makeText(this, "Por favor, rellena todas las contraseñas", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validación 2: Coincidencia de la nueva contraseña
            if (!nueva.equals(repetir)) {
                Toast.makeText(this, "Las contraseñas nuevas no coinciden", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validación 3: No usar la misma que ya tenías
            if (actual.equals(nueva)) {
                Toast.makeText(this, "La nueva contraseña debe ser diferente a la actual", Toast.LENGTH_SHORT).show();
                return;
            }

            // Si la totalidad es correcto, actualizamos en la Base de Datos
            try (ExpedienteHelper dbHelper = new ExpedienteHelper(this)) {

                boolean exito = dbHelper.cambiarContrasena(idUsuarioActual, actual, nueva);
                // Si hay un cambio me devuelve true o false
                if (exito) {
                    Toast.makeText(this, "Contraseña actualizada correctamente", Toast.LENGTH_LONG).show();
                    finish(); // Cerramos y volvemos al menú
                } else {
                    Toast.makeText(this, "La contraseña actual es incorrecta", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}