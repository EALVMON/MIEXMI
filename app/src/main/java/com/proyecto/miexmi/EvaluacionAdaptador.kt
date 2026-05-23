package com.proyecto.miexmi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// ===================Explicacion  resto de los adaptadores son iguales ==========================
// ====================================================================
// 1. EL MODELO DE DATOS
// ====================================================================
// 'data class' es una estructura especial de Kotlin.
// Genera completamente lo necesario (getters,setters,constructores)
data class EvaluacionModelo(
    val idEvaluacion: Int, // Guarda el ID (número entero)
    val nombre: String,    // Guarda el nombre de la evaluación (texto)
    val resultado: String, // Guarda el resultado (Apto, No Apto, etc.)
    val fechaBod: String,  // Guarda la fecha de publicación (texto)
    val numBod: String,     // Guarda el número del boletín (texto)
)

// ====================================================================
// 2. EL ADAPTADOR
// ====================================================================
// Creamos la clase. El adaptador recibe la información desde fuera a través de su constructor:
class EvaluacionAdaptador(
    // Recibe la lista completa con todas las evaluaciones que hay que mostrar.
    private val listaDatos: List<EvaluacionModelo>,

    // Usamos una función Lambda para saber cuándo el usuario toca una fila.
    private val listener: (EvaluacionModelo) -> Unit,
) : RecyclerView.Adapter<EvaluacionAdaptador.EvaluacionViewHolder>() {

    // ====================================================================
    // 2.1 LOS TRES MÉTODOS OBLIGATORIOS
    // ====================================================================

    // CREAR LA VISTA VISUAL
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EvaluacionViewHolder {
        // LayoutInflater coge el archivo de diseño XML (item_evaluacion) y lo "infla",
        // transformando ese código visual en un objeto real que la pantalla puede pintar.
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_evaluacion, parent, false)
        // devuelvo esa vista ya fabricada
        return EvaluacionViewHolder(view)
    }

    // RELLENAR LOS DATOS
    override fun onBindViewHolder(holder: EvaluacionViewHolder, position: Int) {
        // Busca en nuestra lista de datos la linea de nuestra evaluación que toca dibujar
        val actual = listaDatos[position]

        // Rellenamos los textos de la fila con los datos reales que tiene nuestra evaluación
        holder.tvNombre.text = actual.nombre
        holder.tvResultado.text = actual.resultado
        holder.tvFechaBod.text = actual.fechaBod
        holder.tvNumBod.text = actual.numBod

        // 'itemView' es la fila completa. Le ponemos un setOnClickListener
        // Si el usuario toca esa fila, activamos el 'listener' y enviamos los datos de la evaluación que tocó.
        holder.itemView.setOnClickListener { listener(actual) }
    }

    // CONTAR LOS ELEMENTOS
    // Con el siguiente metodo le decimos al sistema el número exacto de elementos que tiene la lista.
    // Así Android sabe de qué tamaño debe dibujar la barra de desplazamiento (scroll).
    override fun getItemCount() = listaDatos.size


    // ====================================================================
    // 2.2. EL VIEWHOLDER
    // ====================================================================
    // Esta clase anidada busca los textos una sola vez al principio y los guarda en memoria.
    class EvaluacionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // Enlazamos las variables de Kotlin con los IDs que creé en el diseño XML.
        val tvNombre: TextView = itemView.findViewById(R.id.tvItemNombreEvaluacion)
        val tvResultado: TextView = itemView.findViewById(R.id.tvItemResultadoEvaluacion)
        val tvFechaBod: TextView = itemView.findViewById(R.id.tvItemFechaEvaluacion)
        val tvNumBod: TextView = itemView.findViewById(R.id.tvItemBodEvaluacion)
    }
}