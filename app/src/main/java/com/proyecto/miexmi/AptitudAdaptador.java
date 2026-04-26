package com.proyecto.miexmi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AptitudAdaptador extends RecyclerView.Adapter<AptitudAdaptador.AptitudViewHolder> {

    public static class AptitudModelo {
        int idAptitud;
        String nombre;
        String fechaBod;
        String numBod;

        public AptitudModelo(int idAptitud, String nombre, String fechaBod, String numBod) {
            this.idAptitud = idAptitud;
            this.nombre = nombre;
            this.fechaBod = fechaBod;
            this.numBod = numBod;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(AptitudModelo aptitud);
    }

    private final List<AptitudModelo> listaDatos;
    private final OnItemClickListener listener;

    public AptitudAdaptador(List<AptitudModelo> listaDatos, OnItemClickListener listener) {
        this.listaDatos = listaDatos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AptitudViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_aptitud, parent, false);
        return new AptitudViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AptitudViewHolder holder, int position) {
        AptitudModelo actual = listaDatos.get(position);

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

    public static class AptitudViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumFila, tvNombre, tvFechaBod, tvNumBod;

        public AptitudViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumFila = itemView.findViewById(R.id.tvItemNumAptitud);
            tvNombre = itemView.findViewById(R.id.tvItemNombreAptitud);
            tvFechaBod = itemView.findViewById(R.id.tvItemFechaAptitud);
            tvNumBod = itemView.findViewById(R.id.tvItemBodAptitud);
        }
    }
}