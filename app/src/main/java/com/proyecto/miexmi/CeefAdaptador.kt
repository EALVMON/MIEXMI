package com.proyecto.miexmi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CeefAdaptador extends RecyclerView.Adapter<CeefAdaptador.CeefViewHolder> {

    // --- 1. MODELO DE DATOS ---
    public static class CeefModelo {
        int idCeef;
        String nombre;
        String fechaBod;
        String numBod;

        public CeefModelo(int idCeef, String nombre, String fechaBod, String numBod) {
            this.idCeef = idCeef;
            this.nombre = nombre;
            this.fechaBod = fechaBod;
            this.numBod = numBod;
        }
    }

    // --- 2. INTERFAZ PARA CLICS ---
    public interface OnItemClickListener {
        void onItemClick(CeefModelo ceef);
    }

    private final List<CeefModelo> listaDatos;
    private final OnItemClickListener listener;

    // Constructor
    public CeefAdaptador(List<CeefModelo> listaDatos, OnItemClickListener listener) {
        this.listaDatos = listaDatos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CeefViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cee_fundamental, parent, false);
        return new CeefViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CeefViewHolder holder, int position) {
        CeefModelo actual = listaDatos.get(position);

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

    // --- 3. VIEWHOLDER (AQUÍ ENLAZAMOS LOS IDs EXACTOS) ---
    public static class CeefViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumFila, tvNombre, tvFechaBod, tvNumBod;

        public CeefViewHolder(@NonNull View itemView) {
            super(itemView);
            // Estos IDs coinciden perfectamente con tu XML
            tvNumFila = itemView.findViewById(R.id.tvItemNumCEEF);
            tvNombre = itemView.findViewById(R.id.tvItemNombreCEEF);
            tvFechaBod = itemView.findViewById(R.id.tvItemFechaCEEF);
            tvNumBod = itemView.findViewById(R.id.tvItemBodCEEF);
        }
    }
}
