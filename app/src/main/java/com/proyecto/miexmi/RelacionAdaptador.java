package com.proyecto.miexmi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * ADAPTADOR PARA LA LISTA DE RELACIONES CON LA ADMINISTRACIÓN
 * Esta clase es el "puente" entre los datos de la Base de Datos y la pantalla visual.
 */
public class RelacionAdaptador extends RecyclerView.Adapter<RelacionAdaptador.RelacionViewHolder> {

    // ========================================================================
    // === CLASE MODELO (EL CONTENEDOR DE DATOS)                         ===
    // ========================================================================
    public static class RelacionModelo {
        int idRelacion;
        String nombre;
        String fechaBod;
        String numBod;

        public RelacionModelo(int idRelacion, String nombre, String fechaBod, String numBod) {
            this.idRelacion = idRelacion;
            this.nombre = nombre;
            this.fechaBod = fechaBod;
            this.numBod = numBod;
        }
    }

    // ========================================================================
    // === INTERFAZ PARA DETECTAR CLICS                                  ===
    // ========================================================================
    public interface OnItemClickListener {
        void onItemClick(RelacionModelo relacion);
    }

    private final List<RelacionModelo> listaDatos;
    private final OnItemClickListener listener;

    public RelacionAdaptador(List<RelacionModelo> listaDatos, OnItemClickListener listener) {
        this.listaDatos = listaDatos;
        this.listener = listener;
    }

    // ========================================================================
    // === MÉTODOS OBLIGATORIOS DEL RECYCLERVIEW ADAPTER                 ===
    // ========================================================================
    @NonNull
    @Override
    public RelacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Enlazamos directamente con tu archivo item_relacion_admin.xml
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_relacion_admin, parent, false);
        return new RelacionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RelacionViewHolder holder, int position) {
        RelacionModelo actual = listaDatos.get(position);

        // Rellenamos los datos en la fila visual
        holder.tvNumFila.setText(String.valueOf(position + 1));
        holder.tvNombre.setText(actual.nombre);
        holder.tvFechaBod.setText(actual.fechaBod);
        holder.tvNumBod.setText(actual.numBod);

        // Activamos el clic en la fila
        holder.itemView.setOnClickListener(v -> listener.onItemClick(actual));
    }

    @Override
    public int getItemCount() {
        return listaDatos.size();
    }

    // ========================================================================
    // === VIEWHOLDER (EL BUSCADOR DE IDs)                               ===
    // ========================================================================
    public static class RelacionViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumFila, tvNombre, tvFechaBod, tvNumBod;

        public RelacionViewHolder(@NonNull View itemView) {
            super(itemView);
            // Estos IDs coinciden perfectamente con tu item_relacion_admin.xml
            tvNumFila = itemView.findViewById(R.id.tvItemNumRelacion);
            tvNombre = itemView.findViewById(R.id.tvItemNombreRelacion);
            tvFechaBod = itemView.findViewById(R.id.tvItemFechaRelacion);
            tvNumBod = itemView.findViewById(R.id.tvItemBodRelacion);
        }
    }
}
