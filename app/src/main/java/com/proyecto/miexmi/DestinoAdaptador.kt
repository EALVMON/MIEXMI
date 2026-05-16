package com.proyecto.miexmi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * ADAPTADOR PARA LA LISTA DE DESTINOS
 * Esta clase es el "puente" entre los datos de la Base de Datos y la pantalla visual.
 * Se encarga de coger cada Destino guardado e "inyectarlo" en la fila correspondiente del XML.
 */
public class DestinoAdaptador extends RecyclerView.Adapter<DestinoAdaptador.DestinoViewHolder> {

    // ========================================================================
    // === 1. CLASE MODELO (EL CONTENEDOR DE DATOS)                         ===
    // ========================================================================
    /**
     * Esta clase interna representa una única fila de la lista.
     * Solo sirve para empaquetar los datos de un destino y moverlos fácilmente.
     */
    public static class DestinoModelo {
        int idDestino;
        String nombre;
        String fechaBod;
        String numBod;

        // Constructor para guardar los datos al crear el objeto
        public DestinoModelo(int idDestino, String nombre, String fechaBod, String numBod) {
            this.idDestino = idDestino;
            this.nombre = nombre;
            this.fechaBod = fechaBod;
            this.numBod = numBod;
        }
    }

    // ========================================================================
    // === 2. INTERFAZ PARA DETECTAR CLICS                                  ===
    // ========================================================================
    /**
     * Como los adaptadores no saben en qué pantalla están, creamos esta interfaz.
     * Es como un "timbre": cuando tocamos una fila, el adaptador toca este timbre
     * y la pantalla principal (Destinos.java) lo escucha y reacciona.
     */
    public interface OnItemClickListener {
        void onItemClick(DestinoModelo destino);
    }

    // Variables globales del adaptador (final porque no cambian una vez asignadas)
    private final List<DestinoModelo> listaDatos;
    private final OnItemClickListener listener;

    // Constructor: Aquí recibimos los datos y el listener desde Destinos.java
    public DestinoAdaptador(List<DestinoModelo> listaDatos, OnItemClickListener listener) {
        this.listaDatos = listaDatos;
        this.listener = listener;
    }

    // ========================================================================
    // === 3. MÉTODOS OBLIGATORIOS DEL RECYCLERVIEW ADAPTER                 ===
    // ========================================================================

    /**
     * PASO A: "Inflar" (crear) la parte visual de la fila.
     * Aquí le decimos a Android: "Coge el archivo item_destino.xml y conviértelo en una vista real".
     */
    @NonNull
    @Override
    public DestinoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_destino, parent, false);
        return new DestinoViewHolder(view);
    }

    /**
     * PASO B: Llenar de datos la fila visual.
     * Este metodo se ejecuta una vez por cada fila visible en la pantalla.
     */
    @Override
    public void onBindViewHolder(@NonNull DestinoViewHolder holder, int position) {
        // 1. Sacamos el destino correspondiente a esta fila (posición)
        DestinoModelo actual = listaDatos.get(position);

        // 2. Escribimos los datos en los TextViews
        // El número de fila es la posición + 1 (para que no empiece en 0)
        holder.tvNumFila.setText(String.valueOf(position + 1));
        holder.tvNombre.setText(actual.nombre);
        holder.tvFechaBod.setText(actual.fechaBod);
        holder.tvNumBod.setText(actual.numBod);

        // 3. Le ponemos la "oreja" (listener) a toda la fila para detectar si la tocan
        holder.itemView.setOnClickListener(v -> listener.onItemClick(actual));
    }

    /**
     * PASO C: Decirle a la lista cuántos elementos hay en total.
     */
    @Override
    public int getItemCount() {
        return listaDatos.size();
    }

    // ========================================================================
    // === 4. VIEWHOLDER (EL BUSCADOR DE IDs)                               ===
    // ========================================================================
    /**
     * El trabajo de esta clase es buscar los IDs en el XML (findViewById) SOLO UNA VEZ.
     * Al guardarlos en variables, la app va súper fluida al hacer scroll porque
     * no tiene que buscar los IDs una y otra vez por cada fila.
     */
    public static class DestinoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumFila, tvNombre, tvFechaBod, tvNumBod;

        public DestinoViewHolder(@NonNull View itemView) {
            super(itemView);
            // Estos IDs coinciden perfectamente con tu item_destino.xml
            tvNumFila = itemView.findViewById(R.id.tvItemNum);
            tvNombre = itemView.findViewById(R.id.tvItemNombre);
            tvFechaBod = itemView.findViewById(R.id.tvItemFecha);
            tvNumBod = itemView.findViewById(R.id.tvItemBod);
        }
    }
}