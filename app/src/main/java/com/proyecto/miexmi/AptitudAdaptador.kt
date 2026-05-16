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
data class AptitudModelo(
    val idAptitud: Int,    // Guarda el ID (número entero)
    val nombre: String,    // Guarda el nombre de la aptitud (texto)
    val fechaBod: String,  // Guarda la fecha de publicación (texto)
    val numBod: String     // Guarda el número del boletín (texto)
)

// ====================================================================
// 2. EL ADAPTADOR
// ====================================================================
// Creamos la clase. El adaptador recibe la información desde fuera a través de su constructor:
class AptitudAdaptador(
    // Recibe la lista completa con todas las aptitudes que hay que mostrar.
    private val listaDatos: List<AptitudModelo>,

    //Usamos una función Lambda para saber cuándo el usuario toca una fila.
    private val listener: (AptitudModelo) -> Unit
) : RecyclerView.Adapter<AptitudAdaptador.AptitudViewHolder>()

{
    // ====================================================================
    // 2.1 LOS TRES MÉTODOS OBLIGATORIOS
    // ====================================================================

    //  CREAR LA VISTA VISUAL
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AptitudViewHolder {
        // LayoutInflater coge el archivo de diseño XML (item_aptitud) y lo "infla",
        // transformando ese código visual en un objeto real que la pantalla puede pintar.
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_aptitud, parent, false)
        // devuelvo esa vista ya fabricada
        return AptitudViewHolder(view)
    }

    // RELLENAR LOS DATOS
    override fun onBindViewHolder(holder: AptitudViewHolder, position: Int) {
        // Busca en nuestra lista de datos la linea de nuestra aptidud que toca dibujar
        val actual = listaDatos[position]
        // para imprimir el número en la primera columna. Le sumamos 1 porque las listas en programación empiezan en el número 0.
        holder.tvNumFila.text = holder.itemView.context.getString(R.string.numero_fila, position + 1)
        // Rellenamos los textos de la fila con los datos reales que tiene nuestra aptitud
        holder.tvNombre.text = actual.nombre
        holder.tvFechaBod.text = actual.fechaBod
        holder.tvNumBod.text = actual.numBod
        // 'itemView' es la fila completa.le ponemos un setOnClickListener
        // Si el usuario toca esa fila, activamos el 'listener' y enviamos los datos de la aptitud que tocó.
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
    class AptitudViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // Enlazamos las variables de Kotlin con los IDs que cree en el diseño XML.
        val tvNumFila: TextView = itemView.findViewById(R.id.tvItemNumAptitud)
        val tvNombre: TextView = itemView.findViewById(R.id.tvItemNombreAptitud)
        val tvFechaBod: TextView = itemView.findViewById(R.id.tvItemFechaAptitud)
        val tvNumBod: TextView = itemView.findViewById(R.id.tvItemBodAptitud)
    }
}