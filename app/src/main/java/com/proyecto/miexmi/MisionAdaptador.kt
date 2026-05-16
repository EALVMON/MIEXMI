package com.proyecto.miexmi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * ADAPTADOR PARA LA LISTA DE MISIONES
 * Esta clase es el "puente" entre los datos de la Base de Datos y la pantalla visual.
 */
public class MisionAdaptador extends RecyclerView.Adapter<MisionAdaptador.MisionViewHolder> {

    // ========================================================================
    // === CLASE MODELO (EL CONTENEDOR DE DATOS)                         ===
    // ========================================================================
    public static class MisionModelo {
        int idMision;
        String nombre;
        String fechaBod;
        String numBod;

        public MisionModelo(int idMision, String nombre, String fechaBod, String numBod) {
            this.idMision = idMision;
            this.nombre = nombre;
            this.fechaBod = fechaBod;
            this.numBod = numBod;
        }
    }

    // ========================================================================
    // === INTERFAZ PARA DETECTAR CLICS                                  ===
    // ========================================================================
    public interface OnItemClickListener {
        void onItemClick(MisionModelo mision);
    }

    private final List<MisionModelo> listaDatos;
    private final OnItemClickListener listener;

    public MisionAdaptador(List<MisionModelo> listaDatos, OnItemClickListener listener) {
        this.listaDatos = listaDatos;
        this.listener = listener;
    }

    // ========================================================================
    // === MÉTODOS OBLIGATORIOS DEL RECYCLERVIEW ADAPTER                 ===
    // ========================================================================
    @NonNull
    @Override
    public MisionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mision, parent, false);
        return new MisionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MisionViewHolder holder, int position) {
        MisionModelo actual = listaDatos.get(position);

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
    public static class MisionViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumFila, tvNombre, tvFechaBod, tvNumBod;

        public MisionViewHolder(@NonNull View itemView) {
            super(itemView);
            // Estos IDs coinciden perfectamente con tu item_mision.xml
            tvNumFila = itemView.findViewById(R.id.tvItemNumMision);
            tvNombre = itemView.findViewById(R.id.tvItemNombreMision);
            tvFechaBod = itemView.findViewById(R.id.tvItemFechaMision);
            tvNumBod = itemView.findViewById(R.id.tvItemBodMision);
        }
    }
}