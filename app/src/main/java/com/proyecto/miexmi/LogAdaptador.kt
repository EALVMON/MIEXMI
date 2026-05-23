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
data class LogModelo(
    val dni: String,       // Guarda el DNI del usuario que accedió (texto)
    val fechaHora: String,  // Guarda el momento exacto del acceso (texto)
)

// ====================================================================
// 2. EL ADAPTADOR
// ====================================================================
// Creamos la clase. El adaptador recibe la información desde fuera a través de su constructor:
class LogAdaptador(
    // Recibe la lista completa con todos los registros del log que hay que mostrar.
    private val listaDatos: List<LogModelo>,
) : RecyclerView.Adapter<LogAdaptador.LogViewHolder>() {

    // ====================================================================
    // 2.1 LOS TRES MÉTODOS OBLIGATORIOS
    // ====================================================================

    // CREAR LA VISTA VISUAL
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        // LayoutInflater coge el archivo de diseño XML (item_log_actividad) y lo "infla",
        // transformando ese código visual en un objeto real que la pantalla puede pintar.
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_log_actividad, parent, false)
        // devuelvo esa vista ya fabricada
        return LogViewHolder(view)
    }

    // RELLENAR LOS DATOS
    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        // Busca en nuestra lista de datos la linea de nuestro log que toca dibujar
        val actual = listaDatos[position]

        // Para imprimir el número en la primera columna. Le sumamos 1 porque las listas en programación empiezan en el número 0.
        // Utilizamos el recurso de texto oficial de Android para evitar el warning de concatenación.
        holder.tvNum.text = holder.itemView.context.getString(R.string.numero_fila, position + 1)

        // Rellenamos los textos de la fila con los datos reales que tiene nuestro registro de log
        holder.tvDni.text = actual.dni
        holder.tvFec.text = actual.fechaHora
    }

    // CONTAR LOS ELEMENTOS
    // Con el siguiente metodo le decimos al sistema el número exacto de elementos que tiene la lista.
    // Así Android sabe de qué tamaño debe dibujar la barra de desplazamiento (scroll).
    override fun getItemCount() = listaDatos.size


    // ====================================================================
    // 2.2. EL VIEWHOLDER
    // ====================================================================
    // Esta clase anidada busca los textos una sola vez al principio y los guarda en memoria.
    class LogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // Enlazamos las variables de Kotlin con los IDs que creé en el diseño XML.
        val tvNum: TextView = itemView.findViewById(R.id.tvIdLog)
        val tvDni: TextView = itemView.findViewById(R.id.tvDniLog)
        val tvFec: TextView = itemView.findViewById(R.id.tvFechaHoraLog)
    }
}