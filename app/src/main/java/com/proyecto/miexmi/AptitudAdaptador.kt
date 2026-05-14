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
// Kotlin hace todos eso por ti en una sola línea. Simplemente define qué datos tiene una Aptitud.
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

    // Recibe una "acción" (una función Lambda). Es el cable que usaremos para
    // avisar a la pantalla principal cuando el usuario toque una fila.
    private val listener: (AptitudModelo) -> Unit

// Los dos puntos ':' significan que esta clase hereda de RecyclerView.Adapter.
// Es decir, le promete a Android que va a cumplir las reglas para ser un adaptador oficial.
) : RecyclerView.Adapter<AptitudAdaptador.AptitudViewHolder>() {


    // ====================================================================
    // 3. LOS TRES MÉTODOS OBLIGATORIOS
    // ====================================================================

    // PASO A: CREAR LA VISTA VISUAL (La carcasa vacía)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AptitudViewHolder {
        // LayoutInflater coge tu archivo de diseño XML (item_aptitud) y lo "infla",
        // transformando ese código visual en un objeto real que la pantalla puede pintar.
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_aptitud, parent, false)

        // Mete esa vista ya fabricada dentro de nuestra caja fuerte (AptitudViewHolder) y la devuelve.
        return AptitudViewHolder(view)
    }

    // PASO B: RELLENAR LOS DATOS (Poner la información en la carcasa)
    override fun onBindViewHolder(holder: AptitudViewHolder, position: Int) {
        // Busca en nuestra lista de datos la aptitud exacta que toca dibujar ahora mismo (su 'position').
        val actual = listaDatos[position]

        // Accedemos a los recursos de idiomas (strings.xml) y usamos la plantilla "numero_fila"
        // para imprimir el número. Le sumamos 1 porque las listas en programación empiezan en el número 0.
        holder.tvNumFila.text = holder.itemView.context.getString(R.string.numero_fila, position + 1)

        // Rellenamos los textos de la fila con los datos reales que tiene nuestra aptitud 'actual'.
        holder.tvNombre.text = actual.nombre
        holder.tvFechaBod.text = actual.fechaBod
        holder.tvNumBod.text = actual.numBod

        // 'itemView' es la fila completa. Le ponemos un "oreja" (setOnClickListener).
        // Si el usuario toca esa fila, activamos el 'listener' y enviamos los datos de la aptitud que tocó.
        holder.itemView.setOnClickListener { listener(actual) }
    }

    // PASO C: CONTAR LOS ELEMENTOS
    // Le chiva al sistema el número exacto de elementos que tiene la lista.
    // Así Android sabe de qué tamaño debe dibujar la barra de desplazamiento (scroll).
    override fun getItemCount() = listaDatos.size


    // ====================================================================
    // 4. EL VIEWHOLDER (La caja fuerte de rendimiento)
    // ====================================================================
    // Buscar cosas en la pantalla con 'findViewById' gasta mucha batería y memoria.
    // Esta clase busca los textos una sola vez al principio y los "cachea" (los guarda en memoria).
    class AptitudViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // Enlazamos las variables de Kotlin con los IDs que creaste en el diseño XML.
        val tvNumFila: TextView = itemView.findViewById(R.id.tvItemNumAptitud)
        val tvNombre: TextView = itemView.findViewById(R.id.tvItemNombreAptitud)
        val tvFechaBod: TextView = itemView.findViewById(R.id.tvItemFechaAptitud)
        val tvNumBod: TextView = itemView.findViewById(R.id.tvItemBodAptitud)
    }
}