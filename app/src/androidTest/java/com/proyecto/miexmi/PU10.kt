package com.proyecto.miexmi

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PU10 {

    private lateinit var dbHelper: ExpedienteHelper
    private var idUsuarioTest = -1

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        dbHelper = ExpedienteHelper(context)

        val db = dbHelper.writableDatabase
        db.execSQL("DELETE FROM USUARIO")
        db.execSQL("DELETE FROM MOD_EMPLEOS")

        val passCifrada = Utilidades.cifrarContrasena("pass123")
        idUsuarioTest = dbHelper.registrarUsuario("12345678Z", passCifrada).toInt()

        // Insertamos dos registros con BODs claramente diferentes
        dbHelper.anadirEmpleo(idUsuarioTest, "Sargento", "15/07/2023", "150") // BOD 150
        dbHelper.anadirEmpleo(idUsuarioTest, "Cabo 1º", "10/05/2018", "170")  // BOD 170
    }

    @Test
    fun verificarFiltroPorBOD() {
        // --- EJECUCIÓN (CU.06 Búsqueda) ---
        // Buscamos EXCLUSIVAMENTE por el BOD "150"
        val resumenFiltrado = dbHelper.obtenerResumenExpediente(idUsuarioTest, "150", "")

        // --- VERIFICACIONES ---
        assertNotNull(resumenFiltrado)

        // 1. Debe mostrar lo que SÍ coincide (El Sargento tiene BOD 150)
        assertTrue("Debe encontrar el BOD 150 (Sargento)", resumenFiltrado.contains("Sargento"))

        // 2. Debe OCULTAR lo que NO coincide (El Cabo 1º tiene BOD 170)
        assertFalse("NO debe mostrar el BOD 170 (Cabo 1º)", resumenFiltrado.contains("Cabo 1º"))
    }
}