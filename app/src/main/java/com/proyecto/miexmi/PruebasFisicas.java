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
public class PruebasFisicas extends Temporizador {

    // Declaramos las variables "globales" para que cualquier
    // botón o función dentro de esta pantalla pueda usarlas y verlas.
    private TextInputEditText etPuntuacion, etFecha;
    private AutoCompleteTextView etApto;

    // Declaro ExpedienteHelper que será el que se comunica con la base de datos SQLite
    private ExpedienteHelper dbHelper;

    // Variables internas
    private int idUsuarioActual; // La utilizo para saber el ID del usuario que está utilizando la app
    private int idSeleccionado = -1; // La inicializo a -1. Si hubiera algo seleccionado,
    // El ID sería de 0 en adelante. Así me aseguro de que no hay nada seleccionado aún.

    // Variables que voy a usar para la lista visual
    private TcgfAdaptador adaptador;
    private List<TcgfModelo> listaDatos;

    // ====================================================================
    // 2.Enlazo los botones del XML en el onCreate.
    // ====================================================================
    // Este método es lo primero que se ejecuta
    // cuando el usuario abre esta pantalla en el móvil.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Se conecta con el archivo XML de diseño
        setContentView(R.layout.activity_tcgf);

        // Llamamos al metodo que está en la clase Utilidades y es genérico para todas las pantallas.
        // Funciona cuando el usuario hace clic en la flecha de "Atrás", para volver a la pantalla anterior.
        Utilidades.configurarBotonVolver(this, R.id.btnVolverTcgf);

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
        etPuntuacion = findViewById(R.id.etPuntuacionTcgf);
        etFecha = findViewById(R.id.etFechaTcgf);
        etApto = findViewById(R.id.etAptoTcgf);

        Button btnAnadir = findViewById(R.id.btnAnadirTcgf);
        Button btnModificar = findViewById(R.id.btnModificarTcgf);
        Button btnEliminar = findViewById(R.id.btnEliminarTcgf);
        Button btnLimpiar = findViewById(R.id.btnLimpiarTcgf);
        RecyclerView rvTcgf = findViewById(R.id.rvTcgf);

        // utilizamos los métodos que están en la clase Utilidades.
        // Uno para que al tocar la fecha salga el calendario y otro para el desplegable de aptitud (Apto, No Apto...)
        Utilidades.configurarCalendario(this, etFecha);
        Utilidades.configurarDesplegableAptoTcgf(this, etApto);

        // ====================================================================
        // 4. PREPARAMOS LA LISTA (RecyclerView)
        // ====================================================================
        // Le decimos a la lista que se dibuje de arriba a abajo con el LinearLayoutManager
        rvTcgf.setLayoutManager(new LinearLayoutManager(this));
        listaDatos = new ArrayList<>(); // Creamos la lista vacía en memoria

        // Conectamos el Adaptador que está en Kotlin (TcgfAdaptador.kt)
        adaptador = new TcgfAdaptador(listaDatos, item -> {
            // CUANDO EL USUARIO TOCA UNA FILA DE LA LISTA:
            // 1. Guardamos el ID de esa prueba para saber cuál modificar/borrar
            idSeleccionado = item.getId();
            // 2. Rellenamos las cajas de texto de arriba con los datos de la fila que tocó
            etFecha.setText(item.getFecha());
            etPuntuacion.setText(item.getPuntuacion());
            // Usamos 'false' en etApto para que el menú desplegable no se abra automáticamente
            etApto.setText(item.getApto(), false);

            // Mostramos un mensaje para indicarle que lo puede modificar
            Toast.makeText(PruebasFisicas.this, "Seleccionado para editar", Toast.LENGTH_SHORT).show();
            return kotlin.Unit.INSTANCE; // Requisito de Kotlin para terminar el clic
        });

        // Enganchamos el adaptador terminado a la lista visual
        rvTcgf.setAdapter(adaptador);

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
            String fec = etFecha.getText() != null ? etFecha.getText().toString().trim() : "";
            String pun = etPuntuacion.getText() != null ? etPuntuacion.getText().toString().trim() : "";
            String apto = etApto.getText().toString().trim();

