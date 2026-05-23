package com.proyecto.miexmi;

import android.annotation.SuppressLint;
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
// Hereda de la clase Temporizador para poder controlar el tiempo de 3 minutos en todos los On...
public class TarjetaMilitar extends Temporizador {

    // Declaramos las variables "globales" para que cualquier
    // botón o función dentro de esta pantalla pueda usarlas y verlas.
    private TextInputEditText etNumeroTmi, etFechaCaducidad;

    // Declaro ExpedienteHelper que será el que se comunica con la base de datos SQLite
    private ExpedienteHelper dbHelper;

    // Variables internas
    private int idUsuarioActual; // La utilizo para saber el ID del usuario que está utilizando la app
    private int idTmiSeleccionada = -1; // La inicializo a -1. Si hubiera algo seleccionado,
    // El ID sería de 0 en adelante. Así me aseguro de que no hay nada seleccionado aún.

    // Variables que voy a usar para la lista visual
    private TmiAdaptador adaptador;
    private List<TmiModelo> listaDatos;

    // ====================================================================
    // 2.Enlazo los botones del XML en el onCreate.
    // ====================================================================
    // Este método es lo primero que se ejecuta
    // cuando el usuario abre esta pantalla en el móvil.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Se conecta con el archivo XML de diseño
        setContentView(R.layout.activity_tmi);

        // Llamamos al metodo que está en la clase Utilidades y es genérico para todas las pantallas.
        // Funciona cuando el usuario hace clic en la flecha de "Atrás", para volver a la pantalla anterior.
        Utilidades.configurarBotonVolver(this, R.id.btnVolverTmi);

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
        etNumeroTmi = findViewById(R.id.etNumeroTmi);
        etFechaCaducidad = findViewById(R.id.etFechaCaducidadTmi);

        Button btnAnadir = findViewById(R.id.btnAnadirTmi);
        Button btnModificar = findViewById(R.id.btnModificarTmi);
        Button btnEliminar = findViewById(R.id.btnEliminarTmi);
        Button btnLimpiar = findViewById(R.id.btnLimpiarTmi);
        RecyclerView rvTmi = findViewById(R.id.rvTmi);

        // Activamos el método que está en la clase Utilidades para que al tocar la fecha salga el calendario desplegable,
        // ya que no le dejamos al usuario meter la fecha si no es desplegando el calendario.
        Utilidades.configurarCalendario(this, etFechaCaducidad);

        // ====================================================================
        // 4. PREPARAMOS LA LISTA (RecyclerView)
        // ====================================================================
        // Le decimos a la lista que se dibuje de arriba a abajo con el LinearLayoutManager
        rvTmi.setLayoutManager(new LinearLayoutManager(this));
        listaDatos = new ArrayList<>(); // Creamos la lista vacía en memoria

        // Conectamos el Adaptador que está en Kotlin (TmiAdaptador.kt)
        adaptador = new TmiAdaptador(listaDatos, tmi -> {
            // CUANDO EL USUARIO TOCA UNA FILA DE LA LISTA:
            // 1. Guardamos el ID de esa tarjeta para saber cuál modificar/borrar
            idTmiSeleccionada = tmi.getIdTmi();
            // 2. Rellenamos las cajas de texto de arriba con los datos de la fila que tocó
            etNumeroTmi.setText(tmi.getNumeroTarjeta());
            etFechaCaducidad.setText(tmi.getFechaCaducidad());

            // Mostramos un mensaje para indicarle que lo puede modificar
            Toast.makeText(TarjetaMilitar.this, "Tarjeta seleccionada", Toast.LENGTH_SHORT).show();
            return kotlin.Unit.INSTANCE; // Requisito de Kotlin para terminar el clic
        });

        // Enganchamos el adaptador terminado a la lista visual
        rvTmi.setAdapter(adaptador);

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
            // Recogemos los datos y forzamos MAYÚSCULAS para el número de tarjeta
            String numTarjeta = etNumeroTmi.getText() != null ? etNumeroTmi.getText().toString().trim().toUpperCase() : "";
            String fecha = etFechaCaducidad.getText() != null ? etFechaCaducidad.getText().toString().trim() : "";

