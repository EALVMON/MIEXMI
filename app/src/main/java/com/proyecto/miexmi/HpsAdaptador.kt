package com.proyecto.miexmi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class HpsAdaptador extends RecyclerView.Adapter<HpsAdaptador.HpsViewHolder> {

    public static class HpsModelo {
        int idHps;
        String nombre;
        String fechaConcesion;
        String fechaCaducidad;

        public HpsModelo(int idHps, String nombre, String fechaConcesion, String fechaCaducidad) {
            this.idHps = idHps;
            this.nombre = nombre;
            this.fechaConcesion = fechaConcesion;
            this.fechaCaducidad = fechaCaducidad;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(HpsModelo hps);
    }

    private final List<HpsModelo> listaDatos;
    private final OnItemClickListener listener;

    public HpsAdaptador(List<HpsModelo> listaDatos, OnItemClickListener listener) {
        this.listaDatos = listaDatos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HpsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hps, parent, false);
        return new HpsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HpsViewHolder holder, int position) {
        HpsModelo actual = listaDatos.get(position);

        holder.tvNombre.setText(actual.nombre);
        holder.tvConcesion.setText(actual.fechaConcesion);
        holder.tvCaducidad.setText(actual.fechaCaducidad);

        holder.itemView.setOnClickListener(v -> listener.onItemClick(actual));
    }

    @Override
    public int getItemCount() {
        return listaDatos.size();
    }

    public static class HpsViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvConcesion, tvCaducidad;

        public HpsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvItemNombreHps);
            tvConcesion = itemView.findViewById(R.id.tvItemConcesionHps);
            tvCaducidad = itemView.findViewById(R.id.tvItemCaducidadHps);
        }
    }
}
