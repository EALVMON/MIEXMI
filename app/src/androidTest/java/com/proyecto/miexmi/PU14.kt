package com.proyecto.miexmi

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PU14 {

    private lateinit var dbHelper: ExpedienteHelper
    private var idUsuarioGenerado: Int = -1

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        dbHelper = ExpedienteHelper(context)

        val db = dbHelper.writableDatabase
        // 1. Limpiamos para tener un entorno controlado
        db.execSQL("DELETE FROM MOD_EMPLEOS")
        db.execSQL("DELETE FROM USUARIO")

        // 2. Creamos un usuario de prueba
        idUsuarioGenerado =
            dbHelper.registrarUsuario("12345678Z", Utilidades.cifrarContrasena("pass123")).toInt()

        // 3. Añadimos un dato real (un empleo) para que la exportación no esté vacía
        dbHelper.anadirEmpleo(idUsuarioGenerado, "Sargento", "15/05/2026", "100")
    }

    @Test
    fun verificarFormatosDeExportacion() {
        // ==========================================================
        // === PRUEBA A: EXPORTACIÓN EN FORMATO CSV (Excel)       ===
        // ==========================================================
        val textoCsv = dbHelper.exportarACsv(idUsuarioGenerado)

        // 1. Verificamos que no esté vacío y que contenga la clave UTF-8 para Excel
        assertNotNull("El CSV no debe ser nulo", textoCsv)
        assertTrue("Debe contener la marca BOM para español (UTF-8)", textoCsv.startsWith("\uFEFF"))

        // 2. Verificamos que tenga el título de la tabla y los datos que insertamos
        assertTrue("Debe contener el título de la sección", textoCsv.contains("--- EMPLEOS ---"))
        assertTrue(
            "Debe contener el empleo insertado separado por formato CSV",
            textoCsv.contains("Sargento")
        )
        assertTrue("Debe contener la fecha insertada", textoCsv.contains("15/05/2026"))


        // ==========================================================
        // === PRUEBA B: EXPORTACIÓN EN FORMATO JSON (Backup)     ===
        // ==========================================================
        val textoJson = dbHelper.exportarTodoAJson(idUsuarioGenerado)

        // 1. Verificamos la estructura básica de un JSON (Empieza por { y termina por })
        assertNotNull("El JSON no debe ser nulo", textoJson)
        val jsonLimpio = textoJson.trim()
        assertTrue(
            "El texto debe tener formato de objeto JSON",
            jsonLimpio.startsWith("{") && jsonLimpio.endsWith("}")
        )

        // 2. Intentamos convertir el texto en un Objeto JSON real para validarlo
        val raizJson = JSONObject(jsonLimpio)

        // Comprobamos que existe la lista de "Empleos"
        assertTrue("El JSON debe contener la etiqueta Empleos", raizJson.has("Empleos"))

        val listaEmpleos = raizJson.getJSONArray("Empleos")
        assertEquals("Debe haber exactamente 1 empleo en la lista", 1, listaEmpleos.length())

        // 3. Entramos al objeto y leemos el dato para ver si coincide
        val empleoGuardado = listaEmpleos.getJSONObject(0)
        assertEquals(
            "El empleo en el JSON debe ser Sargento",
            "Sargento",
            empleoGuardado.getString("Nom_Empleo")
        )
    }
}