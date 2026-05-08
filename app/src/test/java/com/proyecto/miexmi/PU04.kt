package com.proyecto.miexmi

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PU04 {

    @Test
    fun verificarCamposVaciosLogin() {
        // Caso A: Ambos campos vacíos -> Debe dar FALSO (bloquear)
        val dniVacio = ""
        val passVacia = ""
        assertFalse("Debe fallar si ambos están vacíos", Utilidades.camposRellenos(dniVacio, passVacia))

        // Caso B: Solo DNI vacío -> Debe dar FALSO
        val passOk = "Soldado123"
        assertFalse("Debe fallar si el DNI está vacío", Utilidades.camposRellenos("", passOk))

        // Caso C: Solo Password vacío -> Debe dar FALSO
        val dniOk = "12345678Z"
        assertFalse("Debe fallar si la clave está vacía", Utilidades.camposRellenos(dniOk, ""))

        // Caso D: Ambos rellenos -> Debe dar VERDADERO (permitir continuar)
        assertTrue("Debe aceptar si ambos campos tienen texto", Utilidades.camposRellenos(dniOk, passOk))
    }
}

