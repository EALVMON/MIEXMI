package com.proyecto.miexmi;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
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
public class IdiomasSLP extends AppCompatActivity {

    // Declaramos las variables "globales" para que cualquier
    // botón o función dentro de esta pantalla pueda usarlas y verlas.
    private AutoCompleteTextView etNombre;
    private TextInputEditText etResultado, etFecha, etBod;

    // Declaro ExpedienteHelper que será el que se comunica con la base de datos SQLite
    private ExpedienteHelper dbHelper;

    // Variables internas
    private int idUsuarioActual; // La utilizo para saber el ID del usuario que está utilizando la app
    private int idIdiomaSeleccionado = -1; // La inicializo a -1. Si hubiera algo seleccionado,
    // El ID sería de 0 en adelante. Así me aseguro de que no hay nada seleccionado aún.

    // Variables que voy a usar para la lista visual
    private IdiomaAdaptador adaptador;
    private List<IdiomaModelo> listaDatos;

    // ====================================================================
    // 2.Enlazo los botones del XML en el onCreate.
    // ====================================================================
    // Este método es lo primero que se ejecuta
    // cuando el usuario abre esta pantalla en el móvil.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Se conecta con el archivo XML de diseño
        setContentView(R.layout.activity_idioma);

        // Llamamos al metodo que está en la clase Utilidades y es genérico para todas las pantallas.
        // Funciona cuando el usuario hace clic en la flecha de "Atrás", para volver a la pantalla anterior.
        Utilidades.configurarBotonVolver(this, R.id.btnVolverIdioma);

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
        etNombre = findViewById(R.id.etNombreIdioma);
        etResultado = findViewById(R.id.etResultadoIdioma);
        etFecha = findViewById(R.id.etFechaBodIdioma);
        etBod = findViewById(R.id.etNumBodIdioma);

        Button btnAnadir = findViewById(R.id.btnAnadirIdioma);
        Button btnModificar = findViewById(R.id.btnModificarIdioma);
        Button btnEliminar = findViewById(R.id.btnEliminarIdioma);
        Button btnLimpiar = findViewById(R.id.btnLimpiarIdioma);
        RecyclerView rvIdioma = findViewById(R.id.rvIdioma);

        // Activamos los métodos que están en la clase Utilidades.
        // Uno para el desplegable de idiomas y otro para que al tocar la fecha salga el calendario.
        Utilidades.configurarCalendario(this, etFecha);
        Utilidades.configurarDesplegableIdiomas(this, etNombre);

        // ====================================================================
        // 4. PREPARAMOS LA LISTA (RecyclerView)
        // ====================================================================
        // Le decimos a la lista que se dibuje de arriba a abajo con el LinearLayoutManager
        rvIdioma.setLayoutManager(new LinearLayoutManager(this));
        listaDatos = new ArrayList<>(); // Creamos la lista vacía en memoria

        // Conectamos el Adaptador que está en Kotlin (IdiomaAdaptador.kt)
        adaptador = new IdiomaAdaptador(listaDatos, idioma -> {
            // CUANDO EL USUARIO TOCA UNA FILA DE LA LISTA:
            // 1. Guardamos el ID de ese idioma para saber cuál modificar/borrar
            idIdiomaSeleccionado = idioma.getId();
            // 2. Rellenamos las cajas de texto de arriba con los datos de la fila que tocó
            // Usamos 'false' en etNombre para que el menú desplegable no se abra automáticamente
            etNombre.setText(idioma.getNombre(), false);
            etResultado.setText(idioma.getResultado());
            etFecha.setText(idioma.getFecha());
            // Si el boletín es 0 (no tiene), lo dejamos en blanco
            etBod.setText(idioma.getNbod().equals("0") ? "" : idioma.getNbod());

            // Mostramos un mensaje para indicarle que lo puede modificar
            Toast.makeText(IdiomasSLP.this, "Seleccionado para editar", Toast.LENGTH_SHORT).show();
            return kotlin.Unit.INSTANCE; // Requisito de Kotlin para terminar el clic
        });

        // Enganchamos el adaptador terminado a la lista visual
        rvIdioma.setAdapter(adaptador);

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
            String nom = etNombre.getText().toString().trim();
            String res = etResultado.getText() != null ? etResultado.getText().toString().trim() : "";
            String fec = etFecha.getText() != null ? etFecha.getText().toString().trim() : "";
            String bod = etBod.getText() != null ? etBod.getText().toString().trim() : "";

