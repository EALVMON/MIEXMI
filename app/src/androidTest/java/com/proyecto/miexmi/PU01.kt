package com.proyecto.miexmi

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PU01 {

    private lateinit var dbHelper: ExpedienteHelper

    @Before
    fun setUp() {
        // Conseguimos el Contexto del emulador para la BD
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        dbHelper = ExpedienteHelper(context)

        // Limpiamos la tabla de usuarios antes de empezar la prueba
        val db = dbHelper.writableDatabase
        db.execSQL("DELETE FROM USUARIO")
    }

    @Test
    fun comprobarDniDuplicado() {
        val dniPrueba = "12345678Z"
        val passPrueba = "Soldado123"

        // Paso 1: Registramos al usuario por primera vez (debe tener éxito y no devolver -1)
        val primerResultado = dbHelper.registrarUsuario(dniPrueba, passPrueba)
        assertNotEquals("El primer registro debería tener éxito", -1L, primerResultado)

        // Paso 2: Intentamos registrar el MISMO DNI otra vez
        val segundoResultado = dbHelper.registrarUsuario(dniPrueba, "otraClave")

        // El test tiene éxito si el sistema LO BLOQUEA (devolviendo -1)
        assertEquals("La base de datos debería rechazar el DNI duplicado", -1L, segundoResultado)
    }
}