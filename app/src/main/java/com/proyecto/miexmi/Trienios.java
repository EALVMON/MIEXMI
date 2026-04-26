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

public class Trienios extends AppCompatActivity {

    private TextInputEditText etFechaBod, etNumBod;
    private AutoCompleteTextView etTipoTrienio;
    private ExpedienteHelper dbHelper;
    private int idUsuarioActual;
    private int idTrienioSeleccionado = -1;
    private TrienioAdaptador adaptador;
    private List<TrienioAdaptador.TrienioModelo> listaDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trienios);

        Utilidades.configurarBotonVolver(this, R.id.btnVolverTrienios);

        dbHelper = new ExpedienteHelper(this);
        idUsuarioActual = Utilidades.obtenerUsuarioActual(this);

        if (idUsuarioActual == -1) {
            Toast.makeText(this, "Error de sesión", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etTipoTrienio = findViewById(R.id.etTipoTrienio);
        etFechaBod = findViewById(R.id.etFechaBodTrienio);
        etNumBod = findViewById(R.id.etNumBodTrienio);

        Button btnAnadir = findViewById(R.id.btnAnadirTrienio);
        Button btnModificar = findViewById(R.id.btnModificarTrienio);
        Button btnEliminar = findViewById(R.id.btnEliminarTrienio);
        Button btnLimpiar = findViewById(R.id.btnLimpiarTrienio);
        RecyclerView rvTrienios = findViewById(R.id.rvTrienios);

        // Activamos las herramientas globales
        Utilidades.configurarDesplegableTrienios(this, etTipoTrienio);
        Utilidades.configurarCalendario(this, etFechaBod);

        // --- CONFIGURACIÓN DE LA LISTA ---
        rvTrienios.setLayoutManager(new LinearLayoutManager(this));
        listaDatos = new ArrayList<>();

        adaptador = new TrienioAdaptador(listaDatos, trienio -> {
            idTrienioSeleccionado = trienio.idTrienio;
            etTipoTrienio.setText(trienio.tipoTrienio, false);
            etFechaBod.setText(trienio.fechaBod);
            etNumBod.setText(trienio.numBod.equals("0") ? "" : trienio.numBod);
            Toast.makeText(Trienios.this, "Seleccionado para editar", Toast.LENGTH_SHORT).show();
        });
        rvTrienios.setAdapter(adaptador);

        cargarLista();

        // --- BOTONES ---

        btnLimpiar.setOnClickListener(v -> limpiarFormulario());

        btnAnadir.setOnClickListener(v -> {
            String tipo = etTipoTrienio.getText() != null ? etTipoTrienio.getText().toString().trim() : "";
            String fecha = etFechaBod.getText() != null ? etFechaBod.getText().toString().trim() : "";
            String numBod = etNumBod.getText() != null ? etNumBod.getText().toString().trim() : "";

            if (tipo.isEmpty()) {
                Toast.makeText(this, "El grupo del trienio es obligatorio", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.anadirTrienio(idUsuarioActual, tipo, fecha, numBod)) {
                Toast.makeText(this, "Guardado con éxito", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarLista();
            } else {
                Toast.makeText(this, "Error al guardar", Toast.LENGTH_LONG).show();
            }
        });

        btnModificar.setOnClickListener(v -> {
            if (idTrienioSeleccionado == -1) {
                Toast.makeText(this, "Selecciona un trienio de la lista", Toast.LENGTH_SHORT).show();
                return;
            }

            String tipo = etTipoTrienio.getText() != null ? etTipoTrienio.getText().toString().trim() : "";
            String fecha = etFechaBod.getText() != null ? etFechaBod.getText().toString().trim() : "";
            String numBod = etNumBod.getText() != null ? etNumBod.getText().toString().trim() : "";

            if (tipo.isEmpty()) {
                Toast.makeText(this, "El grupo del trienio es obligatorio", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.modificarTrienio(idTrienioSeleccionado, tipo, fecha, numBod)) {
                Toast.makeText(this, "Actualizado correctamente", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarLista();
            }
        });

        btnEliminar.setOnClickListener(v -> {
            if (idTrienioSeleccionado == -1) {
                Toast.makeText(this, "Selecciona un trienio de la lista", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.eliminarTrienio(idTrienioSeleccionado)) {
                Toast.makeText(this, "Borrado correctamente", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarLista();
            }
        });
    }

    private void limpiarFormulario() {
        etTipoTrienio.setText("", false);
        etFechaBod.setText("");
        etNumBod.setText("");
        idTrienioSeleccionado = -1;
    }

    @android.annotation.SuppressLint("NotifyDataSetChanged")
    private void cargarLista() {
        listaDatos.clear();
        Cursor cursor = dbHelper.obtenerTrienios(idUsuarioActual);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("Id_M_Trie"));
                String tipoTrienio = cursor.getString(cursor.getColumnIndexOrThrow("Tipo_Trienio"));
                String fecha = cursor.getString(cursor.getColumnIndexOrThrow("M_Trie_Fecha_Bod"));
                int nBod = cursor.getInt(cursor.getColumnIndexOrThrow("M_Trie_Nbod"));

                if (tipoTrienio == null) tipoTrienio = "";
                if (fecha == null) fecha = "";

                listaDatos.add(new TrienioAdaptador.TrienioModelo(id, tipoTrienio, fecha, String.valueOf(nBod)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adaptador.notifyDataSetChanged();
    }
}