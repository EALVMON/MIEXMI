package com.proyecto.miexmi;

import android.annotation.SuppressLint;
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

public class TarjetaMilitar extends AppCompatActivity {

    // 1. Campos de la clase (Solo los que se usan en varios métodos)
    private TextInputEditText etNumeroTmi, etFechaCaducidad;
    private ExpedienteHelper dbHelper;
    private int idUsuarioActual;
    private int idTmiSeleccionada = -1;
    private TmiAdaptador adaptador;
    private List<TmiAdaptador.TmiModelo> listaDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmi);

        // Configuramos el botón volver
        Utilidades.configurarBotonVolver(this, R.id.btnVolverTmi);

        // Inicializamos base de datos y sesión
        dbHelper = new ExpedienteHelper(this);

        // RECUPERAMOS LA SESIÓN usando la función que nos hemos creado en Utilidades.java
        idUsuarioActual = Utilidades.obtenerUsuarioActual(this);
        if (idUsuarioActual == -1) {
            Toast.makeText(this, "Error de sesión", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // --- ENLAZAMOS VARIABLES ---
        etNumeroTmi = findViewById(R.id.etNumeroTmi);
        etFechaCaducidad = findViewById(R.id.etFechaCaducidadTmi);

        // Variables locales (Para quitar el Warning de Android Studio)
        Button btnAnadir = findViewById(R.id.btnAnadirTmi);
        Button btnModificar = findViewById(R.id.btnModificarTmi);
        Button btnEliminar = findViewById(R.id.btnEliminarTmi);
        Button btnLimpiar = findViewById(R.id.btnLimpiarTmi);
        RecyclerView rvTmi = findViewById(R.id.rvTmi);

        // Calendario automático
        Utilidades.configurarCalendario(this, etFechaCaducidad);

        // --- CONFIGURACIÓN DE LA LISTA Y EL ADAPTADOR ---
        rvTmi.setLayoutManager(new LinearLayoutManager(this));
        listaDatos = new ArrayList<>();

        adaptador = new TmiAdaptador(listaDatos, tmi -> {
            idTmiSeleccionada = tmi.idTmi;
            etNumeroTmi.setText(tmi.numeroTarjeta);
            etFechaCaducidad.setText(tmi.fechaCaducidad);
            Toast.makeText(TarjetaMilitar.this, "Tarjeta seleccionada", Toast.LENGTH_SHORT).show();
        });
        rvTmi.setAdapter(adaptador);

        cargarLista();

        // --- ACCIONES DE LOS BOTONES ---

        btnLimpiar.setOnClickListener(v -> limpiarFormulario());

        btnAnadir.setOnClickListener(v -> {
            // Recogemos los datos y forzamos MAYÚSCULAS
            String numTarjeta = etNumeroTmi.getText() != null ? etNumeroTmi.getText().toString().trim().toUpperCase() : "";
            String fecha = etFechaCaducidad.getText() != null ? etFechaCaducidad.getText().toString().trim() : "";

            if (numTarjeta.isEmpty()) {
                Toast.makeText(this, "Introduce el número de tarjeta", Toast.LENGTH_SHORT).show();
                return;
            }

            // Intentamos guardar
            if (dbHelper.anadirTMI(idUsuarioActual, numTarjeta, fecha)) {
                Toast.makeText(this, "Guardado con éxito", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarLista();
            } else {
                // Si devuelve false, es porque ha saltado nuestra protección anti-duplicados (o un error )
                Toast.makeText(this, "Error: Esta TMI ya está registrada", Toast.LENGTH_LONG).show();
            }
        });

        btnModificar.setOnClickListener(v -> {
            if (idTmiSeleccionada == -1) {
                Toast.makeText(this, "Selecciona una tarjeta de la lista", Toast.LENGTH_SHORT).show();
                return;
            }

            String numTarjeta = etNumeroTmi.getText() != null ? etNumeroTmi.getText().toString().trim().toUpperCase() : "";
            String fecha = etFechaCaducidad.getText() != null ? etFechaCaducidad.getText().toString().trim() : "";

            if (dbHelper.modificarTMI(idTmiSeleccionada, numTarjeta, fecha)) {
                Toast.makeText(this, "Actualizado correctamente", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarLista();
            }
        });

        btnEliminar.setOnClickListener(v -> {
            if (idTmiSeleccionada == -1) {
                Toast.makeText(this, "Selecciona una tarjeta de la lista", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.eliminarTMI(idTmiSeleccionada)) {
                Toast.makeText(this, "Borrado correctamente", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarLista();
            }
        });
    }

    private void limpiarFormulario() {
        etNumeroTmi.setText("");
        etFechaCaducidad.setText("");
        idTmiSeleccionada = -1;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void cargarLista() {
        listaDatos.clear();
        Cursor cursor = dbHelper.obtenerTMIs(idUsuarioActual);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("Id_M_Tmi"));
                String numero = cursor.getString(cursor.getColumnIndexOrThrow("N_Tarjeta"));
                String caducidad = cursor.getString(cursor.getColumnIndexOrThrow("M_Tmi_Fecha_Cadu"));

                if (caducidad == null) caducidad = "";

                listaDatos.add(new TmiAdaptador.TmiModelo(id, numero, caducidad));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adaptador.notifyDataSetChanged();
    }
}
