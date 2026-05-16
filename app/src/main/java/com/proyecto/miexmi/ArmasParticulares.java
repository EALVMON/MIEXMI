package com.proyecto.miexmi;

import android.database.Cursor;
import android.os.Bundle;
import android.text.InputFilter;
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
// 5. Hago un método para leer la base de datos (cargarLista()).
// ====================================================================
// Al heredar de 'AppCompatActivity', le indico a Android que esta clase es una pantalla visual y me permite usar todos sus métodos nativos.
public class ArmasParticulares extends AppCompatActivity {

    // Declaramos las variables "globales" para que cualquier
    // botón o función dentro de esta pantalla pueda usarlas y verlas.
    private TextInputEditText etNombre, etNumSerie, etFecha;

    // Declaro ExpedienteHelper que será el que se comunica con la base de datos SQLite
    private ExpedienteHelper dbHelper;

    // Variables internas
    private int idUsuarioActual; // La utilizo para saber el ID del usuario que está utilizando la app
    private int idSeleccionado = -1; // La inicializo a -1. Si hubiera algo seleccionado,
    // el ID sería de 0 en adelante. Así me aseguro de que no hay nada seleccionado aún.

    // Variables que voy a usar para la lista visual
    private ArmaAdaptador adaptador;
    private List<ArmaModelo> listaDatos;

    // ====================================================================
    // 2. EL MOTOR DE ARRANQUE: onCreate()
    // ====================================================================
    // Este método es lo primerísimo que se ejecuta
    // cuando el usuario abre esta pantalla en el móvil.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Se conecta con el archivo XML de diseño
        setContentView(R.layout.activity_armas_particulares);

        // Llamamos al método que está en la clase Utilidades y es genérico para todas las pantallas.
        // Funciona cuando el usuario hace clic en la flecha de "Atrás", para volver a la pantalla anterior.
        Utilidades.configurarBotonVolver(this, R.id.btnVolverArmas);

        // Nos conectamos a la base de datos y preguntamos quién es el usuario, mediante otro método
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
        etNombre = findViewById(R.id.etNombreArma);
        etNumSerie = findViewById(R.id.etNumeroSerieArma);
        etFecha = findViewById(R.id.etFechaCaducidadArma);

        // Forzamos mayúsculas para el número de serie (Filtro nativo de Android)
        etNumSerie.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        Button btnAnadir = findViewById(R.id.btnAnadirArma);
        Button btnModificar = findViewById(R.id.btnModificarArma);
        Button btnEliminar = findViewById(R.id.btnEliminarArma);
        Button btnLimpiar = findViewById(R.id.btnLimpiarArma);
        RecyclerView rvArmas = findViewById(R.id.rvArmas);

        // Activamos el método que está en la clase Utilidades para que al tocar la fecha salga el calendario desplegable,
        // ya que no le dejamos al usuario meter la fecha si no es desplegando el calendario.
        Utilidades.configurarCalendario(this, etFecha);

        // ====================================================================
        // 4. PREPARAMOS LA LISTA (RecyclerView)
        // ====================================================================
        // Le decimos a la lista que se dibuje de arriba a abajo con el LinearLayoutManager
        rvArmas.setLayoutManager(new LinearLayoutManager(this));
        listaDatos = new ArrayList<>(); // Creamos la lista vacía en memoria

        // Conectamos el Adaptador que está en Kotlin (ArmaAdaptador.kt)
        adaptador = new ArmaAdaptador(listaDatos, arma -> {
            // CUANDO EL USUARIO TOCA UNA FILA DE LA LISTA:
            // 1. Guardamos el ID de esa arma para saber cuál modificar/borrar
            idSeleccionado = arma.getId();
            // 2. Rellenamos las cajas de texto de arriba con los datos de la fila que tocó
            etNombre.setText(arma.getNombre());
            etNumSerie.setText(arma.getNumSerie());
            etFecha.setText(arma.getFecha());
            // Mostramos un mensaje para indicarle que lo puede modificar
            Toast.makeText(ArmasParticulares.this, "Seleccionada para editar", Toast.LENGTH_SHORT).show();
            return kotlin.Unit.INSTANCE; // Requisito de Kotlin para terminar el clic
        });

