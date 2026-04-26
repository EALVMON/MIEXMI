package com.proyecto.miexmi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DistintivoAdaptador extends RecyclerView.Adapter<DistintivoAdaptador.DistintivoViewHolder> {

    public static class DistintivoModelo {
        int id;
        String nombre, fecha, nbod;
        public DistintivoModelo(int id, String nombre, String fecha, String nbod) {
            this.id = id; this.nombre = nombre; this.fecha = fecha; this.nbod = nbod;
        }
    }

    private final List<DistintivoModelo> lista;
    private final OnItemClickListener listener;

    public interface OnItemClickListener { void onItemClick(DistintivoModelo item); }

    public DistintivoAdaptador(List<DistintivoModelo> lista, OnItemClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DistintivoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_distintivo, parent, false);
        return new DistintivoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DistintivoViewHolder holder, int position) {
        DistintivoModelo m = lista.get(position);
        holder.tvNum.setText(String.valueOf(position + 1));
        holder.tvNom.setText(m.nombre);
        holder.tvFec.setText(m.fecha);
        holder.tvBod.setText(m.nbod);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(m));
    }

    @Override
    public int getItemCount() { return lista.size(); }

    public static class DistintivoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNum, tvNom, tvFec, tvBod;
        public DistintivoViewHolder(@NonNull View iv) {
            super(iv);
            tvNum = iv.findViewById(R.id.tvItemNumDistintivo);
            tvNom = iv.findViewById(R.id.tvItemNombreDistintivo);
            tvFec = iv.findViewById(R.id.tvItemFechaDistintivo);
            tvBod = iv.findViewById(R.id.tvItemBodDistintivo);
        }
    }
}