            if (numTarjeta.isEmpty()) {
                Toast.makeText(TarjetaMilitar.this, "Introduce el número de tarjeta", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lo guardamos en la Base de Datos, mediante el método anadirTMI que está en ExpedienteHelper
            if (dbHelper.anadirTMI(idUsuarioActual, numTarjeta, fecha)) {
                Toast.makeText(TarjetaMilitar.this, "Guardado con éxito", Toast.LENGTH_SHORT).show();
                limpiarFormulario(); // Vaciamos los EditText
                cargarLista();       // Refrescamos la lista para que aparezca el nuevo
            } else {
                // Si devuelve false, es porque ha saltado nuestra protección anti-duplicados (o un error)
                Toast.makeText(TarjetaMilitar.this, "Error: Esta TMI ya está registrada", Toast.LENGTH_LONG).show();
            }
        });

        // Botón MODIFICAR
        btnModificar.setOnClickListener(v -> {
            // Si el ID es -1, significa que no ha tocado ninguna fila de la lista para editar
            if (idTmiSeleccionada == -1) {
                Toast.makeText(TarjetaMilitar.this, "Selecciona una tarjeta de la lista", Toast.LENGTH_SHORT).show();
                return;
            }

            String numTarjeta = etNumeroTmi.getText() != null ? etNumeroTmi.getText().toString().trim().toUpperCase() : "";
            String fecha = etFechaCaducidad.getText() != null ? etFechaCaducidad.getText().toString().trim() : "";

            if (numTarjeta.isEmpty()) {
                Toast.makeText(TarjetaMilitar.this, "Introduce el número de tarjeta", Toast.LENGTH_SHORT).show();
                return;
            }

            // Actualizamos la base de datos buscando por el ID que seleccionó
            if (dbHelper.modificarTMI(idTmiSeleccionada, numTarjeta, fecha)) {
                Toast.makeText(TarjetaMilitar.this, "Actualizado correctamente", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarLista();
            } else {
                //  Salta si el SQLite devuelve false (porque la tarjeta ya existe)
                Toast.makeText(TarjetaMilitar.this, "Error: Este número de TMI ya está registrado", Toast.LENGTH_LONG).show();
            }
        });

        // Botón ELIMINAR
        btnEliminar.setOnClickListener(v -> {
            if (idTmiSeleccionada == -1) {
                Toast.makeText(TarjetaMilitar.this, "Selecciona una tarjeta de la lista", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.eliminarTMI(idTmiSeleccionada)) {
                Toast.makeText(TarjetaMilitar.this, "Borrado correctamente", Toast.LENGTH_SHORT).show();
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
        etNumeroTmi.setText("");
        etFechaCaducidad.setText("");
        idTmiSeleccionada = -1; // Vuelve al estado inicial
    }

    // Función que lee de SQLite y llena el RecyclerView
    @SuppressLint("NotifyDataSetChanged") // Lo puse para quitar un
    // warning del entorno de desarrollo porque necesito hacer un repintado total de la vista en cargarLista()
    private void cargarLista() {
        listaDatos.clear(); // Borramos la lista actual para no duplicar datos

        // Usamos un Cursor que va señalando fila por fila en la base de datos
        Cursor cursor = dbHelper.obtenerTMIs(idUsuarioActual);

        // Si el Cursor ha encontrado al menos un resultado (moveToFirst)...
        if (cursor.moveToFirst()) {
            do {
                // Extraemos las columnas de la tabla de la base de datos
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("Id_M_Tmi"));
                String numero = cursor.getString(cursor.getColumnIndexOrThrow("N_Tarjeta"));
                String caducidad = cursor.getString(cursor.getColumnIndexOrThrow("M_Tmi_Fecha_Cadu"));

                if (caducidad == null) caducidad = ""; // Si no existe fecha lo dejamos en blanco

                // Instanciamos la data class que teníamos creada en TmiAdaptador en Kotlin
                // y la metemos en nuestra lista de Java
                listaDatos.add(new TmiModelo(id, numero, caducidad));

            } while (cursor.moveToNext()); //  ... Y repetimos hasta que no haya más filas
        }
        cursor.close(); // Cerramos el Cursor

        // Le indicamos al adaptador que ha habido cambios y que vuelva a pintar la lista
        adaptador.notifyDataSetChanged();
    }
}