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
data class ArmaModelo(
    val id: Int,
    val nombre: String,
    val numSerie: String,
    val fecha: String,
)

// ====================================================================
// 2. EL ADAPTADOR
// ====================================================================
// Creamos la clase. El adaptador recibe la información desde fuera a través de su constructor:
class ArmaAdaptador(
    private val listaDatos: List<ArmaModelo>,
    // Función Lambda que avisa al tocar una fila.
    private val listener: (ArmaModelo) -> Unit,
) : RecyclerView.Adapter<ArmaAdaptador.ArmaViewHolder>() {

    // ====================================================================
    // 2.1. LOS TRES MÉTODOS OBLIGATORIOS
    // ====================================================================

    // CREAR LA VISTA VISUAL
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArmaViewHolder {
        // LayoutInflater coge el archivo de diseño XML (item_arma_particular) y lo "infla",
        // transformando ese código visual en un objeto real que la pantalla puede pintar.
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_arma_particular, parent, false)
        // Devuelvo esa vista ya fabricada
        return ArmaViewHolder(view)
    }

    // RELLENAR LOS DATOS
    override fun onBindViewHolder(holder: ArmaViewHolder, position: Int) {
        // Busca en nuestra lista de datos, la línea de nuestra arma que toca dibujar
        val actual = listaDatos[position]

        // Para imprimir el número en la primera columna. Le sumamos 1 porque las listas empiezan en 0.
        holder.tvNum.text = holder.itemView.context.getString(R.string.numero_fila, position + 1)

        // Rellenamos los textos de la fila con los datos reales que tiene nuestra arma
        holder.tvNom.text = actual.nombre
        holder.tvSerie.text = actual.numSerie
        holder.tvFec.text = actual.fecha

        // 'itemView' es la fila completa. Le ponemos un setOnClickListener
        // Si el usuario toca esa fila, activamos el 'listener' y enviamos los datos del arma que tocó.
        holder.itemView.setOnClickListener { listener(actual) }
    }

    // CONTAR LOS ELEMENTOS
    // Con el siguiente método le decimos al sistema el número exacto de elementos que tiene la lista.
    // Así Android sabe de qué tamaño debe dibujar la barra de desplazamiento (scroll).
    override fun getItemCount() = listaDatos.size


    // ====================================================================
    // 3. EL VIEWHOLDER
    // ====================================================================
    // Esta clase busca los textos una sola vez al principio y los guarda en memoria.
    class ArmaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Enlazamos las variables de Kotlin con los IDs que creé en el diseño XML.
        val tvNum: TextView = itemView.findViewById(R.id.tvItemNumArma)
        val tvNom: TextView = itemView.findViewById(R.id.tvItemNombreArma)
        val tvSerie: TextView = itemView.findViewById(R.id.tvItemNumSerieArma)
        val tvFec: TextView = itemView.findViewById(R.id.tvItemCaducidadArma)
    }
}
