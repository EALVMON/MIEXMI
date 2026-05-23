package com.proyecto.miexmi

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PU06 {

    private lateinit var dbHelper: ExpedienteHelper

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        dbHelper = ExpedienteHelper(context)

        val db = dbHelper.writableDatabase
        db.execSQL("DELETE FROM USUARIO")

        // REGISTRAMOS UN USUARIO REAL PARA PODER LOGUEARNOS

        val passCifrada = Utilidades.cifrarContrasena("Soldado2026")
        dbHelper.registrarUsuario("12345678Z", passCifrada)
    }

    @Test
    fun verificarLoginCorrecto() {
        // Intentamos entrar con los datos que acabamos de guardar
        val passCifradaParaLogin = Utilidades.cifrarContrasena("Soldado2026")
        val idUsuario = dbHelper.comprobarLogin("12345678Z", passCifradaParaLogin)

        // El test tiene éxito si el ID es distinto de -1 (es decir, el usuario existe)
        assertNotEquals(
            "El sistema debe permitir el acceso con credenciales correctas",
            -1,
            idUsuario
        )
    }
}