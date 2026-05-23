package com.proyecto.miexmi;
/*
  ========================================================================
  PROYECTO: Mi Expediente Militar
  AUTOR: Eduardo José Álvarez Montes
  FECHA DE ENTREGA: Junio 2026
  * AGRADECIMIENTOS:
  Quiero dedicar este proyecto a mi familia por su paciencia, y en especial
  a mi cuñada Amparo (gracias, nos volveremos a ver)
  ========================================================================
 */
// Importaciones necesarias

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


// Importamos la clase base de todas las Activities
import androidx.appcompat.app.AppCompatActivity;

// Creamos la clase LoginActivity que Hereda de AppCompatActivity
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

            // Obtenemos lo que escribe el usuario, (con .trim()  quitamos espacios inútiles)
            // Añado .toUpperCase() para que las minúsculas no den problemas al iniciar sesión, ya que
            // en registro obligo a que de guarde la letra del dni en mayusculas, asi aqui aunuque el usuario
            // no se de cuenta de ponerlo en mayusculsa se transfoma
            String dni = etDni.getText().toString().trim().toUpperCase();
            String pass = etPassword.getText().toString().trim();

            // 3. Comprobamos si hay campos vacíos
            if (!Utilidades.camposRellenos(dni, pass)) { // si me devuelve falso si algun campo esta vacio
                Toast.makeText(this, " Debe rellenar todos los campos", Toast.LENGTH_SHORT).show();
            } else {

                // 4. Comprobamos si el usuario existe y si la contraseña es correcta
                // ciframos la contraseña escrita, para poder compararla con la de la Base de Datos
                String contrasenaCifrada = Utilidades.cifrarContrasena(pass);
                int idUsuario = dbHelper.comprobarLogin(dni, contrasenaCifrada);

                // Si no devuelve -1, significa que el usuario existe y la contraseña es correcta
                if (idUsuario != -1) {

                    // === Registramos el acceso del usuario en la tabla REGISTRO_ACTIVIDAD ===
                    // 1. Creamos el formato de fecha: día/mes/año hora:minuto:segundo para pasarsela a la funcion
                    // registrarAcceso
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
                    // 2. Obtenemos la hora actual del sistema
                    String fechaHoraActual = sdf.format(new Date());
                    // 3. Llamamos a la base de datos para guardar este acceso
                    dbHelper.registrarAcceso(idUsuario, dni, fechaHoraActual);

                    // 5. Guardmos el id del usuario de la Sesion y asi porder utilizarlo en
                    // en otras pantallas
                    // Me creo un pequeño archivo oculto en la memoria del movil llamado SesionApp,
                    // el mode_private es para que ninguna otra app del movil pueda accerder a el
                    SharedPreferences prefs = getSharedPreferences("SesionApp", Context.MODE_PRIVATE);
                    // preparo para editar el prefs y le pongo de nombre prefs
                    SharedPreferences.Editor editor = prefs.edit();
                    // meto el idUsuario en una clave que se va a llamar ID_USUARIO_ACTUAL y va a poder verse
                    // en el resto de las pantallas de esta sesion
                    editor.putInt("ID_USUARIO_ACTUAL", idUsuario);
                    // lo guardo en editor
                    editor.apply();

                    // Mensaje visual de quetodo ha ido bien, aparece en la parte inferior de la pantalla
                    Toast.makeText(this, "Accediendo a Mi Expediente...", Toast.LENGTH_SHORT).show();

                    // Creamos el Intent indicando de que pantalla venimos y a que pantalla vamos
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
            // Creamos el Intent indicando de que pantalla venimos y a que pantalla vamos
            Intent intent = new Intent(LoginActivity.this, Registro.class);
            // Iniciamos la nueva Activity
            startActivity(intent);
        });
    }
}