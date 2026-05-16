package com.proyecto.miexmi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TcgfAdaptador extends RecyclerView.Adapter<TcgfAdaptador.TcgfViewHolder> {

    public static class TcgfModelo {
        int id;
        String fecha, puntuacion, apto;
        public TcgfModelo(int id, String fecha, String puntuacion, String apto) {
            this.id = id; this.fecha = fecha; this.puntuacion = puntuacion; this.apto = apto;
        }
    }

    private final List<TcgfModelo> lista;
    private final OnItemClickListener listener;

    public interface OnItemClickListener { void onItemClick(TcgfModelo item); }

    public TcgfAdaptador(List<TcgfModelo> lista, OnItemClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TcgfViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tcgf, parent, false);
        return new TcgfViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TcgfViewHolder holder, int position) {
        TcgfModelo m = lista.get(position);
        holder.tvNum.setText(String.valueOf(position + 1));
        holder.tvFec.setText(m.fecha);
        holder.tvPun.setText(m.puntuacion);
        holder.tvApt.setText(m.apto);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(m));
    }

    @Override
    public int getItemCount() { return lista.size(); }

    public static class TcgfViewHolder extends RecyclerView.ViewHolder {
        TextView tvNum, tvFec, tvPun, tvApt;
        public TcgfViewHolder(@NonNull View iv) {
            super(iv);
            tvNum = iv.findViewById(R.id.tvItemNumTcgf);
            tvFec = iv.findViewById(R.id.tvItemFechaTcgf);
            tvPun = iv.findViewById(R.id.tvItemPuntuacionTcgf);
            tvApt = iv.findViewById(R.id.tvItemAptoTcgf);
        }
    }
}
