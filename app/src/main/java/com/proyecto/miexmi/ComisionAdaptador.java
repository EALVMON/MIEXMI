package com.proyecto.miexmi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * ADAPTADOR PARA LA LISTA DE COMISIONES DE SERVICIO
 * Esta clase es el "puente" entre los datos de la Base de Datos y la pantalla visual.
 */
public class ComisionAdaptador extends RecyclerView.Adapter<ComisionAdaptador.ComisionViewHolder> {

    // ========================================================================
    // === CLASE MODELO (EL CONTENEDOR DE DATOS)                         ===
    // ========================================================================
    public static class ComisionModelo {
        int idComision;
        String nombre;
        String fechaBod;
        String numBod;

        public ComisionModelo(int idComision, String nombre, String fechaBod, String numBod) {
            this.idComision = idComision;
            this.nombre = nombre;
            this.fechaBod = fechaBod;
            this.numBod = numBod;
        }
    }

    // ========================================================================
    // === INTERFAZ PARA DETECTAR CLICS                                  ===
    // ========================================================================
    public interface OnItemClickListener {
        void onItemClick(ComisionModelo comision);
    }

    private final List<ComisionModelo> listaDatos;
    private final OnItemClickListener listener;

    public ComisionAdaptador(List<ComisionModelo> listaDatos, OnItemClickListener listener) {
        this.listaDatos = listaDatos;
        this.listener = listener;
    }

    // ========================================================================
    // === MÉTODOS OBLIGATORIOS DEL RECYCLERVIEW ADAPTER                 ===
    // ========================================================================
    @NonNull
    @Override
    public ComisionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comision, parent, false);
        return new ComisionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComisionViewHolder holder, int position) {
        ComisionModelo actual = listaDatos.get(position);

        holder.tvNumFila.setText(String.valueOf(position + 1));
        holder.tvNombre.setText(actual.nombre);
        holder.tvFechaBod.setText(actual.fechaBod);
        holder.tvNumBod.setText(actual.numBod);

        holder.itemView.setOnClickListener(v -> listener.onItemClick(actual));
    }

    @Override
    public int getItemCount() {
        return listaDatos.size();
    }

    // ========================================================================
    // === VIEWHOLDER (EL BUSCADOR DE IDs)                               ===
    // ========================================================================
    public static class ComisionViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumFila, tvNombre, tvFechaBod, tvNumBod;

        public ComisionViewHolder(@NonNull View itemView) {
            super(itemView);
            // Estos IDs coinciden perfectamente con tu item_comision.xml
            tvNumFila = itemView.findViewById(R.id.tvItemNumComision);
            tvNombre = itemView.findViewById(R.id.tvItemNombreComision);
            tvFechaBod = itemView.findViewById(R.id.tvItemFechaComision);
            tvNumBod = itemView.findViewById(R.id.tvItemBodComision);
        }
    }
}
