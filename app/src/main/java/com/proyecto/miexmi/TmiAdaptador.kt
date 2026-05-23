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
data class TmiModelo(
    val idTmi: Int,            // Guarda el ID (número entero)
    val numeroTarjeta: String, // Guarda el número de la TMI (texto)
    val fechaCaducidad: String, // Guarda la fecha de caducidad (texto)
)

// ====================================================================
// 2. EL ADAPTADOR
// ====================================================================
// Creamos la clase. El adaptador recibe la información desde fuera a través de su constructor:
class TmiAdaptador(
    // Recibe la lista completa con todas las tarjetas que hay que mostrar.
    // las pongo val (constantes) porque son variables que nacen y mueren ya.
    // Una vez que le paso la lista y el listener al constructor, no cambian.
    private val listaTmis: List<TmiModelo>,

    // Usamos una función Lambda para saber cuándo el usuario toca una fila.
    private val listener: (TmiModelo) -> Unit,
) : RecyclerView.Adapter<TmiAdaptador.TmiViewHolder>() {

    // ====================================================================
    // 2.1 LOS TRES MÉTODOS OBLIGATORIOS
    // ====================================================================

    // CREAR LA VISTA VISUAL
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TmiViewHolder {
        // LayoutInflater coge el archivo de diseño XML (item_tmi) y lo "infla",
        // transformando ese código visual en un objeto real que la pantalla puede pintar.
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tmi, parent, false)
        // devuelvo esa vista ya fabricada
        return TmiViewHolder(view)
    }

    // RELLENAR LOS DATOS
    override fun onBindViewHolder(holder: TmiViewHolder, position: Int) {
        // Busca en nuestra lista de datos la linea de nuestra tarjeta que toca dibujar
        val actual = listaTmis[position]

        // Para imprimir el número en la primera columna. Le sumamos 1 porque las listas en programación empiezan en el número 0.
        // Utilizamos el recurso de texto oficial de Android para evitar el warning de concatenación.
        holder.tvNumFila.text =
            holder.itemView.context.getString(R.string.numero_fila, position + 1)

        // Rellenamos los textos de la fila con los datos reales que tiene nuestra tarjeta
        holder.tvNumeroTarjeta.text = actual.numeroTarjeta
        holder.tvCaducidad.text = actual.fechaCaducidad

        // 'itemView' es la fila completa. Le ponemos un setOnClickListener
        // Si el usuario toca esa fila, activamos el 'listener' y enviamos los datos de la tarjeta que tocó.
        holder.itemView.setOnClickListener { listener(actual) }
    }

    // CONTAR LOS ELEMENTOS
    // Con el siguiente metodo le decimos al sistema el número exacto de elementos que tiene la lista.
    // Así Android sabe de qué tamaño debe dibujar la barra de desplazamiento (scroll).
    override fun getItemCount() = listaTmis.size


    // ====================================================================
    // 2.2. EL VIEWHOLDER
    // ====================================================================
    // Esta clase anidada busca los textos una sola vez al principio y los guarda en memoria.
    class TmiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // Enlazamos las variables de Kotlin con los IDs que creé en el diseño XML.
        val tvNumFila: TextView = itemView.findViewById(R.id.tvItemNumFilaTmi)
        val tvNumeroTarjeta: TextView = itemView.findViewById(R.id.tvItemNumeroTarjeta)
        val tvCaducidad: TextView = itemView.findViewById(R.id.tvItemCaducidadTarjeta)
    }
}