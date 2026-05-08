package com.proyecto.miexmi

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PU05 {

    private lateinit var dbHelper: ExpedienteHelper

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        dbHelper = ExpedienteHelper(context)

        // Limpiamos la base de datos para tener un entorno de prueba controlado
        val db = dbHelper.writableDatabase
        db.execSQL("DELETE FROM USUARIO")

        // Registramos un usuario real para la segunda parte de la prueba
        // Nota: registrarUsuario ya debería cifrar o recibir la pass lista según tu lógica
        dbHelper.registrarUsuario("12345678Z", Utilidades.cifrarContrasena("ClaveCorrecta123"))
    }

    @Test
    fun verificarLoginErroneo() {
        //  DNI que no existe en la base de datos
        val loginDniInexistente = dbHelper.comprobarLogin("99999999R", Utilidades.cifrarContrasena("CualquierClave"))
        assertEquals("El sistema no debe permitir el acceso a un DNI no registrado", -1, loginDniInexistente)

        // DNI correcto pero contraseña incorrecta
        val loginPasswordMal = dbHelper.comprobarLogin("12345678Z", Utilidades.cifrarContrasena("ClaveErronea"))
        assertEquals("El sistema debe rechazar una contraseña que no coincide", -1, loginPasswordMal)
    }
}

