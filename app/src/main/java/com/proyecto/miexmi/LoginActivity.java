
package com.proyecto.miexmi;

// Importaciones necesarias
import android.content.Intent;        // Para cambiar de pantalla (Activity)
import android.os.Bundle;             // Para el ciclo de vida de la Activity
import android.widget.Button;         // Botón
import android.widget.EditText;       // Campos de texto
import android.widget.TextView;       // Texto (como "Regístrate")
import android.widget.Toast;          // Mensajes emergentes

// Importamos la clase base de todas las Activities
import androidx.appcompat.app.AppCompatActivity;

// Creamos la clase LoginActivity que HEREDA de AppCompatActivity
public class LoginActivity extends AppCompatActivity {

    // Declaramos variables para los elementos del XML
    EditText etDni, etPassword;
    Button btnLogin;
    TextView tvRegistro;

    // Método principal que se ejecuta al abrir la pantalla
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Conectamos esta clase con el XML (activity_login.xml)
        setContentView(R.layout.activity_login);

        // Asociamos variables con los elementos del XML mediante su ID
        etDni = findViewById(R.id.etDni);
        etPassword = findViewById(R.id.etContrasena); //
        btnLogin = findViewById(R.id.btnLogin);
        tvRegistro = findViewById(R.id.tvRegistro);

        // Acción al pulsar el botón LOGIN
        btnLogin.setOnClickListener(v -> {

            // Obtenemos lo que escribe el usuario
            String dni = etDni.getText().toString();
            String pass = etPassword.getText().toString();

            // Comprobamos si hay campos vacíos
            if (dni.isEmpty() || pass.isEmpty()) {

                // Mostramos mensaje de error
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();

            } else {

                // Mensaje de login correcto (aqui tengo que poner lo que me hace que es pasar a la pantalla de menu)
                Toast.makeText(this, "Login correcto", Toast.LENGTH_SHORT).show();
            }
        });

        // Acción al pulsar "Regístrate"
        tvRegistro.setOnClickListener(v -> {

            // Creamos un Intent para cambiar a la pantalla de registro
            Intent intent = new Intent(LoginActivity.this, Registro.class);

            // Iniciamos la nueva Activity
            startActivity(intent);
        });
    }
}