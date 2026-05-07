package com.proyecto.miexmi;

// Importamos lo necesario para que funcione la Activity
import static com.proyecto.miexmi.ComprobarDni.validarDNI;

import android.os.Bundle;
import android.text.InputFilter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Registro extends AppCompatActivity {

    // Declaramos variables para los elementos del XML
    EditText etDniRegistro, etPasswordRegistro;
    Button btnRegistro;

    // 1. Declaramos nuestra Base de Datos
    private ExpedienteHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // 2. Inicializamos la conexión con la Base de Datos
        dbHelper = new ExpedienteHelper(this);

        // 3. ENLAZAMOS CON LOS IDs DEL  XML
        etDniRegistro = findViewById(R.id.etDniRegistro);
        etPasswordRegistro = findViewById(R.id.etContrasenaRegistro);
        btnRegistro = findViewById(R.id.btnRegistrarse);

        // 4.  ANTI-MINÚSCULAS
        etDniRegistro.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        // Acción cuando se pulsa el botón "Registrarse"
        btnRegistro.setOnClickListener(v -> {

            // Guardamos lo que escribe el usuario
            String dni = etDniRegistro.getText().toString().trim().toUpperCase();
            String pass = etPasswordRegistro.getText().toString().trim();

            // === 5. CASCADA DE VALIDACIONES ===

            // A. Comprobamos si hay campos vacíos
            if (dni.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();

                // B. Comprobamos la seguridad de la contraseña
            } else if (!Utilidades.esPasswordSegura(pass)) {
                Toast.makeText(this, "La contraseña debe tener al menos 8 caracteres", Toast.LENGTH_LONG).show();

                // C. Validamos el formato del DNI
            } else if (!validarDNI(dni)) {
                Toast.makeText(this, "DNI incorrecto", Toast.LENGTH_SHORT).show();

                // === 6. SI ESTÁ CORRECTO, PROCEDEMOS A GUARDAR ===
            } else {
                // Cogemos la contraseña que ha escrito el usuario y la ciframos
                String contrasenaCifrada = Utilidades.cifrarContrasena(pass);

                // Guardamos el usuario en la BD con la contraseña ya triturada
                long resultado = dbHelper.registrarUsuario(dni, contrasenaCifrada);

                // Comprobamos el resultado de la base de datos
                if (resultado != -1) {
                    Toast.makeText(this, "Registrado correctamente", Toast.LENGTH_SHORT).show();
                    finish(); // Cerramos y volvemos al login
                } else {
                    // (PRUEBA PU.01) El DNI ya existía en la base de datos
                    Toast.makeText(this, "Error: Este DNI ya tiene una cuenta", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}