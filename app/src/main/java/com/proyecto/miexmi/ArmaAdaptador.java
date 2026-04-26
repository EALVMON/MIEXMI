package com.proyecto.miexmi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ArmaAdaptador extends RecyclerView.Adapter<ArmaAdaptador.ArmaViewHolder> {

    public static class ArmaModelo {
        int id;
        String nombre, numSerie, fecha;
        public ArmaModelo(int id, String nombre, String numSerie, String fecha) {
            this.id = id; this.nombre = nombre; this.numSerie = numSerie; this.fecha = fecha;
        }
    }

    private final List<ArmaModelo> lista;
    private final OnItemClickListener listener;

    public interface OnItemClickListener { void onItemClick(ArmaModelo item); }

    public ArmaAdaptador(List<ArmaModelo> lista, OnItemClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ArmaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_arma_particular, parent, false);
        return new ArmaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ArmaViewHolder holder, int position) {
        ArmaModelo m = lista.get(position);
        holder.tvNum.setText(String.valueOf(position + 1));
        holder.tvNom.setText(m.nombre);
        holder.tvSerie.setText(m.numSerie);
        holder.tvFec.setText(m.fecha);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(m));
    }

    @Override
    public int getItemCount() { return lista.size(); }

    public static class ArmaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNum, tvNom, tvSerie, tvFec;
        public ArmaViewHolder(@NonNull View iv) {
            super(iv);
            tvNum = iv.findViewById(R.id.tvItemNumArma);
            tvNom = iv.findViewById(R.id.tvItemNombreArma);
            tvSerie = iv.findViewById(R.id.tvItemNumSerieArma);
            tvFec = iv.findViewById(R.id.tvItemCaducidadArma);
        }
    }
}
