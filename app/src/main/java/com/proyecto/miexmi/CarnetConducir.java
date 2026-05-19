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
public class CarnetConducir extends AppCompatActivity {

    // Declaramos las variables "globales" para que cualquier
    // botón o función dentro de esta pantalla pueda usarlas y verlas.
    private AutoCompleteTextView etTipo;
    private TextInputEditText etFechaConcesion, etFechaCaducidad;

    // Declaro ExpedienteHelper que será el que se comunica con la base de datos SQLite
    private ExpedienteHelper dbHelper;

    // Variables internas
    private int idUsuarioActual; // La utilizo para saber el ID del usuario que está utilizando la app
    private int idCarnetSeleccionado = -1; // La inicializo a -1. Si hubiera algo seleccionado,
    // El ID sería de 0 en adelante. Así me aseguro de que no hay nada seleccionado aún.

    // Variables que voy a usar para la lista visual
    private CarnetAdaptador adaptador;
    private List<CarnetModelo> listaDatos;

    // ====================================================================
    // 2.Enlazo los botones del XML en el onCreate.
    // ====================================================================
    // Este método es lo primero que se ejecuta
    // cuando el usuario abre esta pantalla en el móvil.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Se conecta con el archivo XML de diseño
        setContentView(R.layout.activity_carnet);

        // Llamamos al metodo que está en la clase Utilidades y es genérico para todas las pantallas.
        // Funciona cuando el usuario hace clic en la flecha de "Atrás", para volver a la pantalla anterior.
        Utilidades.configurarBotonVolver(this, R.id.btnVolverCarnet);

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
        etTipo = findViewById(R.id.etTipoCarnet);
        etFechaConcesion = findViewById(R.id.etFechaConcesionCarnet);
        etFechaCaducidad = findViewById(R.id.etFechaCaducidadCarnet);

        Button btnAnadir = findViewById(R.id.btnAnadirCarnet);
        Button btnModificar = findViewById(R.id.btnModificarCarnet);
        Button btnEliminar = findViewById(R.id.btnEliminarCarnet);
        Button btnLimpiar = findViewById(R.id.btnLimpiarCarnet);
        RecyclerView rvCarnet = findViewById(R.id.rvCarnet);

        // Activamos los métodos que están en la clase Utilidades.
        // Uno para el desplegable de tipos de carnet y otros para que al tocar la fecha salga el calendario.
        Utilidades.configurarDesplegableCarnets(this, etTipo);
        Utilidades.configurarCalendario(this, etFechaConcesion);
        Utilidades.configurarCalendario(this, etFechaCaducidad);

        // ====================================================================
        // 4. PREPARAMOS LA LISTA (RecyclerView)
        // ====================================================================
        // Le decimos a la lista que se dibuje de arriba a abajo con el LinearLayoutManager
        rvCarnet.setLayoutManager(new LinearLayoutManager(this));
        listaDatos = new ArrayList<>(); // Creamos la lista vacía en memoria

        // Conectamos el Adaptador que está en Kotlin (CarnetAdaptador.kt)
        adaptador = new CarnetAdaptador(listaDatos, carnet -> {
            // CUANDO EL USUARIO TOCA UNA FILA DE LA LISTA:
            // 1. Guardamos el ID de ese carnet para saber cuál modificar/borrar
            idCarnetSeleccionado = carnet.getId();
            // 2. Rellenamos las cajas de texto de arriba con los datos de la fila que tocó
            // Usamos 'false' en etTipo para que el menú desplegable no se abra automáticamente
            etTipo.setText(carnet.getTipo(), false);
            etFechaConcesion.setText(carnet.getFechaConcesion());
            etFechaCaducidad.setText(carnet.getFechaCaducidad());
            // Mostramos un mensaje para indicarle que lo puede modificar
            Toast.makeText(CarnetConducir.this, "Seleccionado para editar", Toast.LENGTH_SHORT).show();
            return kotlin.Unit.INSTANCE; // Requisito de Kotlin para terminar el clic
        });

        // Enganchamos el adaptador terminado a la lista visual
        rvCarnet.setAdapter(adaptador);

        // Llamamos a la base de datos para que traiga los datos y los pinte, mediante el método
        // cargarLista() cuyo código está al final
        cargarLista();


        // ====================================================================
        // 5. PROGRAMAR LOS BOTONES (CRUD)
        // ====================================================================

        // Botón LIMPIAR: Llama a la función limpiarFormulario() que está al final y que vacía los textos
        btnLimpiar.setOnClickListener(v -> limpiarFormulario());