            // No le dejamos guardar si faltan campos requeridos
            if (nom.isEmpty() || res.isEmpty()) {
                Toast.makeText(IdiomasSLP.this, "Idioma y SLP son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            // Compruebo formato tipo 3.3.3+.2
            String patronSLP = "^([0-5]\\+?\\.){3}[0-5]\\+?$";
            if (!res.matches(patronSLP)) {
                Toast.makeText(IdiomasSLP.this, "Formato SLP incorrecto. Usa puntos (Ej: 3.3.3+.2)", Toast.LENGTH_LONG).show();
                return;
            }

            // Lo guardamos en la Base de Datos, mediante el metodo anadirIdioma que está en ExpedienteHelper
            if (dbHelper.anadirIdioma(idUsuarioActual, nom, res, fec, bod)) {
                Toast.makeText(IdiomasSLP.this, "Guardado con éxito", Toast.LENGTH_SHORT).show();
                limpiarFormulario(); // Vaciamos los EditText
                cargarLista();       // Refrescamos la lista para que aparezca el nuevo
            } else {
                Toast.makeText(IdiomasSLP.this, "Error: Este idioma ya está guardado", Toast.LENGTH_SHORT).show();
            }
        });

        // Botón MODIFICAR
        btnModificar.setOnClickListener(v -> {
            // Si el ID es -1, significa que no hay ninguna fila de la lista para editar
            if (idIdiomaSeleccionado == -1) {
                Toast.makeText(IdiomasSLP.this, "Selecciona un idioma de la lista", Toast.LENGTH_SHORT).show();
                return;
            }

            String nom = etNombre.getText().toString().trim();
            String res = etResultado.getText() != null ? etResultado.getText().toString().trim() : "";
            String fec = etFecha.getText() != null ? etFecha.getText().toString().trim() : "";
            String bod = etBod.getText() != null ? etBod.getText().toString().trim() : "";

            if (nom.isEmpty() || res.isEmpty()) {
                Toast.makeText(IdiomasSLP.this, "Idioma y SLP son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            // Compruebo formato tipo 3.3.3+.2
            String patronSLP = "^([0-5]\\+?\\.){3}[0-5]\\+?$";
            if (!res.matches(patronSLP)) {
                Toast.makeText(IdiomasSLP.this, "Formato SLP incorrecto. Usa puntos (Ej: 3.3.3+.2)", Toast.LENGTH_LONG).show();
                return;
            }

            // Actualizamos la base de datos buscando por el ID que seleccionó
            if (dbHelper.modificarIdioma(idIdiomaSeleccionado, nom, res, fec, bod)) {
                Toast.makeText(IdiomasSLP.this, "Actualizado correctamente", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarLista();
            } else {
                // Aviso en caso de que la validación en SQLite detecte un duplicado
                Toast.makeText(IdiomasSLP.this, "Error: Ese idioma ya está registrado", Toast.LENGTH_LONG).show();
            }
        });

        // Botón ELIMINAR
        btnEliminar.setOnClickListener(v -> {
            if (idIdiomaSeleccionado == -1) {
                Toast.makeText(IdiomasSLP.this, "Selecciona un idioma de la lista", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.eliminarIdioma(idIdiomaSeleccionado)) {
                Toast.makeText(IdiomasSLP.this, "Borrado correctamente", Toast.LENGTH_SHORT).show();
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
        etNombre.setText("", false); // El false evita que se despliegue el menú al borrar
        etResultado.setText("");
        etFecha.setText("");
        etBod.setText("");
        idIdiomaSeleccionado = -1; // Vuelve al estado inicial
    }

    // Función que lee de SQLite y llena el RecyclerView
    @android.annotation.SuppressLint("NotifyDataSetChanged") // Lo puse para quitar un
    // warning del entorno de desarrollo porque necesito hacer un repintado total de la vista en cargarLista()
    private void cargarLista() {
        listaDatos.clear(); // Borramos la lista actual para no duplicar datos

        // Usamos un Cursor que va señalando fila por fila en la base de datos
        Cursor c = dbHelper.obtenerIdiomas(idUsuarioActual);

        // Si el Cursor ha encontrado al menos un resultado (moveToFirst)...
        if (c.moveToFirst()) {
            do {
                // Extraemos las columnas de la tabla de la base de datos
                int id = c.getInt(c.getColumnIndexOrThrow("Id_M_Idi"));
                String nombre = c.getString(c.getColumnIndexOrThrow("Nom_idioma"));
                String resultado = c.getString(c.getColumnIndexOrThrow("Resultado"));
                String fecha = c.getString(c.getColumnIndexOrThrow("M_Idi_Fecha_Bod"));
                int nBod = c.getInt(c.getColumnIndexOrThrow("M_Idi_Nbod"));

                if (fecha == null) fecha = ""; // Si no existe fecha lo dejamos en blanco

                // Instanciamos la data class que teníamos creada en IdiomaAdaptador en Kotlin
                // y la metemos en nuestra lista de Java
                listaDatos.add(new IdiomaModelo(id, nombre, resultado, fecha, String.valueOf(nBod)));

            } while (c.moveToNext()); //  ... Y repetimos hasta que no haya más filas
        }
        c.close(); // Cerramos el Cursor

        // Le indicamos al adaptador que ha habido cambios y que vuelva a pintar la lista
        adaptador.notifyDataSetChanged();
    }
}