        // Enganchamos el adaptador terminado a la lista visual
        rvArmas.setAdapter(adaptador);

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
            String nom = etNombre.getText() != null ? etNombre.getText().toString().trim() : "";
            String serie = etNumSerie.getText() != null ? etNumSerie.getText().toString().trim().toUpperCase() : "";
            String fec = etFecha.getText() != null ? etFecha.getText().toString().trim() : "";

            // No le dejamos guardar si no ha puesto el nombre o el número de serie
            if (nom.isEmpty() || serie.isEmpty()) {
                Toast.makeText(this, "El nombre y el número de serie son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lo guardamos en la Base de Datos, mediante el método anadirArma que está en ExpedienteHelper
            if (dbHelper.anadirArma(idUsuarioActual, nom, serie, fec)) {
                Toast.makeText(this, "Guardada con éxito", Toast.LENGTH_SHORT).show();
                limpiarFormulario(); // Vaciamos los EditText
                cargarLista();       // Refrescamos la lista para que aparezca la nueva
            } else {
                Toast.makeText(this, "Error: El número de serie ya está registrado", Toast.LENGTH_LONG).show();
            }
        });

        // Botón MODIFICAR
        btnModificar.setOnClickListener(v -> {
            // Si el ID es -1, significa que no ha tocado ninguna fila de la lista para editar
            if (idSeleccionado == -1) {
                Toast.makeText(this, "Selecciona un arma de la lista", Toast.LENGTH_SHORT).show();
                return;
            }

            String nom = etNombre.getText() != null ? etNombre.getText().toString().trim() : "";
            String serie = etNumSerie.getText() != null ? etNumSerie.getText().toString().trim().toUpperCase() : "";
            String fec = etFecha.getText() != null ? etFecha.getText().toString().trim() : "";

            if (nom.isEmpty() || serie.isEmpty()) {
                Toast.makeText(this, "El nombre y el número de serie son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            // Actualizamos la base de datos buscando por el ID que seleccionó
            if (dbHelper.modificarArma(idSeleccionado, nom, serie, fec)) {
                Toast.makeText(this, "Actualizada correctamente", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarLista();
            }
        });

        // Botón ELIMINAR
        btnEliminar.setOnClickListener(v -> {
            if (idSeleccionado == -1) {
                Toast.makeText(this, "Selecciona un arma de la lista", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.eliminarArma(idSeleccionado)) {
                Toast.makeText(this, "Borrada correctamente", Toast.LENGTH_SHORT).show();
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
        etNombre.setText("");
        etNumSerie.setText("");
        etFecha.setText("");
        idSeleccionado = -1; // Vuelve al estado inicial
    }

    // Función que lee de SQLite y llena el RecyclerView
    @android.annotation.SuppressLint("NotifyDataSetChanged") // Lo puse para quitar un
    // warning del entorno de desarrollo porque necesito hacer un repintado total de la vista en cargarLista()
    private void cargarLista() {
        listaDatos.clear(); // Borramos la lista actual para no duplicar datos

        // Usamos un Cursor que va señalando fila por fila en la base de datos
        Cursor c = dbHelper.obtenerArmas(idUsuarioActual);

        // Si el Cursor ha encontrado al menos un resultado (moveToFirst)...
        if (c.moveToFirst()) {
            do {
                // Instanciamos la data class que teníamos creada en ArmaAdaptador en Kotlin
                // y la metemos en nuestra lista de Java
                listaDatos.add(new ArmaModelo(
                        c.getInt(c.getColumnIndexOrThrow("Id_M_EArm")),
                        c.getString(c.getColumnIndexOrThrow("Nom_Arma")),
                        c.getString(c.getColumnIndexOrThrow("M_EArm_Nserie")),
                        c.getString(c.getColumnIndexOrThrow("M_EArm_Fecha_Cad"))
                ));
            } while (c.moveToNext()); // ...y repetimos hasta que no haya más filas
        }
        c.close(); // Cerramos el Cursor

        // Le indicamos al adaptador que ha habido cambios y que vuelva a pintar la lista
        adaptador.notifyDataSetChanged();
    }
}