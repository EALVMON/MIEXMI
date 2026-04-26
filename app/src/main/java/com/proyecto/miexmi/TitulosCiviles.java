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

public class TitulosCiviles extends AppCompatActivity {

    private TextInputEditText etNombreTitulo;
    private ExpedienteHelper dbHelper;
    private int idUsuarioActual;
    private int idTituloSeleccionado = -1;
    private TituloCivilAdaptador adaptador;
    private List<TituloCivilAdaptador.TituloModelo> listaDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_titulos_civiles);

        Utilidades.configurarBotonVolver(this, R.id.btnVolverTitulos);

        dbHelper = new ExpedienteHelper(this);
        idUsuarioActual = Utilidades.obtenerUsuarioActual(this);

        if (idUsuarioActual == -1) {
            Toast.makeText(this, "Error de sesión", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etNombreTitulo = findViewById(R.id.etNombreTituloCivil);

        Button btnAnadir = findViewById(R.id.btnAnadirTitulo);
        Button btnModificar = findViewById(R.id.btnModificarTitulo);
        Button btnEliminar = findViewById(R.id.btnEliminarTitulo);
        Button btnLimpiar = findViewById(R.id.btnLimpiarTitulo);
        RecyclerView rvTitulos = findViewById(R.id.rvTitulosCiviles);

        // --- CONFIGURACIÓN DE LA LISTA ---
        rvTitulos.setLayoutManager(new LinearLayoutManager(this));
        listaDatos = new ArrayList<>();

        adaptador = new TituloCivilAdaptador(listaDatos, titulo -> {
            idTituloSeleccionado = titulo.idTitulo;
            etNombreTitulo.setText(titulo.nombre);
            Toast.makeText(TitulosCiviles.this, "Seleccionado para editar", Toast.LENGTH_SHORT).show();
        });
        rvTitulos.setAdapter(adaptador);

        cargarLista();

        // --- BOTONES ---

        btnLimpiar.setOnClickListener(v -> limpiarFormulario());

        btnAnadir.setOnClickListener(v -> {
            String nombre = etNombreTitulo.getText() != null ? etNombreTitulo.getText().toString().trim() : "";

            if (nombre.isEmpty()) {
                Toast.makeText(this, "El nombre del título es obligatorio", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.anadirTituloCivil(idUsuarioActual, nombre)) {
                Toast.makeText(this, "Guardado con éxito", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarLista();
            } else {
                Toast.makeText(this, "Error: Ese título ya está registrado", Toast.LENGTH_LONG).show();
            }
        });

        btnModificar.setOnClickListener(v -> {
            if (idTituloSeleccionado == -1) {
                Toast.makeText(this, "Selecciona un título de la lista", Toast.LENGTH_SHORT).show();
                return;
            }

            String nombre = etNombreTitulo.getText() != null ? etNombreTitulo.getText().toString().trim() : "";

            if (nombre.isEmpty()) {
                Toast.makeText(this, "El nombre del título es obligatorio", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.modificarTituloCivil(idTituloSeleccionado, nombre)) {
                Toast.makeText(this, "Actualizado correctamente", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarLista();
            }
        });

        btnEliminar.setOnClickListener(v -> {
            if (idTituloSeleccionado == -1) {
                Toast.makeText(this, "Selecciona un título de la lista", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.eliminarTituloCivil(idTituloSeleccionado)) {
                Toast.makeText(this, "Borrado correctamente", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarLista();
            }
        });
    }

    private void limpiarFormulario() {
        etNombreTitulo.setText("");
        idTituloSeleccionado = -1;
    }

    @android.annotation.SuppressLint("NotifyDataSetChanged")
    private void cargarLista() {
        listaDatos.clear();
        Cursor cursor = dbHelper.obtenerTitulosCiviles(idUsuarioActual);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("Id_M_Tcivi"));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("Nom_Titulo"));

                listaDatos.add(new TituloCivilAdaptador.TituloModelo(id, nombre));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adaptador.notifyDataSetChanged();
    }
}
