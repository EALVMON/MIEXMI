package com.proyecto.miexmi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * ADAPTADOR PARA LA LISTA DE EVALUACIÓN DE ASCENSO
 */
public class EvaluacionAdaptador extends RecyclerView.Adapter<EvaluacionAdaptador.EvaluacionViewHolder> {

    // ========================================================================
    // === CLASE MODELO (EL CONTENEDOR DE DATOS)                         ===
    // ========================================================================
    public static class EvaluacionModelo {
        int idEvaluacion;
        String nombre;
        String resultado; // Apto, No Apto, etc.
        String fechaBod;
        String numBod;

        public EvaluacionModelo(int idEvaluacion, String nombre, String resultado, String fechaBod, String numBod) {
            this.idEvaluacion = idEvaluacion;
            this.nombre = nombre;
            this.resultado = resultado;
            this.fechaBod = fechaBod;
            this.numBod = numBod;
        }
    }

    // ========================================================================
    // === INTERFAZ PARA DETECTAR CLICS                                  ===
    // ========================================================================
    public interface OnItemClickListener {
        void onItemClick(EvaluacionModelo evaluacion);
    }

    private final List<EvaluacionModelo> listaDatos;
    private final OnItemClickListener listener;

    public EvaluacionAdaptador(List<EvaluacionModelo> listaDatos, OnItemClickListener listener) {
        this.listaDatos = listaDatos;
        this.listener = listener;
    }

    // ========================================================================
    // === MÉTODOS OBLIGATORIOS DEL RECYCLERVIEW ADAPTER                 ===
    // ========================================================================
    @NonNull
    @Override
    public EvaluacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evaluacion, parent, false);
        return new EvaluacionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EvaluacionViewHolder holder, int position) {
        EvaluacionModelo actual = listaDatos.get(position);

        holder.tvNombre.setText(actual.nombre);
        holder.tvResultado.setText(actual.resultado);
        holder.tvFechaBod.setText(actual.fechaBod);
        holder.tvNumBod.setText(actual.numBod);

        holder.itemView.setOnClickListener(v -> listener.onItemClick(actual));
    }

    @Override
    public int getItemCount() {
        return listaDatos.size();
    }

    // ========================================================================
    // === VIEWHOLDER (EL BUSCADOR DE IDs)                               ===
    // ========================================================================
    public static class EvaluacionViewHolder extends RecyclerView.ViewHolder {
        // En tu XML no pusiste el TextView del "Nº de fila" a la izquierda,
        // así que solo enlazamos estos 4
        TextView tvNombre, tvResultado, tvFechaBod, tvNumBod;

        public EvaluacionViewHolder(@NonNull View itemView) {
            super(itemView);
            // Estos IDs coinciden perfectamente con tu item_evaluacion.xml
            tvNombre = itemView.findViewById(R.id.tvItemNombreEvaluacion);
            tvResultado = itemView.findViewById(R.id.tvItemResultadoEvaluacion);
            tvFechaBod = itemView.findViewById(R.id.tvItemFechaEvaluacion);
            tvNumBod = itemView.findViewById(R.id.tvItemBodEvaluacion);
        }
    }
}