package com.proyecto.miexmi

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PU03 {

    @Test
    fun verificarCoincidenciaContrasenas() {
        //1-Las contraseñas son DIFERENTES -> Debe dar FALSO
        val passNueva = "Soldado123"
        val passRepetida = "Sargento123"

        //  Aquí estamos llamando a la  clase Utilidades.java donde compruebo si son iguales
        val coincidenDiferentes = Utilidades.contrasenasCoinciden(passNueva, passRepetida)
        assertFalse("El sistema debe rechazar contraseñas que no coinciden", coincidenDiferentes)

        // 2-Las contraseñas son IGUALES -> Debe dar VERDADERO
        val passCorrecta = "Capitan2024"
        val passRepetidaCorrecta = "Capitan2024"

        // Llamamos a la clase real de nuevo
        val coincidenIguales = Utilidades.contrasenasCoinciden(passCorrecta, passRepetidaCorrecta)
        assertTrue("El sistema debe aceptar contraseñas idénticas", coincidenIguales)
    }
}

