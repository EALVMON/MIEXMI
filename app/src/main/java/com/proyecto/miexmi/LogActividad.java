package com.proyecto.miexmi;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

// ====================================================================
// 1. DECLARACIÓN DE LA CLASE Y VARIABLES GLOBALES
// 1. Declaro variables.
// 2. Enlazo los botones del XML en el onCreate.
// 3. Configuro la lista (RecyclerView).
// 4. Le digo a los botones lo que tienen que hacer con los clics.
// 5. Hago un metodo para leer la base de datos (cargarLista()).
// ====================================================================
// Le digo que herede de 'AppCompatActivity' así sabe que va a ser una pantalla visual y puedo utilizar sus métodos.
public class LogActividad extends AppCompatActivity {

    // Declaro ExpedienteHelper que será el que se comunica con la base de datos SQLite
    private ExpedienteHelper dbHelper;

    // Variables internas
    private int idUsuarioActual; // La utilizo para saber el ID del usuario que está utilizando la app

    // Variables que voy a usar para la lista visual
    private LogAdaptador adaptador;
    private List<LogModelo> listaDatos;

    // ====================================================================
    // 2.Enlazo los botones del XML en el onCreate.
    // ====================================================================
    // Este método es lo primero que se ejecuta
    // cuando el usuario abre esta pantalla en el móvil.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Se conecta con el archivo XML de diseño
        setContentView(R.layout.activity_log_actividad);

        // Llamamos al metodo que está en la clase Utilidades y es genérico para todas las pantallas.
        // Funciona cuando el usuario hace clic en la flecha de "Atrás", para volver a la pantalla anterior.
        Utilidades.configurarBotonVolver(this, R.id.btnVolverLog);

        // Nos conectamos a la base de datos y preguntamos quién es el usuario, mediante otro metodo
        // que está en la clase Utilidades y es genérico para todas las pantallas (devuelve el ID del usuario actual).
        dbHelper = new ExpedienteHelper(this);
        idUsuarioActual = Utilidades.obtenerUsuarioActual(this);

        // Si por algún fallo el usuario no existe, le echamos de la pantalla
        if (idUsuarioActual == -1) {
            Toast.makeText(this, "Error de sesión", Toast.LENGTH_SHORT).show();
            finish(); // Cierra esta pantalla
            return;   // Corta la ejecución para que no dé error
        }

        // ====================================================================
        // 3. ENLAZAMOS JAVA CON EL XML (findViewById)
        // ====================================================================
        // Buscamos la lista en la pantalla
        RecyclerView rvLogActividad = findViewById(R.id.rvLogActividad);

        // ====================================================================
        // 4. PREPARAMOS LA LISTA (RecyclerView)
        // ====================================================================
        // Le decimos a la lista que se dibuje de arriba a abajo con el LinearLayoutManager
        rvLogActividad.setLayoutManager(new LinearLayoutManager(this));
        listaDatos = new ArrayList<>(); // Creamos la lista vacía en memoria

        // Conectamos el Adaptador que está en Kotlin (LogAdaptador.kt)
        // No necesita listener ya que los registros de log son solo para visualizar
        adaptador = new LogAdaptador(listaDatos);

        // Enganchamos el adaptador terminado a la lista visual
        rvLogActividad.setAdapter(adaptador);

        // Llamamos a la base de datos para que traiga los datos y los pinte, mediante el método
        // cargarLista() cuyo código está al final
        cargarLista();

        // ====================================================================
        // 5. PROGRAMAR LOS BOTONES (CRUD)
        // ====================================================================
        // Esta pantalla es un registro de auditoría de seguridad histórico,
        // por lo tanto, no se permiten añadir, modificar ni eliminar datos de forma manual.
    }

    // ====================================================================
    // 6. MÉTODOS AUXILIARES
    // ====================================================================

    // Función que lee de SQLite y llena el RecyclerView
    @android.annotation.SuppressLint("NotifyDataSetChanged") // Lo puse para quitar un
    // warning del entorno de desarrollo porque necesito hacer un repintado total de la vista en cargarLista()
    private void cargarLista() {
        listaDatos.clear(); // Borramos la lista actual para no duplicar datos

        // Usamos un Cursor que va señalando fila por fila en la base de datos
        Cursor c = dbHelper.obtenerAccesos(idUsuarioActual);

        // Si el Cursor ha encontrado al menos un resultado (moveToFirst)...
        if (c.moveToFirst()) {
            do {
                // Extraemos las columnas de la tabla de la base de datos
                String dni = c.getString(c.getColumnIndexOrThrow("DNI"));
                String fechaHora = c.getString(c.getColumnIndexOrThrow("Fecha_Hora"));

                // Instanciamos la data class que teníamos creada en LogAdaptador en Kotlin
                // y la metemos en nuestra lista de Java
                listaDatos.add(new LogModelo(dni, fechaHora));

            } while (c.moveToNext()); // ... Y repetimos hasta que no haya más filas
        }
        c.close(); // Cerramos el Cursor

        // Le indicamos al adaptador que ha habido cambios y que vuelva a pintar la lista
        adaptador.notifyDataSetChanged();
    }
}