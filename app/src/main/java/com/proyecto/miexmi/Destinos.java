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

public class Destinos extends AppCompatActivity {

    private TextInputEditText etNombreDestino, etFechaBod, etNumBod;
    private ExpedienteHelper dbHelper;
    private int idUsuarioActual;
    private int idDestinoSeleccionado = -1;
    private DestinoAdaptador adaptador;
    private List<DestinoAdaptador.DestinoModelo> listaDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destinos);

        Utilidades.configurarBotonVolver(this, R.id.btnVolverDestinos);

        dbHelper = new ExpedienteHelper(this);
        idUsuarioActual = Utilidades.obtenerUsuarioActual(this);

        if (idUsuarioActual == -1) {
            Toast.makeText(this, "Error de sesión", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etNombreDestino = findViewById(R.id.etNombreDestino);
        etFechaBod = findViewById(R.id.etFechaBod);
        etNumBod = findViewById(R.id.etNumBod);

        Button btnAnadir = findViewById(R.id.btnAnadir);
        Button btnModificar = findViewById(R.id.btnModificar);
        Button btnEliminar = findViewById(R.id.btnEliminar);
        Button btnLimpiar = findViewById(R.id.btnLimpiar);
        RecyclerView rvDestinos = findViewById(R.id.rvDestinos);

        // Activamos la herramienta global de Calendario
        Utilidades.configurarCalendario(this, etFechaBod);

        // --- CONFIGURACIÓN DE LA LISTA ---
        rvDestinos.setLayoutManager(new LinearLayoutManager(this));
        listaDatos = new ArrayList<>();

        adaptador = new DestinoAdaptador(listaDatos, destino -> {
            idDestinoSeleccionado = destino.idDestino;
            etNombreDestino.setText(destino.nombre);
            etFechaBod.setText(destino.fechaBod);
            etNumBod.setText(destino.numBod.equals("0") ? "" : destino.numBod);
            Toast.makeText(Destinos.this, "Seleccionado para editar", Toast.LENGTH_SHORT).show();
        });
        rvDestinos.setAdapter(adaptador);

        cargarLista();

        // --- BOTONES ---

        btnLimpiar.setOnClickListener(v -> limpiarFormulario());

        btnAnadir.setOnClickListener(v -> {
            String nombre = etNombreDestino.getText() != null ? etNombreDestino.getText().toString().trim() : "";
            String fecha = etFechaBod.getText() != null ? etFechaBod.getText().toString().trim() : "";
            String numBod = etNumBod.getText() != null ? etNumBod.getText().toString().trim() : "";

            if (nombre.isEmpty()) {
                Toast.makeText(this, "El nombre del destino es obligatorio", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.anadirDestino(idUsuarioActual, nombre, fecha, numBod)) {
                Toast.makeText(this, "Guardado con éxito", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarLista();
            } else {
                Toast.makeText(this, "Error: Ese destino ya está registrado", Toast.LENGTH_LONG).show();
            }
        });

        btnModificar.setOnClickListener(v -> {
            if (idDestinoSeleccionado == -1) {
                Toast.makeText(this, "Selecciona un destino de la lista", Toast.LENGTH_SHORT).show();
                return;
            }

            String nombre = etNombreDestino.getText() != null ? etNombreDestino.getText().toString().trim() : "";
            String fecha = etFechaBod.getText() != null ? etFechaBod.getText().toString().trim() : "";
            String numBod = etNumBod.getText() != null ? etNumBod.getText().toString().trim() : "";

            if (nombre.isEmpty()) {
                Toast.makeText(this, "El nombre del destino es obligatorio", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.modificarDestino(idDestinoSeleccionado, nombre, fecha, numBod)) {
                Toast.makeText(this, "Actualizado correctamente", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarLista();
            }
        });

        btnEliminar.setOnClickListener(v -> {
            if (idDestinoSeleccionado == -1) {
                Toast.makeText(this, "Selecciona un destino de la lista", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.eliminarDestino(idDestinoSeleccionado)) {
                Toast.makeText(this, "Borrado correctamente", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarLista();
            }
        });
    }

    private void limpiarFormulario() {
        etNombreDestino.setText("");
        etFechaBod.setText("");
        etNumBod.setText("");
        idDestinoSeleccionado = -1;
    }

    @android.annotation.SuppressLint("NotifyDataSetChanged")
    private void cargarLista() {
        listaDatos.clear();
        Cursor cursor = dbHelper.obtenerDestinos(idUsuarioActual);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("Id_M_Dest"));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("Nom_Destino"));
                String fecha = cursor.getString(cursor.getColumnIndexOrThrow("M_Dest_Fecha_Bod"));
                int nBod = cursor.getInt(cursor.getColumnIndexOrThrow("M_Dest_Nbod"));

                if (fecha == null) fecha = "";

                listaDatos.add(new DestinoAdaptador.DestinoModelo(id, nombre, fecha, String.valueOf(nBod)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adaptador.notifyDataSetChanged();
    }
}
