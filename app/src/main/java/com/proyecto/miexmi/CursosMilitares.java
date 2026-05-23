package com.proyecto.miexmi;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

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
// Hereda de la clase Temporizador para poder controlar el tiempo de 3 minutos en todos los On...
public class CursosMilitares extends Temporizador {

    // Declaramos las variables "globales" para que cualquier
    // botón o función dentro de esta pantalla pueda usarlas y verlas.
    private AutoCompleteTextView etNombre;
    private TextInputEditText etFecha, etBod;

    // Declaro ExpedienteHelper que será el que se comunica con la base de datos SQLite
    private ExpedienteHelper dbHelper;

    // Variables internas
    private int idUsuarioActual; // La utilizo para saber el ID del usuario que está utilizando la app
    private int idCursoSeleccionado = -1; // La inicializo a -1. Si hubiera algo seleccionado,
    // El ID sería de 0 en adelante. Así me aseguro de que no hay nada seleccionado aún.

    // Variables que voy a usar para la lista visual
    private CursoMilitarAdaptador adaptador;
    private List<CursoModelo> listaDatos;

    // ====================================================================
    // 2.Enlazo los botones del XML en el onCreate.
    // ====================================================================
    // Este método es lo primero que se ejecuta
    // cuando el usuario abre esta pantalla en el móvil.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Se conecta con el archivo XML de diseño
        setContentView(R.layout.activity_cursos_militares);

        // Llamamos al metodo que está en la clase Utilidades y es genérico para todas las pantallas.
        // Funciona cuando el usuario hace clic en la flecha de "Atrás", para volver a la pantalla anterior.
        Utilidades.configurarBotonVolver(this, R.id.btnVolverCursos);

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
        etNombre = findViewById(R.id.etNombreCurso);
        etFecha = findViewById(R.id.etFechaBodCurso);
        etBod = findViewById(R.id.etNumBodCurso);

        Button btnAnadir = findViewById(R.id.btnAnadirCurso);
        Button btnModificar = findViewById(R.id.btnModificarCurso);
        Button btnEliminar = findViewById(R.id.btnEliminarCurso);
        Button btnLimpiar = findViewById(R.id.btnLimpiarCurso);
        RecyclerView rvCursos = findViewById(R.id.rvCursos);

        // Activamos los métodos que están en la clase Utilidades.
        // Uno para el desplegable de cursos y otro para que al tocar la fecha salga el calendario.
        Utilidades.configurarCalendario(this, etFecha);
        Utilidades.configurarDesplegableCursosMilitares(this, etNombre);

        // ====================================================================
        // 4. PREPARAMOS LA LISTA (RecyclerView)
        // ====================================================================
        // Le decimos a la lista que se dibuje de arriba a abajo con el LinearLayoutManager
        rvCursos.setLayoutManager(new LinearLayoutManager(this));
        listaDatos = new ArrayList<>(); // Creamos la lista vacía en memoria

        // Conectamos el Adaptador que está en Kotlin (CursoMilitarAdaptador.kt)
        adaptador = new CursoMilitarAdaptador(listaDatos, curso -> {
            // CUANDO EL USUARIO TOCA UNA FILA DE LA LISTA:
            // 1. Guardamos el ID de ese curso para saber cuál modificar/borrar
            idCursoSeleccionado = curso.getId();
            // 2. Rellenamos las cajas de texto de arriba con los datos de la fila que tocó
            // Usamos 'false' en etNombre para que el menú desplegable no se abra automáticamente
            etNombre.setText(curso.getNombre(), false);
            etFecha.setText(curso.getFecha());
            // Si el boletín es 0 (no tiene), lo dejamos en blanco
            etBod.setText(curso.getNbod().equals("0") ? "" : curso.getNbod());
            // Mostramos un mensaje para indicarle que lo puede modificar
            Toast.makeText(CursosMilitares.this, "Seleccionado para editar", Toast.LENGTH_SHORT).show();
            return kotlin.Unit.INSTANCE; // Requisito de Kotlin para terminar el clic
        });

        // Enganchamos el adaptador terminado a la lista visual
        rvCursos.setAdapter(adaptador);

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
            String fec = etFecha.getText() != null ? etFecha.getText().toString().trim() : "";
            String bod = etBod.getText() != null ? etBod.getText().toString().trim() : "";

