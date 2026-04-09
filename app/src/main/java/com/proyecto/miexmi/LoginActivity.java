
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

    // Metodo principal que se ejecuta al abrir la pantalla
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
            // Despues de ver como funciona la pagina de menu principal quitar este inet y ponerlo en
            // el esle
            // Mensaje visual de que todo ha ido bien (opcional)
            Toast.makeText(this, "Accediendo a Mi Expediente...", Toast.LENGTH_SHORT).show();

            // 1. Creamos el Intent indicando: (De donde venimos , A donde vamos)
            Intent intent = new Intent(LoginActivity.this, MenuPrincipal.class);

            // 2. Iniciamos la nueva actividad (pantalla)
            startActivity(intent);

            // 3. (Recomendado) Cerramos la pantalla de Login actual.
            // Así, si el usuario pulsa el botón "Atrás" del móvil, sale de la app en lugar de volver a ver el Login.
            finish();
            /*
            // Comprobamos si hay campos vacíos
            if (dni.isEmpty() || pass.isEmpty()) {

                // Mostramos mensaje de error descomentar despues de hacer las pruebas
               Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();

            } else {

                // Mensaje de login correcto (aqui tengo que poner lo que me hace que es pasar a la pantalla de menu)
                Toast.makeText(this, "Login correcto", Toast.LENGTH_SHORT).show();
            }
            */

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