package com.proyecto.miexmi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SituacionAdaptador extends RecyclerView.Adapter<SituacionAdaptador.SituacionViewHolder> {

    public static class SituacionModelo {
        int idSituacion;
        String nombre;
        String fechaBod;
        String numBod;

        public SituacionModelo(int idSituacion, String nombre, String fechaBod, String numBod) {
            this.idSituacion = idSituacion;
            this.nombre = nombre;
            this.fechaBod = fechaBod;
            this.numBod = numBod;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(SituacionModelo situacion);
    }

    private final List<SituacionModelo> listaDatos;
    private final OnItemClickListener listener;

    public SituacionAdaptador(List<SituacionModelo> listaDatos, OnItemClickListener listener) {
        this.listaDatos = listaDatos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SituacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_situacion_admin, parent, false);
        return new SituacionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SituacionViewHolder holder, int position) {
        SituacionModelo actual = listaDatos.get(position);

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

    public static class SituacionViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumFila, tvNombre, tvFechaBod, tvNumBod;

        public SituacionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumFila = itemView.findViewById(R.id.tvItemNumSituacion);
            tvNombre = itemView.findViewById(R.id.tvItemNombreSituacion);
            tvFechaBod = itemView.findViewById(R.id.tvItemFechaSituacion);
            tvNumBod = itemView.findViewById(R.id.tvItemBodSituacion);
        }
    }
}
