package com.proyecto.miexmi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CarnetAdaptador extends RecyclerView.Adapter<CarnetAdaptador.CarnetViewHolder> {

    public static class CarnetModelo {
        int id;
        String tipo, fechaConcesion, fechaCaducidad;
        public CarnetModelo(int id, String tipo, String fechaConcesion, String fechaCaducidad) {
            this.id = id; this.tipo = tipo; this.fechaConcesion = fechaConcesion; this.fechaCaducidad = fechaCaducidad;
        }
    }

    private final List<CarnetModelo> lista;
    private final OnItemClickListener listener;

    public interface OnItemClickListener { void onItemClick(CarnetModelo item); }

    public CarnetAdaptador(List<CarnetModelo> lista, OnItemClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CarnetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carnet, parent, false);
        return new CarnetViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CarnetViewHolder holder, int position) {
        CarnetModelo m = lista.get(position);
        holder.tvTipo.setText(m.tipo);
        holder.tvFecCon.setText(m.fechaConcesion);
        holder.tvFecCad.setText(m.fechaCaducidad);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(m));
    }

    @Override
    public int getItemCount() { return lista.size(); }

    public static class CarnetViewHolder extends RecyclerView.ViewHolder {
        TextView tvTipo, tvFecCon, tvFecCad;
        public CarnetViewHolder(@NonNull View iv) {
            super(iv);
            tvTipo = iv.findViewById(R.id.tvItemTipoCarnet);
            tvFecCon = iv.findViewById(R.id.tvItemConcesion);
            tvFecCad = iv.findViewById(R.id.tvItemCaducidad);
        }
    }
}