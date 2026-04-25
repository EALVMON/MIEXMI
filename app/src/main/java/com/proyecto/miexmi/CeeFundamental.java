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

public class CeeFundamental extends AppCompatActivity {

    // Campos de la clase
    private TextInputEditText etNombreCEEF, etFechaBodCEEF, etNumBodCEEF;
    private ExpedienteHelper dbHelper;
    private int idUsuarioActual;
    private int idCeefSeleccionado = -1;
    private CeefAdaptador adaptador;
    private List<CeefAdaptador.CeefModelo> listaDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cee_fundamental);

        // Configuramos el botón volver
        Utilidades.configurarBotonVolver(this, R.id.btnVolverCEEF);

        // Inicializamos base de datos y sesión
        dbHelper = new ExpedienteHelper(this);

        // RECUPERAMOS LA SESIÓN usando la función que nos hemos creado en Utilidades.java
        idUsuarioActual = Utilidades.obtenerUsuarioActual(this);
        if (idUsuarioActual == -1) {
            Toast.makeText(this, "Error de sesión", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // --- ENLAZAMOS VARIABLES CON EL XML ---
        etNombreCEEF = findViewById(R.id.etNombreCEEF);
        etFechaBodCEEF = findViewById(R.id.etFechaBodCEEF);
        etNumBodCEEF = findViewById(R.id.etNumBodCEEF);

        Button btnAnadir = findViewById(R.id.btnAnadirCEEF);
        Button btnModificar = findViewById(R.id.btnModificarCEEF);
        Button btnEliminar = findViewById(R.id.btnEliminarCEEF);
        Button btnLimpiar = findViewById(R.id.btnLimpiarCEEF);
        RecyclerView rvCEEF = findViewById(R.id.rvCEEF);

        // Activamos el calendario maestro
        Utilidades.configurarCalendario(this, etFechaBodCEEF);

        // --- CONFIGURACIÓN DE LA LISTA Y EL ADAPTADOR ---
        rvCEEF.setLayoutManager(new LinearLayoutManager(this));
        listaDatos = new ArrayList<>();


        adaptador = new CeefAdaptador(listaDatos, ceef -> {
            idCeefSeleccionado = ceef.idCeef;
            etNombreCEEF.setText(ceef.nombre);
            etFechaBodCEEF.setText(ceef.fechaBod);
            etNumBodCEEF.setText(ceef.numBod.equals("0") ? "" : ceef.numBod); // Ocultamos el 0 visualmente
            Toast.makeText(CeeFundamental.this, "Seleccionado para editar", Toast.LENGTH_SHORT).show();
        });
        rvCEEF.setAdapter(adaptador);

        // Cargamos los datos por primera vez
        cargarLista();

        // ==========================================
        // --- ACCIONES DE LOS BOTONES            ---
        // ==========================================

        btnLimpiar.setOnClickListener(v -> limpiarFormulario());

        btnAnadir.setOnClickListener(v -> {
            String nombre = etNombreCEEF.getText() != null ? etNombreCEEF.getText().toString().trim() : "";
            String fecha = etFechaBodCEEF.getText() != null ? etFechaBodCEEF.getText().toString().trim() : "";
            String numBod = etNumBodCEEF.getText() != null ? etNumBodCEEF.getText().toString().trim() : "";

            if (nombre.isEmpty()) {
                Toast.makeText(this, "El nombre de la especialidad es obligatorio", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.anadirCEEF(idUsuarioActual, nombre, fecha, numBod)) {
                Toast.makeText(this, "Guardado con éxito", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarLista();
            } else {
                Toast.makeText(this, "Error: Esa especialidad ya está registrada", Toast.LENGTH_LONG).show();
            }
        });

        btnModificar.setOnClickListener(v -> {
            if (idCeefSeleccionado == -1) {
                Toast.makeText(this, "Selecciona una especialidad de la lista", Toast.LENGTH_SHORT).show();
                return;
            }

            String nombre = etNombreCEEF.getText() != null ? etNombreCEEF.getText().toString().trim() : "";
            String fecha = etFechaBodCEEF.getText() != null ? etFechaBodCEEF.getText().toString().trim() : "";
            String numBod = etNumBodCEEF.getText() != null ? etNumBodCEEF.getText().toString().trim() : "";

            if (nombre.isEmpty()) {
                Toast.makeText(this, "El nombre de la especialidad es obligatorio", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.modificarCEEF(idCeefSeleccionado, nombre, fecha, numBod)) {
                Toast.makeText(this, "Actualizado correctamente", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarLista();
            }
        });

        btnEliminar.setOnClickListener(v -> {
            if (idCeefSeleccionado == -1) {
                Toast.makeText(this, "Selecciona una especialidad de la lista", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.eliminarCEEF(idCeefSeleccionado)) {
                Toast.makeText(this, "Borrado correctamente", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarLista();
            }
        });
    }

      private void limpiarFormulario() {
        etNombreCEEF.setText("");
        etFechaBodCEEF.setText("");
        etNumBodCEEF.setText("");
        idCeefSeleccionado = -1;
    }

    @android.annotation.SuppressLint("NotifyDataSetChanged")
    private void cargarLista() {
        listaDatos.clear();
        Cursor cursor = dbHelper.obtenerCEEFs(idUsuarioActual);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("Id_M_CEEF"));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("Nom_CEEF"));
                String fecha = cursor.getString(cursor.getColumnIndexOrThrow("M_Ceef_Fecha_Bod"));
                int nBod = cursor.getInt(cursor.getColumnIndexOrThrow("M_Ceef_Nbod"));

                if (fecha == null) fecha = "";

                // Añadimos el objeto a la lista
                listaDatos.add(new CeefAdaptador.CeefModelo(id, nombre, fecha, String.valueOf(nBod)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adaptador.notifyDataSetChanged();
    }
}