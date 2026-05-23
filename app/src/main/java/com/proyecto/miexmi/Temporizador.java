package com.proyecto.miexmi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// Le digo que herede de 'AppCompatActivity' así sabe que va a ser una pantalla visual y puedo utilizar sus métodos.
public class Temporizador extends AppCompatActivity {

    // Variables que voy a utilizar para controlar el cierre por inactividad
    // Usamos un Handler para contar el tiempo en milisegundos es un temporizador
    private final Handler handlerInactividad = new Handler();
    // defino un objeto runnable que lo defino en la linea 66 y se dispra si pasan 3 minutos
    private Runnable runnableInactividad;
    // Definimos el tiempo límite: 3 minutos (3 * 60 segundos * 1000 milisegundos)
    private static final long TIEMPO_INACTIVIDAD = 3 * 60 * 1000;

    // Aquí guardamos el texto justo antes de meterlo en el archivo,lo inicializo para que no sea null
    // y no me de fallo si no hay datos que guardar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Se define qué pasará cuando el tiempo de 3 minutos se agote y se realice el cierre automático de la sesión
        // Programo el runnable que son las instrucciones que se realizarán

        runnableInactividad = () -> {
            // 1. Borramos la sesión para obligar a loguearse de nuevo
            SharedPreferences prefsSalir = getSharedPreferences("SesionApp", Context.MODE_PRIVATE);
            prefsSalir.edit().clear().apply();

            // 2. Mostramos aviso
            Toast.makeText(this, "Sesión cerrada por inactividad (3 min)", Toast.LENGTH_LONG).show();

            // 3. Redirigimos al Login
            Intent intentInactividad = new Intent(this, LoginActivity.class);

            // 4- Evitamos que el usuario pueda darle al botón "Atrás" del móvil y volver a entrar a la APP
            intentInactividad.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intentInactividad);
            finish();
        };
    }

    // Con este metodo se reinicia el contador de 3 min cuando el usuario realice algo en la app
    private void reiniciarTemporizador() {
        // con la siguiente línea le digo que se olvide de la cuenta atrás anterior
        handlerInactividad.removeCallbacks(runnableInactividad);
        // en la siguiente línea le digo que coja las instrucciones del runnable y las ejecute en 3 minutos
        // una nueva cuenta atrás
        handlerInactividad.postDelayed(runnableInactividad, TIEMPO_INACTIVIDAD);
    }

    // Detector nativo de Android: salta cada vez que el usuario toca la pantalla o hace scroll
    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        // Si el usuario toca algo, le damos otros 3 minutos de tiempo
        reiniciarTemporizador();
    }

    // Cuando la pantalla se vuelve visible para el usuario (al entrar o al volver de otra app)
    @Override
    protected void onResume() {
        super.onResume();
        // Si el usuario toca algo, le damos otros 3 minutos de tiempo
        reiniciarTemporizador();
    }

    // Cuando la pantalla deja de verse (el usuario bloquea el móvil o minimiza la app)
    @Override
    protected void onPause() {
        super.onPause();
        // Paramos el temporizador para evitar que cierre la sesión mientras no usamos la app
        handlerInactividad.removeCallbacks(runnableInactividad);
    }
}
