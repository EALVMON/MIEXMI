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

public class CursosMilitares extends AppCompatActivity {

    private AutoCompleteTextView etNombre;
    private TextInputEditText etFecha, etBod;
    private ExpedienteHelper dbHelper;
    private int idUsuarioActual, idSeleccionado = -1;
    private CursoMilitarAdaptador adaptador;
    private List<CursoMilitarAdaptador.CursoModelo> listaDatos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursos_militares);

        dbHelper = new ExpedienteHelper(this);
        idUsuarioActual = Utilidades.obtenerUsuarioActual(this);

        etNombre = findViewById(R.id.etNombreCurso);
        etFecha = findViewById(R.id.etFechaBodCurso);
        etBod = findViewById(R.id.etNumBodCurso);

        Utilidades.configurarBotonVolver(this, R.id.btnVolverCursos);
        Utilidades.configurarCalendario(this, etFecha);
        Utilidades.configurarDesplegableCursosMilitares(this, etNombre);

        RecyclerView rv = findViewById(R.id.rvCursos);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adaptador = new CursoMilitarAdaptador(listaDatos, item -> {
            idSeleccionado = item.id;
            // ¡Pasamos false para que no sobrescriba lo que el usuario ha tecleado si es distinto de la lista!
            etNombre.setText(item.nombre, false);
            etFecha.setText(item.fecha);
            etBod.setText(item.nbod);
        });
        rv.setAdapter(adaptador);

        findViewById(R.id.btnAnadirCurso).setOnClickListener(v -> guardar(false));
        findViewById(R.id.btnModificarCurso).setOnClickListener(v -> guardar(true));
        findViewById(R.id.btnEliminarCurso).setOnClickListener(v -> eliminar());
        findViewById(R.id.btnLimpiarCurso).setOnClickListener(v -> limpiar());

        cargarLista();
    }

    private void guardar(boolean esModificar) {
        String nom = etNombre.getText().toString().trim();
        String fec = etFecha.getText().toString().trim();
        String bod = etBod.getText().toString().trim();

        if (nom.isEmpty()) {
            Toast.makeText(this, "El nombre es obligatorio", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean exito;
        if (esModificar && idSeleccionado != -1) {
            exito = dbHelper.modificarCursoMilitar(idSeleccionado, nom, fec, bod);
        } else {
            exito = dbHelper.anadirCursoMilitar(idUsuarioActual, nom, fec, bod);
        }

        if (exito) { cargarLista(); limpiar(); }
    }

    private void eliminar() {
        if (idSeleccionado != -1 && dbHelper.eliminarCursoMilitar(idSeleccionado)) {
            cargarLista(); limpiar();
        }
    }

    private void limpiar() {
        etNombre.setText("", false); etFecha.setText(""); etBod.setText(""); idSeleccionado = -1;
    }

    private void cargarLista() {
        listaDatos.clear();
        Cursor c = dbHelper.obtenerCursosMilitares(idUsuarioActual);
        if (c.moveToFirst()) {
            do {
                listaDatos.add(new CursoMilitarAdaptador.CursoModelo(
                        c.getInt(c.getColumnIndexOrThrow("Id_M_Cmili")),
                        c.getString(c.getColumnIndexOrThrow("Nom_Cur_Mili")),
                        c.getString(c.getColumnIndexOrThrow("M_Cmili_Fecha_Bod")),
                        String.valueOf(c.getInt(c.getColumnIndexOrThrow("M_Cmili_Nbod")))
                ));
            } while (c.moveToNext());
        }
        c.close();
        adaptador.notifyDataSetChanged();
    }
}