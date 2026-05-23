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
data class IdiomaModelo(
    val id: Int,              // Guarda el ID (número entero)
    val nombre: String,       // Guarda el nombre del idioma (texto)
    val resultado: String,    // Guarda el perfil lingüístico SLP (texto)
    val fecha: String,        // Guarda la fecha de publicación (texto)
    val nbod: String,          // Guarda el número del boletín (texto)
)

// ====================================================================
// 2. EL ADAPTADOR
// ====================================================================
// Creamos la clase. El adaptador recibe la información desde fuera a través de su constructor:
class IdiomaAdaptador(
    // Recibe la lista completa con todos los idiomas que hay que mostrar.
    private val listaDatos: List<IdiomaModelo>,

    // Usamos una función Lambda para saber cuándo el usuario toca una fila.
    private val listener: (IdiomaModelo) -> Unit,
) : RecyclerView.Adapter<IdiomaAdaptador.IdiomaViewHolder>() {

    // ====================================================================
    // 2.1 LOS TRES MÉTODOS OBLIGATORIOS
    // ====================================================================

    // CREAR LA VISTA VISUAL
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IdiomaViewHolder {
        // LayoutInflater coge el archivo de diseño XML (item_idioma) y lo "infla",
        // transformando ese código visual en un objeto real que la pantalla puede pintar.
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_idioma, parent, false)
        // devuelvo esa vista ya fabricada
        return IdiomaViewHolder(view)
    }

    // RELLENAR LOS DATOS
    override fun onBindViewHolder(holder: IdiomaViewHolder, position: Int) {
        // Busca en nuestra lista de datos la linea de nuestro idioma que toca dibujar
        val actual = listaDatos[position]

        // Rellenamos los textos de la fila con los datos reales que tiene nuestro idioma
        holder.tvNom.text = actual.nombre
        holder.tvRes.text = actual.resultado
        holder.tvFec.text = actual.fecha
        holder.tvBod.text = actual.nbod

        // 'itemView' es la fila completa. Le ponemos un setOnClickListener
        // Si el usuario toca esa fila, activamos el 'listener' y enviamos los datos del idioma que tocó.
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
    class IdiomaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // Enlazamos las variables de Kotlin con los IDs que creé en el diseño XML.
        val tvNom: TextView = itemView.findViewById(R.id.tvItemNombreIdioma)
        val tvRes: TextView = itemView.findViewById(R.id.tvItemResultadoSlp)
        val tvFec: TextView = itemView.findViewById(R.id.tvItemFechaIdioma)
        val tvBod: TextView = itemView.findViewById(R.id.tvItemBodIdioma)
    }
}