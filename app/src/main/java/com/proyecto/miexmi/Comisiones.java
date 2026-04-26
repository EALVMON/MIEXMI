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

public class Comisiones extends AppCompatActivity {

    private TextInputEditText etNombreComision, etFechaBod, etNumBod;
    private ExpedienteHelper dbHelper;
    private int idUsuarioActual;
    private int idComisionSeleccionada = -1;
    private ComisionAdaptador adaptador;
    private List<ComisionAdaptador.ComisionModelo> listaDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comisiones);

        Utilidades.configurarBotonVolver(this, R.id.btnVolverComisiones);

        dbHelper = new ExpedienteHelper(this);
        idUsuarioActual = Utilidades.obtenerUsuarioActual(this);

        if (idUsuarioActual == -1) {
            Toast.makeText(this, "Error de sesión", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etNombreComision = findViewById(R.id.etNombreComision);
        etFechaBod = findViewById(R.id.etFechaBodComision);
        etNumBod = findViewById(R.id.etNumBodComision);

        Button btnAnadir = findViewById(R.id.btnAnadirComision);
        Button btnModificar = findViewById(R.id.btnModificarComision);
        Button btnEliminar = findViewById(R.id.btnEliminarComision);
        Button btnLimpiar = findViewById(R.id.btnLimpiarComision);
        RecyclerView rvComisiones = findViewById(R.id.rvComisiones);

        // Activamos la herramienta global de Calendario
        Utilidades.configurarCalendario(this, etFechaBod);

        // --- CONFIGURACIÓN DE LA LISTA ---
        rvComisiones.setLayoutManager(new LinearLayoutManager(this));
        listaDatos = new ArrayList<>();

        adaptador = new ComisionAdaptador(listaDatos, comision -> {
            idComisionSeleccionada = comision.idComision;
            etNombreComision.setText(comision.nombre);
            etFechaBod.setText(comision.fechaBod);
            etNumBod.setText(comision.numBod.equals("0") ? "" : comision.numBod);
            Toast.makeText(Comisiones.this, "Seleccionado para editar", Toast.LENGTH_SHORT).show();
        });
        rvComisiones.setAdapter(adaptador);

        cargarLista();

        // --- BOTONES ---

        btnLimpiar.setOnClickListener(v -> limpiarFormulario());

        btnAnadir.setOnClickListener(v -> {
            String nombre = etNombreComision.getText() != null ? etNombreComision.getText().toString().trim() : "";
            String fecha = etFechaBod.getText() != null ? etFechaBod.getText().toString().trim() : "";
            String numBod = etNumBod.getText() != null ? etNumBod.getText().toString().trim() : "";

            if (nombre.isEmpty()) {
                Toast.makeText(this, "El nombre de la comisión es obligatorio", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.anadirComision(idUsuarioActual, nombre, fecha, numBod)) {
                Toast.makeText(this, "Guardado con éxito", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarLista();
            } else {
                Toast.makeText(this, "Error: Esa comisión ya está registrada", Toast.LENGTH_LONG).show();
            }
        });

        btnModificar.setOnClickListener(v -> {
            if (idComisionSeleccionada == -1) {
                Toast.makeText(this, "Selecciona una comisión de la lista", Toast.LENGTH_SHORT).show();
                return;
            }

            String nombre = etNombreComision.getText() != null ? etNombreComision.getText().toString().trim() : "";
            String fecha = etFechaBod.getText() != null ? etFechaBod.getText().toString().trim() : "";
            String numBod = etNumBod.getText() != null ? etNumBod.getText().toString().trim() : "";

            if (nombre.isEmpty()) {
                Toast.makeText(this, "El nombre de la comisión es obligatorio", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.modificarComision(idComisionSeleccionada, nombre, fecha, numBod)) {
                Toast.makeText(this, "Actualizado correctamente", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarLista();
            }
        });

        btnEliminar.setOnClickListener(v -> {
            if (idComisionSeleccionada == -1) {
                Toast.makeText(this, "Selecciona una comisión de la lista", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.eliminarComision(idComisionSeleccionada)) {
                Toast.makeText(this, "Borrado correctamente", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarLista();
            }
        });
    }

    private void limpiarFormulario() {
        etNombreComision.setText("");
        etFechaBod.setText("");
        etNumBod.setText("");
        idComisionSeleccionada = -1;
    }

    @android.annotation.SuppressLint("NotifyDataSetChanged")
    private void cargarLista() {
        listaDatos.clear();
        Cursor cursor = dbHelper.obtenerComisiones(idUsuarioActual);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("Id_M_Cser"));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("Nom_Comision"));
                String fecha = cursor.getString(cursor.getColumnIndexOrThrow("M_Cser_Fecha_Bod"));
                int nBod = cursor.getInt(cursor.getColumnIndexOrThrow("M_Cser_Nbod"));

                if (fecha == null) fecha = "";

                listaDatos.add(new ComisionAdaptador.ComisionModelo(id, nombre, fecha, String.valueOf(nBod)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adaptador.notifyDataSetChanged();
    }
}
