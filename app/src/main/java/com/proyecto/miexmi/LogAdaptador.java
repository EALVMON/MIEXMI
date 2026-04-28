package com.proyecto.miexmi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;


public class LogAdaptador extends RecyclerView.Adapter<LogAdaptador.LogViewHolder> {

    // 1. EL MODELO DE DATOS

    public static class LogModelo {
        String dni, fechaHora;

        // El constructor: se ejecuta cuando creamos un log
        public LogModelo(String dni, String fechaHora) {
            this.dni = dni;
            this.fechaHora = fechaHora;
        }
    }

    // Esta es la lista que contiene todos los registros del log
    private final List<LogModelo> lista;

    // 2. EL CONSTRUCTOR DEL ADAPTADOR
    // Cuando creamos este adaptador desde LogActividad.java, le pasamos la lista de datos.
    public LogAdaptador(List<LogModelo> lista) {
        this.lista = lista;
    }

    // 3. ON CREATE VIEW HOLDER (Crear la vista de la fila)
    // Este metodo se ejecuta cuando la lista necesita dibujar una fila NUEVA en la pantalla.
    // Coge el archivo XML (item_log_actividad.xml) y lo convierte en código visual.
    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_log_actividad, parent, false);
        return new LogViewHolder(v); // Le pasa esa fila vacía al ViewHolder para que encuentre los TextViews
    }

    // 4. ON BIND VIEW HOLDER (Para unir los datos con la vista)
    //  Se ejecuta por cada fila que aparece en pantalla.
    // Coge los datos de la lista y los escribe en los TextViews de la fila.
    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {
        // Sacamos de la lista los datos correspondiente a esta posición
        LogModelo m = lista.get(position);

        // Escribimos los datos en la pantalla
        // Le sumamos 1 a la posición para crear el "Número de fila" (1, 2, 3...)
        // ya que en programación las listas siempre empiezan a contar desde 0.
        holder.tvNum.setText(String.valueOf(position + 1));
        holder.tvDni.setText(m.dni);
        holder.tvFec.setText(m.fechaHora);
    }

    // 5. GET ITEM COUNT (Contar los elementos)
    // Para saber cunatas lineas tiene que dibujar

    @Override
    public int getItemCount() {
        return lista.size();
    }

    // 6. VIEW HOLDER
    // Busca un elemento en pantalla usando 'findViewById'
    // los TextViews una sola vez y los guarda en la memoria.
    // Así, cuando haces scroll, el móvil no se traba buscando los elementos una y otra vez.
    public static class LogViewHolder extends RecyclerView.ViewHolder {
        TextView tvNum, tvDni, tvFec;

        public LogViewHolder(@NonNull View iv) {
            super(iv);
            // 'iv' es la fila entera (item_log_actividad.xml). Aquí buscamos las columnas de esa fila.
            tvNum = iv.findViewById(R.id.tvIdLog);
            tvDni = iv.findViewById(R.id.tvDniLog);
            tvFec = iv.findViewById(R.id.tvFechaHoraLog);
        }
    }
}