            // No le dejamos guardar si faltan campos obligatorios
            if (fec.isEmpty() || apto.isEmpty()) {
                Toast.makeText(PruebasFisicas.this, "La fecha y el resultado son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lo guardamos en la Base de Datos, mediante el método anadirTcgf que está en ExpedienteHelper
            if (dbHelper.anadirTcgf(idUsuarioActual, fec, pun, apto)) {
                Toast.makeText(PruebasFisicas.this, "Guardado con éxito", Toast.LENGTH_SHORT).show();
                limpiarFormulario(); // Vaciamos los EditText
                cargarLista();       // Refrescamos la lista para que aparezca el nuevo
            } else {
                Toast.makeText(PruebasFisicas.this, "Ya hay unas pruebas físicas registradas en esa fecha", Toast.LENGTH_LONG).show();
            }
        });

       
        // Botón MODIFICAR
        btnModificar.setOnClickListener(v -> {
            // Si el ID es -1, significa que no ha tocado ninguna fila de la lista para editar
            if (idSeleccionado == -1) {
                Toast.makeText(PruebasFisicas.this, "Selecciona una prueba física de la lista", Toast.LENGTH_SHORT).show();
                return;
            }

            String fec = etFecha.getText() != null ? etFecha.getText().toString().trim() : "";
            String pun = etPuntuacion.getText() != null ? etPuntuacion.getText().toString().trim() : "";
            String apto = etApto.getText().toString().trim();

            if (fec.isEmpty() || apto.isEmpty()) {
                Toast.makeText(PruebasFisicas.this, "La fecha y el resultado son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            // Actualizamos la base de datos buscando por el ID que seleccionó
            if (dbHelper.modificarTcgf(idSeleccionado, fec, pun, apto)) {
                Toast.makeText(PruebasFisicas.this, "Actualizado correctamente", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarLista();
            } else {
                // Aviso en caso de que la validación en SQLite detecte una prueba en la misma fecha
                Toast.makeText(PruebasFisicas.this, "Error: Ya existe una prueba física registrada en esa fecha", Toast.LENGTH_LONG).show();
            }
        });
        // Botón ELIMINAR
        btnEliminar.setOnClickListener(v -> {
            if (idSeleccionado == -1) {
                Toast.makeText(PruebasFisicas.this, "Selecciona una prueba física de la lista", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.eliminarTcgf(idSeleccionado)) {
                Toast.makeText(PruebasFisicas.this, "Borrado correctamente", Toast.LENGTH_SHORT).show();
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
        etFecha.setText("");
        etPuntuacion.setText("");
        etApto.setText("", false); // El false evita que se despliegue el menú al borrar
        idSeleccionado = -1; // Vuelve al estado inicial
    }

    // Función que lee de SQLite y llena el RecyclerView
    @android.annotation.SuppressLint("NotifyDataSetChanged") // Lo puse para quitar un
    // warning del entorno de desarrollo porque necesito hacer un repintado total de la vista en cargarLista()
    private void cargarLista() {
        listaDatos.clear(); // Borramos la lista actual para no duplicar datos

        // Usamos un Cursor que va señalando fila por fila en la base de datos
        Cursor c = dbHelper.obtenerTcgf(idUsuarioActual);

        // Si el Cursor ha encontrado al menos un resultado (moveToFirst)...
        if (c.moveToFirst()) {
            do {
                // Extraemos las columnas de la tabla de la base de datos
                int id = c.getInt(c.getColumnIndexOrThrow("Id_M_TCGF"));
                String fecha = c.getString(c.getColumnIndexOrThrow("M_Tcgf_Fecha"));
                String puntuacion = c.getString(c.getColumnIndexOrThrow("M_Tcgf_Puntuacion"));
                String apto = c.getString(c.getColumnIndexOrThrow("M_Tcgf_Apto"));

                // Instanciamos la data class que teníamos creada en TcgfAdaptador en Kotlin
                // y la metemos en nuestra lista de Java
                listaDatos.add(new TcgfModelo(id, fecha, puntuacion, apto));

            } while (c.moveToNext()); // ... Y repetimos hasta que no haya más filas
        }
        c.close(); // Cerramos el Cursor

        // Le indicamos al adaptador que ha habido cambios y que vuelva a pintar la lista
        adaptador.notifyDataSetChanged();
    }
}