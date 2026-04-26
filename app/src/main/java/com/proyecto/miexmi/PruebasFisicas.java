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

public class PruebasFisicas extends AppCompatActivity {

    private TextInputEditText etPuntuacion, etFecha;
    private AutoCompleteTextView etApto;
    private ExpedienteHelper dbHelper;
    private int idUsuarioActual, idSeleccionado = -1;
    private TcgfAdaptador adaptador;
    private List<TcgfAdaptador.TcgfModelo> listaDatos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcgf);

        dbHelper = new ExpedienteHelper(this);
        idUsuarioActual = Utilidades.obtenerUsuarioActual(this);

        etPuntuacion = findViewById(R.id.etPuntuacionTcgf);
        etFecha = findViewById(R.id.etFechaTcgf);
        etApto = findViewById(R.id.etAptoTcgf);

        Utilidades.configurarBotonVolver(this, R.id.btnVolverTcgf);
        Utilidades.configurarCalendario(this, etFecha);
        Utilidades.configurarDesplegableAptoTcgf(this, etApto);

        RecyclerView rv = findViewById(R.id.rvTcgf);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adaptador = new TcgfAdaptador(listaDatos, item -> {
            idSeleccionado = item.id;
            etFecha.setText(item.fecha);
            etPuntuacion.setText(item.puntuacion);
            etApto.setText(item.apto, false);
        });
        rv.setAdapter(adaptador);

        findViewById(R.id.btnAnadirTcgf).setOnClickListener(v -> guardar(false));
        findViewById(R.id.btnModificarTcgf).setOnClickListener(v -> guardar(true));
        findViewById(R.id.btnEliminarTcgf).setOnClickListener(v -> eliminar());
        findViewById(R.id.btnLimpiarTcgf).setOnClickListener(v -> limpiar());

        cargarLista();
    }

    private void guardar(boolean esModificar) {
        String fec = etFecha.getText() != null ? etFecha.getText().toString().trim() : "";
        String pun = etPuntuacion.getText() != null ? etPuntuacion.getText().toString().trim() : "";
        String apto = etApto.getText().toString().trim();

        if (fec.isEmpty() || apto.isEmpty()) {
            Toast.makeText(this, "La fecha y el resultado son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean exito;
        if (esModificar && idSeleccionado != -1) {
            exito = dbHelper.modificarTcgf(idSeleccionado, fec, pun, apto);
        } else {
            exito = dbHelper.anadirTcgf(idUsuarioActual, fec, pun, apto);
        }

        if (exito) {
            cargarLista();
            limpiar();
        } else if (!esModificar) {
            Toast.makeText(this, "Ya hay unas pruebas físicas registradas en esa fecha", Toast.LENGTH_LONG).show();
        }
    }

    private void eliminar() {
        if (idSeleccionado != -1 && dbHelper.eliminarTcgf(idSeleccionado)) {
            cargarLista();
            limpiar();
        }
    }

    private void limpiar() {
        etFecha.setText("");
        etPuntuacion.setText("");
        etApto.setText("", false);
        idSeleccionado = -1;
    }

    private void cargarLista() {
        listaDatos.clear();
        Cursor c = dbHelper.obtenerTcgf(idUsuarioActual);
        if (c.moveToFirst()) {
            do {
                listaDatos.add(new TcgfAdaptador.TcgfModelo(
                        c.getInt(c.getColumnIndexOrThrow("Id_M_TCGF")),
                        c.getString(c.getColumnIndexOrThrow("M_Tcgf_Fecha")),
                        c.getString(c.getColumnIndexOrThrow("M_Tcgf_Puntuacion")),
                        c.getString(c.getColumnIndexOrThrow("M_Tcgf_Apto"))
                ));
            } while (c.moveToNext());
        }
        c.close();
        adaptador.notifyDataSetChanged();
    }
}