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

public class CarnetConducir extends AppCompatActivity {

    private AutoCompleteTextView etTipo;
    private TextInputEditText etFechaConcesion, etFechaCaducidad;
    private ExpedienteHelper dbHelper;
    private int idUsuarioActual, idSeleccionado = -1;
    private CarnetAdaptador adaptador;
    private List<CarnetAdaptador.CarnetModelo> listaDatos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carnet);

        dbHelper = new ExpedienteHelper(this);
        idUsuarioActual = Utilidades.obtenerUsuarioActual(this);

        etTipo = findViewById(R.id.etTipoCarnet);
        etFechaConcesion = findViewById(R.id.etFechaConcesionCarnet);
        etFechaCaducidad = findViewById(R.id.etFechaCaducidadCarnet);

        Utilidades.configurarBotonVolver(this, R.id.btnVolverCarnet);
        Utilidades.configurarDesplegableCarnets(this, etTipo);

        Utilidades.configurarCalendario(this, etFechaConcesion);
        Utilidades.configurarCalendario(this, etFechaCaducidad);

        RecyclerView rv = findViewById(R.id.rvCarnet);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adaptador = new CarnetAdaptador(listaDatos, item -> {
            idSeleccionado = item.id;
            etTipo.setText(item.tipo, false);
            etFechaConcesion.setText(item.fechaConcesion);
            etFechaCaducidad.setText(item.fechaCaducidad);
        });
        rv.setAdapter(adaptador);

        findViewById(R.id.btnAnadirCarnet).setOnClickListener(v -> guardar(false));
        findViewById(R.id.btnModificarCarnet).setOnClickListener(v -> guardar(true));
        findViewById(R.id.btnEliminarCarnet).setOnClickListener(v -> eliminar());
        findViewById(R.id.btnLimpiarCarnet).setOnClickListener(v -> limpiar());

        cargarLista();
    }

    private void guardar(boolean esModificar) {
        String tipo = etTipo.getText().toString().trim();
        String fecCon = etFechaConcesion.getText() != null ? etFechaConcesion.getText().toString().trim() : "";
        String fecCad = etFechaCaducidad.getText() != null ? etFechaCaducidad.getText().toString().trim() : "";

        if (tipo.isEmpty()) {
            Toast.makeText(this, "Selecciona una clase de carnet", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean exito;
        if (esModificar && idSeleccionado != -1) {
            exito = dbHelper.modificarCarnet(idSeleccionado, tipo, fecCon, fecCad);
        } else {
            exito = dbHelper.anadirCarnet(idUsuarioActual, tipo, fecCon, fecCad);
        }

        if (exito) {
            cargarLista();
            limpiar();
        } else if (!esModificar) {
            Toast.makeText(this, "Esta clase de carnet ya está registrada", Toast.LENGTH_LONG).show();
        }
    }

    private void eliminar() {
        if (idSeleccionado != -1 && dbHelper.eliminarCarnet(idSeleccionado)) {
            cargarLista();
            limpiar();
        }
    }

    private void limpiar() {
        etTipo.setText("", false);
        etFechaConcesion.setText("");
        etFechaCaducidad.setText("");
        idSeleccionado = -1;
    }

    private void cargarLista() {
        listaDatos.clear();
        Cursor c = dbHelper.obtenerCarnets(idUsuarioActual);
        if (c.moveToFirst()) {
            do {
                listaDatos.add(new CarnetAdaptador.CarnetModelo(
                        c.getInt(c.getColumnIndexOrThrow("Id_M_Carnet")),
                        c.getString(c.getColumnIndexOrThrow("Tipo_Carnet")),
                        c.getString(c.getColumnIndexOrThrow("M_Carn_Fecha_Concesion")),
                        c.getString(c.getColumnIndexOrThrow("M_Carn_Fecha_Caducidad"))
                ));
            } while (c.moveToNext());
        }
        c.close();
        adaptador.notifyDataSetChanged();
    }
}