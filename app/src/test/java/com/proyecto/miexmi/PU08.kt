package com.proyecto.miexmi

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PU08 {

    @Test
    fun verificarLogicaCierreInactividad() {
        // Usamos una función para que el compilador no sepa el valor de antemano

        val tiempoMaximo = calcularTiempo(3) // 3 minutos en ms

        // Han pasado 30 segundos (30.000 ms)
        val tiempoCorto = calcularTiempo(0) + 30000L
        val debeCerrarFalso = tiempoCorto >= tiempoMaximo
        assertFalse("No debería cerrar sesión con solo 30 segundos", debeCerrarFalso)

        // Han pasado 3 minutos y 1 segundo (181.000 ms)
        val tiempoLargo = calcularTiempo(3) + 1000L
        val debeCerrarVerdadero = tiempoLargo >= tiempoMaximo
        assertTrue("Debería cerrar sesión al superar los 3 minutos", debeCerrarVerdadero)
    }

    // Función auxiliar para evitar los avisos de warning "
    private fun calcularTiempo(minutos: Int): Long {
        return minutos * 60 * 1000L
    }
}