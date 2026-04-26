package com.proyecto.miexmi;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class EvaluacionAscenso extends AppCompatActivity {

    private AutoCompleteTextView etNombreEvaluacion, etResultadoEvaluacion;
    private TextInputEditText etFechaBod, etNumBod;
    private ExpedienteHelper dbHelper;
    private int idUsuarioActual;
    private int idEvaluacionSeleccionada = -1;
    private EvaluacionAdaptador adaptador;
    private List<EvaluacionAdaptador.EvaluacionModelo> listaDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluacion_ascenso);

        Utilidades.configurarBotonVolver(this, R.id.btnVolverEvaluacion);

        dbHelper = new ExpedienteHelper(this);
        idUsuarioActual = Utilidades.obtenerUsuarioActual(this);

        if (idUsuarioActual == -1) {
            Toast.makeText(this, "Error de sesión", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // ENLACES DEL XML
        etNombreEvaluacion = findViewById(R.id.etNombreEvaluacion);
        etResultadoEvaluacion = findViewById(R.id.etResultadoEvaluacion);
        etFechaBod = findViewById(R.id.etFechaBodEvaluacion);
        etNumBod = findViewById(R.id.etNumBodEvaluacion);

        Button btnAnadir = findViewById(R.id.btnAnadirEvaluacion);
        Button btnModificar = findViewById(R.id.btnModificarEvaluacion);
        Button btnEliminar = findViewById(R.id.btnEliminarEvaluacion);
        Button btnLimpiar = findViewById(R.id.btnLimpiarEvaluacion);
        RecyclerView rvEvaluaciones = findViewById(R.id.rvEvaluaciones);

        // --- CONFIGURACIÓN DE LOS DESPLEGABLES Y CALENDARIO ---
        // 1. Desplegable de Empleos (reutilizando la herramienta global)
        Utilidades.configurarDesplegableEmpleos(this, etNombreEvaluacion);

        // 2. Desplegable de Resultados (Apto, No Apto)
        String[] opcionesResultado = new String[]{"Apto", "No Apto", "No Presentado"};
        ArrayAdapter<String> adapterResultado = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, opcionesResultado);
        etResultadoEvaluacion.setAdapter(adapterResultado);

        // 3. Calendario global
        Utilidades.configurarCalendario(this, etFechaBod);

        // --- CONFIGURACIÓN DE LA LISTA ---
        rvEvaluaciones.setLayoutManager(new LinearLayoutManager(this));
        listaDatos = new ArrayList<>();

        adaptador = new EvaluacionAdaptador(listaDatos, evaluacion -> {
            idEvaluacionSeleccionada = evaluacion.idEvaluacion;
            // Pasamos false para que el desplegable no filtre el texto al seleccionarlo
            etNombreEvaluacion.setText(evaluacion.nombre, false);
            etResultadoEvaluacion.setText(evaluacion.resultado, false);
            etFechaBod.setText(evaluacion.fechaBod);
            etNumBod.setText(evaluacion.numBod.equals("0") ? "" : evaluacion.numBod);
            Toast.makeText(EvaluacionAscenso.this, "Seleccionado para editar", Toast.LENGTH_SHORT).show();
        });
        rvEvaluaciones.setAdapter(adaptador);

        cargarLista();

        // --- BOTONES ---

        btnLimpiar.setOnClickListener(v -> limpiarFormulario());

        btnAnadir.setOnClickListener(v -> {
            String nombre = etNombreEvaluacion.getText() != null ? etNombreEvaluacion.getText().toString().trim() : "";
            String resultado = etResultadoEvaluacion.getText() != null ? etResultadoEvaluacion.getText().toString().trim() : "";
            String fecha = etFechaBod.getText() != null ? etFechaBod.getText().toString().trim() : "";
            String numBod = etNumBod.getText() != null ? etNumBod.getText().toString().trim() : "";

            if (nombre.isEmpty() || resultado.isEmpty()) {
                Toast.makeText(this, "El ascenso y el resultado son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.anadirEvaluacion(idUsuarioActual, nombre, resultado, fecha, numBod)) {
                Toast.makeText(this, "Guardado con éxito", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarLista();
            } else {
                Toast.makeText(this, "Error: Esa evaluación ya está registrada", Toast.LENGTH_LONG).show();
            }
        });

        btnModificar.setOnClickListener(v -> {
            if (idEvaluacionSeleccionada == -1) {
                Toast.makeText(this, "Selecciona una evaluación de la lista", Toast.LENGTH_SHORT).show();
                return;
            }

            String nombre = etNombreEvaluacion.getText() != null ? etNombreEvaluacion.getText().toString().trim() : "";
            String resultado = etResultadoEvaluacion.getText() != null ? etResultadoEvaluacion.getText().toString().trim() : "";
            String fecha = etFechaBod.getText() != null ? etFechaBod.getText().toString().trim() : "";
            String numBod = etNumBod.getText() != null ? etNumBod.getText().toString().trim() : "";

            if (nombre.isEmpty() || resultado.isEmpty()) {
                Toast.makeText(this, "El ascenso y el resultado son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.modificarEvaluacion(idEvaluacionSeleccionada, nombre, resultado, fecha, numBod)) {
                Toast.makeText(this, "Actualizado correctamente", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarLista();
            }
        });

        btnEliminar.setOnClickListener(v -> {
            if (idEvaluacionSeleccionada == -1) {
                Toast.makeText(this, "Selecciona una evaluación de la lista", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.eliminarEvaluacion(idEvaluacionSeleccionada)) {
                Toast.makeText(this, "Borrado correctamente", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarLista();
            }
        });
    }

    private void limpiarFormulario() {
        etNombreEvaluacion.setText("", false);
        etResultadoEvaluacion.setText("", false);
        etFechaBod.setText("");
        etNumBod.setText("");
        idEvaluacionSeleccionada = -1;
    }

    @android.annotation.SuppressLint("NotifyDataSetChanged")
    private void cargarLista() {
        listaDatos.clear();
        Cursor cursor = dbHelper.obtenerEvaluaciones(idUsuarioActual);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("Id_M_Eva"));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("Nom_Evaluacion"));
                String resultado = cursor.getString(cursor.getColumnIndexOrThrow("Resultado"));
                String fecha = cursor.getString(cursor.getColumnIndexOrThrow("M_Eva_Fecha_Bod"));
                int nBod = cursor.getInt(cursor.getColumnIndexOrThrow("M_Eva_Nbod"));

                if (fecha == null) fecha = "";
                if (resultado == null) resultado = "";

                listaDatos.add(new EvaluacionAdaptador.EvaluacionModelo(id, nombre, resultado, fecha, String.valueOf(nBod)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adaptador.notifyDataSetChanged();
    }
}