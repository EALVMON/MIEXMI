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

public class Aptitudes extends AppCompatActivity {

    private TextInputEditText etNombreAptitud, etFechaBod, etNumBod;
    private ExpedienteHelper dbHelper;
    private int idUsuarioActual;
    private int idAptitudSeleccionada = -1;
    private AptitudAdaptador adaptador;
    private List<AptitudAdaptador.AptitudModelo> listaDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aptitudes);

        Utilidades.configurarBotonVolver(this, R.id.btnVolverAptitudes);

        dbHelper = new ExpedienteHelper(this);
        idUsuarioActual = Utilidades.obtenerUsuarioActual(this);

        if (idUsuarioActual == -1) {
            Toast.makeText(this, "Error de sesión", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etNombreAptitud = findViewById(R.id.etNombreAptitud);
        etFechaBod = findViewById(R.id.etFechaBodAptitud);
        etNumBod = findViewById(R.id.etNumBodAptitud);

        Button btnAnadir = findViewById(R.id.btnAnadirAptitud);
        Button btnModificar = findViewById(R.id.btnModificarAptitud);
        Button btnEliminar = findViewById(R.id.btnEliminarAptitud);
        Button btnLimpiar = findViewById(R.id.btnLimpiarAptitud);
        RecyclerView rvAptitudes = findViewById(R.id.rvAptitudes);

        // Activamos la herramienta global de Calendario
        Utilidades.configurarCalendario(this, etFechaBod);

        // --- CONFIGURACIÓN DE LA LISTA ---
        rvAptitudes.setLayoutManager(new LinearLayoutManager(this));
        listaDatos = new ArrayList<>();

        adaptador = new AptitudAdaptador(listaDatos, aptitud -> {
            idAptitudSeleccionada = aptitud.idAptitud;
            etNombreAptitud.setText(aptitud.nombre);
            etFechaBod.setText(aptitud.fechaBod);
            etNumBod.setText(aptitud.numBod.equals("0") ? "" : aptitud.numBod);
            Toast.makeText(Aptitudes.this, "Seleccionado para editar", Toast.LENGTH_SHORT).show();
        });
        rvAptitudes.setAdapter(adaptador);

        cargarLista();

        // --- BOTONES ---

        btnLimpiar.setOnClickListener(v -> limpiarFormulario());

        btnAnadir.setOnClickListener(v -> {
            String nombre = etNombreAptitud.getText() != null ? etNombreAptitud.getText().toString().trim() : "";
            String fecha = etFechaBod.getText() != null ? etFechaBod.getText().toString().trim() : "";
            String numBod = etNumBod.getText() != null ? etNumBod.getText().toString().trim() : "";

            if (nombre.isEmpty()) {
                Toast.makeText(this, "El nombre de la aptitud es obligatorio", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.anadirAptitud(idUsuarioActual, nombre, fecha, numBod)) {
                Toast.makeText(this, "Guardado con éxito", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarLista();
            } else {
                Toast.makeText(this, "Error: Esa aptitud ya está registrada", Toast.LENGTH_LONG).show();
            }
        });

        btnModificar.setOnClickListener(v -> {
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

            if (dbHelper.modificarAptitud(idAptitudSeleccionada, nombre, fecha, numBod)) {
                Toast.makeText(this, "Actualizado correctamente", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarLista();
            }
        });

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

    private void limpiarFormulario() {
        etNombreAptitud.setText("");
        etFechaBod.setText("");
        etNumBod.setText("");
        idAptitudSeleccionada = -1;
    }

    @android.annotation.SuppressLint("NotifyDataSetChanged")
    private void cargarLista() {
        listaDatos.clear();
        Cursor cursor = dbHelper.obtenerAptitudes(idUsuarioActual);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("Id_M_Apti"));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("Nom_Aptitud"));
                String fecha = cursor.getString(cursor.getColumnIndexOrThrow("M_Apti_Fecha_Bod"));
                int nBod = cursor.getInt(cursor.getColumnIndexOrThrow("M_Apti_Nbod"));

                if (fecha == null) fecha = "";

                listaDatos.add(new AptitudAdaptador.AptitudModelo(id, nombre, fecha, String.valueOf(nBod)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adaptador.notifyDataSetChanged();
    }
}