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
data class HpsModelo(
    val idHps: Int,            // Guarda el ID (número entero)
    val nombre: String,        // Guarda el nombre de la habilitación (texto)
    val fechaConcesion: String, // Guarda la fecha de concesión (texto)
    val fechaCaducidad: String,  // Guarda la fecha de caducidad (texto)
)

// ====================================================================
// 2. EL ADAPTADOR
// ====================================================================
// Creamos la clase. El adaptador recibe la información desde fuera a través de su constructor:
class HpsAdaptador(
    // Recibe la lista completa con todas las HPS que hay que mostrar.
    private val listaDatos: List<HpsModelo>,

    // Usamos una función Lambda para saber cuándo el usuario toca una fila.
    private val listener: (HpsModelo) -> Unit,
) : RecyclerView.Adapter<HpsAdaptador.HpsViewHolder>() {

    // ====================================================================
    // 2.1 LOS TRES MÉTODOS OBLIGATORIOS
    // ====================================================================

    // CREAR LA VISTA VISUAL
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HpsViewHolder {
        // LayoutInflater coge el archivo de diseño XML (item_hps) y lo "infla",
        // transformando ese código visual en un objeto real que la pantalla puede pintar.
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hps, parent, false)
        // devuelvo esa vista ya fabricada
        return HpsViewHolder(view)
    }

    // RELLENAR LOS DATOS
    override fun onBindViewHolder(holder: HpsViewHolder, position: Int) {
        // Busca en nuestra lista de datos la linea de nuestra HPS que toca dibujar
        val actual = listaDatos[position]

        // Rellenamos los textos de la fila con los datos reales que tiene nuestra HPS
        holder.tvNombre.text = actual.nombre
        holder.tvConcesion.text = actual.fechaConcesion
        holder.tvCaducidad.text = actual.fechaCaducidad

        // 'itemView' es la fila completa. Le ponemos un setOnClickListener
        // Si el usuario toca esa fila, activamos el 'listener' y enviamos los datos de la HPS que tocó.
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
    class HpsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // Enlazamos las variables de Kotlin con los IDs que creé en el diseño XML.
        val tvNombre: TextView = itemView.findViewById(R.id.tvItemNombreHps)
        val tvConcesion: TextView = itemView.findViewById(R.id.tvItemConcesionHps)
        val tvCaducidad: TextView = itemView.findViewById(R.id.tvItemCaducidadHps)
    }
}