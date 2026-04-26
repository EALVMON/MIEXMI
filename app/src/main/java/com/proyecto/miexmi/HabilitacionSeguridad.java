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

public class HabilitacionSeguridad extends AppCompatActivity {

    private AutoCompleteTextView etNombreHps;
    private TextInputEditText etFechaConcesion, etFechaCaducidad;
    private ExpedienteHelper dbHelper;
    private int idUsuarioActual;
    private int idHpsSeleccionada = -1;
    private HpsAdaptador adaptador;
    private List<HpsAdaptador.HpsModelo> listaDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hps);

        Utilidades.configurarBotonVolver(this, R.id.btnVolverHps);

        dbHelper = new ExpedienteHelper(this);
        idUsuarioActual = Utilidades.obtenerUsuarioActual(this);

        if (idUsuarioActual == -1) {
            Toast.makeText(this, "Error de sesión", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // ENLACES
        etNombreHps = findViewById(R.id.etNombreHps);
        etFechaConcesion = findViewById(R.id.etFechaConcesionHps);
        etFechaCaducidad = findViewById(R.id.etFechaCaducidadHps);

        Button btnAnadir = findViewById(R.id.btnAnadirHps);
        Button btnModificar = findViewById(R.id.btnModificarHps);
        Button btnEliminar = findViewById(R.id.btnEliminarHps);
        Button btnLimpiar = findViewById(R.id.btnLimpiarHps);
        RecyclerView rvHps = findViewById(R.id.rvHps);

        // --- CONFIGURACIÓN DE DESPLEGABLE Y CALENDARIOS ---
        // Llamamos a tu nueva función en Utilidades
        Utilidades.configurarDesplegableHPS(this, etNombreHps);

        // ¡Tenemos que activar el calendario para las DOS fechas!
        Utilidades.configurarCalendario(this, etFechaConcesion);
        Utilidades.configurarCalendario(this, etFechaCaducidad);

        // --- CONFIGURACIÓN DE LA LISTA ---
        rvHps.setLayoutManager(new LinearLayoutManager(this));
        listaDatos = new ArrayList<>();

        adaptador = new HpsAdaptador(listaDatos, hps -> {
            idHpsSeleccionada = hps.idHps;
            etNombreHps.setText(hps.nombre, false);
            etFechaConcesion.setText(hps.fechaConcesion);
            etFechaCaducidad.setText(hps.fechaCaducidad);
            Toast.makeText(HabilitacionSeguridad.this, "Seleccionado para editar", Toast.LENGTH_SHORT).show();
        });
        rvHps.setAdapter(adaptador);

        cargarLista();

        // --- BOTONES ---

        btnLimpiar.setOnClickListener(v -> limpiarFormulario());

        btnAnadir.setOnClickListener(v -> {
            String nombre = etNombreHps.getText() != null ? etNombreHps.getText().toString().trim() : "";
            String concesion = etFechaConcesion.getText() != null ? etFechaConcesion.getText().toString().trim() : "";
            String caducidad = etFechaCaducidad.getText() != null ? etFechaCaducidad.getText().toString().trim() : "";

            if (nombre.isEmpty()) {
                Toast.makeText(this, "La habilitación es obligatoria", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.anadirHps(idUsuarioActual, nombre, concesion, caducidad)) {
                Toast.makeText(this, "Guardado con éxito", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarLista();
            } else {
                Toast.makeText(this, "Error: Esa HPS ya está registrada", Toast.LENGTH_LONG).show();
            }
        });

        btnModificar.setOnClickListener(v -> {
            if (idHpsSeleccionada == -1) {
                Toast.makeText(this, "Selecciona una HPS de la lista", Toast.LENGTH_SHORT).show();
                return;
            }

            String nombre = etNombreHps.getText() != null ? etNombreHps.getText().toString().trim() : "";
            String concesion = etFechaConcesion.getText() != null ? etFechaConcesion.getText().toString().trim() : "";
            String caducidad = etFechaCaducidad.getText() != null ? etFechaCaducidad.getText().toString().trim() : "";

            if (nombre.isEmpty()) {
                Toast.makeText(this, "La habilitación es obligatoria", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.modificarHps(idHpsSeleccionada, nombre, concesion, caducidad)) {
                Toast.makeText(this, "Actualizado correctamente", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarLista();
            }
        });

        btnEliminar.setOnClickListener(v -> {
            if (idHpsSeleccionada == -1) {
                Toast.makeText(this, "Selecciona una HPS de la lista", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.eliminarHps(idHpsSeleccionada)) {
                Toast.makeText(this, "Borrado correctamente", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarLista();
            }
        });
    }

    private void limpiarFormulario() {
        etNombreHps.setText("", false);
        etFechaConcesion.setText("");
        etFechaCaducidad.setText("");
        idHpsSeleccionada = -1;
    }

    @android.annotation.SuppressLint("NotifyDataSetChanged")
    private void cargarLista() {
        listaDatos.clear();
        Cursor cursor = dbHelper.obtenerHps(idUsuarioActual);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("Id_M_Hps"));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("Nom_Habilitacion"));
                String concesion = cursor.getString(cursor.getColumnIndexOrThrow("Fecha_M_Concesion"));
                String caducidad = cursor.getString(cursor.getColumnIndexOrThrow("Fecha_M_Caducidad"));

                if (concesion == null) concesion = "";
                if (caducidad == null) caducidad = "";

                listaDatos.add(new HpsAdaptador.HpsModelo(id, nombre, concesion, caducidad));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adaptador.notifyDataSetChanged();
    }
}