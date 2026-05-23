package com.proyecto.miexmi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


// ====================================================================
// 1. EL MODELO DE DATOS
// ====================================================================
// 'data class' es una estructura especial de Kotlin.
// Genera completamente lo necesario (getters,setters,constructores)
data class DestinoModelo(
    val idDestino: Int,    // Guarda el ID (número entero)
    val nombre: String,    // Guarda el nombre del destino (texto)
    val fechaBod: String,  // Guarda la fecha de publicación (texto)
    val numBod: String,     // Guarda el número del boletín (texto)
)

// ====================================================================
// 2. EL ADAPTADOR
// ====================================================================
// Creamos la clase. El adaptador recibe la información desde fuera a través de su constructor:
class DestinoAdaptador(
    // Recibe la lista completa con todos los destinos que hay que mostrar.
    private val listaDatos: List<DestinoModelo>,

    // Usamos una función Lambda para saber cuándo el usuario toca una fila.
    private val listener: (DestinoModelo) -> Unit,
) : RecyclerView.Adapter<DestinoAdaptador.DestinoViewHolder>() {

    // ====================================================================
    // 2.1 LOS TRES MÉTODOS OBLIGATORIOS
    // ====================================================================

    // CREAR LA VISTA VISUAL
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DestinoViewHolder {
        // LayoutInflater coge el archivo de diseño XML (item_destino) y lo "infla",
        // transformando ese código visual en un objeto real que la pantalla puede pintar.
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_destino, parent, false)
        // devuelvo esa vista ya fabricada
        return DestinoViewHolder(view)
    }

    // RELLENAR LOS DATOS
    override fun onBindViewHolder(holder: DestinoViewHolder, position: Int) {
        // Busca en nuestra lista de datos la linea de nuestro destino que toca dibujar
        val actual = listaDatos[position]

        // Para imprimir el número en la primera columna. Le sumamos 1 porque las listas en programación empiezan en el número 0.

        holder.tvNumFila.text =
            holder.itemView.context.getString(R.string.numero_fila, position + 1)

        // Rellenamos los textos de la fila con los datos reales que tiene nuestro destino
        holder.tvNombre.text = actual.nombre
        holder.tvFechaBod.text = actual.fechaBod
        holder.tvNumBod.text = actual.numBod

        // 'itemView' es la fila completa. Le ponemos un setOnClickListener
        // Si el usuario toca esa fila, activamos el 'listener' y enviamos los datos del destino que tocó.
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
    class DestinoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // Enlazamos las variables de Kotlin con los IDs que creé en el diseño XML.
        val tvNumFila: TextView = itemView.findViewById(R.id.tvItemNum)
        val tvNombre: TextView = itemView.findViewById(R.id.tvItemNombre)
        val tvFechaBod: TextView = itemView.findViewById(R.id.tvItemFecha)
        val tvNumBod: TextView = itemView.findViewById(R.id.tvItemBod)
    }
}