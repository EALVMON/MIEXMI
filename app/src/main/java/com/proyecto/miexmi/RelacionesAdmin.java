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

public class RelacionesAdmin extends AppCompatActivity {

    private TextInputEditText etNombreRelacion, etFechaBod, etNumBod;
    private ExpedienteHelper dbHelper;
    private int idUsuarioActual;
    private int idRelacionSeleccionada = -1;
    private RelacionAdaptador adaptador;
    private List<RelacionAdaptador.RelacionModelo> listaDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relaciones_admin);

        Utilidades.configurarBotonVolver(this, R.id.btnVolverRelaciones);

        dbHelper = new ExpedienteHelper(this);
        idUsuarioActual = Utilidades.obtenerUsuarioActual(this);

        if (idUsuarioActual == -1) {
            Toast.makeText(this, "Error de sesión", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etNombreRelacion = findViewById(R.id.etNombreRelacion);
        etFechaBod = findViewById(R.id.etFechaBodRelacion);
        etNumBod = findViewById(R.id.etNumBodRelacion);

        Button btnAnadir = findViewById(R.id.btnAnadirRelacion);
        Button btnModificar = findViewById(R.id.btnModificarRelacion);
        Button btnEliminar = findViewById(R.id.btnEliminarRelacion);
        Button btnLimpiar = findViewById(R.id.btnLimpiarRelacion);
        RecyclerView rvRelaciones = findViewById(R.id.rvRelaciones);

        // Activamos la herramienta global de Calendario
        Utilidades.configurarCalendario(this, etFechaBod);

        // --- CONFIGURACIÓN DE LA LISTA ---
        rvRelaciones.setLayoutManager(new LinearLayoutManager(this));
        listaDatos = new ArrayList<>();

        adaptador = new RelacionAdaptador(listaDatos, relacion -> {
            idRelacionSeleccionada = relacion.idRelacion;
            etNombreRelacion.setText(relacion.nombre);
            etFechaBod.setText(relacion.fechaBod);
            etNumBod.setText(relacion.numBod.equals("0") ? "" : relacion.numBod);
            Toast.makeText(RelacionesAdmin.this, "Seleccionado para editar", Toast.LENGTH_SHORT).show();
        });
        rvRelaciones.setAdapter(adaptador);

        cargarLista();

        // --- BOTONES ---

        btnLimpiar.setOnClickListener(v -> limpiarFormulario());

        btnAnadir.setOnClickListener(v -> {
            String nombre = etNombreRelacion.getText() != null ? etNombreRelacion.getText().toString().trim() : "";
            String fecha = etFechaBod.getText() != null ? etFechaBod.getText().toString().trim() : "";
            String numBod = etNumBod.getText() != null ? etNumBod.getText().toString().trim() : "";

            if (nombre.isEmpty()) {
                Toast.makeText(this, "La relación administrativa es obligatoria", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.anadirRelacionAdmin(idUsuarioActual, nombre, fecha, numBod)) {
                Toast.makeText(this, "Guardado con éxito", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarLista();
            } else {
                Toast.makeText(this, "Error: Esa relación ya está registrada", Toast.LENGTH_LONG).show();
            }
        });

        btnModificar.setOnClickListener(v -> {
            if (idRelacionSeleccionada == -1) {
                Toast.makeText(this, "Selecciona un registro de la lista", Toast.LENGTH_SHORT).show();
                return;
            }

            String nombre = etNombreRelacion.getText() != null ? etNombreRelacion.getText().toString().trim() : "";
            String fecha = etFechaBod.getText() != null ? etFechaBod.getText().toString().trim() : "";
            String numBod = etNumBod.getText() != null ? etNumBod.getText().toString().trim() : "";

            if (nombre.isEmpty()) {
                Toast.makeText(this, "La relación administrativa es obligatoria", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.modificarRelacionAdmin(idRelacionSeleccionada, nombre, fecha, numBod)) {
                Toast.makeText(this, "Actualizado correctamente", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarLista();
            }
        });

        btnEliminar.setOnClickListener(v -> {
            if (idRelacionSeleccionada == -1) {
                Toast.makeText(this, "Selecciona un registro de la lista", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.eliminarRelacionAdmin(idRelacionSeleccionada)) {
                Toast.makeText(this, "Borrado correctamente", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarLista();
            }
        });
    }

    private void limpiarFormulario() {
        etNombreRelacion.setText("");
        etFechaBod.setText("");
        etNumBod.setText("");
        idRelacionSeleccionada = -1;
    }

    @android.annotation.SuppressLint("NotifyDataSetChanged")
    private void cargarLista() {
        listaDatos.clear();
        Cursor cursor = dbHelper.obtenerRelacionesAdmin(idUsuarioActual);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("Id_M_Radm"));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("Nom_Rel_Admin"));
                String fecha = cursor.getString(cursor.getColumnIndexOrThrow("M_Radm_Fecha_Bod"));
                int nBod = cursor.getInt(cursor.getColumnIndexOrThrow("M_Radm_Nbod"));

                if (fecha == null) fecha = "";

                listaDatos.add(new RelacionAdaptador.RelacionModelo(id, nombre, fecha, String.valueOf(nBod)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adaptador.notifyDataSetChanged();
    }
}