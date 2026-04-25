package com.proyecto.miexmi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TmiAdaptador extends RecyclerView.Adapter<TmiAdaptador.TmiViewHolder> {

    // MODELO DE DATOS ---
    public static class TmiModelo {
        int idTmi;
        String numeroTarjeta;
        String fechaCaducidad;

        public TmiModelo(int idTmi, String numeroTarjeta, String fechaCaducidad) {
            this.idTmi = idTmi;
            this.numeroTarjeta = numeroTarjeta;
            this.fechaCaducidad = fechaCaducidad;
        }
    }

    // INTERFAZ PARA CLICS ---
    public interface OnItemClickListener {
        void onItemClick(TmiModelo tmi);
    }

    // las pongo final porque son variables que nacen y mueren ya, que una vez que tengo
    //  que les paso la lista de datos (listaTmis) y la acción del clic (listener) al constructor
    //  del adaptador, nunca más las cambio  por otras diferentes
    private final List<TmiModelo> listaTmis;
    private final OnItemClickListener listener;

    // Constructor del Adaptador
    public TmiAdaptador(List<TmiModelo> listaTmis, OnItemClickListener listener) {
        this.listaTmis = listaTmis;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TmiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tmi, parent, false);
        return new TmiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TmiViewHolder holder, int position) {
        TmiModelo tmiActual = listaTmis.get(position);

        holder.tvNumFila.setText(String.valueOf(position + 1));
        holder.tvNumeroTarjeta.setText(tmiActual.numeroTarjeta);
        holder.tvCaducidad.setText(tmiActual.fechaCaducidad);

        holder.itemView.setOnClickListener(v -> listener.onItemClick(tmiActual));
    }

    @Override
    public int getItemCount() {
        return listaTmis.size();
    }

    // VIEWHOLDER ---
    public static class TmiViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumFila, tvNumeroTarjeta, tvCaducidad;

        public TmiViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumFila = itemView.findViewById(R.id.tvItemNumFilaTmi);
            tvNumeroTarjeta = itemView.findViewById(R.id.tvItemNumeroTarjeta);
            tvCaducidad = itemView.findViewById(R.id.tvItemCaducidadTarjeta);
        }
    }
}