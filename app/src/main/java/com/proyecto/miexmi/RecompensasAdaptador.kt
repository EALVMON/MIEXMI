package com.proyecto.miexmi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RecompensasAdaptador extends RecyclerView.Adapter<RecompensasAdaptador.RecompensaViewHolder> {

    public static class RecompensaModelo {
        int id;
        String nombre, fecha, nbod;
        public RecompensaModelo(int id, String nombre, String fecha, String nbod) {
            this.id = id; this.nombre = nombre; this.fecha = fecha; this.nbod = nbod;
        }
    }

    private final List<RecompensaModelo> lista;
    private final OnItemClickListener listener;

    public interface OnItemClickListener { void onItemClick(RecompensaModelo item); }

    public RecompensasAdaptador(List<RecompensaModelo> lista, OnItemClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecompensaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recompensa, parent, false);
        return new RecompensaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecompensaViewHolder holder, int position) {
        RecompensaModelo m = lista.get(position);
        holder.tvNum.setText(String.valueOf(position + 1));
        holder.tvNom.setText(m.nombre);
        holder.tvFec.setText(m.fecha);
        holder.tvBod.setText(m.nbod);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(m));
    }

    @Override
    public int getItemCount() { return lista.size(); }

    public static class RecompensaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNum, tvNom, tvFec, tvBod;
        public RecompensaViewHolder(@NonNull View iv) {
            super(iv);
            tvNum = iv.findViewById(R.id.tvItemNumRecompensa);
            tvNom = iv.findViewById(R.id.tvItemNombreRecompensa);
            tvFec = iv.findViewById(R.id.tvItemFechaRecompensa);
            tvBod = iv.findViewById(R.id.tvItemBodRecompensa);
        }
    }
}