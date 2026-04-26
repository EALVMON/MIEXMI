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

public class Recompensas extends AppCompatActivity {

    private AutoCompleteTextView etNombre;
    private TextInputEditText etFecha, etBod;
    private ExpedienteHelper dbHelper;
    private int idUsuarioActual, idSeleccionado = -1;
    private RecompensasAdaptador adaptador;
    private List<RecompensasAdaptador.RecompensaModelo> listaDatos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recompensas);

        dbHelper = new ExpedienteHelper(this);
        idUsuarioActual = Utilidades.obtenerUsuarioActual(this);

        etNombre = findViewById(R.id.etNombreRecompensa);
        etFecha = findViewById(R.id.etFechaBodRecompensa);
        etBod = findViewById(R.id.etNumBodRecompensa);

        Utilidades.configurarBotonVolver(this, R.id.btnVolverRecompensas);
        Utilidades.configurarCalendario(this, etFecha);
        Utilidades.configurarDesplegableRecompensas(this, etNombre);

        RecyclerView rv = findViewById(R.id.rvRecompensas);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adaptador = new RecompensasAdaptador(listaDatos, item -> {
            idSeleccionado = item.id;
            etNombre.setText(item.nombre, false);
            etFecha.setText(item.fecha);
            etBod.setText(item.nbod);
        });
        rv.setAdapter(adaptador);

        findViewById(R.id.btnAnadirRecompensa).setOnClickListener(v -> guardar(false));
        findViewById(R.id.btnModificarRecompensa).setOnClickListener(v -> guardar(true));
        findViewById(R.id.btnEliminarRecompensa).setOnClickListener(v -> eliminar());
        findViewById(R.id.btnLimpiarRecompensa).setOnClickListener(v -> limpiar());

        cargarLista();
    }

    private void guardar(boolean esModificar) {
        String nom = etNombre.getText().toString();
        String fec = etFecha.getText().toString();
        String bod = etBod.getText().toString();

        if (nom.isEmpty()) return;

        boolean exito;
        if (esModificar && idSeleccionado != -1) {
            exito = dbHelper.modificarRecompensa(idSeleccionado, nom, fec, bod);
        } else {
            exito = dbHelper.anadirRecompensa(idUsuarioActual, nom, fec, bod);
        }

        if (exito) { cargarLista(); limpiar(); }
    }

    private void eliminar() {
        if (idSeleccionado != -1 && dbHelper.eliminarRecompensa(idSeleccionado)) {
            cargarLista(); limpiar();
        }
    }

    private void limpiar() {
        etNombre.setText("", false); etFecha.setText(""); etBod.setText(""); idSeleccionado = -1;
    }

    private void cargarLista() {
        listaDatos.clear();
        Cursor c = dbHelper.obtenerRecompensas(idUsuarioActual);
        if (c.moveToFirst()) {
            do {
                listaDatos.add(new RecompensasAdaptador.RecompensaModelo(
                        c.getInt(c.getColumnIndexOrThrow("Id_M_Reco")),
                        c.getString(c.getColumnIndexOrThrow("Nom_Recompensa")),
                        c.getString(c.getColumnIndexOrThrow("M_Reco_Fecha_Bod")),
                        String.valueOf(c.getInt(c.getColumnIndexOrThrow("M_Reco_Nbod")))
                ));
            } while (c.moveToNext());
        }
        c.close();
        adaptador.notifyDataSetChanged();
    }
}
