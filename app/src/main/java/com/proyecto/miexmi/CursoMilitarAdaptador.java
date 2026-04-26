package com.proyecto.miexmi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CursoMilitarAdaptador extends RecyclerView.Adapter<CursoMilitarAdaptador.CursoViewHolder> {

    public static class CursoModelo {
        int id;
        String nombre, fecha, nbod;
        public CursoModelo(int id, String nombre, String fecha, String nbod) {
            this.id = id; this.nombre = nombre; this.fecha = fecha; this.nbod = nbod;
        }
    }

    private final List<CursoModelo> lista;
    private final OnItemClickListener listener;

    public interface OnItemClickListener { void onItemClick(CursoModelo item); }

    public CursoMilitarAdaptador(List<CursoModelo> lista, OnItemClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CursoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_curso_militar, parent, false);
        return new CursoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CursoViewHolder holder, int position) {
        CursoModelo m = lista.get(position);
        holder.tvNum.setText(String.valueOf(position + 1));
        holder.tvNom.setText(m.nombre);
        holder.tvFec.setText(m.fecha);
        holder.tvBod.setText(m.nbod);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(m));
    }

    @Override
    public int getItemCount() { return lista.size(); }

    public static class CursoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNum, tvNom, tvFec, tvBod;
        public CursoViewHolder(@NonNull View iv) {
            super(iv);
            tvNum = iv.findViewById(R.id.tvItemNumCurso);
            tvNom = iv.findViewById(R.id.tvItemNombreCurso);
            tvFec = iv.findViewById(R.id.tvItemFechaCurso);
            tvBod = iv.findViewById(R.id.tvItemBodCurso);
        }
    }
}