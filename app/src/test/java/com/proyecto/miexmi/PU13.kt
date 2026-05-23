package com.proyecto.miexmi

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PU13 {

    @Test
    fun verificarLogicaAlertasTMI() {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        // --- CASO 1: TMI que caduca en 1 mes (DENTRO DEL RANGO DE 3 MESES) ---
        val calProxima = Calendar.getInstance()
        calProxima.add(Calendar.MONTH, 1)
        val fechaProxima = sdf.format(calProxima.time)

        // Debe ser TRUE porque está entre HOY y HOY + 3 meses
        assertTrue(
            "Debería avisar si caduca en un mes",
            Utilidades.estaCercaDeCaducar(fechaProxima)
        )

        // --- CASO 2: TMI que caduca en 5 meses (FUERA DEL RANGO) ---
        val calLejana = Calendar.getInstance()
        calLejana.add(Calendar.MONTH, 5)
        val fechaLejana = sdf.format(calLejana.time)

        // Debe ser FALSE porque supera el límite de 3 meses
        assertFalse(
            "No debería avisar si faltan 5 meses",
            Utilidades.estaCercaDeCaducar(fechaLejana)
        )

        // --- CASO 3: TMI que ya caducó hace 10 días ---
        val calPasada = Calendar.getInstance()
        calPasada.add(Calendar.DAY_OF_YEAR, -10)
        val fechaPasada = sdf.format(calPasada.time)

        // Según tu código (fechaCaducidad.after(hoy)), esto devuelve FALSE
        assertFalse(
            "No avisa si ya está caducada (lógica after(hoy))",
            Utilidades.estaCercaDeCaducar(fechaPasada)
        )

        // --- CASO 4: Texto que no es una fecha (Seguridad) ---
        // Mi catch devuelve false, evitando que la app se cierre
        assertFalse(
            "Debe devolver false si el texto es inválido",
            Utilidades.estaCercaDeCaducar("error_fecha")
        )
    }
}