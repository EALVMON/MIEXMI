package com.proyecto.miexmi;

// Los "imports" son las herramientas que Android nos presta.
// Botones, Listas (RecyclerView), mensajes emergentes (Toast), etc.
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

// 1-Declaro variables arriba.
// 2-Enlazo los botones del XML en el onCreate.
// 3-Configuro la lista (RecyclerView).
// 4-Le doy "vida" a los botones con los clics.
// 5-Hago un metodo para leer la base de datos (cargarLista()).
// ====================================================================
// Heredar de 'AppCompatActivity' le dice a Android: "Hola, soy una pantalla visual".
public class Aptitudes extends AppCompatActivity {

    // Declaramos las variables "globales" (arriba del todo) para que cualquier
    // botón o función dentro de esta pantalla pueda usarlas y verlas.

    // Cajas de texto donde el usuario escribe
    private TextInputEditText etNombreAptitud, etFechaBod, etNumBod;

    // El "ayudante" que se comunica con la base de datos SQLite
    private ExpedienteHelper dbHelper;

    // Variables de control interno
    private int idUsuarioActual; // ¿Quién está usando la app?
    private int idAptitudSeleccionada = -1; // -1 significa "no hay nada seleccionado aún"

    // Herramientas para la lista visual
    private AptitudAdaptador adaptador;
    private List<AptitudModelo> listaDatos;

    // ====================================================================
    // 2. EL MOTOR DE ARRANQUE: onCreate()
    // ====================================================================
    // Este método es como el contacto del coche. Es lo primerísimo que se ejecuta
    // cuando el usuario abre esta pantalla en el móvil.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Conecta este cerebro Java con su "cara" visual (el archivo XML de diseño)
        setContentView(R.layout.activity_aptitudes);

        // Llamamos a una herramienta tuya personalizada para que funcione la flecha de "Atrás"
        Utilidades.configurarBotonVolver(this, R.id.btnVolverAptitudes);

        // Arrancamos la conexión a la base de datos y preguntamos quién es el usuario
        dbHelper = new ExpedienteHelper(this);
        idUsuarioActual = Utilidades.obtenerUsuarioActual(this);

        // MEDIDA DE SEGURIDAD: Si por algún fallo el usuario no existe, le echamos de la pantalla
        if (idUsuarioActual == -1) {
            Toast.makeText(this, "Error de sesión", Toast.LENGTH_SHORT).show();
            finish(); // Cierra esta pantalla
            return;   // Corta la ejecución para que no de error
        }

        // ====================================================================
        // 3. ENLAZAR JAVA CON EL XML (findViewById)
        // ====================================================================
        // Aquí le decimos a Java: "Oye, búscame en la pantalla el botón que tiene este ID"
        etNombreAptitud = findViewById(R.id.etNombreAptitud);
        etFechaBod = findViewById(R.id.etFechaBodAptitud);
        etNumBod = findViewById(R.id.etNumBodAptitud);

        Button btnAnadir = findViewById(R.id.btnAnadirAptitud);
        Button btnModificar = findViewById(R.id.btnModificarAptitud);
        Button btnEliminar = findViewById(R.id.btnEliminarAptitud);
        Button btnLimpiar = findViewById(R.id.btnLimpiarAptitud);
        RecyclerView rvAptitudes = findViewById(R.id.rvAptitudes);

        // Activamos la herramienta para que al tocar la fecha salga el calendario desplegable
        Utilidades.configurarCalendario(this, etFechaBod);

        // ====================================================================
        // 4. PREPARAR LA LISTA (RecyclerView)
        // ====================================================================
        // Le decimos a la lista que se dibuje de arriba a abajo (LinearLayoutManager)
        rvAptitudes.setLayoutManager(new LinearLayoutManager(this));
        listaDatos = new ArrayList<>(); // Creamos la lista vacía en memoria

