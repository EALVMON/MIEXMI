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
data class DistintivoModelo(
    val id: Int,           // Guarda el ID (número entero)
    val nombre: String,    // Guarda el nombre del distintivo (texto)
    val fecha: String,     // Guarda la fecha de publicación (texto)
    val nbod: String       // Guarda el número del boletín (texto)
)

// ====================================================================
// 2. EL ADAPTADOR
// ====================================================================
// Creamos la clase. El adaptador recibe la información desde fuera a través de su constructor:
class DistintivoAdaptador(
    // Recibe la lista completa con todos los distintivos que hay que mostrar.
    private val listaDatos: List<DistintivoModelo>,

    // Usamos una función Lambda para saber cuándo el usuario toca una fila.
    private val listener: (DistintivoModelo) -> Unit
) : RecyclerView.Adapter<DistintivoAdaptador.DistintivoViewHolder>() {

    // ====================================================================
    // 2.1 LOS TRES MÉTODOS OBLIGATORIOS
    // ====================================================================

    // CREAR LA VISTA VISUAL
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DistintivoViewHolder {
        // LayoutInflater coge el archivo de diseño XML (item_distintivo) y lo "infla",
        // transformando ese código visual en un objeto real que la pantalla puede pintar.
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_distintivo, parent, false)
        // devuelvo esa vista ya fabricada
        return DistintivoViewHolder(view)
    }

    // RELLENAR LOS DATOS
    override fun onBindViewHolder(holder: DistintivoViewHolder, position: Int) {
        // Busca en nuestra lista de datos la linea de nuestro distintivo que toca dibujar
        val actual = listaDatos[position]

        // Para imprimir el número en la primera columna. Le sumamos 1 porque las listas en programación empiezan en el número 0.
        holder.tvNum.text = holder.itemView.context.getString(R.string.numero_fila, position + 1)

        // Rellenamos los textos de la fila con los datos reales que tiene nuestro distintivo
        holder.tvNom.text = actual.nombre
        holder.tvFec.text = actual.fecha
        holder.tvBod.text = actual.nbod

        // 'itemView' es la fila completa. Le ponemos un setOnClickListener
        // Si el usuario toca esa fila, activamos el 'listener' y enviamos los datos del distintivo que tocó.
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
    class DistintivoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // Enlazamos las variables de Kotlin con los IDs que creé en el diseño XML.
        val tvNum: TextView = itemView.findViewById(R.id.tvItemNumDistintivo)
        val tvNom: TextView = itemView.findViewById(R.id.tvItemNombreDistintivo)
        val tvFec: TextView = itemView.findViewById(R.id.tvItemFechaDistintivo)
        val tvBod: TextView = itemView.findViewById(R.id.tvItemBodDistintivo)
    }
}