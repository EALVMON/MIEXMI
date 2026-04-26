package com.proyecto.miexmi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TituloCivilAdaptador extends RecyclerView.Adapter<TituloCivilAdaptador.TituloViewHolder> {

    public static class TituloModelo {
        int idTitulo;
        String nombre;

        public TituloModelo(int idTitulo, String nombre) {
            this.idTitulo = idTitulo;
            this.nombre = nombre;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(TituloModelo titulo);
    }

    private final List<TituloModelo> listaDatos;
    private final OnItemClickListener listener;

    public TituloCivilAdaptador(List<TituloModelo> listaDatos, OnItemClickListener listener) {
        this.listaDatos = listaDatos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TituloViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_titulo_civil, parent, false);
        return new TituloViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TituloViewHolder holder, int position) {
        TituloModelo actual = listaDatos.get(position);

        holder.tvNumFila.setText(String.valueOf(position + 1));
        holder.tvNombre.setText(actual.nombre);

        holder.itemView.setOnClickListener(v -> listener.onItemClick(actual));
    }

    @Override
    public int getItemCount() {
        return listaDatos.size();
    }

    public static class TituloViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumFila, tvNombre;

        public TituloViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumFila = itemView.findViewById(R.id.tvItemNumTitulo);
            tvNombre = itemView.findViewById(R.id.tvItemNombreTitulo);
        }
    }
}