        // Conectamos el Adaptador (el puente) que hicimos en Kotlin.
        adaptador = new AptitudAdaptador(listaDatos, aptitud -> {
            // ESTO PASA CUANDO EL USUARIO TOCA UNA FILA DE LA LISTA:
            // 1. Guardamos el ID secreto de esa aptitud para saber cuál modificar/borrar
            idAptitudSeleccionada = aptitud.getIdAptitud();
            // 2. Rellenamos las cajas de texto de arriba con los datos de la fila que tocó
            etNombreAptitud.setText(aptitud.getNombre());
            etFechaBod.setText(aptitud.getFechaBod());
            // Si el boletín es 0 (no tiene), lo dejamos en blanco para que quede más bonito
            etNumBod.setText(aptitud.getNumBod().equals("0") ? "" : aptitud.getNumBod());

            Toast.makeText(Aptitudes.this, "Seleccionado para editar", Toast.LENGTH_SHORT).show();
            return kotlin.Unit.INSTANCE; // Requisito de Kotlin para terminar el clic
        });

        // Enganchamos el adaptador terminado a la lista visual
        rvAptitudes.setAdapter(adaptador);

        // Llamamos a la base de datos para que traiga los datos y los pinte
        cargarLista();


        // ====================================================================
        // 5. PROGRAMAR LOS BOTONES (CRUD)
        // ====================================================================

        // Botón LIMPIAR: Llama a la función de abajo que vacía los textos
        btnLimpiar.setOnClickListener(v -> limpiarFormulario());

        // Botón AÑADIR (Guardar nuevo)
        btnAnadir.setOnClickListener(v -> {
            // Recogemos lo que ha escrito el usuario. "trim()" borra los espacios en blanco sobrantes.
            String nombre = etNombreAptitud.getText() != null ? etNombreAptitud.getText().toString().trim() : "";
            String fecha = etFechaBod.getText() != null ? etFechaBod.getText().toString().trim() : "";
            String numBod = etNumBod.getText() != null ? etNumBod.getText().toString().trim() : "";

            // FILTRO DE VALIDACIÓN: No le dejamos guardar si no ha puesto el nombre
            if (nombre.isEmpty()) {
                Toast.makeText(this, "El nombre de la aptitud es obligatorio", Toast.LENGTH_SHORT).show();
                return;
            }

            // Intentamos guardarlo en la Base de Datos
            if (dbHelper.anadirAptitud(idUsuarioActual, nombre, fecha, numBod)) {
                Toast.makeText(this, "Guardado con éxito", Toast.LENGTH_SHORT).show();
                limpiarFormulario(); // Vaciamos las cajas de texto
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
    // 6. FUNCIONES AUXILIARES
    // ====================================================================

    // Función que simplemente pone las cajas de texto en blanco y reinicia la selección
    private void limpiarFormulario() {
        etNombreAptitud.setText("");
        etFechaBod.setText("");
        etNumBod.setText("");
        idAptitudSeleccionada = -1; // Vuelve al estado inicial
    }

    // Función vital: Lee de SQLite y llena el RecyclerView
    @android.annotation.SuppressLint("NotifyDataSetChanged")
    private void cargarLista() {
        listaDatos.clear(); // Borramos la lista actual para no duplicar datos

        // Un "Cursor" es como un dedo que va señalando fila por fila en la base de datos
        Cursor cursor = dbHelper.obtenerAptitudes(idUsuarioActual);

        // Si el dedo ha encontrado al menos un resultado (moveToFirst)...
        if (cursor.moveToFirst()) {
            do {
                // Extraemos las columnas de la tabla de la base de datos
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("Id_M_Apti"));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("Nom_Aptitud"));
                String fecha = cursor.getString(cursor.getColumnIndexOrThrow("M_Apti_Fecha_Bod"));
                int nBod = cursor.getInt(cursor.getColumnIndexOrThrow("M_Apti_Nbod"));

                if (fecha == null) fecha = "";

                // Fabricamos el "molde" de Kotlin y lo metemos en nuestra lista de Java
                listaDatos.add(new AptitudModelo(id, nombre, fecha, String.valueOf(nBod)));

            } while (cursor.moveToNext()); // ...y repetimos hasta que no haya más filas
        }
        cursor.close(); // Siempre hay que cerrar el cursor para liberar memoria

        // Le chivamos al adaptador: "Oye, los datos han cambiado, vuelve a pintar la pantalla"
        adaptador.notifyDataSetChanged();
    }
}