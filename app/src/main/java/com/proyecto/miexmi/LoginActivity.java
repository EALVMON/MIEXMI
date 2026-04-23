package com.proyecto.miexmi;

// Importaciones necesarias
import android.content.Context;           // Necesario para SharedPreferences
import android.content.Intent;            // Para cambiar de pantalla (Activity)
import android.content.SharedPreferences; // Para guardar la "sesión" del usuario
import android.os.Bundle;                 // Para el ciclo de vida de la Activity
import android.widget.Button;             // Botón
import android.widget.EditText;           // Campos de texto
import android.widget.TextView;           // Texto (como "Regístrate")
import android.widget.Toast;              // Mensajes emergentes

// Importamos la clase base de todas las Activities
import androidx.appcompat.app.AppCompatActivity;

// Creamos la clase LoginActivity que HEREDA de AppCompatActivity
public class LoginActivity extends AppCompatActivity {

    // Declaramos variables para los elementos del XML
    EditText etDni, etPassword;
    Button btnLogin;
    TextView tvRegistro;

    // 1. Declaramos nuestra Base de Datos
    private ExpedienteHelper dbHelper;

    // Metodo principal que se ejecuta al abrir la pantalla
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Conectamos esta clase con el XML (activity_login.xml)
        setContentView(R.layout.activity_login);

        // 2. Inicializamos la conexión con la Base de Datos
        dbHelper = new ExpedienteHelper(this);

        // Asociamos variables con los elementos del XML mediante su ID
        etDni = findViewById(R.id.etDni);
        etPassword = findViewById(R.id.etContrasena);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegistro = findViewById(R.id.tvRegistro);

        // Acción al pulsar el botón LOGIN
        btnLogin.setOnClickListener(v -> {

            // Obtenemos lo que escribe el usuario (con .trim() para quitar espacios inútiles)
            // ¡NUEVO!: Añadimos .toUpperCase() para que las minúsculas no den problemas al iniciar sesión
            String dni = etDni.getText().toString().trim().toUpperCase();
            String pass = etPassword.getText().toString().trim();

            // 3. Comprobamos si hay campos vacíos
            if (dni.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();
            } else {

                // 4. Comprobamos si el usuario existe y si la contraseña es correcta
                int idUsuario = dbHelper.comprobarLogin(dni, pass);

                // Si no devuelve -1, significa que el usuario existe y la contraseña es correcta
                if (idUsuario != -1) {

                    // 5. GUARDAMOS LA SESIÓN: Guardamos el ID del usuario para usarlo en otras pantallas
                    SharedPreferences prefs = getSharedPreferences("SesionApp", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("ID_USUARIO_ACTUAL", idUsuario);
                    editor.apply();

                    // Mensaje visual de quetodo ha ido bien
                    Toast.makeText(this, "Accediendo a Mi Expediente...", Toast.LENGTH_SHORT).show();

                    // Creamos el Intent indicando: (De donde venimos , A donde vamos)
                    Intent intent = new Intent(LoginActivity.this, MenuPrincipal.class);
                    startActivity(intent);

                    // Cerramos la pantalla de Login actual.
                    // Así, si el usuario pulsa el botón "Atrás", sale de la app en lugar de volver a ver el Login.
                    finish();

                } else {
                    // Si devuelve -1, o el DNI no existe, o la contraseña está mal
                    Toast.makeText(this, "DNI o contraseña incorrectos", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Acción al pulsar "Regístrate"
        tvRegistro.setOnClickListener(v -> {

            // Limpiamos los campos antes de saltar de pantalla, para que al volver a esta esten vacios
            etDni.setText("");
            etPassword.setText("");
            // Creamos un Intent para cambiar a la pantalla de registro
            Intent intent = new Intent(LoginActivity.this, Registro.class);
            // Iniciamos la nueva Activity
            startActivity(intent);
        });
    }
}