        // Botón AÑADIR
        btnAnadir.setOnClickListener(v -> {
            // Recogemos lo que ha escrito el usuario. "trim()" borra los espacios en blanco sobrantes.
            String tipo = etTipo.getText().toString().trim();
            String fecCon = etFechaConcesion.getText() != null ? etFechaConcesion.getText().toString().trim() : "";
            String fecCad = etFechaCaducidad.getText() != null ? etFechaCaducidad.getText().toString().trim() : "";

            // No le dejamos guardar si no ha puesto el tipo de carnet
            if (tipo.isEmpty()) {
                Toast.makeText(this, "Selecciona una clase de carnet", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lo guardamos en la Base de Datos, mediante el método anadirCarnet que está en ExpedienteHelper
            if (dbHelper.anadirCarnet(idUsuarioActual, tipo, fecCon, fecCad)) {
                Toast.makeText(this, "Guardado con éxito", Toast.LENGTH_SHORT).show();
                limpiarFormulario(); // Vaciamos los EditText
                cargarLista();       // Refrescamos la lista para que aparezca el nuevo
            } else {
                Toast.makeText(this, "Error: Esta clase de carnet ya está registrada", Toast.LENGTH_LONG).show();
            }
        });

        // Botón MODIFICAR
        btnModificar.setOnClickListener(v -> {
            // Si el ID es -1, significa que no ha tocado ninguna fila de la lista para editar
            if (idCarnetSeleccionado == -1) {
                Toast.makeText(this, "Selecciona un carnet de la lista", Toast.LENGTH_SHORT).show();
                return;
            }

            String tipo = etTipo.getText().toString().trim();
            String fecCon = etFechaConcesion.getText() != null ? etFechaConcesion.getText().toString().trim() : "";
            String fecCad = etFechaCaducidad.getText() != null ? etFechaCaducidad.getText().toString().trim() : "";

            if (tipo.isEmpty()) {
                Toast.makeText(this, "La clase de carnet no puede estar vacía", Toast.LENGTH_SHORT).show();
                return;
            }

            // Actualizamos la base de datos buscando por el ID que seleccionó
            if (dbHelper.modificarCarnet(idCarnetSeleccionado, tipo, fecCon, fecCad)) {
                Toast.makeText(this, "Actualizado correctamente", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarLista();
            } else {
                // Aviso en caso de que la validación en SQLite detecte un carnet duplicado
                Toast.makeText(this, "Error: Esta clase de carnet ya está registrada", Toast.LENGTH_LONG).show();
            }
        });

        // Botón ELIMINAR
        btnEliminar.setOnClickListener(v -> {
            if (idCarnetSeleccionado == -1) {
                Toast.makeText(this, "Selecciona un carnet de la lista", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.eliminarCarnet(idCarnetSeleccionado)) {
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
        etTipo.setText("", false); // El false evita que se despliegue el menú de opciones al borrar
        etFechaConcesion.setText("");
        etFechaCaducidad.setText("");
        idCarnetSeleccionado = -1; // Vuelve al estado inicial
    }

    // Función que lee de SQLite y llena el RecyclerView
    @android.annotation.SuppressLint("NotifyDataSetChanged") // Lo puse para quitar un
    // warning del entorno de desarrollo porque necesito hacer un repintado total de la vista en cargarLista()
    private void cargarLista() {
        listaDatos.clear(); // Borramos la lista actual para no duplicar datos

        // Usamos un Cursor que va señalando fila por fila en la base de datos
        Cursor cursor = dbHelper.obtenerCarnets(idUsuarioActual);

        // Si el Cursor ha encontrado al menos un resultado (moveToFirst)...
        if (cursor.moveToFirst()) {
            do {
                // Extraemos las columnas de la tabla de la base de datos
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("Id_M_Carnet"));
                String tipo = cursor.getString(cursor.getColumnIndexOrThrow("Tipo_Carnet"));
                String fecCon = cursor.getString(cursor.getColumnIndexOrThrow("M_Carn_Fecha_Concesion"));
                String fecCad = cursor.getString(cursor.getColumnIndexOrThrow("M_Carn_Fecha_Caducidad"));

                if (fecCon == null) fecCon = ""; // Si no existe fecha lo dejamos en blanco
                if (fecCad == null) fecCad = "";

                // Instanciamos la data class que teníamos creada en CarnetAdaptador en Kotlin
                // y la metemos en nuestra lista de Java
                listaDatos.add(new CarnetModelo(id, tipo, fecCon, fecCad));

            } while (cursor.moveToNext()); //  ... Y repetimos hasta que no haya más filas
        }
        cursor.close(); // Cerramos el Cursor

        // Le indicamos al adaptador que ha habido cambios y que vuelva a pintar la lista
        adaptador.notifyDataSetChanged();
    }
}