package com.proyecto.miexmi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TrienioAdaptador extends RecyclerView.Adapter<TrienioAdaptador.TrienioViewHolder> {

    public static class TrienioModelo {
        int idTrienio;
        String tipoTrienio;
        String fechaBod;
        String numBod;

        public TrienioModelo(int idTrienio, String tipoTrienio, String fechaBod, String numBod) {
            this.idTrienio = idTrienio;
            this.tipoTrienio = tipoTrienio;
            this.fechaBod = fechaBod;
            this.numBod = numBod;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(TrienioModelo trienio);
    }

    private final List<TrienioModelo> listaDatos;
    private final OnItemClickListener listener;

    public TrienioAdaptador(List<TrienioModelo> listaDatos, OnItemClickListener listener) {
        this.listaDatos = listaDatos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TrienioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trienio, parent, false);
        return new TrienioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrienioViewHolder holder, int position) {
        TrienioModelo actual = listaDatos.get(position);

        // ¡MAGIA! El número de trienio es simplemente su posición en la lista
        holder.tvNumFila.setText(String.valueOf(position + 1));
        holder.tvTipoTrienio.setText(actual.tipoTrienio);
        holder.tvFechaBod.setText(actual.fechaBod);
        holder.tvNumBod.setText(actual.numBod);

        holder.itemView.setOnClickListener(v -> listener.onItemClick(actual));
    }

    @Override
    public int getItemCount() {
        return listaDatos.size();
    }

    public static class TrienioViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumFila, tvTipoTrienio, tvFechaBod, tvNumBod;

        public TrienioViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumFila = itemView.findViewById(R.id.tvItemNumFilaTrienio);
            tvTipoTrienio = itemView.findViewById(R.id.tvItemTipoTrienio);
            tvFechaBod = itemView.findViewById(R.id.tvItemFechaTrienio);
            tvNumBod = itemView.findViewById(R.id.tvItemBodTrienio);
        }
    }
}