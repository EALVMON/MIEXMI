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

public class Empleos extends AppCompatActivity {

    private AutoCompleteTextView etNombreEmpleo;
    private TextInputEditText etFechaBodEmpleo, etNumBodEmpleo;
    private ExpedienteHelper dbHelper;
    private int idUsuarioActual;
    private int idEmpleoSeleccionado = -1;
    private EmpleoAdaptador adaptador;
    private List<EmpleoAdaptador.EmpleoModelo> listaDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empleos);

        // Configuramos el botón volver
        Utilidades.configurarBotonVolver(this, R.id.btnVolverEmpleos);

        // Inicializamos base de datos y sesión
        dbHelper = new ExpedienteHelper(this);

        // RECUPERAMOS LA SESIÓN usando la función que nos hemos creado en Utilidades.java
        idUsuarioActual = Utilidades.obtenerUsuarioActual(this);
        if (idUsuarioActual == -1) {
            Toast.makeText(this, "Error de sesión", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // --- ENLAZAMOS VARIABLES CON EL XML ---
        etNombreEmpleo = findViewById(R.id.etNombreEmpleo);
        etFechaBodEmpleo = findViewById(R.id.etFechaBodEmpleo);
        etNumBodEmpleo = findViewById(R.id.etNumBodEmpleo);

        Button btnAnadir = findViewById(R.id.btnAnadirEmpleo);
        Button btnModificar = findViewById(R.id.btnModificarEmpleo);
        Button btnEliminar = findViewById(R.id.btnEliminarEmpleo);
        Button btnLimpiar = findViewById(R.id.btnLimpiarEmpleo);
        RecyclerView rvEmpleos = findViewById(R.id.rvEmpleos);

        // Activamos el calendario maestro
        Utilidades.configurarCalendario(this, etFechaBodEmpleo);

        // --- CONFIGURACIÓN DE LA LISTA DESPLEGABLE DE EMPLEOS (EJÉRCITO DE TIERRA) ---
        // Llamamos a nuestra herramienta global en Utilidades
        Utilidades.configurarDesplegableEmpleos(this, etNombreEmpleo);

        // --- CONFIGURACIÓN DE LA LISTA Y EL ADAPTADOR ---
        rvEmpleos.setLayoutManager(new LinearLayoutManager(this));
        listaDatos = new ArrayList<>();

        adaptador = new EmpleoAdaptador(listaDatos, empleo -> {
            idEmpleoSeleccionado = empleo.idEmpleo;
            // Para cambiar el texto en un AutoCompleteTextView necesitamos false al final para que no filtre la lista
            etNombreEmpleo.setText(empleo.nombre, false);
            etFechaBodEmpleo.setText(empleo.fechaBod);
            etNumBodEmpleo.setText(empleo.numBod.equals("0") ? "" : empleo.numBod);
            Toast.makeText(Empleos.this, "Seleccionado para editar", Toast.LENGTH_SHORT).show();
        });
        rvEmpleos.setAdapter(adaptador);

        // Cargamos los datos por primera vez
        cargarLista();

        // ==========================================
        // --- ACCIONES DE LOS BOTONES            ---
        // ==========================================

        btnLimpiar.setOnClickListener(v -> limpiarFormulario());

        btnAnadir.setOnClickListener(v -> {
            String nombre = etNombreEmpleo.getText() != null ? etNombreEmpleo.getText().toString().trim() : "";
            String fecha = etFechaBodEmpleo.getText() != null ? etFechaBodEmpleo.getText().toString().trim() : "";
            String numBod = etNumBodEmpleo.getText() != null ? etNumBodEmpleo.getText().toString().trim() : "";

            if (nombre.isEmpty()) {
                Toast.makeText(this, "El empleo es obligatorio", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.anadirEmpleo(idUsuarioActual, nombre, fecha, numBod)) {
                Toast.makeText(this, "Guardado con éxito", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarLista();
            } else {
                Toast.makeText(this, "Error: Ese empleo ya está registrado", Toast.LENGTH_LONG).show();
            }
        });

        btnModificar.setOnClickListener(v -> {
            if (idEmpleoSeleccionado == -1) {
                Toast.makeText(this, "Selecciona un empleo de la lista", Toast.LENGTH_SHORT).show();
                return;
            }

            String nombre = etNombreEmpleo.getText() != null ? etNombreEmpleo.getText().toString().trim() : "";
            String fecha = etFechaBodEmpleo.getText() != null ? etFechaBodEmpleo.getText().toString().trim() : "";
            String numBod = etNumBodEmpleo.getText() != null ? etNumBodEmpleo.getText().toString().trim() : "";

            if (nombre.isEmpty()) {
                Toast.makeText(this, "El empleo es obligatorio", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.modificarEmpleo(idEmpleoSeleccionado, nombre, fecha, numBod)) {
                Toast.makeText(this, "Actualizado correctamente", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarLista();
            }
        });

        btnEliminar.setOnClickListener(v -> {
            if (idEmpleoSeleccionado == -1) {
                Toast.makeText(this, "Selecciona un empleo de la lista", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.eliminarEmpleo(idEmpleoSeleccionado)) {
                Toast.makeText(this, "Borrado correctamente", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarLista();
            }
        });
    }

    private void limpiarFormulario() {
        etNombreEmpleo.setText("", false); // El false evita que despliegue el menú al limpiar
        etFechaBodEmpleo.setText("");
        etNumBodEmpleo.setText("");
        idEmpleoSeleccionado = -1;
    }

    @android.annotation.SuppressLint("NotifyDataSetChanged")
    private void cargarLista() {
        listaDatos.clear();
        Cursor cursor = dbHelper.obtenerEmpleos(idUsuarioActual);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("Id_M_Empl"));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("Nom_Empleo"));
                String fecha = cursor.getString(cursor.getColumnIndexOrThrow("M_Empl_Fecha_Bod"));
                int nBod = cursor.getInt(cursor.getColumnIndexOrThrow("M_Empl_Nbod"));

                if (fecha == null) fecha = "";

                listaDatos.add(new EmpleoAdaptador.EmpleoModelo(id, nombre, fecha, String.valueOf(nBod)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adaptador.notifyDataSetChanged();
    }
}