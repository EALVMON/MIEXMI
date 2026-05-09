package com.proyecto.miexmi

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PU12 {

    @Test
    fun verificarValidacionCamposObligatoriosModulo() {
        // Simulamos los campos que el usuario rellenaría en la pantalla de Empleos
        val empleoValido = "Cabo Primero"
        val fechaValida = "10/05/2026"
        val bodValido = "123"

        // CASO A: El usuario olvida el nombre del empleo (Campo obligatorio)
        val nombreVacio = ""
        val resultadoA = Utilidades.camposRellenos(nombreVacio, fechaValida, bodValido)
        assertFalse("El sistema debe detectar que falta el nombre del empleo", resultadoA)

        // CASO B: El usuario olvida la fecha (Campo obligatorio)
        val fechaVacia = " "
        val resultadoB = Utilidades.camposRellenos(empleoValido, fechaVacia, bodValido)
        assertFalse("El sistema debe detectar que falta la fecha", resultadoB)

        // CASO C: Esta  rellenado correctamente
        val resultadoC = Utilidades.camposRellenos(empleoValido, fechaValida, bodValido)
        assertTrue("El sistema debe permitir el paso si todo está relleno", resultadoC)
    }
}