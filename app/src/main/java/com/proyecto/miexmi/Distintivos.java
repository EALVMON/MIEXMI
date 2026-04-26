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

public class Distintivos extends AppCompatActivity {

    private AutoCompleteTextView etNombre;
    private TextInputEditText etFecha, etBod;
    private ExpedienteHelper dbHelper;
    private int idUsuarioActual, idSeleccionado = -1;
    private DistintivoAdaptador adaptador;
    private List<DistintivoAdaptador.DistintivoModelo> listaDatos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distintivos);

        dbHelper = new ExpedienteHelper(this);
        idUsuarioActual = Utilidades.obtenerUsuarioActual(this);

        etNombre = findViewById(R.id.etNombreDistintivo);
        etFecha = findViewById(R.id.etFechaBodDistintivo);
        etBod = findViewById(R.id.etNumBodDistintivo);

        Utilidades.configurarBotonVolver(this, R.id.btnVolverDistintivos);
        Utilidades.configurarCalendario(this, etFecha);
        Utilidades.configurarDesplegableDistintivos(this, etNombre); // ¡Conectamos con la lista!

        RecyclerView rv = findViewById(R.id.rvDistintivos);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adaptador = new DistintivoAdaptador(listaDatos, item -> {
            idSeleccionado = item.id;
            etNombre.setText(item.nombre, false);
            etFecha.setText(item.fecha);
            etBod.setText(item.nbod);
        });
        rv.setAdapter(adaptador);

        findViewById(R.id.btnAnadirDistintivo).setOnClickListener(v -> guardar(false));
        findViewById(R.id.btnModificarDistintivo).setOnClickListener(v -> guardar(true));
        findViewById(R.id.btnEliminarDistintivo).setOnClickListener(v -> eliminar());
        findViewById(R.id.btnLimpiarDistintivo).setOnClickListener(v -> limpiar());

        cargarLista();
    }

    private void guardar(boolean esModificar) {
        String nom = etNombre.getText().toString();
        String fec = etFecha.getText().toString();
        String bod = etBod.getText().toString();

        if (nom.isEmpty()) {
            Toast.makeText(this, "El nombre es obligatorio", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean exito;
        if (esModificar && idSeleccionado != -1) {
            exito = dbHelper.modificarDistintivo(idSeleccionado, nom, fec, bod);
        } else {
            exito = dbHelper.anadirDistintivo(idUsuarioActual, nom, fec, bod);
        }

        if (exito) { cargarLista(); limpiar(); }
    }

    private void eliminar() {
        if (idSeleccionado != -1 && dbHelper.eliminarDistintivo(idSeleccionado)) {
            cargarLista(); limpiar();
        }
    }

    private void limpiar() {
        etNombre.setText("", false); etFecha.setText(""); etBod.setText(""); idSeleccionado = -1;
    }

    private void cargarLista() {
        listaDatos.clear();
        Cursor c = dbHelper.obtenerDistintivos(idUsuarioActual);
        if (c.moveToFirst()) {
            do {
                listaDatos.add(new DistintivoAdaptador.DistintivoModelo(
                        c.getInt(c.getColumnIndexOrThrow("Id_M_Dist")),
                        c.getString(c.getColumnIndexOrThrow("Nom_Distintivo")),
                        c.getString(c.getColumnIndexOrThrow("M_Dist_Fecha_Bod")),
                        String.valueOf(c.getInt(c.getColumnIndexOrThrow("M_Dist_Nbod")))
                ));
            } while (c.moveToNext());
        }
        c.close();
        adaptador.notifyDataSetChanged();
    }
}