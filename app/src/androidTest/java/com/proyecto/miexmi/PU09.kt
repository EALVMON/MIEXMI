package com.proyecto.miexmi

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PU09 {

    private lateinit var dbHelper: ExpedienteHelper
    private var idUsuarioTest = -1

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        dbHelper = ExpedienteHelper(context)

        // 1. Limpiamos la base de datos para la prueba
        val db = dbHelper.writableDatabase
        db.execSQL("DELETE FROM USUARIO")
        db.execSQL("DELETE FROM MOD_EMPLEOS")

        // 2. Registramos un usuario
        val passCifrada = Utilidades.cifrarContrasena("pass123")
        idUsuarioTest = dbHelper.registrarUsuario("12345678Z", passCifrada).toInt()

        // 3. Insertamos un par de empleos usando tu metodo habitual
        dbHelper.anadirEmpleo(idUsuarioTest, "Sargento", "15/07/2023", "145")
        dbHelper.anadirEmpleo(idUsuarioTest, "Cabo 1º", "10/05/2018", "90")
    }

    @Test
    fun verificarResumenExpedienteCompleto() {

        // Le pedimos el resumen sin filtros ("" y "")
        val resumen = dbHelper.obtenerResumenExpediente(idUsuarioTest, "", "")

        // --- VERIFICACIONES ---
        assertNotNull("El texto generado no debe ser nulo", resumen)

        // Comprobamos que traiga  el contenido insertado completo
        assertTrue("Debe contener el empleo de Sargento", resumen.contains("Sargento"))
        assertTrue("Debe contener el empleo de Cabo 1º", resumen.contains("Cabo 1º"))
        assertTrue("Debe mantener el formato de separadores", resumen.contains("==="))
    }
}