            // No le dejamos guardar si no ha puesto el nombre del curso
            if (nom.isEmpty()) {
                Toast.makeText(CursosMilitares.this, "El nombre del curso es obligatorio", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lo guardamos en la Base de Datos, mediante el método anadirCursoMilitar
            if (dbHelper.anadirCursoMilitar(idUsuarioActual, nom, fec, bod)) {
                Toast.makeText(CursosMilitares.this, "Guardado con éxito", Toast.LENGTH_SHORT).show();
                limpiarFormulario(); // Vaciamos los EditText
                cargarLista();       // Refrescamos la lista para que aparezca el nuevo
            } else {

                Toast.makeText(CursosMilitares.this, "Error: Ese curso ya está registrado", Toast.LENGTH_LONG).show();
            }
        });

        // Botón MODIFICAR
        btnModificar.setOnClickListener(v -> {
            // Si el ID es -1, significa que no hay ninguna fila de la lista para editar
            if (idCursoSeleccionado == -1) {
                Toast.makeText(CursosMilitares.this, "Selecciona un curso de la lista", Toast.LENGTH_SHORT).show();
                return;
            }

            String nom = etNombre.getText().toString().trim();
            String fec = etFecha.getText() != null ? etFecha.getText().toString().trim() : "";
            String bod = etBod.getText() != null ? etBod.getText().toString().trim() : "";

            if (nom.isEmpty()) {
                Toast.makeText(CursosMilitares.this, "El nombre del curso es obligatorio", Toast.LENGTH_SHORT).show();
                return;
            }

            // Actualizamos la base de datos buscando por el ID que seleccionó
            if (dbHelper.modificarCursoMilitar(idCursoSeleccionado, nom, fec, bod)) {
                Toast.makeText(CursosMilitares.this, "Actualizado correctamente", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarLista();
            } else {

                Toast.makeText(CursosMilitares.this, "Error: Ese curso ya está registrado", Toast.LENGTH_LONG).show();
            }
        });

        // Botón ELIMINAR
        btnEliminar.setOnClickListener(v -> {
            if (idCursoSeleccionado == -1) {
                Toast.makeText(this, "Selecciona un curso de la lista", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.eliminarCursoMilitar(idCursoSeleccionado)) {
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
        etNombre.setText("", false); // El false evita que se despliegue el menú al borrar
        etFecha.setText("");
        etBod.setText("");
        idCursoSeleccionado = -1; // Vuelve al estado inicial
    }

    // Función que lee de SQLite y llena el RecyclerView
    @android.annotation.SuppressLint("NotifyDataSetChanged") // Lo puse para quitar un
    // warning del entorno de desarrollo porque necesito hacer un repintado total de la vista en cargarLista()
    private void cargarLista() {
        listaDatos.clear(); // Borramos la lista actual para no duplicar datos

        // Usamos un Cursor que va señalando fila porন্দে la base de datos
        Cursor c = dbHelper.obtenerCursosMilitares(idUsuarioActual);

        // Si el Cursor ha encontrado al menos un resultado (moveToFirst)...
        if (c.moveToFirst()) {
            do {
                // Extraemos las columnas de la tabla de la base de datos
                int id = c.getInt(c.getColumnIndexOrThrow("Id_M_Cmili"));
                String nombre = c.getString(c.getColumnIndexOrThrow("Nom_Cur_Mili"));
                String fecha = c.getString(c.getColumnIndexOrThrow("M_Cmili_Fecha_Bod"));
                int nBod = c.getInt(c.getColumnIndexOrThrow("M_Cmili_Nbod"));

                if (fecha == null) fecha = ""; // Si no existe fecha lo dejamos en blanco

                // Instanciamos la data class que teníamos creada en CursoMilitarAdaptador en Kotlin
                // y la metemos en nuestra lista de Java
                listaDatos.add(new CursoModelo(id, nombre, fecha, String.valueOf(nBod)));

            } while (c.moveToNext()); //  ... Y repetimos hasta que no haya más filas
        }
        c.close(); // Cerramos el Cursor

        // Le indicamos al adaptador que ha habido cambios y que vuelva a pintar la lista
        adaptador.notifyDataSetChanged();
    }
}