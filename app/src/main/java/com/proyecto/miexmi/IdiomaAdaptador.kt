package com.proyecto.miexmi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class IdiomaAdaptador extends RecyclerView.Adapter<IdiomaAdaptador.IdiomaViewHolder> {

    public static class IdiomaModelo {
        int id;
        String nombre, resultado, fecha, nbod;
        public IdiomaModelo(int id, String nombre, String resultado, String fecha, String nbod) {
            this.id = id; this.nombre = nombre; this.resultado = resultado; this.fecha = fecha; this.nbod = nbod;
        }
    }

    private final List<IdiomaModelo> lista;
    private final OnItemClickListener listener;

    public interface OnItemClickListener { void onItemClick(IdiomaModelo item); }

    public IdiomaAdaptador(List<IdiomaModelo> lista, OnItemClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public IdiomaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_idioma, parent, false);
        return new IdiomaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull IdiomaViewHolder holder, int position) {
        IdiomaModelo m = lista.get(position);
        holder.tvNom.setText(m.nombre);
        holder.tvRes.setText(m.resultado);
        holder.tvFec.setText(m.fecha);
        holder.tvBod.setText(m.nbod);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(m));
    }

    @Override
    public int getItemCount() { return lista.size(); }

    public static class IdiomaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNom, tvRes, tvFec, tvBod;
        public IdiomaViewHolder(@NonNull View iv) {
            super(iv);
            tvNom = iv.findViewById(R.id.tvItemNombreIdioma);
            tvRes = iv.findViewById(R.id.tvItemResultadoSlp);
            tvFec = iv.findViewById(R.id.tvItemFechaIdioma);
            tvBod = iv.findViewById(R.id.tvItemBodIdioma);
        }
    }
}
