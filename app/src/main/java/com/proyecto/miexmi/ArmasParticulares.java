package com.proyecto.miexmi;

import android.database.Cursor;
import android.os.Bundle;
import android.text.InputFilter;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.List;

public class ArmasParticulares extends AppCompatActivity {

    private TextInputEditText etNombre, etNumSerie, etFecha;
    private ExpedienteHelper dbHelper;
    private int idUsuarioActual, idSeleccionado = -1;
    private ArmaAdaptador adaptador;
    private List<ArmaAdaptador.ArmaModelo> listaDatos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_armas_particulares);

        dbHelper = new ExpedienteHelper(this);
        idUsuarioActual = Utilidades.obtenerUsuarioActual(this);

        etNombre = findViewById(R.id.etNombreArma);
        etNumSerie = findViewById(R.id.etNumeroSerieArma);
        etFecha = findViewById(R.id.etFechaCaducidadArma);

        // Forzamos mayúsculas para el número de serie
        etNumSerie.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        Utilidades.configurarBotonVolver(this, R.id.btnVolverArmas);
        Utilidades.configurarCalendario(this, etFecha);

        RecyclerView rv = findViewById(R.id.rvArmas);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adaptador = new ArmaAdaptador(listaDatos, item -> {
            idSeleccionado = item.id;
            etNombre.setText(item.nombre);
            etNumSerie.setText(item.numSerie);
            etFecha.setText(item.fecha);
        });
        rv.setAdapter(adaptador);

        findViewById(R.id.btnAnadirArma).setOnClickListener(v -> guardar(false));
        findViewById(R.id.btnModificarArma).setOnClickListener(v -> guardar(true));
        findViewById(R.id.btnEliminarArma).setOnClickListener(v -> eliminar());
        findViewById(R.id.btnLimpiarArma).setOnClickListener(v -> limpiar());

        cargarLista();
    }

    private void guardar(boolean esModificar) {
        String nom = etNombre.getText() != null ? etNombre.getText().toString().trim() : "";
        String serie = etNumSerie.getText() != null ? etNumSerie.getText().toString().trim().toUpperCase() : "";
        String fec = etFecha.getText() != null ? etFecha.getText().toString().trim() : "";

        if (nom.isEmpty() || serie.isEmpty()) {
            Toast.makeText(this, "El nombre y el número de serie son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean exito;
        if (esModificar && idSeleccionado != -1) {
            exito = dbHelper.modificarArma(idSeleccionado, nom, serie, fec);
        } else {
            exito = dbHelper.anadirArma(idUsuarioActual, nom, serie, fec);
        }

        if (exito) {
            cargarLista();
            limpiar();
        } else {
            Toast.makeText(this, "Error: El número de serie ya está registrado", Toast.LENGTH_LONG).show();
        }
    }

    private void eliminar() {
        if (idSeleccionado != -1 && dbHelper.eliminarArma(idSeleccionado)) {
            cargarLista();
            limpiar();
        }
    }

    private void limpiar() {
        etNombre.setText("");
        etNumSerie.setText("");
        etFecha.setText("");
        idSeleccionado = -1;
    }

    private void cargarLista() {
        listaDatos.clear();
        Cursor c = dbHelper.obtenerArmas(idUsuarioActual);
        if (c.moveToFirst()) {
            do {
                listaDatos.add(new ArmaAdaptador.ArmaModelo(
                        c.getInt(c.getColumnIndexOrThrow("Id_M_EArm")),
                        c.getString(c.getColumnIndexOrThrow("Nom_Arma")),
                        c.getString(c.getColumnIndexOrThrow("M_EArm_Nserie")),
                        c.getString(c.getColumnIndexOrThrow("M_EArm_Fecha_Cad"))
                ));
            } while (c.moveToNext());
        }
        c.close();
        adaptador.notifyDataSetChanged();
    }
}