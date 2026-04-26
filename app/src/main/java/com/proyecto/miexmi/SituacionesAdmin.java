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

public class SituacionesAdmin extends AppCompatActivity {

    private TextInputEditText etNombreSituacion, etFechaBod, etNumBod;
    private ExpedienteHelper dbHelper;
    private int idUsuarioActual;
    private int idSituacionSeleccionada = -1;
    private SituacionAdaptador adaptador;
    private List<SituacionAdaptador.SituacionModelo> listaDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_situaciones_admin);

        Utilidades.configurarBotonVolver(this, R.id.btnVolverSituaciones);

        dbHelper = new ExpedienteHelper(this);
        idUsuarioActual = Utilidades.obtenerUsuarioActual(this);

        if (idUsuarioActual == -1) {
            Toast.makeText(this, "Error de sesión", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etNombreSituacion = findViewById(R.id.etNombreSituacion);
        etFechaBod = findViewById(R.id.etFechaBodSituacion);
        etNumBod = findViewById(R.id.etNumBodSituacion);

        Button btnAnadir = findViewById(R.id.btnAnadirSituacion);
        Button btnModificar = findViewById(R.id.btnModificarSituacion);
        Button btnEliminar = findViewById(R.id.btnEliminarSituacion);
        Button btnLimpiar = findViewById(R.id.btnLimpiarSituacion);
        RecyclerView rvSituaciones = findViewById(R.id.rvSituaciones);

        // Activamos la herramienta global de Calendario
        Utilidades.configurarCalendario(this, etFechaBod);

        // --- CONFIGURACIÓN DE LA LISTA ---
        rvSituaciones.setLayoutManager(new LinearLayoutManager(this));
        listaDatos = new ArrayList<>();

        adaptador = new SituacionAdaptador(listaDatos, situacion -> {
            idSituacionSeleccionada = situacion.idSituacion;
            etNombreSituacion.setText(situacion.nombre);
            etFechaBod.setText(situacion.fechaBod);
            etNumBod.setText(situacion.numBod.equals("0") ? "" : situacion.numBod);
            Toast.makeText(SituacionesAdmin.this, "Seleccionado para editar", Toast.LENGTH_SHORT).show();
        });
        rvSituaciones.setAdapter(adaptador);

        cargarLista();

        // --- BOTONES ---

        btnLimpiar.setOnClickListener(v -> limpiarFormulario());

        btnAnadir.setOnClickListener(v -> {
            String nombre = etNombreSituacion.getText() != null ? etNombreSituacion.getText().toString().trim() : "";
            String fecha = etFechaBod.getText() != null ? etFechaBod.getText().toString().trim() : "";
            String numBod = etNumBod.getText() != null ? etNumBod.getText().toString().trim() : "";

            if (nombre.isEmpty()) {
                Toast.makeText(this, "La situación administrativa es obligatoria", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.anadirSituacion(idUsuarioActual, nombre, fecha, numBod)) {
                Toast.makeText(this, "Guardado con éxito", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarLista();
            } else {
                Toast.makeText(this, "Error: Esa situación ya está registrada", Toast.LENGTH_LONG).show();
            }
        });

        btnModificar.setOnClickListener(v -> {
            if (idSituacionSeleccionada == -1) {
                Toast.makeText(this, "Selecciona un registro de la lista", Toast.LENGTH_SHORT).show();
                return;
            }

            String nombre = etNombreSituacion.getText() != null ? etNombreSituacion.getText().toString().trim() : "";
            String fecha = etFechaBod.getText() != null ? etFechaBod.getText().toString().trim() : "";
            String numBod = etNumBod.getText() != null ? etNumBod.getText().toString().trim() : "";

            if (nombre.isEmpty()) {
                Toast.makeText(this, "La situación administrativa es obligatoria", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.modificarSituacion(idSituacionSeleccionada, nombre, fecha, numBod)) {
                Toast.makeText(this, "Actualizado correctamente", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarLista();
            }
        });

        btnEliminar.setOnClickListener(v -> {
            if (idSituacionSeleccionada == -1) {
                Toast.makeText(this, "Selecciona un registro de la lista", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.eliminarSituacion(idSituacionSeleccionada)) {
                Toast.makeText(this, "Borrado correctamente", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarLista();
            }
        });
    }

    private void limpiarFormulario() {
        etNombreSituacion.setText("");
        etFechaBod.setText("");
        etNumBod.setText("");
        idSituacionSeleccionada = -1;
    }

    @android.annotation.SuppressLint("NotifyDataSetChanged")
    private void cargarLista() {
        listaDatos.clear();
        Cursor cursor = dbHelper.obtenerSituaciones(idUsuarioActual);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("Id_M_Sadm"));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("Nom_Sit_Admini"));
                String fecha = cursor.getString(cursor.getColumnIndexOrThrow("M_Sadm_Fecha_Bod"));
                int nBod = cursor.getInt(cursor.getColumnIndexOrThrow("M_Sadm_Nbod"));

                if (fecha == null) fecha = "";

                listaDatos.add(new SituacionAdaptador.SituacionModelo(id, nombre, fecha, String.valueOf(nBod)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adaptador.notifyDataSetChanged();
    }
}