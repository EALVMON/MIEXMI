package com.proyecto.miexmi

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PU11 {

    private lateinit var dbHelper: ExpedienteHelper

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        dbHelper = ExpedienteHelper(context)

        val db = dbHelper.writableDatabase
        // 1. Limpiamos primero la tabla "hija" y luego la "padre" para evitar errores de restricción
        db.execSQL("DELETE FROM MOD_EMPLEOS")
        db.execSQL("DELETE FROM USUARIO")
    }

    @Test
    fun cicloCompletoGestionEmpleos() {
        // 2. CREAMOS EL USUARIO PRIMERO ( para que la Foreign Key no falle)
        val dniPrueba = "12345678Z"
        val passCifrada = Utilidades.cifrarContrasena("pass123")

        // Registramos y obtenemos el ID real generado (debería ser 1, pero lo recuperamos por si acaso)
        val idUsuarioGenerado = dbHelper.registrarUsuario(dniPrueba, passCifrada).toInt()

        // Verificamos que el usuario se creó (si es -1, algo falló en el registro)
        assertNotEquals(
            "El usuario debe crearse correctamente para poder añadir empleos",
            -1,
            idUsuarioGenerado
        )

        // --- 3. ALTA: Ahora  añadimos el empleo con el ID real ---
        val exitoAlta = dbHelper.anadirEmpleo(idUsuarioGenerado, "Soldado", "01/01/2022", "10")
        assertTrue("El alta del empleo debería ser exitosa", exitoAlta)

        // --- 4. CONSULTA: Verificamos y obtenemos el ID del registro ---
        var cursor = dbHelper.obtenerEmpleos(idUsuarioGenerado)
        assertTrue("Debe existir el registro insertado", cursor.moveToFirst())

        val idRegistro = cursor.getInt(cursor.getColumnIndexOrThrow("Id_M_Empl"))
        val nombreLeido = cursor.getString(cursor.getColumnIndexOrThrow("Nom_Empleo"))
        assertEquals("El nombre inicial debe ser Soldado", "Soldado", nombreLeido)
        cursor.close()

        // --- 5. MODIFICACIÓN: Simulamos un ascenso ---
        val exitoModif = dbHelper.modificarEmpleo(idRegistro, "Cabo", "01/01/2024", "50")
        assertTrue("La modificación del registro debería ser exitosa", exitoModif)

        // Verificamos el cambio
        cursor = dbHelper.obtenerEmpleos(idUsuarioGenerado)
        cursor.moveToFirst()
        assertEquals(
            "El nombre debería haber cambiado a Cabo",
            "Cabo",
            cursor.getString(cursor.getColumnIndexOrThrow("Nom_Empleo"))
        )
        cursor.close()

        // --- 6. ELIMINACIÓN: Borramos el registro ---
        val exitoBorrado = dbHelper.eliminarEmpleo(idRegistro)
        assertTrue("La eliminación del registro debería ser exitosa", exitoBorrado)

        // Verificación final
        cursor = dbHelper.obtenerEmpleos(idUsuarioGenerado)
        assertFalse("Tras el borrado, el cursor no debería tener datos", cursor.moveToFirst())
        cursor.close()
    }
}