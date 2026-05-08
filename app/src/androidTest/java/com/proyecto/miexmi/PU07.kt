package com.proyecto.miexmi

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PU07 {

    private lateinit var dbHelper: ExpedienteHelper

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        dbHelper = ExpedienteHelper(context)

        val db = dbHelper.writableDatabase
        // 1. Limpiamos primero la tabla de registros (hija) y luego la de usuarios (padre)
        // para evitar errores de restricción al borrar
        db.execSQL("DELETE FROM REGISTRO_ACTIVIDAD")
        db.execSQL("DELETE FROM USUARIO")
    }

    @Test
    fun verificarHistorialDeAccesos() {
        // 2. CREAR EL USUARIO PRIMERO (Vital para que la Foreign Key no falle)
        val dniPrueba = "12345678Z"
        val passCifrada = Utilidades.cifrarContrasena("Pass123")

        // Registramos y obtenemos el ID real que le asigne la base de datos
        val idGenerado = dbHelper.registrarUsuario(dniPrueba, passCifrada).toInt()

        // Verificamos que el usuario se creó bien
        assertNotEquals("El usuario debe crearse correctamente", -1, idGenerado)

        // 3. REGISTRAR EL ACCESO
        val fechaPrueba = "15/05/2026 21:00:00"
        val resultadoInsert = dbHelper.registrarAcceso(idGenerado, dniPrueba, fechaPrueba)

        assertTrue("El método registrarAcceso debe devolver true", resultadoInsert)

        // 4. VERIFICAR QUE SE HA GUARDADO
        val cursor = dbHelper.obtenerAccesos(idGenerado)

        assertNotNull("El cursor no debe ser nulo", cursor)
        assertTrue("El historial debe tener al menos un registro", cursor.moveToFirst())

        // Comprobamos los datos
        val dniGuardado = cursor.getString(cursor.getColumnIndexOrThrow("DNI"))
        assertEquals("El DNI guardado no coincide", dniPrueba, dniGuardado)

        cursor.close()
    }
}