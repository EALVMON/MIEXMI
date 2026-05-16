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
data class CarnetModelo(
    val id: Int,                // Guarda el ID (número entero)
    val tipo: String,           // Guarda la clase de carnet (texto)
    val fechaConcesion: String, // Guarda la fecha de concesión (texto)
    val fechaCaducidad: String  // Guarda la fecha de caducidad (texto)
)

// ====================================================================
// 2. EL ADAPTADOR
// ====================================================================
// Creamos la clase. El adaptador recibe la información desde fuera a través de su constructor:
class CarnetAdaptador(
    // Recibe la lista completa con todos los carnets que hay que mostrar.
    private val listaDatos: List<CarnetModelo>,

    // Usamos una función Lambda para saber cuándo el usuario toca una fila.
    private val listener: (CarnetModelo) -> Unit
) : RecyclerView.Adapter<CarnetAdaptador.CarnetViewHolder>() {

    // ====================================================================
    // 2.1 LOS TRES MÉTODOS OBLIGATORIOS
    // ====================================================================

    //  CREAR LA VISTA VISUAL
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarnetViewHolder {
        // LayoutInflater coge el archivo de diseño XML (item_carnet) y lo "infla",
        // transformando ese código visual en un objeto real que la pantalla puede pintar.
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_carnet, parent, false)
        // devuelvo esa vista ya fabricada
        return CarnetViewHolder(view)
    }

    // RELLENAR LOS DATOS
    override fun onBindViewHolder(holder: CarnetViewHolder, position: Int) {
        // Busca en nuestra lista de datos la linea de nuestro carnet que toca dibujar
        val actual = listaDatos[position]

        // Rellenamos los textos de la fila con los datos reales que tiene nuestro carnet
        holder.tvTipo.text = actual.tipo
        holder.tvFecCon.text = actual.fechaConcesion
        holder.tvFecCad.text = actual.fechaCaducidad

        // 'itemView' es la fila completa.le ponemos un setOnClickListener
        // Si el usuario toca esa fila, activamos el 'listener' y enviamos los datos del carnet que tocó.
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
    class CarnetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // Enlazamos las variables de Kotlin con los IDs que cree en el diseño XML.
        val tvTipo: TextView = itemView.findViewById(R.id.tvItemTipoCarnet)
        val tvFecCon: TextView = itemView.findViewById(R.id.tvItemConcesion)
        val tvFecCad: TextView = itemView.findViewById(R.id.tvItemCaducidad)
    }
}