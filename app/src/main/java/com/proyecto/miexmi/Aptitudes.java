package com.proyecto.miexmi;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textfield.TextInputEditText;
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
public class Aptitudes extends AppCompatActivity {

    // Declaramos las variables "globales" para que cualquier
    // botón o función dentro de esta pantalla pueda usarlas y verlas.
    private TextInputEditText etNombreAptitud, etFechaBod, etNumBod;

    // Declaro ExpedienteHelper que será el que se comunica con la base de datos SQLite
    private ExpedienteHelper dbHelper;

    // Variables internas
    private int idUsuarioActual; // La utilizo para saber el ID del usuario que está utilizando la app
    private int idAptitudSeleccionada = -1; // La inicializo a -1. Si hubiera algo seleccionado,
    // El ID sería de 0 en adelante. Así me aseguro de que no hay nada seleccionado aún.

    // Variables que voy a usar para la lista visual
    private AptitudAdaptador adaptador;
    private List<AptitudModelo> listaDatos;

    // ====================================================================
    // 2.Enlazo los botones del XML en el onCreate.
    // ====================================================================
    // Este método es lo primero que se ejecuta
    // cuando el usuario abre esta pantalla en el móvil.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Se conecta con el archivo XML de diseño
        setContentView(R.layout.activity_aptitudes);

        // Llamamos al metodo que está en la clase Utilidades y es genérico para todas las pantallas.
        // Funciona cuando el usuario hace clic en la flecha de "Atrás", para volver a la pantalla anterior.
        Utilidades.configurarBotonVolver(this, R.id.btnVolverAptitudes);

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
        // Buscamos los EditText y los botones en la pantalla y los asignamos a las variables
        etNombreAptitud = findViewById(R.id.etNombreAptitud);
        etFechaBod = findViewById(R.id.etFechaBodAptitud);
        etNumBod = findViewById(R.id.etNumBodAptitud);

        Button btnAnadir = findViewById(R.id.btnAnadirAptitud);
        Button btnModificar = findViewById(R.id.btnModificarAptitud);
        Button btnEliminar = findViewById(R.id.btnEliminarAptitud);
        Button btnLimpiar = findViewById(R.id.btnLimpiarAptitud);
        RecyclerView rvAptitudes = findViewById(R.id.rvAptitudes);

        // Activamos el método que está en la clase Utilidades para que al tocar la fecha salga el calendario desplegable,
        // ya que no le dejamos al usuario meter la fecha si no es desplegando el calendario.
        Utilidades.configurarCalendario(this, etFechaBod);

        // ====================================================================
        // 4. PREPARAMOS LA LISTA (RecyclerView)
        // ====================================================================
        // Le decimos a la lista que se dibuje de arriba a abajo con el LinearLayoutManager
        rvAptitudes.setLayoutManager(new LinearLayoutManager(this));
        listaDatos = new ArrayList<>(); // Creamos la lista vacía en memoria

        // Conectamos el Adaptador que está en Kotlin (AptitudAdaptador.kt)
        adaptador = new AptitudAdaptador(listaDatos, aptitud -> {
            // CUANDO EL USUARIO TOCA UNA FILA DE LA LISTA:
            // 1. Guardamos el ID de esa aptitud para saber cuál modificar/borrar
            idAptitudSeleccionada = aptitud.getIdAptitud();
            // 2. Rellenamos las cajas de texto de arriba con los datos de la fila que tocó
            etNombreAptitud.setText(aptitud.getNombre());
            etFechaBod.setText(aptitud.getFechaBod());
            // Si el boletín es 0 (no tiene), lo dejamos en blanco
            etNumBod.setText(aptitud.getNumBod().equals("0") ? "" : aptitud.getNumBod());
            // Mostramos un mensaje para indicarle que lo puede modificar
            Toast.makeText(Aptitudes.this, "Seleccionado para editar", Toast.LENGTH_SHORT).show();
            return kotlin.Unit.INSTANCE; // Requisito de Kotlin para terminar el clic
        });

        // Enganchamos el adaptador terminado a la lista visual
        rvAptitudes.setAdapter(adaptador);

        // Llamamos a la base de datos para que traiga los datos y los pinte, mediante el método
        // cargarLista() cuyo código está al final
        cargarLista();


        // ====================================================================
        // 5. PROGRAMAR LOS BOTONES (CRUD)
        // ====================================================================

        // Botón LIMPIAR: Llama a la función limpiarFormulario() que está al final y que vacía los textos
        btnLimpiar.setOnClickListener(v -> limpiarFormulario());

