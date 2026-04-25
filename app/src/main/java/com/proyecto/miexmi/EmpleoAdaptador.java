package com.proyecto.miexmi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * ADAPTADOR PARA LA LISTA DE EMPLEOS
 * Esta clase es el "puente" entre los datos de la Base de Datos y la pantalla visual.
 * Se encarga de coger cada Empleo guardado e "inyectarlo" en la fila correspondiente del XML.
 */
public class EmpleoAdaptador extends RecyclerView.Adapter<EmpleoAdaptador.EmpleoViewHolder> {

    // ========================================================================
    // === 1. CLASE MODELO (EL CONTENEDOR DE DATOS)                         ===
    // ========================================================================
    /**
     * Esta clase interna representa una única fila de la lista.
     * Solo sirve para empaquetar los datos de un empleo y moverlos fácilmente.
     */
    public static class EmpleoModelo {
        int idEmpleo;
        String nombre;
        String fechaBod;
        String numBod;

        // Constructor para guardar los datos al crear el objeto
        public EmpleoModelo(int idEmpleo, String nombre, String fechaBod, String numBod) {
            this.idEmpleo = idEmpleo;
            this.nombre = nombre;
            this.fechaBod = fechaBod;
            this.numBod = numBod;
        }
    }

    // ========================================================================
    // === 2. INTERFAZ PARA DETECTAR CLICS                                  ===
    // ========================================================================

    public interface OnItemClickListener {
        void onItemClick(EmpleoModelo empleo);
    }

    // Variables globales del adaptador (final porque no cambian una vez asignadas)
    private final List<EmpleoModelo> listaDatos; // La lista completa de empleos
    private final OnItemClickListener listener;  // El "timbre" para los clics

    // Constructor: Aquí recibimos los datos y el listener desde Empleos.java
    public EmpleoAdaptador(List<EmpleoModelo> listaDatos, OnItemClickListener listener) {
        this.listaDatos = listaDatos;
        this.listener = listener;
    }

    // ========================================================================
    // === 3. MÉTODOS OBLIGATORIOS DEL RECYCLERVIEW ADAPTER                 ===
    // ========================================================================

    /**
     * PASO A: "Inflar" (crear) la parte visual de la fila.
     * Aquí le decimos a Android: "Coge el archivo item_empleo.xml y conviértelo en una vista real".
     */
    @NonNull
    @Override
    public EmpleoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empleo, parent, false);
        return new EmpleoViewHolder(view);
    }

    /**
     * PASO B: Llenar de datos la fila visual.
     * Este metodo se ejecuta una vez por cada fila visible en la pantalla.
     */
    @Override
    public void onBindViewHolder(@NonNull EmpleoViewHolder holder, int position) {
        // 1. Sacamos el empleo correspondiente a esta fila (posición)
        EmpleoModelo actual = listaDatos.get(position);

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
    public static class EmpleoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumFila, tvNombre, tvFechaBod, tvNumBod;

        public EmpleoViewHolder(@NonNull View itemView) {
            super(itemView);
            // Enlazamos las variables con los IDs de tu archivo item_empleo.xml
            tvNumFila = itemView.findViewById(R.id.tvItemNumEmpleo);
            tvNombre = itemView.findViewById(R.id.tvItemNombreEmpleo);
            tvFechaBod = itemView.findViewById(R.id.tvItemFechaEmpleo);
            tvNumBod = itemView.findViewById(R.id.tvItemBodEmpleo);
        }
    }
}