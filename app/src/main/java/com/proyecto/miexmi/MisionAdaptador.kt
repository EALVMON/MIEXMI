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
data class MisionModelo(
    val idMision: Int,    // Guarda el ID (número entero)
    val nombre: String,    // Guarda el nombre de la misión (texto)
    val fechaBod: String,  // Guarda la fecha de publicación (texto)
    val numBod: String,     // Guarda el número del boletín (texto)
)

// ====================================================================
// 2. EL ADAPTADOR
// ====================================================================
// Creamos la clase. El adaptador recibe la información desde fuera a través de su constructor:
class MisionAdaptador(
    // Recibe la lista completa con todas las misiones que hay que mostrar.
    private val listaDatos: List<MisionModelo>,

    // Usamos una función Lambda para saber cuándo el usuario toca una fila.
    private val listener: (MisionModelo) -> Unit,
) : RecyclerView.Adapter<MisionAdaptador.MisionViewHolder>() {

    // ====================================================================
    // 2.1 LOS TRES MÉTODOS OBLIGATORIOS
    // ====================================================================

    // CREAR LA VISTA VISUAL
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MisionViewHolder {
        // LayoutInflater coge el archivo de diseño XML (item_mision) y lo "infla",
        // transformando ese código visual en un objeto real que la pantalla puede pintar.
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mision, parent, false)
        // devuelvo esa vista ya fabricada
        return MisionViewHolder(view)
    }

    // RELLENAR LOS DATOS
    override fun onBindViewHolder(holder: MisionViewHolder, position: Int) {
        // Busca en nuestra lista de datos la linea de nuestra misión que toca dibujar
        val actual = listaDatos[position]

        // Para imprimir el número en la primera columna. Le sumamos 1 porque las listas en programación empiezan en el número 0.
        // Utilizamos el recurso de texto oficial de Android para evitar el warning de concatenación.
        holder.tvNumFila.text =
            holder.itemView.context.getString(R.string.numero_fila, position + 1)

        // Rellenamos los textos de la fila con los datos reales que tiene nuestra misión
        holder.tvNombre.text = actual.nombre
        holder.tvFechaBod.text = actual.fechaBod
        holder.tvNumBod.text = actual.numBod

        // 'itemView' es la fila completa. Le ponemos un setOnClickListener
        // Si el usuario toca esa fila, activamos el 'listener' y enviamos los datos de la misión que tocó.
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
    class MisionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // Enlazamos las variables de Kotlin con los IDs que creé en el diseño XML.
        val tvNumFila: TextView = itemView.findViewById(R.id.tvItemNumMision)
        val tvNombre: TextView = itemView.findViewById(R.id.tvItemNombreMision)
        val tvFechaBod: TextView = itemView.findViewById(R.id.tvItemFechaMision)
        val tvNumBod: TextView = itemView.findViewById(R.id.tvItemBodMision)
    }
}