package com.proyecto.miexmi;

// Importamos lo necesario para que funcione la Activity
import static com.proyecto.miexmi.ComprobarDni.validarDNI;

import android.os.Bundle;
import android.text.InputFilter; // ¡NUEVO! Necesario para forzar mayúsculas
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

// Importamos AppCompatActivity
import androidx.appcompat.app.AppCompatActivity;

// Creamos la clase Registro que HEREDA de AppCompatActivity
public class Registro extends AppCompatActivity {

    // Declaramos variables para los elementos del XML
    EditText etDniRegistro, etPasswordRegistro;
    Button btnRegistro;

    // 1. Declaramos nuestra Base de Datos
    private ExpedienteHelper dbHelper;

    // Metodo principal que se ejecuta al abrir la pantalla
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Conectamos esta clase con el NUEVO XML (activity_registro.xml)
        setContentView(R.layout.activity_registro);

        // 2. Inicializamos la conexión con la Base de Datos
        dbHelper = new ExpedienteHelper(this);

        // 3. ENLAZAMOS CON LOS IDs DEL NUEVO XML (¡Esto evita que casque!)
        etDniRegistro = findViewById(R.id.etDniRegistro);
        etPasswordRegistro = findViewById(R.id.etContrasenaRegistro);
        btnRegistro = findViewById(R.id.btnRegistrarse);

        // 4. EL ESCUDO ANTI-MINÚSCULAS:
        // Esto fuerza a que la casilla escriba siempre en mayúsculas, tenga el teclado que tenga
        etDniRegistro.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        // Acción cuando se pulsa el botón "Registrarse"
        btnRegistro.setOnClickListener(v -> {

            // Guardamos lo que escribe el usuario (el toUpperCase() sigue siendo una buena red de seguridad)
            String dni = etDniRegistro.getText().toString().trim().toUpperCase();
            String pass = etPasswordRegistro.getText().toString().trim();

            // Comprobamos si hay campos vacíos
            if (dni.isEmpty() || pass.isEmpty()) {
                // Mostramos mensaje si falta algo
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();

                // Valido el DNI para saber si es correcto
            } else if (!validarDNI(dni)) {
                Toast.makeText(this, "DNI incorrecto", Toast.LENGTH_SHORT).show();

            } else {
                // AQUÍ HACEMOS LA MAGIA: Guardamos el usuario en la BD
                long resultado = dbHelper.registrarUsuario(dni, pass);

                // Si resultado NO es -1, significa que se ha guardado bien
                if (resultado != -1) {
                    // Mensaje de registro correcto
                    Toast.makeText(this, "Registrado correctamente", Toast.LENGTH_SHORT).show();
                    // Cerramos esta pantalla y volvemos al login mágicamente
                    finish();
                } else {
                    // Si resultado es -1, es porque el DNI ya existe en la base de datos
                    Toast.makeText(this, "Error: Este DNI ya tiene una cuenta", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}