package com.proyecto.miexmi

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PU02 {

    @Test
    fun verificarLongitudPassword() {
        // 1-Contraseña corta (7 caracteres) -> Debe dar FALSO
        val passCorta = "1234567"
        // Llamamos directamente a tu lógica real de la App
        assertFalse(
            "La contraseña de 7 caracteres debe ser rechazada",
            Utilidades.esPasswordSegura(passCorta)
        )

        // 2- Contraseña límite (8 caracteres) -> Debe dar VERDADERO
        val passCorrecta = "12345678"
        assertTrue(
            "La contraseña de 8 caracteres debe ser aceptada",
            Utilidades.esPasswordSegura(passCorrecta)
        )

        // C3-Contraseña larga (12 caracteres) -> Debe dar VERDADERO
        val passLarga = "SoldadoBase1"
        assertTrue("La contraseña larga debe ser aceptada", Utilidades.esPasswordSegura(passLarga))
    }
}