        // Botón AÑADIR (Guardar nuevo)
        btnAnadir.setOnClickListener(v -> {
            // Recogemos lo que ha escrito el usuario. "trim()" borra los espacios en blanco sobrantes.
            String nombre = etNombreAptitud.getText() != null ? etNombreAptitud.getText().toString().trim() : "";
            String fecha = etFechaBod.getText() != null ? etFechaBod.getText().toString().trim() : "";
            String numBod = etNumBod.getText() != null ? etNumBod.getText().toString().trim() : "";

            // No le dejamos guardar si no ha puesto el nombre
            if (nombre.isEmpty()) {
                Toast.makeText(this, "El nombre de la aptitud es obligatorio", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lo guardamos en la Base de Datos, mediante el método anadirAptitud que está en ExpedienteHelper
            if (dbHelper.anadirAptitud(idUsuarioActual, nombre, fecha, numBod)) {
                Toast.makeText(this, "Guardado con éxito", Toast.LENGTH_SHORT).show();
                limpiarFormulario(); // Vaciamos los EditText
                cargarLista();       // Refrescamos la lista para que aparezca el nuevo
            } else {
                Toast.makeText(this, "Error: Esa aptitud ya está registrada", Toast.LENGTH_LONG).show();
            }
        });

        // Botón MODIFICAR
        btnModificar.setOnClickListener(v -> {
            // Si el ID es -1, significa que no ha tocado ninguna fila de la lista para editar
            if (idAptitudSeleccionada == -1) {
                Toast.makeText(this, "Selecciona una aptitud de la lista", Toast.LENGTH_SHORT).show();
                return;
            }

            String nombre = etNombreAptitud.getText() != null ? etNombreAptitud.getText().toString().trim() : "";
            String fecha = etFechaBod.getText() != null ? etFechaBod.getText().toString().trim() : "";
            String numBod = etNumBod.getText() != null ? etNumBod.getText().toString().trim() : "";

            if (nombre.isEmpty()) {
                Toast.makeText(this, "El nombre de la aptitud es obligatorio", Toast.LENGTH_SHORT).show();
                return;
            }

            // Actualizamos la base de datos buscando por el ID que seleccionó
            if (dbHelper.modificarAptitud(idAptitudSeleccionada, nombre, fecha, numBod)) {
                Toast.makeText(this, "Actualizado correctamente", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarLista();
            }
        });

        // Botón ELIMINAR
        btnEliminar.setOnClickListener(v -> {
            if (idAptitudSeleccionada == -1) {
                Toast.makeText(this, "Selecciona una aptitud de la lista", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.eliminarAptitud(idAptitudSeleccionada)) {
                Toast.makeText(this, "Borrado correctamente", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarLista();
            }
        });
    }

    // ====================================================================
    // 6. MÉTODOS AUXILIARES
    // ====================================================================

    // Método para vaciar las cajas de texto y reiniciar la selección
    private void limpiarFormulario() {
        etNombreAptitud.setText("");
        etFechaBod.setText("");
        etNumBod.setText("");
        idAptitudSeleccionada = -1; // Vuelve al estado inicial
    }

    // Función que lee de SQLite y llena el RecyclerView
    @android.annotation.SuppressLint("NotifyDataSetChanged") // Lo puse para quitar un
    // warning del entorno de desarrollo porque necesito hacer un repintado total de la vista en cargarLista()
    private void cargarLista() {
        listaDatos.clear(); // Borramos la lista actual para no duplicar datos

        // Usamos un Cursor que va señalando fila por fila en la base de datos
        Cursor cursor = dbHelper.obtenerAptitudes(idUsuarioActual);

        // Si el Cursor ha encontrado al menos un resultado (moveToFirst)...
        if (cursor.moveToFirst()) {
            do {
                // Extraemos las columnas de la tabla de la base de datos
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("Id_M_Apti"));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("Nom_Aptitud"));
                String fecha = cursor.getString(cursor.getColumnIndexOrThrow("M_Apti_Fecha_Bod"));
                int nBod = cursor.getInt(cursor.getColumnIndexOrThrow("M_Apti_Nbod"));

                if (fecha == null) fecha = ""; // Si no existe fecha lo dejamos en blanco

                // Instanciamos la data class que teníamos creada en AptitudAdaptador en Kotlin
                // y la metemos en nuestra lista de Java
                listaDatos.add(new AptitudModelo(id, nombre, fecha, String.valueOf(nBod)));

            } while (cursor.moveToNext()); //  ... Y repetimos hasta que no haya más filas
        }
        cursor.close(); // Cerramos el Cursor

        // Le indicamos al adaptador que ha habido cambios y que vuelva a pintar la lista
        adaptador.notifyDataSetChanged();
    }
}