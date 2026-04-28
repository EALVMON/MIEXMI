package com.proyecto.miexmi;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LogActividad extends AppCompatActivity {

    private ExpedienteHelper dbHelper;
    private int idUsuarioActual;
    private LogAdaptador adaptador;
    private final List<LogAdaptador.LogModelo> listaDatos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_actividad);

        Utilidades.configurarBotonVolver(this, R.id.btnVolverLog);

        dbHelper = new ExpedienteHelper(this);
        idUsuarioActual = Utilidades.obtenerUsuarioActual(this);

        if (idUsuarioActual == -1) {
            Toast.makeText(this, "Error de sesión", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Configuramos la lista
        RecyclerView rv = findViewById(R.id.rvLogActividad);
        rv.setLayoutManager(new LinearLayoutManager(this));

        // El adaptador no necesita el "listener" (clic) porque no editamos los logs
        adaptador = new LogAdaptador(listaDatos);
        rv.setAdapter(adaptador);

        cargarLista();
    }
    @android.annotation.SuppressLint("NotifyDataSetChanged")
    private void cargarLista() {
        listaDatos.clear();
        Cursor c = dbHelper.obtenerAccesos(idUsuarioActual);

        if (c.moveToFirst()) {
            do {
                listaDatos.add(new LogAdaptador.LogModelo(
                        c.getString(c.getColumnIndexOrThrow("DNI")),
                        c.getString(c.getColumnIndexOrThrow("Fecha_Hora"))
                ));
            } while (c.moveToNext());
        }
        c.close();
        adaptador.notifyDataSetChanged();
    }
}