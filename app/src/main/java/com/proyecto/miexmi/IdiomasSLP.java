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

public class IdiomasSLP extends AppCompatActivity {

    private AutoCompleteTextView etNombre;
    private TextInputEditText etResultado, etFecha, etBod;
    private ExpedienteHelper dbHelper;
    private int idUsuarioActual, idSeleccionado = -1;
    private IdiomaAdaptador adaptador;
    private List<IdiomaAdaptador.IdiomaModelo> listaDatos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idioma);

        dbHelper = new ExpedienteHelper(this);
        idUsuarioActual = Utilidades.obtenerUsuarioActual(this);

        etNombre = findViewById(R.id.etNombreIdioma);
        etResultado = findViewById(R.id.etResultadoIdioma);
        etFecha = findViewById(R.id.etFechaBodIdioma);
        etBod = findViewById(R.id.etNumBodIdioma);

        Utilidades.configurarBotonVolver(this, R.id.btnVolverIdioma);
        Utilidades.configurarCalendario(this, etFecha);
        Utilidades.configurarDesplegableIdiomas(this, etNombre);

        RecyclerView rv = findViewById(R.id.rvIdioma);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adaptador = new IdiomaAdaptador(listaDatos, item -> {
            idSeleccionado = item.id;
            etNombre.setText(item.nombre, false);
            etResultado.setText(item.resultado);
            etFecha.setText(item.fecha);
            etBod.setText(item.nbod);
        });
        rv.setAdapter(adaptador);

        findViewById(R.id.btnAnadirIdioma).setOnClickListener(v -> guardar(false));
        findViewById(R.id.btnModificarIdioma).setOnClickListener(v -> guardar(true));
        findViewById(R.id.btnEliminarIdioma).setOnClickListener(v -> eliminar());
        findViewById(R.id.btnLimpiarIdioma).setOnClickListener(v -> limpiar());

        cargarLista();
    }

    private void guardar(boolean esModificar) {
        String nom = etNombre.getText().toString().trim();
        String res = etResultado.getText() != null ? etResultado.getText().toString().trim() : "";
        String fec = etFecha.getText().toString().trim();
        String bod = etBod.getText().toString().trim();

        if (nom.isEmpty() || res.isEmpty()) {
            Toast.makeText(this, "Idioma y SLP son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        // EL PERRO GUARDIÁN (Regex): Comprueba formato tipo 3.3.3+.2
        // Explicación: [0-5]\+?\. significa un número del 0 al 5, un posible +, y un punto. Esto se repite 3 veces.
        // Y termina con [0-5]\+? que es el último dígito con su posible +.
        String patronSLP = "^([0-5]\\+?\\.){3}[0-5]\\+?$";
        if (!res.matches(patronSLP)) {
            Toast.makeText(this, "Formato SLP incorrecto. Usa puntos (Ej: 3.3.3+.2)", Toast.LENGTH_LONG).show();
            return;
        }

        boolean exito;
        if (esModificar && idSeleccionado != -1) {
            exito = dbHelper.modificarIdioma(idSeleccionado, nom, res, fec, bod);
        } else {
            exito = dbHelper.anadirIdioma(idUsuarioActual, nom, res, fec, bod);
        }

        if (exito) { cargarLista(); limpiar(); }
        else if (!esModificar) { Toast.makeText(this, "Este idioma ya está guardado", Toast.LENGTH_SHORT).show(); }
    }

    private void eliminar() {
        if (idSeleccionado != -1 && dbHelper.eliminarIdioma(idSeleccionado)) {
            cargarLista(); limpiar();
        }
    }

    private void limpiar() {
        etNombre.setText("", false); etResultado.setText(""); etFecha.setText(""); etBod.setText(""); idSeleccionado = -1;
    }

    private void cargarLista() {
        listaDatos.clear();
        Cursor c = dbHelper.obtenerIdiomas(idUsuarioActual);
        if (c.moveToFirst()) {
            do {
                listaDatos.add(new IdiomaAdaptador.IdiomaModelo(
                        c.getInt(c.getColumnIndexOrThrow("Id_M_Idi")),
                        c.getString(c.getColumnIndexOrThrow("Nom_idioma")),
                        c.getString(c.getColumnIndexOrThrow("Resultado")),
                        c.getString(c.getColumnIndexOrThrow("M_Idi_Fecha_Bod")),
                        String.valueOf(c.getInt(c.getColumnIndexOrThrow("M_Idi_Nbod")))
                ));
            } while (c.moveToNext());
        }
        c.close();
        adaptador.notifyDataSetChanged();
    }
}
