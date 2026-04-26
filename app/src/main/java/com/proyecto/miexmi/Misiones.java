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

public class Misiones extends AppCompatActivity {

    private TextInputEditText etNombreMision, etFechaBod, etNumBod;
    private ExpedienteHelper dbHelper;
    private int idUsuarioActual;
    private int idMisionSeleccionada = -1;
    private MisionAdaptador adaptador;
    private List<MisionAdaptador.MisionModelo> listaDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_misiones);

        Utilidades.configurarBotonVolver(this, R.id.btnVolverMisiones);

        dbHelper = new ExpedienteHelper(this);
        idUsuarioActual = Utilidades.obtenerUsuarioActual(this);

        if (idUsuarioActual == -1) {
            Toast.makeText(this, "Error de sesión", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etNombreMision = findViewById(R.id.etNombreMision);
        etFechaBod = findViewById(R.id.etFechaBodMision);
        etNumBod = findViewById(R.id.etNumBodMision);

        Button btnAnadir = findViewById(R.id.btnAnadirMision);
        Button btnModificar = findViewById(R.id.btnModificarMision);
        Button btnEliminar = findViewById(R.id.btnEliminarMision);
        Button btnLimpiar = findViewById(R.id.btnLimpiarMision);
        RecyclerView rvMisiones = findViewById(R.id.rvMisiones);

        // Activamos la herramienta global de Calendario
        Utilidades.configurarCalendario(this, etFechaBod);

        // --- CONFIGURACIÓN DE LA LISTA ---
        rvMisiones.setLayoutManager(new LinearLayoutManager(this));
        listaDatos = new ArrayList<>();

        adaptador = new MisionAdaptador(listaDatos, mision -> {
            idMisionSeleccionada = mision.idMision;
            etNombreMision.setText(mision.nombre);
            etFechaBod.setText(mision.fechaBod);
            etNumBod.setText(mision.numBod.equals("0") ? "" : mision.numBod);
            Toast.makeText(Misiones.this, "Seleccionado para editar", Toast.LENGTH_SHORT).show();
        });
        rvMisiones.setAdapter(adaptador);

        cargarLista();

        // --- BOTONES ---

        btnLimpiar.setOnClickListener(v -> limpiarFormulario());

        btnAnadir.setOnClickListener(v -> {
            String nombre = etNombreMision.getText() != null ? etNombreMision.getText().toString().trim() : "";
            String fecha = etFechaBod.getText() != null ? etFechaBod.getText().toString().trim() : "";
            String numBod = etNumBod.getText() != null ? etNumBod.getText().toString().trim() : "";

            if (nombre.isEmpty()) {
                Toast.makeText(this, "El nombre de la misión es obligatorio", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.anadirMision(idUsuarioActual, nombre, fecha, numBod)) {
                Toast.makeText(this, "Guardado con éxito", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarLista();
            } else {
                Toast.makeText(this, "Error: Esa misión ya está registrada", Toast.LENGTH_LONG).show();
            }
        });

        btnModificar.setOnClickListener(v -> {
            if (idMisionSeleccionada == -1) {
                Toast.makeText(this, "Selecciona una misión de la lista", Toast.LENGTH_SHORT).show();
                return;
            }

            String nombre = etNombreMision.getText() != null ? etNombreMision.getText().toString().trim() : "";
            String fecha = etFechaBod.getText() != null ? etFechaBod.getText().toString().trim() : "";
            String numBod = etNumBod.getText() != null ? etNumBod.getText().toString().trim() : "";

            if (nombre.isEmpty()) {
                Toast.makeText(this, "El nombre de la misión es obligatorio", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.modificarMision(idMisionSeleccionada, nombre, fecha, numBod)) {
                Toast.makeText(this, "Actualizado correctamente", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarLista();
            }
        });

        btnEliminar.setOnClickListener(v -> {
            if (idMisionSeleccionada == -1) {
                Toast.makeText(this, "Selecciona una misión de la lista", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.eliminarMision(idMisionSeleccionada)) {
                Toast.makeText(this, "Borrado correctamente", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarLista();
            }
        });
    }

    private void limpiarFormulario() {
        etNombreMision.setText("");
        etFechaBod.setText("");
        etNumBod.setText("");
        idMisionSeleccionada = -1;
    }

    @android.annotation.SuppressLint("NotifyDataSetChanged")
    private void cargarLista() {
        listaDatos.clear();
        Cursor cursor = dbHelper.obtenerMisiones(idUsuarioActual);

        if (cursor.moveToFirst()) {
            do {
                // AQUÍ ESTABA EL CRASH: Ya hemos puesto los nombres exactos de tu tabla
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("Id_M_Misi"));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("Nom_Mision"));
                String fecha = cursor.getString(cursor.getColumnIndexOrThrow("M_Misi_Fecha_Bod"));
                int nBod = cursor.getInt(cursor.getColumnIndexOrThrow("M_Misi_Nbod"));

                if (fecha == null) fecha = "";

                listaDatos.add(new MisionAdaptador.MisionModelo(id, nombre, fecha, String.valueOf(nBod)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adaptador.notifyDataSetChanged();
    }
}
