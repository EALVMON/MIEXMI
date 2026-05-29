package com.proyecto.miexmi

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
// Importaciones necesarias para exportar datos
import org.json.JSONArray
import org.json.JSONObject

class ExpedienteHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "MiExpediente.db"
        const val DATABASE_VERSION = 1

        // ====================================================================
        // === DEFINICIÓN DE TABLAS 1:1                                     ===
        // ====================================================================

        private const val SQL_CREATE_USUARIO = """
            CREATE TABLE USUARIO (
                Id_Usuario INTEGER PRIMARY KEY AUTOINCREMENT,
                Dni TEXT UNIQUE NOT NULL,
                Contraseña TEXT NOT NULL
            )
        """

        private const val SQL_CREATE_FILIACION = """
            CREATE TABLE FILIACION (
                Id_Filia INTEGER PRIMARY KEY AUTOINCREMENT,
                Id_Usuario INTEGER UNIQUE,
                Nombre TEXT NOT NULL,
                Apellidos TEXT NOT NULL,
                TMI TEXT,
                Fech_Incorp TEXT,
                Nun_Escalafon INTEGER,
                FOREIGN KEY(Id_Usuario) REFERENCES USUARIO(Id_Usuario) ON DELETE CASCADE
            )
        """

        // ====================================================================
        // === TABLAS DE MÓDULOS (RELACIONES 1:N)                           ===
        // ====================================================================

        private const val SQL_CREATE_REGISTRO_ACTIVIDAD = """
            CREATE TABLE REGISTRO_ACTIVIDAD (
                Id_Log INTEGER PRIMARY KEY AUTOINCREMENT,
                Id_Usuario INTEGER,
                DNI TEXT,
                Fecha_Hora TEXT,
                FOREIGN KEY(Id_Usuario) REFERENCES USUARIO(Id_Usuario) ON DELETE CASCADE
            )
        """


        private const val SQL_CREATE_DESTINOS = """
            CREATE TABLE MOD_DESTINOS (
                Id_M_Dest INTEGER PRIMARY KEY AUTOINCREMENT,
                Id_Usuario INTEGER,
                Nom_Destino TEXT NOT NULL,
                M_Dest_Fecha_Bod TEXT,
                M_Dest_Nbod INTEGER,
                FOREIGN KEY(Id_Usuario) REFERENCES USUARIO(Id_Usuario) ON DELETE CASCADE
            )
        """

        private const val SQL_CREATE_EMPLEOS = """
            CREATE TABLE MOD_EMPLEOS (
                Id_M_Empl INTEGER PRIMARY KEY AUTOINCREMENT,
                Id_Usuario INTEGER,
                Nom_Empleo TEXT NOT NULL,
                M_Empl_Fecha_Bod TEXT,
                M_Empl_Nbod INTEGER,
                FOREIGN KEY(Id_Usuario) REFERENCES USUARIO(Id_Usuario) ON DELETE CASCADE
            )
        """

        private const val SQL_CREATE_MISIONES = """
            CREATE TABLE MOD_MISIONES (
                Id_M_Misi INTEGER PRIMARY KEY AUTOINCREMENT,
                Id_Usuario INTEGER,
                Nom_Mision TEXT NOT NULL,
                M_Misi_Fecha_Bod TEXT,
                M_Misi_Nbod INTEGER,
                FOREIGN KEY(Id_Usuario) REFERENCES USUARIO(Id_Usuario) ON DELETE CASCADE
            )
        """

        private const val SQL_CREATE_COMISION_SER = """
            CREATE TABLE MOD_COMISION_SER (
                Id_M_Cser INTEGER PRIMARY KEY AUTOINCREMENT,
                Id_Usuario INTEGER,
                Nom_Comision TEXT NOT NULL,
                M_Cser_Fecha_Bod TEXT,
                M_Cser_Nbod INTEGER,
                FOREIGN KEY(Id_Usuario) REFERENCES USUARIO(Id_Usuario) ON DELETE CASCADE
            )
        """

        private const val SQL_CREATE_SITUA_ADMIN = """
            CREATE TABLE MOD_SITUA_ADMIN (
                Id_M_Sadm INTEGER PRIMARY KEY AUTOINCREMENT,
                Id_Usuario INTEGER,
                Nom_Sit_Admini TEXT NOT NULL,
                M_Sadm_Fecha_Bod TEXT,
                M_Sadm_Nbod INTEGER,
                FOREIGN KEY(Id_Usuario) REFERENCES USUARIO(Id_Usuario) ON DELETE CASCADE
            )
        """

        private const val SQL_CREATE_TRIENIOS = """
            CREATE TABLE MOD_TRIENIOS (
                Id_M_Trie INTEGER PRIMARY KEY AUTOINCREMENT,
                Id_Usuario INTEGER,
                Tipo_Trienio TEXT NOT NULL,
                M_Trie_Fecha_Bod TEXT,
                M_Trie_Nbod INTEGER,
                FOREIGN KEY(Id_Usuario) REFERENCES USUARIO(Id_Usuario) ON DELETE CASCADE
            )
        """

        private const val SQL_CREATE_RECOMPENSAS = """
            CREATE TABLE MOD_RECOMPENSAS (
                Id_M_Reco INTEGER PRIMARY KEY AUTOINCREMENT,
                Id_Usuario INTEGER,
                Nom_Recompensa TEXT NOT NULL,
                M_Reco_Fecha_Bod TEXT,
                M_Reco_Nbod INTEGER,
                FOREIGN KEY(Id_Usuario) REFERENCES USUARIO(Id_Usuario) ON DELETE CASCADE
            )
        """

        private const val SQL_CREATE_DISTINTIVOS = """
            CREATE TABLE MOD_DISTINTIVOS (
                Id_M_Dist INTEGER PRIMARY KEY AUTOINCREMENT,
                Id_Usuario INTEGER,
                Nom_Distintivo TEXT NOT NULL,
                M_Dist_Fecha_Bod TEXT,
                M_Dist_Nbod INTEGER,
                FOREIGN KEY(Id_Usuario) REFERENCES USUARIO(Id_Usuario) ON DELETE CASCADE
            )
        """

        private const val SQL_CREATE_APTITUDES = """
            CREATE TABLE MOD_APTITUDES (
                Id_M_Apti INTEGER PRIMARY KEY AUTOINCREMENT,
                Id_Usuario INTEGER,
                Nom_Aptitud TEXT NOT NULL,
                M_Apti_Fecha_Bod TEXT,
                M_Apti_Nbod INTEGER,
                FOREIGN KEY(Id_Usuario) REFERENCES USUARIO(Id_Usuario) ON DELETE CASCADE
            )
        """

        private const val SQL_CREATE_CEE_FUNDAMENTAL = """
            CREATE TABLE MOD_CEE_FUNDAMENTAL (
                Id_M_CEEF INTEGER PRIMARY KEY AUTOINCREMENT,
                Id_Usuario INTEGER,
                Nom_CEEF TEXT NOT NULL,
                M_Ceef_Fecha_Bod TEXT,
                M_Ceef_Nbod INTEGER,
                FOREIGN KEY(Id_Usuario) REFERENCES USUARIO(Id_Usuario) ON DELETE CASCADE
            )
        """

        private const val SQL_CREATE_RELA_ADMINISTRACION = """
            CREATE TABLE MOD_RELA_ADMINISTRACION (
                Id_M_Radm INTEGER PRIMARY KEY AUTOINCREMENT,
                Id_Usuario INTEGER,
                Nom_Rel_Admin TEXT NOT NULL,
                M_Radm_Fecha_Bod TEXT,
                M_Radm_Nbod INTEGER,
                FOREIGN KEY(Id_Usuario) REFERENCES USUARIO(Id_Usuario) ON DELETE CASCADE
            )
        """

        private const val SQL_CREATE_CUR_MILITAR = """
            CREATE TABLE MOD_CUR_MILITAR (
                Id_M_Cmili INTEGER PRIMARY KEY AUTOINCREMENT,
                Id_Usuario INTEGER,
                Nom_Cur_Mili TEXT NOT NULL,
                M_Cmili_Fecha_Bod TEXT,
                M_Cmili_Nbod INTEGER,
                FOREIGN KEY(Id_Usuario) REFERENCES USUARIO(Id_Usuario) ON DELETE CASCADE
            )
        """

        private const val SQL_CREATE_TITULOS_CIVILES = """
            CREATE TABLE MOD_TITULOS_CIVILES (
                Id_M_Tcivi INTEGER PRIMARY KEY AUTOINCREMENT,
                Id_Usuario INTEGER,
                Nom_Titulo TEXT NOT NULL,
                FOREIGN KEY(Id_Usuario) REFERENCES USUARIO(Id_Usuario) ON DELETE CASCADE
            )
        """

        private const val SQL_CREATE_EVALUACION_ASCENSO = """
            CREATE TABLE MOD_EVALUACION_ASCENSO (
                Id_M_Eva INTEGER PRIMARY KEY AUTOINCREMENT,
                Id_Usuario INTEGER,
                Nom_Evaluacion TEXT NOT NULL,
                Resultado TEXT,
                M_Eva_Fecha_Bod TEXT,
                M_Eva_Nbod INTEGER,
                FOREIGN KEY(Id_Usuario) REFERENCES USUARIO(Id_Usuario) ON DELETE CASCADE
            )
        """

        private const val SQL_CREATE_HPS = """
            CREATE TABLE MOD_HPS (
                Id_M_Hps INTEGER PRIMARY KEY AUTOINCREMENT,
                Id_Usuario INTEGER,
                Nom_Habilitacion TEXT NOT NULL,
                Fecha_M_Concesion TEXT,
                Fecha_M_Caducidad TEXT,
                FOREIGN KEY(Id_Usuario) REFERENCES USUARIO(Id_Usuario) ON DELETE CASCADE
            )
        """

        private const val SQL_CREATE_TMI = """
            CREATE TABLE MOD_TMI (
                Id_M_Tmi INTEGER PRIMARY KEY AUTOINCREMENT,
                Id_Usuario INTEGER,
                N_Tarjeta TEXT NOT NULL,
                M_Tmi_Fecha_Cadu TEXT,
                FOREIGN KEY(Id_Usuario) REFERENCES USUARIO(Id_Usuario) ON DELETE CASCADE
            )
        """

        private const val SQL_CREATE_EXP_ARMAS = """
            CREATE TABLE MOD_EXP_ARMAS (
                Id_M_EArm INTEGER PRIMARY KEY AUTOINCREMENT,
                Id_Usuario INTEGER,
                Nom_Arma TEXT NOT NULL,
                M_EArm_Nserie TEXT UNIQUE,
                M_EArm_Fecha_Cad TEXT,
                FOREIGN KEY(Id_Usuario) REFERENCES USUARIO(Id_Usuario) ON DELETE CASCADE
            )
        """

        private const val SQL_CREATE_TCGF = """
            CREATE TABLE MOD_TCGF (
                Id_M_TCGF INTEGER PRIMARY KEY AUTOINCREMENT,
                Id_Usuario INTEGER,
                M_Tcgf_Fecha TEXT,
                M_Tcgf_Puntuacion TEXT,
                M_Tcgf_Apto TEXT,
                FOREIGN KEY(Id_Usuario) REFERENCES USUARIO(Id_Usuario) ON DELETE CASCADE
            )
        """

        private const val SQL_CREATE_CARNET = """
            CREATE TABLE MOD_CARNET (
                Id_M_Carnet INTEGER PRIMARY KEY AUTOINCREMENT,
                Id_Usuario INTEGER,
                Tipo_Carnet TEXT NOT NULL,
                M_Carn_Fecha_Concesion TEXT,
                M_Carn_Fecha_Caducidad TEXT,
                FOREIGN KEY(Id_Usuario) REFERENCES USUARIO(Id_Usuario) ON DELETE CASCADE
            )
        """

        private const val SQL_CREATE_IDIOMA = """
            CREATE TABLE MOD_IDIOMA (
                Id_M_Idi INTEGER PRIMARY KEY AUTOINCREMENT,
                Id_Usuario INTEGER,
                Nom_idioma TEXT NOT NULL,
                Resultado TEXT,
                M_Idi_Fecha_Bod TEXT,
                M_Idi_Nbod INTEGER,
                FOREIGN KEY(Id_Usuario) REFERENCES USUARIO(Id_Usuario) ON DELETE CASCADE
            )
        """
    }

    // ====================================================================
    // === MÉTODOS OBLIGATORIOS DE SQLiteOpenHelper                     ===
    // ====================================================================

    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        // Activar el soporte para Foreign Keys para que me funcione correctamente el borrado en cascada
        db.setForeignKeyConstraintsEnabled(true)
    }

    override fun onCreate(db: SQLiteDatabase) {
        // En la  creacion  Ejecutamos la creación de todas las tablas
        db.execSQL(SQL_CREATE_USUARIO)
        db.execSQL(SQL_CREATE_FILIACION)
        db.execSQL(SQL_CREATE_REGISTRO_ACTIVIDAD)
        db.execSQL(SQL_CREATE_DESTINOS)
        db.execSQL(SQL_CREATE_EMPLEOS)
        db.execSQL(SQL_CREATE_MISIONES)
        db.execSQL(SQL_CREATE_COMISION_SER)
        db.execSQL(SQL_CREATE_SITUA_ADMIN)
        db.execSQL(SQL_CREATE_TRIENIOS)
        db.execSQL(SQL_CREATE_RECOMPENSAS)
        db.execSQL(SQL_CREATE_DISTINTIVOS)
        db.execSQL(SQL_CREATE_APTITUDES)
        db.execSQL(SQL_CREATE_CEE_FUNDAMENTAL)
        db.execSQL(SQL_CREATE_RELA_ADMINISTRACION)
        db.execSQL(SQL_CREATE_CUR_MILITAR)
        db.execSQL(SQL_CREATE_TITULOS_CIVILES)
        db.execSQL(SQL_CREATE_EVALUACION_ASCENSO)
        db.execSQL(SQL_CREATE_HPS)
        db.execSQL(SQL_CREATE_TMI)
        db.execSQL(SQL_CREATE_EXP_ARMAS)
        db.execSQL(SQL_CREATE_TCGF)
        db.execSQL(SQL_CREATE_CARNET)
        db.execSQL(SQL_CREATE_IDIOMA)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // En la actualizacion de la version de la BBDD, Se borran las tablas antiguas en orden
        // inverso a sus dependencias
        db.execSQL("DROP TABLE IF EXISTS MOD_IDIOMA")
        db.execSQL("DROP TABLE IF EXISTS MOD_CARNET")
        db.execSQL("DROP TABLE IF EXISTS MOD_TCGF")
        db.execSQL("DROP TABLE IF EXISTS MOD_EXP_ARMAS")
        db.execSQL("DROP TABLE IF EXISTS MOD_TMI")
        db.execSQL("DROP TABLE IF EXISTS MOD_HPS")
        db.execSQL("DROP TABLE IF EXISTS MOD_EVALUACION_ASCENSO")
        db.execSQL("DROP TABLE IF EXISTS MOD_TITULOS_CIVILES")
        db.execSQL("DROP TABLE IF EXISTS MOD_CUR_MILITAR")
        db.execSQL("DROP TABLE IF EXISTS MOD_RELA_ADMINISTRACION")
        db.execSQL("DROP TABLE IF EXISTS MOD_CEE_FUNDAMENTAL")
        db.execSQL("DROP TABLE IF EXISTS MOD_APTITUDES")
        db.execSQL("DROP TABLE IF EXISTS MOD_DISTINTIVOS")
        db.execSQL("DROP TABLE IF EXISTS MOD_RECOMPENSAS")
        db.execSQL("DROP TABLE IF EXISTS MOD_TRIENIOS")
        db.execSQL("DROP TABLE IF EXISTS MOD_SITUA_ADMIN")
        db.execSQL("DROP TABLE IF EXISTS MOD_COMISION_SER")
        db.execSQL("DROP TABLE IF EXISTS MOD_MISIONES")
        db.execSQL("DROP TABLE IF EXISTS MOD_EMPLEOS")
        db.execSQL("DROP TABLE IF EXISTS MOD_DESTINOS")
        db.execSQL("DROP TABLE IF EXISTS REGISTRO_ACTIVIDAD")
        db.execSQL("DROP TABLE IF EXISTS FILIACION")
        db.execSQL("DROP TABLE IF EXISTS USUARIO")

        // Se vuelven a crear
        onCreate(db)
    }


    // ====================================================================
    // === MÉTODOS DE USUARIO (LOGIN Y REGISTRO)                        ===
    // ====================================================================

    // Devuelve el ID del nuevo usuario, o -1 si el DNI ya existe
    fun registrarUsuario(dni: String, contrasena: String): Long {
        val db = this.writableDatabase // ponemos la BBDD en modo escritura
        val values = android.content.ContentValues().apply {
            put("Dni", dni)
            put("Contraseña", contrasena)
        }
        // El insert devuelve -1 si falla (ej: si el  DNI esta repetido)
        return db.insert("USUARIO", null, values)
    }

    //  La siguiente funcion, comprueba si un usuario existe en la base de datos y si su contraseña
    //  es correcta.
    // Devuelve el Id_Usuario (ej.: 1, 2, 3...) si  es correcto, o -1 si falla.
    fun comprobarLogin(dni: String, contrasena: String): Int {

        // 1. Abrimos la base de datos en modo lectura.
        // Usamos el modo lectura porque solo vamos a buscar información, no a guardar nada nuevo.
        val db = this.readableDatabase

        // 2. Lanzamos la pregunta (Query) a la base de datos usando el 'Cursor', que es un objeto de tipo Cursor.
        // Las interrogaciones (?) son el valor por el que sustituirá después , las variables qye le paso en este
        // caso se pondrá el DNI en la primera '?', y la clave en la segunda.
        val cursor = db.rawQuery(
            "SELECT Id_Usuario FROM USUARIO WHERE Dni = ? AND Contraseña = ?",
            arrayOf(dni, contrasena)
        )

        // 3. Preparamos una variable con valor -1 (Asi asumimos que el usuario no existe por defecto).
        var idUsuario = -1

        // 4. El cursor intenta moverse al primer resultado que encontró en la tabla (si existe).
        // Si 'moveToFirst()' es verdadero (true), significa que SÍ encontró a ese usuario.
        if (cursor.moveToFirst()) {

            // Como sí lo encontró, leemos el dato de la columna 0. que el Id_Usuario, que es lo que
            // solicitamos en el query sql
            idUsuario = cursor.getInt(0)
        }

        // 5.  Hay que cerrar el cursor al terminar.

        cursor.close()

        // 6. Devolvemos la respuesta final a la pantalla de Login (el ID real o él -1 si no lo encuentra).
        return idUsuario
    }

    // Obtiene el DNI de un usuario a partir de su ID
    fun obtenerDniPorId(idUsuario: Int): String? {
        val db =
            this.readableDatabase // la abrimos la BBDD en modo lectura ya que no vamos escribir nada en ella
        val cursor = db.rawQuery(
            "SELECT Dni FROM USUARIO WHERE Id_Usuario = ?",
            //al igual que en la anterior consulta el  rawQuery espera que se le pasen los valores
            // de ls ? en forma de array y que sea un String por eso lo paso antes a string el idUsuario
            arrayOf(idUsuario.toString())
        )

        var dni: String? =
            null //pongo String? ya que esta variable puede ser no mutable y no tener valor y la inicializo a nula
        if (cursor.moveToFirst()) {
            dni = cursor.getString(0) // le digo que al haber un resultado lo coja
        }
        cursor.close()
        return dni // Si entra en el if devuelve el dni sino devuelve nulo
    }

    // Cambia la contraseña si la actual es correcta. Devuelve true si tiene éxito, false si falla.
    fun cambiarContrasena(idUsuario: Int, passActual: String, passNueva: String): Boolean {
        val db = this.writableDatabase // la BBDD la pongo en modo escritura para la modificación

        // 1. Primero comprobamos que la clave actual es la correcta
        val cursor = db.rawQuery(
            "SELECT Id_Usuario FROM USUARIO WHERE Id_Usuario = ? AND Contraseña = ?",
            arrayOf(idUsuario.toString(), passActual)
        )
        val existe = cursor.moveToFirst()
        cursor.close()

        // 2. Si la contraseña actual es correcta entonces es true y entra en el if ,y  guardamos la nueva
        if (existe) {
            val values = android.content.ContentValues().apply {
                put("Contraseña", passNueva)
            }
            // Actualizamos la fila del usuario
            val filasAfectadas =
                db.update("USUARIO", values, "Id_Usuario = ?", arrayOf(idUsuario.toString()))
            return filasAfectadas > 0 // si la ctualiza la fila mi devuelve un valor mayor que 0 y es true lo que devuelvo
        }

        // Si la clave actual no era correcta, devolvemos falso
        return false
    }

    // ====================================================================
    // === MÉTODOS DEL MODULO DE FILIACIÓN                              ===
    // ====================================================================

    // Guarda o actualiza los datos personales del militar
    fun guardarFiliacion(
        idUsuario: Int,
        nombre: String,
        apellidos: String,
        tmi: String,
        fechaIncorp: String,
        numEscalafon: Int,
    ): Boolean {
        val db = this.writableDatabase
        val values = android.content.ContentValues().apply {
            put("Id_Usuario", idUsuario)
            put("Nombre", nombre)
            put("Apellidos", apellidos)
            put("TMI", tmi)
            put("Fech_Incorp", fechaIncorp)
            put("Nun_Escalafon", numEscalafon)
        }

        // Primero comprobamos si este usuario ya tiene datos guardados
        val cursor = db.rawQuery(
            "SELECT Id_Filia FROM FILIACION WHERE Id_Usuario = ?",
            arrayOf(idUsuario.toString())
        )
        val existe = cursor.moveToFirst() // si existe me devuelve true y si no existe false
        cursor.close()

        if (existe) {
            // Si ya existen, actualizamos los datos (UPDATE)
            val filasAfectadas =
                db.update("FILIACION", values, "Id_Usuario = ?", arrayOf(idUsuario.toString()))
            return filasAfectadas > 0
        } else {
            // Si no existen, CREAMOS el nuevo registro (INSERT)
            val resultado = db.insert("FILIACION", null, values)
            return resultado != -1L
        }
    }

    // Recupera los datos de filiación para mostrarlos en pantalla
    fun obtenerFiliacion(idUsuario: Int): android.database.Cursor {
        val db = this.readableDatabase
        // Devuelve todas las columnas con el * de la tabla FILIACION para ese usuario
        return db.rawQuery(
            "SELECT * FROM FILIACION WHERE Id_Usuario = ?",
            arrayOf(idUsuario.toString())
        )
    }

    // ====================================================================
    // === MÉTODOS DEL MÓDULO DE TMI (TARJETA MILITAR DE IDENTIDAD)     ===
    // ====================================================================

    // AÑadir una nueva TMI
    // 1. Añadir una nueva TMI y pongo una protección anti-duplicados), podia haberlo realizado en
    // la creacion de la tabla habiendo puesto Unique pero en aquel momento no cai y asi practico otra cosa
    fun anadirTMI(idUsuario: Int, numTarjeta: String, fechaCaducidad: String): Boolean {
        val db = this.readableDatabase

        // PRIMERO: Comprobamos si esta TMI, ya existe para este usuario
        val cursor = db.rawQuery(
            "SELECT Id_M_Tmi FROM MOD_TMI WHERE Id_Usuario = ? AND N_Tarjeta = ?",
            arrayOf(idUsuario.toString(), numTarjeta)
        )
        val existe = cursor.moveToFirst() // si existe me devuelve true y entra en el if
        cursor.close()

        // Si ya existe, devolvemos false para que no la guarde
        if (existe) {
            return false
        }

        // SEGUNDO: Si no existe, la guardamos normalmente
        val dbWrite = this.writableDatabase
        val values = android.content.ContentValues().apply {
            put("Id_Usuario", idUsuario)
            put("N_Tarjeta", numTarjeta)
            put("M_Tmi_Fecha_Cadu", fechaCaducidad)
        }
        val resultado = dbWrite.insert("MOD_TMI", null, values)
        return resultado != -1L
    }

    // LEER todas las TMI de un usuario
    fun obtenerTMIs(idUsuario: Int): android.database.Cursor { // me devuelve un objeto de tipo cursor
        // que nos permitira recorrer fila a fila cada resultado de la consulta sql
        val db = this.readableDatabase
        // Ordenamos por Id_M_Tmi descendente para ver la más reciente primero
        return db.rawQuery(
            "SELECT * FROM MOD_TMI WHERE Id_Usuario = ? ORDER BY Id_M_Tmi DESC",
            arrayOf(idUsuario.toString())
        )
    }

    // MODIFICAR una TMI existente y volvemos a mirar si tiene duplicado la TMI para que no meta
    // otra igual durante la modificacion
    fun modificarTMI(idTmi: Int, numTarjeta: String, fechaCaducidad: String): Boolean {
        val db = this.writableDatabase

        //  Comprobamos si este número de TMI ya lo tiene el usuario en OTRA fila distinta.
        // Usamos Id_M_Tmi != ? para decirle que no cuente la tarjeta que estamos editando ahora mismo.
        // Usamos una subconsulta para averiguar el Id_Usuario sin tener que pedirlo por parámetro.
        val cursor = db.rawQuery(
            """
        SELECT Id_M_Tmi FROM MOD_TMI 
        WHERE N_Tarjeta = ? 
        AND Id_M_Tmi != ? 
        AND Id_Usuario = (SELECT Id_Usuario FROM MOD_TMI WHERE Id_M_Tmi = ?)
        """,
            arrayOf(numTarjeta, idTmi.toString(), idTmi.toString())
        )
        val existeDuplicado = cursor.moveToFirst()
        cursor.close()

        // Si existe otra tarjeta diferente con ese mismo número, no realiza la modificación
        if (existeDuplicado) {
            return false
        }

        // Si no hay duplicados o si es la misma tarjeta de antes, guardamos los cambios
        val values = android.content.ContentValues().apply {
            put("N_Tarjeta", numTarjeta)
            put("M_Tmi_Fecha_Cadu", fechaCaducidad)
        }
        val filasAfectadas = db.update("MOD_TMI", values, "Id_M_Tmi = ?", arrayOf(idTmi.toString()))
        return filasAfectadas > 0
    }

    // ELIMINAR una TMI
    fun eliminarTMI(idTmi: Int): Boolean {
        val db = this.writableDatabase
        val filasAfectadas = db.delete("MOD_TMI", "Id_M_Tmi = ?", arrayOf(idTmi.toString()))
        return filasAfectadas > 0
    }

    // ====================================================================
    // === MÉTODOS DEL MÓDULO DE CEE FUNDAMENTAL                        ===
    // ====================================================================

    fun anadirCEEF(idUsuario: Int, nombre: String, fechaBod: String, numBodStr: String): Boolean {
        val db = this.readableDatabase
        // Comprobamos si ya existe esta especialidad para este usuario
        val cursor = db.rawQuery(
            "SELECT Id_M_CEEF FROM MOD_CEE_FUNDAMENTAL WHERE Id_Usuario = ? AND Nom_CEEF = ?",
            arrayOf(idUsuario.toString(), nombre)
        )
        val existe = cursor.moveToFirst()
        cursor.close()

        if (existe) return false // Si ya existe, no realizo el guardado

        // Convertimos el número de BOD a entero si está vacío, guardamos un 0
        val numBod = numBodStr.toIntOrNull() ?: 0

        val dbWrite = this.writableDatabase
        val values = android.content.ContentValues().apply {
            put("Id_Usuario", idUsuario)
            put("Nom_CEEF", nombre)
            put("M_Ceef_Fecha_Bod", fechaBod)
            put("M_Ceef_Nbod", numBod)
        }
        return dbWrite.insert("MOD_CEE_FUNDAMENTAL", null, values) != -1L
    }

    fun obtenerCEEFs(idUsuario: Int): android.database.Cursor {
        val db = this.readableDatabase
        return db.rawQuery(
            "SELECT * FROM MOD_CEE_FUNDAMENTAL WHERE Id_Usuario = ? ORDER BY Id_M_CEEF DESC",
            arrayOf(idUsuario.toString())
        )
    }


    fun modificarCEEF(idCeef: Int, nombre: String, fechaBod: String, numBodStr: String): Boolean {
        val db = this.writableDatabase

        // Comprobamos si el usuario ya tiene esta especialidad en OTRA fila.
        // Usamos Id_M_CEEF != ? para excluir la fila que estamos editando ahora mismo.
        val cursor = db.rawQuery(
            """
        SELECT Id_M_CEEF FROM MOD_CEE_FUNDAMENTAL 
        WHERE Nom_CEEF = ? 
        AND Id_M_CEEF != ? 
        AND Id_Usuario = (SELECT Id_Usuario FROM MOD_CEE_FUNDAMENTAL WHERE Id_M_CEEF = ?)
        """,
            arrayOf(nombre, idCeef.toString(), idCeef.toString())
        )
        val existeDuplicado = cursor.moveToFirst()
        cursor.close()

        // Si ya existe esa especialidad, devolvemos false para bloquear la actualización
        if (existeDuplicado) {
            return false
        }

        // 2. Si no hay duplicados, actualizamos los datos
        val numBod = numBodStr.toIntOrNull() ?: 0
        val values = android.content.ContentValues().apply {
            put("Nom_CEEF", nombre)
            put("M_Ceef_Fecha_Bod", fechaBod)
            put("M_Ceef_Nbod", numBod)
        }

        return db.update(
            "MOD_CEE_FUNDAMENTAL",
            values,
            "Id_M_CEEF = ?",
            arrayOf(idCeef.toString())
        ) > 0
    }

    fun eliminarCEEF(idCeef: Int): Boolean {
        val db = this.writableDatabase
        return db.delete("MOD_CEE_FUNDAMENTAL", "Id_M_CEEF = ?", arrayOf(idCeef.toString())) > 0
    }

    // ====================================================================
    // === MÉTODOS DEL MÓDULO DE EMPLEOS                                ===
    // ====================================================================

    fun anadirEmpleo(idUsuario: Int, nombre: String, fechaBod: String, numBodStr: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT Id_M_Empl FROM MOD_EMPLEOS WHERE Id_Usuario = ? AND Nom_Empleo = ?",
            arrayOf(idUsuario.toString(), nombre)
        )
        val existe = cursor.moveToFirst()
        cursor.close()

        if (existe) return false

        val numBod = numBodStr.toIntOrNull() ?: 0
        val dbWrite = this.writableDatabase
        val values = android.content.ContentValues().apply {
            put("Id_Usuario", idUsuario)
            put("Nom_Empleo", nombre)
            put("M_Empl_Fecha_Bod", fechaBod)
            put("M_Empl_Nbod", numBod)
        }
        return dbWrite.insert("MOD_EMPLEOS", null, values) != -1L
    }

    fun obtenerEmpleos(idUsuario: Int): android.database.Cursor {
        val db = this.readableDatabase
        return db.rawQuery(
            "SELECT * FROM MOD_EMPLEOS WHERE Id_Usuario = ? ORDER BY Id_M_Empl DESC",
            arrayOf(idUsuario.toString())
        )
    }


    fun modificarEmpleo(
        idEmpleo: Int,
        nombre: String,
        fechaBod: String,
        numBodStr: String,
    ): Boolean {
        val numBod = numBodStr.toIntOrNull() ?: 0
        val db = this.writableDatabase
        val values = android.content.ContentValues().apply {
            put("Nom_Empleo", nombre)
            put("M_Empl_Fecha_Bod", fechaBod)
            put("M_Empl_Nbod", numBod)
        }
        return db.update("MOD_EMPLEOS", values, "Id_M_Empl = ?", arrayOf(idEmpleo.toString())) > 0
    }

    fun eliminarEmpleo(idEmpleo: Int): Boolean {
        val db = this.writableDatabase
        return db.delete("MOD_EMPLEOS", "Id_M_Empl = ?", arrayOf(idEmpleo.toString())) > 0
    }

    // ====================================================================
    // === MÉTODOS DEL MÓDULO DE DESTINOS                               ===
    // ====================================================================
    // Un usuario puede tener el mismo destino dos veces
    fun anadirDestino(
        idUsuario: Int,
        nombre: String,
        fechaBod: String,
        numBodStr: String,
    ): Boolean {

        val numBod = numBodStr.toIntOrNull() ?: 0


        val dbWrite = this.writableDatabase


        val values = android.content.ContentValues().apply {
            put("Id_Usuario", idUsuario)
            put("Nom_Destino", nombre)
            put("M_Dest_Fecha_Bod", fechaBod)
            put("M_Dest_Nbod", numBod)
        }


        return dbWrite.insert("MOD_DESTINOS", null, values) != -1L
    }

    fun obtenerDestinos(idUsuario: Int): android.database.Cursor {
        val db = this.readableDatabase
        // Ordenamos DESC para que el último destino añadido salga el primero
        return db.rawQuery(
            "SELECT * FROM MOD_DESTINOS WHERE Id_Usuario = ? ORDER BY Id_M_Dest DESC",
            arrayOf(idUsuario.toString())
        )
    }

    // Un usuario puede tener el mismo destino dos veces
    fun modificarDestino(
        idDestino: Int,
        nombre: String,
        fechaBod: String,
        numBodStr: String,
    ): Boolean {
        val numBod = numBodStr.toIntOrNull() ?: 0
        val db = this.writableDatabase
        val values = android.content.ContentValues().apply {
            put("Nom_Destino", nombre)
            put("M_Dest_Fecha_Bod", fechaBod)
            put("M_Dest_Nbod", numBod)
        }
        return db.update("MOD_DESTINOS", values, "Id_M_Dest = ?", arrayOf(idDestino.toString())) > 0
    }

    fun eliminarDestino(idDestino: Int): Boolean {
        val db = this.writableDatabase
        return db.delete("MOD_DESTINOS", "Id_M_Dest = ?", arrayOf(idDestino.toString())) > 0
    }

    // ====================================================================
    // === MÉTODOS DEL MÓDULO DE MISIONES                               ===
    // ====================================================================

    fun anadirMision(idUsuario: Int, nombre: String, fechaBod: String, numBodStr: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT Id_M_Misi FROM MOD_MISIONES WHERE Id_Usuario = ? AND Nom_Mision = ?",
            arrayOf(idUsuario.toString(), nombre)
        )
        val existe = cursor.moveToFirst()
        cursor.close()

        if (existe) return false

        val numBod = numBodStr.toIntOrNull() ?: 0
        val dbWrite = this.writableDatabase
        val values = android.content.ContentValues().apply {
            put("Id_Usuario", idUsuario)
            put("Nom_Mision", nombre)
            put("M_Misi_Fecha_Bod", fechaBod)
            put("M_Misi_Nbod", numBod)
        }
        return dbWrite.insert("MOD_MISIONES", null, values) != -1L
    }

    fun obtenerMisiones(idUsuario: Int): android.database.Cursor {
        val db = this.readableDatabase
        return db.rawQuery(
            "SELECT * FROM MOD_MISIONES WHERE Id_Usuario = ? ORDER BY Id_M_Misi DESC",
            arrayOf(idUsuario.toString())
        )
    }

    fun modificarMision(
        idMision: Int,
        nombre: String,
        fechaBod: String,
        numBodStr: String,
    ): Boolean {
        val numBod = numBodStr.toIntOrNull() ?: 0
        val db = this.writableDatabase
        val values = android.content.ContentValues().apply {
            put("Nom_Mision", nombre)
            put("M_Misi_Fecha_Bod", fechaBod)
            put("M_Misi_Nbod", numBod)
        }
        return db.update("MOD_MISIONES", values, "Id_M_Misi = ?", arrayOf(idMision.toString())) > 0
    }

    fun eliminarMision(idMision: Int): Boolean {
        val db = this.writableDatabase
        return db.delete("MOD_MISIONES", "Id_M_Misi = ?", arrayOf(idMision.toString())) > 0
    }

    // ====================================================================
    // === MÉTODOS DEL MÓDULO DE COMISIONES DE SERVICIO                 ===
    // ====================================================================

    fun anadirComision(
        idUsuario: Int,
        nombre: String,
        fechaBod: String,
        numBodStr: String,
    ): Boolean {

        val numBod = numBodStr.toIntOrNull() ?: 0
        val dbWrite = this.writableDatabase
        val values = android.content.ContentValues().apply {
            put("Id_Usuario", idUsuario)
            put("Nom_Comision", nombre)
            put("M_Cser_Fecha_Bod", fechaBod)
            put("M_Cser_Nbod", numBod)
        }

        return dbWrite.insert("MOD_COMISION_SER", null, values) != -1L
    }

    fun obtenerComisiones(idUsuario: Int): android.database.Cursor {
        val db = this.readableDatabase
        return db.rawQuery(
            "SELECT * FROM MOD_COMISION_SER WHERE Id_Usuario = ? ORDER BY Id_M_Cser DESC",
            arrayOf(idUsuario.toString())
        )
    }

    fun modificarComision(
        idComision: Int,
        nombre: String,
        fechaBod: String,
        numBodStr: String,
    ): Boolean {
        val numBod = numBodStr.toIntOrNull() ?: 0
        val db = this.writableDatabase
        val values = android.content.ContentValues().apply {
            put("Nom_Comision", nombre)
            put("M_Cser_Fecha_Bod", fechaBod)
            put("M_Cser_Nbod", numBod)
        }
        return db.update(
            "MOD_COMISION_SER",
            values,
            "Id_M_Cser = ?",
            arrayOf(idComision.toString())
        ) > 0
    }

    fun eliminarComision(idComision: Int): Boolean {
        val db = this.writableDatabase
        return db.delete("MOD_COMISION_SER", "Id_M_Cser = ?", arrayOf(idComision.toString())) > 0
    }

// ====================================================================
    // === MÉTODOS DEL MÓDULO DE EVALUACIÓN PARA EL ASCENSO             ===
    // ====================================================================

    fun anadirEvaluacion(
        idUsuario: Int,
        nombre: String,
        resultadoApto: String,
        fechaBod: String,
        numBodStr: String,
    ): Boolean {
        // 1. Preparamos el número de boletín (si viene vacío, le pone un 0)
        val numBod = numBodStr.toIntOrNull() ?: 0

        // 2. Abrimos la BBDD en modo escritura directamente
        val dbWrite = this.writableDatabase

        // 3. Empaquetamos los datos
        val values = android.content.ContentValues().apply {
            put("Id_Usuario", idUsuario)
            put("Nom_Evaluacion", nombre)
            put("Resultado", resultadoApto) // Guardamos Apto, No Apto o No Presentado
            put("M_Eva_Fecha_Bod", fechaBod)
            put("M_Eva_Nbod", numBod)
        }

        // 4. Insertamos la evaluación permitiendo repeticiones (por si suspendió o no se presentó antes)
        return dbWrite.insert("MOD_EVALUACION_ASCENSO", null, values) != -1L
    }

    fun obtenerEvaluaciones(idUsuario: Int): android.database.Cursor {
        val db = this.readableDatabase
        return db.rawQuery(
            "SELECT * FROM MOD_EVALUACION_ASCENSO WHERE Id_Usuario = ? ORDER BY Id_M_Eva DESC",
            arrayOf(idUsuario.toString())
        )
    }

    fun modificarEvaluacion(
        idEvaluacion: Int,
        nombre: String,
        resultadoApto: String,
        fechaBod: String,
        numBodStr: String,
    ): Boolean {
        val numBod = numBodStr.toIntOrNull() ?: 0
        val db = this.writableDatabase
        val values = android.content.ContentValues().apply {
            put("Nom_Evaluacion", nombre)
            put("Resultado", resultadoApto)
            put("M_Eva_Fecha_Bod", fechaBod)
            put("M_Eva_Nbod", numBod)
        }
        return db.update(
            "MOD_EVALUACION_ASCENSO",
            values,
            "Id_M_Eva = ?",
            arrayOf(idEvaluacion.toString())
        ) > 0
    }

    fun eliminarEvaluacion(idEvaluacion: Int): Boolean {
        val db = this.writableDatabase
        return db.delete(
            "MOD_EVALUACION_ASCENSO",
            "Id_M_Eva = ?",
            arrayOf(idEvaluacion.toString())
        ) > 0
    }

    // ====================================================================
    // === MÉTODOS DEL MÓDULO DE HABILITACIÓN DE SEGURIDAD (HPS)        ===
    // ====================================================================

    fun anadirHps(
        idUsuario: Int,
        nombre: String,
        fechaConcesion: String,
        fechaCaducidad: String,
    ): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT Id_M_Hps FROM MOD_HPS WHERE Id_Usuario = ? AND Nom_Habilitacion = ?",
            arrayOf(idUsuario.toString(), nombre)
        )
        val existe = cursor.moveToFirst()
        cursor.close()

        if (existe) return false

        val dbWrite = this.writableDatabase
        val values = android.content.ContentValues().apply {
            put("Id_Usuario", idUsuario)
            put("Nom_Habilitacion", nombre)
            put("Fecha_M_Concesion", fechaConcesion)
            put("Fecha_M_Caducidad", fechaCaducidad)
        }
        return dbWrite.insert("MOD_HPS", null, values) != -1L
    }

    fun obtenerHps(idUsuario: Int): android.database.Cursor {
        val db = this.readableDatabase
        return db.rawQuery(
            "SELECT * FROM MOD_HPS WHERE Id_Usuario = ? ORDER BY Id_M_Hps DESC",
            arrayOf(idUsuario.toString())
        )
    }

    fun modificarHps(
        idHps: Int,
        nombre: String,
        fechaConcesion: String,
        fechaCaducidad: String,
    ): Boolean {
        val db = this.writableDatabase

        // Comprobamos si el usuario ya tiene esta HPS en otra fila distinta.
        // Usamos Id_M_Hps != ? para no contar la propia habilitación que estamos editando.
        // Usamos la subconsulta para averiguar el Id_Usuario automáticamente.
        val cursor = db.rawQuery(
            """
        SELECT Id_M_Hps FROM MOD_HPS 
        WHERE Nom_Habilitacion = ? 
        AND Id_M_Hps != ? 
        AND Id_Usuario = (SELECT Id_Usuario FROM MOD_HPS WHERE Id_M_Hps = ?)
        """,
            arrayOf(nombre, idHps.toString(), idHps.toString())
        )
        val existeDuplicado = cursor.moveToFirst()
        cursor.close()

        // Si ya tiene esa habilitación, cortamos la ejecución y devolvemos false
        if (existeDuplicado) {
            return false
        }

        // 2. Si  está correcto y no hay duplicados, actualizamos
        val values = android.content.ContentValues().apply {
            put("Nom_Habilitacion", nombre)
            put("Fecha_M_Concesion", fechaConcesion)
            put("Fecha_M_Caducidad", fechaCaducidad)
        }

        return db.update("MOD_HPS", values, "Id_M_Hps = ?", arrayOf(idHps.toString())) > 0
    }

    fun eliminarHps(idHps: Int): Boolean {
        val db = this.writableDatabase
        return db.delete("MOD_HPS", "Id_M_Hps = ?", arrayOf(idHps.toString())) > 0
    }

    // ====================================================================
    // === MÉTODOS DEL MÓDULO DE RELACIONES CON LA ADMINISTRACIÓN       ===
    // ====================================================================

    fun anadirRelacionAdmin(
        idUsuario: Int,
        nombre: String,
        fechaBod: String,
        numBodStr: String,
    ): Boolean {
        // 1. Preparamos el número de boletín (si viene vacío, le pone un 0)
        val numBod = numBodStr.toIntOrNull() ?: 0

        // 2. Abrimos la BBDD en modo escritura directamente
        val dbWrite = this.writableDatabase

        // 3. Empaquetamos los datos
        val values = android.content.ContentValues().apply {
            put("Id_Usuario", idUsuario)
            put("Nom_Rel_Admin", nombre)
            put("M_Radm_Fecha_Bod", fechaBod)
            put("M_Radm_Nbod", numBod)
        }

        // 4. Insertamos la relación administrativa permitiendo repeticiones
        return dbWrite.insert("MOD_RELA_ADMINISTRACION", null, values) != -1L
    }

    fun obtenerRelacionesAdmin(idUsuario: Int): android.database.Cursor {
        val db = this.readableDatabase
        return db.rawQuery(
            "SELECT * FROM MOD_RELA_ADMINISTRACION WHERE Id_Usuario = ? ORDER BY Id_M_Radm DESC",
            arrayOf(idUsuario.toString())
        )
    }

    fun modificarRelacionAdmin(
        idRelacion: Int,
        nombre: String,
        fechaBod: String,
        numBodStr: String,
    ): Boolean {
        val numBod = numBodStr.toIntOrNull() ?: 0
        val db = this.writableDatabase
        val values = android.content.ContentValues().apply {
            put("Nom_Rel_Admin", nombre)
            put("M_Radm_Fecha_Bod", fechaBod)
            put("M_Radm_Nbod", numBod)
        }
        return db.update(
            "MOD_RELA_ADMINISTRACION",
            values,
            "Id_M_Radm = ?",
            arrayOf(idRelacion.toString())
        ) > 0
    }

    fun eliminarRelacionAdmin(idRelacion: Int): Boolean {
        val db = this.writableDatabase
        return db.delete(
            "MOD_RELA_ADMINISTRACION",
            "Id_M_Radm = ?",
            arrayOf(idRelacion.toString())
        ) > 0
    }

    // ====================================================================
    // === MÉTODOS DEL MÓDULO DE SITUACIONES ADMINISTRATIVAS            ===
    // ====================================================================

    fun anadirSituacion(
        idUsuario: Int,
        nombre: String,
        fechaBod: String,
        numBodStr: String,
    ): Boolean {
        // 1. Preparamos el número de boletín (si viene vacío, le pone un 0)
        val numBod = numBodStr.toIntOrNull() ?: 0

        val dbWrite = this.writableDatabase

        val values = android.content.ContentValues().apply {
            put("Id_Usuario", idUsuario)
            put("Nom_Sit_Admini", nombre)
            put("M_Sadm_Fecha_Bod", fechaBod)
            put("M_Sadm_Nbod", numBod)
        }

        //  Insertamos la situación administrativa permitiendo repeticiones (ej: Servicio Activo varias veces)
        return dbWrite.insert("MOD_SITUA_ADMIN", null, values) != -1L
    }

    fun obtenerSituaciones(idUsuario: Int): android.database.Cursor {
        val db = this.readableDatabase
        return db.rawQuery(
            "SELECT * FROM MOD_SITUA_ADMIN WHERE Id_Usuario = ? ORDER BY Id_M_Sadm DESC",
            arrayOf(idUsuario.toString())
        )
    }

    fun modificarSituacion(
        idSituacion: Int,
        nombre: String,
        fechaBod: String,
        numBodStr: String,
    ): Boolean {
        val numBod = numBodStr.toIntOrNull() ?: 0
        val db = this.writableDatabase
        val values = android.content.ContentValues().apply {
            put("Nom_Sit_Admini", nombre)
            put("M_Sadm_Fecha_Bod", fechaBod)
            put("M_Sadm_Nbod", numBod)
        }
        return db.update(
            "MOD_SITUA_ADMIN",
            values,
            "Id_M_Sadm = ?",
            arrayOf(idSituacion.toString())
        ) > 0
    }

    fun eliminarSituacion(idSituacion: Int): Boolean {
        val db = this.writableDatabase
        return db.delete("MOD_SITUA_ADMIN", "Id_M_Sadm = ?", arrayOf(idSituacion.toString())) > 0
    }


    // ====================================================================
    // === MÉTODOS DEL MÓDULO DE TRIENIOS                               ===
    // ====================================================================

    fun anadirTrienio(
        idUsuario: Int,
        tipoTrienio: String,
        fechaBod: String,
        numBodStr: String,
    ): Boolean {
        val numBod = numBodStr.toIntOrNull() ?: 0
        val dbWrite = this.writableDatabase
        val values = android.content.ContentValues().apply {
            put("Id_Usuario", idUsuario)
            put("Tipo_Trienio", tipoTrienio)
            put("M_Trie_Fecha_Bod", fechaBod)
            put("M_Trie_Nbod", numBod)
        }
        return dbWrite.insert("MOD_TRIENIOS", null, values) != -1L
    }

    fun obtenerTrienios(idUsuario: Int): android.database.Cursor {
        val db = this.readableDatabase
        // poner lo ASC es  para que el número de fila coincida con el número de trienio cronológico
        return db.rawQuery(
            "SELECT * FROM MOD_TRIENIOS WHERE Id_Usuario = ? ORDER BY Id_M_Trie ASC",
            arrayOf(idUsuario.toString())
        )
    }

    fun modificarTrienio(
        idTrienio: Int,
        tipoTrienio: String,
        fechaBod: String,
        numBodStr: String,
    ): Boolean {
        val numBod = numBodStr.toIntOrNull() ?: 0
        val db = this.writableDatabase
        val values = android.content.ContentValues().apply {
            put("Tipo_Trienio", tipoTrienio)
            put("M_Trie_Fecha_Bod", fechaBod)
            put("M_Trie_Nbod", numBod)
        }
        return db.update("MOD_TRIENIOS", values, "Id_M_Trie = ?", arrayOf(idTrienio.toString())) > 0
    }

    fun eliminarTrienio(idTrienio: Int): Boolean {
        val db = this.writableDatabase
        return db.delete("MOD_TRIENIOS", "Id_M_Trie = ?", arrayOf(idTrienio.toString())) > 0
    }


    // ====================================================================
    // === MÉTODOS DEL MÓDULO DE APTITUDES                              ===
    // ====================================================================

    fun anadirAptitud(
        idUsuario: Int,
        nombre: String,
        fechaBod: String,
        numBodStr: String,
    ): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT Id_M_Apti FROM MOD_APTITUDES WHERE Id_Usuario = ? AND Nom_Aptitud = ?",
            arrayOf(idUsuario.toString(), nombre)
        )
        val existe = cursor.moveToFirst()
        cursor.close()

        if (existe) return false

        val numBod = numBodStr.toIntOrNull() ?: 0
        val dbWrite = this.writableDatabase
        val values = android.content.ContentValues().apply {
            put("Id_Usuario", idUsuario)
            put("Nom_Aptitud", nombre)
            put("M_Apti_Fecha_Bod", fechaBod)
            put("M_Apti_Nbod", numBod)
        }
        return dbWrite.insert("MOD_APTITUDES", null, values) != -1L
    }

    fun obtenerAptitudes(idUsuario: Int): android.database.Cursor {
        val db = this.readableDatabase
        return db.rawQuery(
            "SELECT * FROM MOD_APTITUDES WHERE Id_Usuario = ? ORDER BY Id_M_Apti DESC",
            arrayOf(idUsuario.toString())
        )
    }

    fun modificarAptitud(
        idAptitud: Int,
        nombre: String,
        fechaBod: String,
        numBodStr: String,
    ): Boolean {
        val db = this.writableDatabase

        // Comprobamos si el usuario ya tiene esta aptitud en otra fila distinta.
        // Usamos Id_M_Apti != ? para no contar la propia aptitud que estamos editando.
        // Usamos la subconsulta para averiguar el Id_Usuario automáticamente.
        val cursor = db.rawQuery(
            """
        SELECT Id_M_Apti FROM MOD_APTITUDES 
        WHERE Nom_Aptitud = ? 
        AND Id_M_Apti != ? 
        AND Id_Usuario = (SELECT Id_Usuario FROM MOD_APTITUDES WHERE Id_M_Apti = ?)
        """,
            arrayOf(nombre, idAptitud.toString(), idAptitud.toString())
        )
        val existeDuplicado = cursor.moveToFirst()
        cursor.close()

        // Si ya tiene esa aptitud, cortamos la ejecución y devolvemos false para que salte el Toast de error
        if (existeDuplicado) {
            return false
        }

        // 2. Si  está correcto, procedemos a actualizar los datos
        val numBod = numBodStr.toIntOrNull() ?: 0
        val values = android.content.ContentValues().apply {
            put("Nom_Aptitud", nombre)
            put("M_Apti_Fecha_Bod", fechaBod)
            put("M_Apti_Nbod", numBod)
        }

        return db.update(
            "MOD_APTITUDES",
            values,
            "Id_M_Apti = ?",
            arrayOf(idAptitud.toString())
        ) > 0
    }

    fun eliminarAptitud(idAptitud: Int): Boolean {
        val db = this.writableDatabase
        return db.delete("MOD_APTITUDES", "Id_M_Apti = ?", arrayOf(idAptitud.toString())) > 0
    }

    // ====================================================================
    // === MÉTODOS DEL MÓDULO DE RECOMPENSAS                            ===
    // ====================================================================

    fun anadirRecompensa(
        idUsuario: Int,
        nombre: String,
        fechaBod: String,
        numBodStr: String,
    ): Boolean {
        val numBod = numBodStr.toIntOrNull() ?: 0
        val dbWrite = this.writableDatabase
        val values = android.content.ContentValues().apply {
            put("Id_Usuario", idUsuario)
            put("Nom_Recompensa", nombre)
            put("M_Reco_Fecha_Bod", fechaBod)
            put("M_Reco_Nbod", numBod)
        }
        return dbWrite.insert("MOD_RECOMPENSAS", null, values) != -1L
    }

    fun obtenerRecompensas(idUsuario: Int): android.database.Cursor {
        val db = this.readableDatabase
        return db.rawQuery(
            "SELECT * FROM MOD_RECOMPENSAS WHERE Id_Usuario = ? ORDER BY Id_M_Reco DESC",
            arrayOf(idUsuario.toString())
        )
    }

    fun modificarRecompensa(
        idReco: Int,
        nombre: String,
        fechaBod: String,
        numBodStr: String,
    ): Boolean {
        val numBod = numBodStr.toIntOrNull() ?: 0
        val db = this.writableDatabase
        val values = android.content.ContentValues().apply {
            put("Nom_Recompensa", nombre)
            put("M_Reco_Fecha_Bod", fechaBod)
            put("M_Reco_Nbod", numBod)
        }
        return db.update("MOD_RECOMPENSAS", values, "Id_M_Reco = ?", arrayOf(idReco.toString())) > 0
    }

    fun eliminarRecompensa(idReco: Int): Boolean {
        val db = this.writableDatabase
        return db.delete("MOD_RECOMPENSAS", "Id_M_Reco = ?", arrayOf(idReco.toString())) > 0
    }

    // ====================================================================
    // === MÉTODOS DEL MÓDULO DE DISTINTIVOS                            ===
    // ====================================================================

    fun anadirDistintivo(
        idUsuario: Int,
        nombre: String,
        fechaBod: String,
        numBodStr: String,
    ): Boolean {
        val numBod = numBodStr.toIntOrNull() ?: 0
        val dbWrite = this.writableDatabase
        val values = android.content.ContentValues().apply {
            put("Id_Usuario", idUsuario)
            put("Nom_Distintivo", nombre)
            put("M_Dist_Fecha_Bod", fechaBod)
            put("M_Dist_Nbod", numBod)
        }
        return dbWrite.insert("MOD_DISTINTIVOS", null, values) != -1L
    }

    fun obtenerDistintivos(idUsuario: Int): android.database.Cursor {
        val db = this.readableDatabase
        return db.rawQuery(
            "SELECT * FROM MOD_DISTINTIVOS WHERE Id_Usuario = ? ORDER BY Id_M_Dist DESC",
            arrayOf(idUsuario.toString())
        )
    }

    fun modificarDistintivo(
        idDistintivo: Int,
        nombre: String,
        fechaBod: String,
        numBodStr: String,
    ): Boolean {
        val numBod = numBodStr.toIntOrNull() ?: 0
        val db = this.writableDatabase
        val values = android.content.ContentValues().apply {
            put("Nom_Distintivo", nombre)
            put("M_Dist_Fecha_Bod", fechaBod)
            put("M_Dist_Nbod", numBod)
        }
        return db.update(
            "MOD_DISTINTIVOS",
            values,
            "Id_M_Dist = ?",
            arrayOf(idDistintivo.toString())
        ) > 0
    }

    fun eliminarDistintivo(idDistintivo: Int): Boolean {
        val db = this.writableDatabase
        return db.delete("MOD_DISTINTIVOS", "Id_M_Dist = ?", arrayOf(idDistintivo.toString())) > 0
    }

    // ====================================================================
    // === MÉTODOS DEL MÓDULO DE CURSOS MILITARES                       ===
    // ====================================================================

    fun anadirCursoMilitar(
        idUsuario: Int,
        nombre: String,
        fechaBod: String,
        numBodStr: String,
    ): Boolean {
        val db = this.readableDatabase

        // COMPROBACIÓN:
        val cursor = db.rawQuery(
            "SELECT Id_M_Cmili FROM MOD_CUR_MILITAR WHERE Id_Usuario = ? AND Nom_Cur_Mili = ?",
            arrayOf(idUsuario.toString(), nombre)
        )
        val existe = cursor.moveToFirst()
        cursor.close()

        // Si ya existe, cortamos y devolvemos false para que salte el Toast en Java
        if (existe) return false

        // 2Si no existe, procedemos a guardar
        val numBod = numBodStr.toIntOrNull() ?: 0
        val dbWrite = this.writableDatabase
        val values = android.content.ContentValues().apply {
            put("Id_Usuario", idUsuario)
            put("Nom_Cur_Mili", nombre)
            put("M_Cmili_Fecha_Bod", fechaBod)
            put("M_Cmili_Nbod", numBod)
        }
        return dbWrite.insert("MOD_CUR_MILITAR", null, values) != -1L
    }

    fun obtenerCursosMilitares(idUsuario: Int): android.database.Cursor {
        val db = this.readableDatabase
        return db.rawQuery(
            "SELECT * FROM MOD_CUR_MILITAR WHERE Id_Usuario = ? ORDER BY Id_M_Cmili DESC",
            arrayOf(idUsuario.toString())
        )
    }

    fun modificarCursoMilitar(
        idCurso: Int,
        nombre: String,
        fechaBod: String,
        numBodStr: String,
    ): Boolean {
        val db = this.writableDatabase

        // Evitamos que al editar le ponga el nombre de otro curso existente
        val cursor = db.rawQuery(
            """
        SELECT Id_M_Cmili FROM MOD_CUR_MILITAR 
        WHERE Nom_Cur_Mili = ? 
        AND Id_M_Cmili != ? 
        AND Id_Usuario = (SELECT Id_Usuario FROM MOD_CUR_MILITAR WHERE Id_M_Cmili = ?)
        """,
            arrayOf(nombre, idCurso.toString(), idCurso.toString())
        )
        val existeDuplicado = cursor.moveToFirst()
        cursor.close()

        // Si hay un duplicado con otra fila, bloqueamos
        if (existeDuplicado) return false

        // Si no hay duplicado, modificamos la fila
        val numBod = numBodStr.toIntOrNull() ?: 0
        val values = android.content.ContentValues().apply {
            put("Nom_Cur_Mili", nombre)
            put("M_Cmili_Fecha_Bod", fechaBod)
            put("M_Cmili_Nbod", numBod)
        }
        return db.update(
            "MOD_CUR_MILITAR",
            values,
            "Id_M_Cmili = ?",
            arrayOf(idCurso.toString())
        ) > 0
    }

    fun eliminarCursoMilitar(idCurso: Int): Boolean {
        val db = this.writableDatabase
        return db.delete("MOD_CUR_MILITAR", "Id_M_Cmili = ?", arrayOf(idCurso.toString())) > 0
    }

    // ====================================================================
    // === MÉTODOS DEL MÓDULO DE TÍTULOS CIVILES                        ===
    // ====================================================================

    fun anadirTituloCivil(idUsuario: Int, nombre: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT Id_M_Tcivi FROM MOD_TITULOS_CIVILES WHERE Id_Usuario = ? AND Nom_Titulo = ?",
            arrayOf(idUsuario.toString(), nombre)
        )
        val existe = cursor.moveToFirst()
        cursor.close()

        if (existe) return false

        val dbWrite = this.writableDatabase
        val values = android.content.ContentValues().apply {
            put("Id_Usuario", idUsuario)
            put("Nom_Titulo", nombre)
        }
        return dbWrite.insert("MOD_TITULOS_CIVILES", null, values) != -1L
    }

    fun obtenerTitulosCiviles(idUsuario: Int): android.database.Cursor {
        val db = this.readableDatabase
        return db.rawQuery(
            "SELECT * FROM MOD_TITULOS_CIVILES WHERE Id_Usuario = ? ORDER BY Id_M_Tcivi DESC",
            arrayOf(idUsuario.toString())
        )
    }

    fun modificarTituloCivil(idTitulo: Int, nombre: String): Boolean {
        val db = this.writableDatabase

        //Comprobamos si el usuario ya tiene este título civil en otra  fila distinta.
        // Usamos Id_M_Tcivi != ? para excluir la propia fila que estamos editando ahora mismo.
        // Usamos la subconsulta para averiguar el Id_Usuario automáticamente sin pedirlo por parámetro.
        val cursor = db.rawQuery(
            """
        SELECT Id_M_Tcivi FROM MOD_TITULOS_CIVILES 
        WHERE Nom_Titulo = ? 
        AND Id_M_Tcivi != ? 
        AND Id_Usuario = (SELECT Id_Usuario FROM MOD_TITULOS_CIVILES WHERE Id_M_Tcivi = ?)
        """,
            arrayOf(nombre, idTitulo.toString(), idTitulo.toString())
        )
        val existeDuplicado = cursor.moveToFirst()
        cursor.close()
        if (existeDuplicado) return false


        val values = android.content.ContentValues().apply {
            put("Nom_Titulo", nombre)
        }
        return db.update(
            "MOD_TITULOS_CIVILES",
            values,
            "Id_M_Tcivi = ?",
            arrayOf(idTitulo.toString())
        ) > 0
    }

    fun eliminarTituloCivil(idTitulo: Int): Boolean {
        val db = this.writableDatabase
        return db.delete("MOD_TITULOS_CIVILES", "Id_M_Tcivi = ?", arrayOf(idTitulo.toString())) > 0
    }

    // ====================================================================
    // === MÉTODOS DEL MÓDULO DE IDIOMAS SLP                            ===
    // ====================================================================

    fun anadirIdioma(
        idUsuario: Int,
        nombre: String,
        resultadoSlp: String,
        fechaBod: String,
        numBodStr: String,
    ): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT Id_M_Idi FROM MOD_IDIOMA WHERE Id_Usuario = ? AND Nom_idioma = ?",
            arrayOf(idUsuario.toString(), nombre)
        )
        val existe = cursor.moveToFirst()
        cursor.close()

        if (existe) return false

        val numBod = numBodStr.toIntOrNull() ?: 0
        val dbWrite = this.writableDatabase
        val values = android.content.ContentValues().apply {
            put("Id_Usuario", idUsuario)
            put("Nom_idioma", nombre)
            put("Resultado", resultadoSlp)
            put("M_Idi_Fecha_Bod", fechaBod)
            put("M_Idi_Nbod", numBod)
        }
        return dbWrite.insert("MOD_IDIOMA", null, values) != -1L
    }

    fun obtenerIdiomas(idUsuario: Int): android.database.Cursor {
        val db = this.readableDatabase
        return db.rawQuery(
            "SELECT * FROM MOD_IDIOMA WHERE Id_Usuario = ? ORDER BY Id_M_Idi DESC",
            arrayOf(idUsuario.toString())
        )
    }

    fun modificarIdioma(
        idIdioma: Int,
        nombre: String,
        resultadoSlp: String,
        fechaBod: String,
        numBodStr: String,
    ): Boolean {
        val db = this.writableDatabase

        // Comprobamos si el usuario ya tiene este idioma registrado en OTRA fila distinta.
        // Usamos Id_M_Idi != ? para excluir la propia fila que estamos editando ahora mismo.
        // Usamos la subconsulta para averiguar el Id_Usuario automáticamente sin pedirlo por parámetro.
        val cursor = db.rawQuery(
            """
        SELECT Id_M_Idi FROM MOD_IDIOMA 
        WHERE Nom_idioma = ? 
        AND Id_M_Idi != ? 
        AND Id_Usuario = (SELECT Id_Usuario FROM MOD_IDIOMA WHERE Id_M_Idi = ?)
        """,
            arrayOf(nombre, idIdioma.toString(), idIdioma.toString())
        )
        val existeDuplicado = cursor.moveToFirst()
        cursor.close()


        if (existeDuplicado) return false


        val numBod = numBodStr.toIntOrNull() ?: 0
        val values = android.content.ContentValues().apply {
            put("Nom_idioma", nombre)
            put("Resultado", resultadoSlp)
            put("M_Idi_Fecha_Bod", fechaBod)
            put("M_Idi_Nbod", numBod)
        }
        return db.update("MOD_IDIOMA", values, "Id_M_Idi = ?", arrayOf(idIdioma.toString())) > 0
    }

    fun eliminarIdioma(idIdioma: Int): Boolean {
        val db = this.writableDatabase
        return db.delete("MOD_IDIOMA", "Id_M_Idi = ?", arrayOf(idIdioma.toString())) > 0
    }

// ====================================================================
    // === MÉTODOS DEL MÓDULO DE ARMAS PARTICULARES                     ===
    // ====================================================================

    fun anadirArma(
        idUsuario: Int,
        nombre: String,
        numSerie: String,
        fechaCaducidad: String,
    ): Boolean {
        val db = this.readableDatabase
        // Comprobamos si el número de serie ya existe para evitar errores bruscos de SQLite
        val cursor = db.rawQuery(
            "SELECT Id_M_EArm FROM MOD_EXP_ARMAS WHERE M_EArm_Nserie = ?",
            arrayOf(numSerie)
        )
        val existe = cursor.moveToFirst()
        cursor.close()

        if (existe) return false

        val dbWrite = this.writableDatabase
        val values = android.content.ContentValues().apply {
            put("Id_Usuario", idUsuario)
            put("Nom_Arma", nombre)
            put("M_EArm_Nserie", numSerie)
            put("M_EArm_Fecha_Cad", fechaCaducidad)
        }
        return dbWrite.insert("MOD_EXP_ARMAS", null, values) != -1L
    }

    fun obtenerArmas(idUsuario: Int): android.database.Cursor {
        val db = this.readableDatabase
        return db.rawQuery(
            "SELECT * FROM MOD_EXP_ARMAS WHERE Id_Usuario = ? ORDER BY Id_M_EArm DESC",
            arrayOf(idUsuario.toString())
        )
    }

    fun modificarArma(
        idArma: Int,
        nombre: String,
        numSerie: String,
        fechaCaducidad: String,
    ): Boolean {
        val db = this.writableDatabase

        //Comprobamos si el número de serie ya existe en otra arma distinta.
        // El número de serie es un identificador único físico, por lo que la comprobación es global.
        // Usamos Id_M_EArm != ? para excluir el arma que estamos editando actualmente.
        val cursor = db.rawQuery(
            "SELECT Id_M_EArm FROM MOD_EXP_ARMAS WHERE M_EArm_Nserie = ? AND Id_M_EArm != ?",
            arrayOf(numSerie, idArma.toString())
        )
        val existeDuplicado = cursor.moveToFirst()
        cursor.close()


        if (existeDuplicado) return false


        val values = android.content.ContentValues().apply {
            put("Nom_Arma", nombre)
            put("M_EArm_Nserie", numSerie)
            put("M_EArm_Fecha_Cad", fechaCaducidad)
        }
        return db.update("MOD_EXP_ARMAS", values, "Id_M_EArm = ?", arrayOf(idArma.toString())) > 0
    }

    fun eliminarArma(idArma: Int): Boolean {
        val db = this.writableDatabase
        return db.delete("MOD_EXP_ARMAS", "Id_M_EArm = ?", arrayOf(idArma.toString())) > 0
    }

    // ====================================================================
    // === MÉTODOS DEL MÓDULO DE CARNETS MILITARES                      ===
    // ====================================================================

    fun anadirCarnet(
        idUsuario: Int,
        tipo: String,
        fechaConcesion: String,
        fechaCaducidad: String,
    ): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT Id_M_Carnet FROM MOD_CARNET WHERE Id_Usuario = ? AND Tipo_Carnet = ?",
            arrayOf(idUsuario.toString(), tipo)
        )
        val existe = cursor.moveToFirst()
        cursor.close()

        if (existe) return false

        val dbWrite = this.writableDatabase
        val values = android.content.ContentValues().apply {
            put("Id_Usuario", idUsuario)
            put("Tipo_Carnet", tipo)
            put("M_Carn_Fecha_Concesion", fechaConcesion)
            put("M_Carn_Fecha_Caducidad", fechaCaducidad)
        }
        return dbWrite.insert("MOD_CARNET", null, values) != -1L
    }

    fun obtenerCarnets(idUsuario: Int): android.database.Cursor {
        val db = this.readableDatabase
        return db.rawQuery(
            "SELECT * FROM MOD_CARNET WHERE Id_Usuario = ? ORDER BY Id_M_Carnet DESC",
            arrayOf(idUsuario.toString())
        )
    }

    fun modificarCarnet(
        idCarnet: Int,
        tipo: String,
        fechaConcesion: String,
        fechaCaducidad: String,
    ): Boolean {
        val db = this.writableDatabase

        //Comprobamos si el usuario ya tiene esta clase de carnet registrada en OTRA fila distinta.
        // Usamos Id_M_Carnet != ? para excluir la propia fila que estamos editando ahora mismo.
        // Usamos la subconsulta para averiguar el Id_Usuario automáticamente sin pedirlo por parámetro.
        val cursor = db.rawQuery(
            """
        SELECT Id_M_Carnet FROM MOD_CARNET 
        WHERE Tipo_Carnet = ? 
        AND Id_M_Carnet != ? 
        AND Id_Usuario = (SELECT Id_Usuario FROM MOD_CARNET WHERE Id_M_Carnet = ?)
        """,
            arrayOf(tipo, idCarnet.toString(), idCarnet.toString())
        )
        val existeDuplicado = cursor.moveToFirst()
        cursor.close()

        if (existeDuplicado) return false

        val values = android.content.ContentValues().apply {
            put("Tipo_Carnet", tipo)
            put("M_Carn_Fecha_Concesion", fechaConcesion)
            put("M_Carn_Fecha_Caducidad", fechaCaducidad)
        }
        return db.update("MOD_CARNET", values, "Id_M_Carnet = ?", arrayOf(idCarnet.toString())) > 0
    }

    fun eliminarCarnet(idCarnet: Int): Boolean {
        val db = this.writableDatabase
        return db.delete("MOD_CARNET", "Id_M_Carnet = ?", arrayOf(idCarnet.toString())) > 0
    }

    // ====================================================================
    // === MÉTODOS DEL MÓDULO DE TCGF (PRUEBAS FÍSICAS)                 ===
    // ====================================================================

    fun anadirTcgf(idUsuario: Int, fecha: String, puntuacion: String, apto: String): Boolean {
        val db = this.readableDatabase
        // Comprobamos que no haya ya una prueba guardada en la misma fecha
        val cursor = db.rawQuery(
            "SELECT Id_M_TCGF FROM MOD_TCGF WHERE Id_Usuario = ? AND M_Tcgf_Fecha = ?",
            arrayOf(idUsuario.toString(), fecha)
        )
        val existe = cursor.moveToFirst()
        cursor.close()

        if (existe) return false

        val dbWrite = this.writableDatabase
        val values = android.content.ContentValues().apply {
            put("Id_Usuario", idUsuario)
            put("M_Tcgf_Fecha", fecha)
            put("M_Tcgf_Puntuacion", puntuacion)
            put("M_Tcgf_Apto", apto)
        }
        return dbWrite.insert("MOD_TCGF", null, values) != -1L
    }

    fun obtenerTcgf(idUsuario: Int): android.database.Cursor {
        val db = this.readableDatabase
        return db.rawQuery(
            "SELECT * FROM MOD_TCGF WHERE Id_Usuario = ? ORDER BY Id_M_TCGF DESC",
            arrayOf(idUsuario.toString())
        )
    }

    fun modificarTcgf(idTcgf: Int, fecha: String, puntuacion: String, apto: String): Boolean {
        val db = this.writableDatabase

        // Comprobamos que no haya ya una prueba guardada en la misma fecha
        val cursor = db.rawQuery(
            """
        SELECT Id_M_TCGF FROM MOD_TCGF 
        WHERE M_Tcgf_Fecha = ? 
        AND Id_M_TCGF != ? 
        AND Id_Usuario = (SELECT Id_Usuario FROM MOD_TCGF WHERE Id_M_TCGF = ?)
        """,
            arrayOf(fecha, idTcgf.toString(), idTcgf.toString())
        )
        val existeDuplicado = cursor.moveToFirst()
        cursor.close()

        // Si la fecha ya existe devolvemos false
        if (existeDuplicado) return false

        // Actualización de los datos si la fecha es válida
        val values = android.content.ContentValues().apply {
            put("M_Tcgf_Fecha", fecha)
            put("M_Tcgf_Puntuacion", puntuacion)
            put("M_Tcgf_Apto", apto)
        }
        return db.update("MOD_TCGF", values, "Id_M_TCGF = ?", arrayOf(idTcgf.toString())) > 0
    }

    fun eliminarTcgf(idTcgf: Int): Boolean {
        val db = this.writableDatabase
        return db.delete("MOD_TCGF", "Id_M_TCGF = ?", arrayOf(idTcgf.toString())) > 0
    }

    // ====================================================================
    // === EXPORTACIÓN DE DATOS (CSV Y JSON) EN BASE DE DATOS           ===
    // ====================================================================

    // Función para JSON. Lee una tabla y la convierte en una lista de objetos.
    private fun cursorToJsonArray(cursor: android.database.Cursor): JSONArray {
        // Creamos una lista JSON vacía
        val array = JSONArray()

        // Si la tabla tiene al menos un dato, empezamos a leer
        if (cursor.moveToFirst()) {
            // Guardamos los nombres de todas las columnas (ejemplo: "Nombre", "Num_Bod")
            val columnNames = cursor.columnNames

            // Hacemos un bucle, repetimos esto por cada fila que haya en la tabla
            do {
                // Creamos un Objeto JSON nuevo para esta fila exacta
                val obj = JSONObject()

                // Repasamos una por una todas las columnas de esta fila
                for (col in columnNames) {
                    val colIndex = cursor.getColumnIndex(col)
                    // Si la celda está vacía ponemos "", si tiene algo lo sacamos como texto
                    val value = if (cursor.isNull(colIndex)) "" else cursor.getString(colIndex)
                    // Metemos el dato en el objeto (Ejemplo: "Nombre" -> "Eduardo Jose")
                    obj.put(col, value)
                }
                // Añadimos la fila ya procesada a la lista
                array.put(obj)

            } while (cursor.moveToNext()) // Pasamos a la siguiente fila
        }
        cursor.close() // cerramos el cursor
        return array // devolvemos un array del tipo JSONArray
    }

    // Generamos el texto en formato JSON llamando a todas las tablas.
    // ====================================================================
    // === EXPORTACIÓN DE LA COPIA DE SEGURIDAD (JSON)                  ===
    // ====================================================================

    // Genera el texto en formato JSON llamando a todas las tablas del usuario.
    fun exportarTodoAJson(idUsuario: Int): String {
        // Creamos el objeto "Raíz" (El contenedor principal donde irá todos)
        val raiz = JSONObject()

        try {
            // Llamamos a nuestra función tabla por tabla y la metemos en la raíz con una etiqueta

            raiz.put("Filiacion", cursorToJsonArray(obtenerFiliacion(idUsuario)))
            raiz.put("Empleos", cursorToJsonArray(obtenerEmpleos(idUsuario)))
            raiz.put("Destinos", cursorToJsonArray(obtenerDestinos(idUsuario)))
            raiz.put("Misiones", cursorToJsonArray(obtenerMisiones(idUsuario)))
            raiz.put("Comisiones_Servicio", cursorToJsonArray(obtenerComisiones(idUsuario)))
            raiz.put("Situacion_Administrativa", cursorToJsonArray(obtenerSituaciones(idUsuario)))
            raiz.put("Especialidad_Fundamental", cursorToJsonArray(obtenerCEEFs(idUsuario)))
            raiz.put(
                "Relaciones_Administrativas",
                cursorToJsonArray(obtenerRelacionesAdmin(idUsuario))
            )
            raiz.put("Trienios", cursorToJsonArray(obtenerTrienios(idUsuario)))
            raiz.put("Recompensas", cursorToJsonArray(obtenerRecompensas(idUsuario)))
            raiz.put("Distintivos", cursorToJsonArray(obtenerDistintivos(idUsuario)))
            raiz.put("Aptitudes", cursorToJsonArray(obtenerAptitudes(idUsuario)))
            raiz.put("Cursos_Militares", cursorToJsonArray(obtenerCursosMilitares(idUsuario)))
            raiz.put(
                "Titulos_y_Cursos_Civiles",
                cursorToJsonArray(obtenerTitulosCiviles(idUsuario))
            )
            raiz.put("Idiomas", cursorToJsonArray(obtenerIdiomas(idUsuario)))
            raiz.put("Evaluacion_Ascenso", cursorToJsonArray(obtenerEvaluaciones(idUsuario)))
            raiz.put("TMI", cursorToJsonArray(obtenerTMIs(idUsuario)))
            raiz.put("Habilitaciones_HPS", cursorToJsonArray(obtenerHps(idUsuario)))
            raiz.put("Armas_Particulares", cursorToJsonArray(obtenerArmas(idUsuario)))
            raiz.put("Carnets_Conducir", cursorToJsonArray(obtenerCarnets(idUsuario)))
            raiz.put("Pruebas_Fisicas_TCGF", cursorToJsonArray(obtenerTcgf(idUsuario)))

        } catch (e: Exception) {
            // Si hay algún problema convirtiendo los datos, dejamos un registro interno (Log)
            android.util.Log.e("ExportarJSON", "Error al generar la copia de seguridad", e)

        }
        // Convertimos ese paquete de datos (raiz) en un texto ordenado (poniendo 4 espacios de sangría para que sea legible)
        return raiz.toString(4)
    }


    // ====================================================================
    // === FUNCION DE LA COPIA DE SEGURIDAD (CVS)                       ===
    // ====================================================================
    //  Función para CSV. Lee una tabla y la convierte en texto con punto y coma (;).
    private fun agregarCursorACsv(
        titulo: String,
        cursor: android.database.Cursor,
        sb: StringBuilder,
    ) {

        // Ponemos el título de la tabla (El \n es un salto de línea)
        sb.append("--- $titulo ---\n")

        if (cursor.moveToFirst()) {
            // Sacamos los nombres de las columnas para hacer la cabecera
            val columnNames = cursor.columnNames
            // Unimos los nombres con ";" y damos un Intro
            sb.append(columnNames.joinToString(";")).append("\n")

            // Bucle que repetimos por cada fila
            do {
                // Nos creamos una lista mutable a la cual le podemos seguir añadiendo datos
                val valoresFila = mutableListOf<String>()

                for (col in columnNames) {
                    val colIndex = cursor.getColumnIndex(col)
                    val value = if (cursor.isNull(colIndex)) "" else cursor.getString(colIndex)

                    // Si el usuario escribió un ";" en la app,
                    // rompería las columnas del cvs. Aquí reemplazamos los ";" por "," para evitarlo.
                    val limpiarVariable = value.replace("\n", " ").replace(";", ",")

                    valoresFila.add(limpiarVariable)
                }
                // Unimos todos los datos de la fila con ";" y damos un Intro
                sb.append(valoresFila.joinToString(";")).append("\n")

            } while (cursor.moveToNext())
        } else { // Si no hay registro pone el siguiente mensaje
            sb.append("Sin registros\n")
        }
        // Damos un intro extra para separar visualmente las tablas
        sb.append("\n")
        cursor.close()
    }

    // Generamos el texto completo en CSV
    fun exportarACsv(idUsuario: Int): String {
        // StringBuilder está diseñado para unir textos grandes muy rápido
        val sb = StringBuilder()

        // '\uFEFF' es una clave que le dice a Excel que el archivo usa español (UTF-8)
        // para que las tildes y las eñes se vean perfectas.
        sb.append("\uFEFF")
        // Llamamos a nuestra función tabla por tabla

        agregarCursorACsv("FILIACIÓN", obtenerFiliacion(idUsuario), sb)
        agregarCursorACsv("EMPLEOS", obtenerEmpleos(idUsuario), sb)
        agregarCursorACsv("DESTINOS", obtenerDestinos(idUsuario), sb)
        agregarCursorACsv("MISIONES", obtenerMisiones(idUsuario), sb)
        agregarCursorACsv("COMISIONES DE SERVICIO", obtenerComisiones(idUsuario), sb)
        agregarCursorACsv("SITUACIÓN ADMINISTRATIVA", obtenerSituaciones(idUsuario), sb)
        agregarCursorACsv("ESPECIALIDAD FUNDAMENTAL", obtenerCEEFs(idUsuario), sb)
        agregarCursorACsv("RELACIONES ADMINISTRATIVAS", obtenerRelacionesAdmin(idUsuario), sb)
        agregarCursorACsv("TRIENIOS", obtenerTrienios(idUsuario), sb)
        agregarCursorACsv("RECOMPENSAS", obtenerRecompensas(idUsuario), sb)
        agregarCursorACsv("DISTINTIVOS", obtenerDistintivos(idUsuario), sb)
        agregarCursorACsv("APTITUDES", obtenerAptitudes(idUsuario), sb)
        agregarCursorACsv("CURSOS MILITARES", obtenerCursosMilitares(idUsuario), sb)
        agregarCursorACsv("TÍTULOS Y CURSOS CIVILES", obtenerTitulosCiviles(idUsuario), sb)
        agregarCursorACsv("IDIOMAS", obtenerIdiomas(idUsuario), sb)
        agregarCursorACsv("EVALUACIÓN PARA EL ASCENSO", obtenerEvaluaciones(idUsuario), sb)
        agregarCursorACsv("TMI", obtenerTMIs(idUsuario), sb)
        agregarCursorACsv("HABILITACIONES (HPS)", obtenerHps(idUsuario), sb)
        agregarCursorACsv("ARMAS PARTICULARES", obtenerArmas(idUsuario), sb)
        agregarCursorACsv("CARNETS DE CONDUCIR", obtenerCarnets(idUsuario), sb)
        agregarCursorACsv("PRUEBAS FÍSICAS (TCGF)", obtenerTcgf(idUsuario), sb)

        // Devolvemos el conjunto del texto con todas nuestras tablas pasado a string
        return sb.toString()
    }

    // ====================================================================
    // === MÉTODOS DEL MÓDULO LOG DE ACTIVIDAD                          ===
    // ====================================================================

    // Este metodo se usa en el LoginActivity justo después de que el usuario meta bien su clave

    fun registrarAcceso(idUsuario: Int, dni: String, fechaHora: String): Boolean {
        val dbWrite = this.writableDatabase  // ponemos la BBDD en modo escritura
        val values = android.content.ContentValues().apply {
            put("Id_Usuario", idUsuario)
            put("DNI", dni)
            put("Fecha_Hora", fechaHora)
        }
        return dbWrite.insert("REGISTRO_ACTIVIDAD", null, values) != -1L
    }

    // Este metodo se usa para leer la lista
    fun obtenerAccesos(idUsuario: Int): android.database.Cursor {
        val db = this.readableDatabase
        // Ordenamos DESC para ver la última conexión arriba del item
        return db.rawQuery(
            "SELECT * FROM REGISTRO_ACTIVIDAD WHERE Id_Usuario = ? ORDER BY Id_Log DESC",
            arrayOf(idUsuario.toString())
        )
    }

    // ====================================================================
    // === VISOR Y BUSCADOR GLOBAL DEL EXPEDIENTE       ===
    // ====================================================================

    fun obtenerResumenExpediente(idUsuario: Int, filtroBod: String, filtroFecha: String): String {
        val sb = StringBuilder()
        val db = this.readableDatabase
        // Me creo unas variables para los filtro en las cuales les digo si no estan vacias si estan vacias en un false y si no true
        val usaFiltroBod = filtroBod.isNotEmpty()
        val usaFiltroFecha = filtroFecha.isNotEmpty()

        // 1. Función interna para módulos con BOD (Empleos, Destinos, etc.), ya que no todos los modulos tienen BOD
        fun procesarConBod(
            titulo: String,
            tabla: String,
            colNombre: String,
            colFechaBod: String,
            colNumBod: String,
        ) {
            var sql = "SELECT * FROM $tabla WHERE Id_Usuario = ?"
            val args = mutableListOf(idUsuario.toString())

            if (usaFiltroBod) {
                // Si la variable no esta vacia le añado lo siguente a la sentencia sql que tengo en la variable sql
                // para que busque por el numero de bod
                sql += " AND $colNumBod = ?"
                args.add(filtroBod)
            }
            if (usaFiltroFecha) {
                // Si la variable no esta vacia le añado lo siguente a la sentencia sql que tengo en la variable query
                // para que busque por la fecha del BOD
                sql += " AND $colFechaBod = ?"
                args.add(filtroFecha)
            }

            val cursor =
                db.rawQuery(sql, args.toTypedArray()) // coge mi mutableListOf y lo transforma
            // en un array que es lo que necesita recibir el rawQuery

            // Solo mostramos si hay resultados o si no hay filtros aplicados
            if (cursor.count > 0 || (!usaFiltroBod && !usaFiltroFecha)) {
                sb.append("=== $titulo ===\n")
                if (cursor.moveToFirst()) {
                    do {
                        val nombre = cursor.getString(cursor.getColumnIndexOrThrow(colNombre))
                        val fecha =
                            cursor.getString(cursor.getColumnIndexOrThrow(colFechaBod)) ?: "N/D"
                        val bod = cursor.getString(cursor.getColumnIndexOrThrow(colNumBod)) ?: "N/D"
                        sb.append("• $nombre (BOD: $bod - Fecha: $fecha)\n")
                    } while (cursor.moveToNext())
                } else {
                    sb.append("  Sin registros.\n")
                }
                sb.append("__________________________________________\n\n")
            }
            cursor.close()
        }

        // 2. Función interna para módulos sin BOD (TMI, Carnets, etc.) la misma explicacion que la
        // funcion anterior pero solo filtra por fecha
        fun procesarSinBod(titulo: String, tabla: String, colNombre: String, colExtra: String?) {
            // Si el usuario busca un BOD, omitimos las  tablas que no tienen BOD, y nos salimos de la funcion
            if (usaFiltroBod) return

            var sql = "SELECT * FROM $tabla WHERE Id_Usuario = ?"
            val args = mutableListOf(idUsuario.toString())

            // Solo filtramos si la tabla tiene una columna  de fecha y el usuario ha buscado por fecha
            if (usaFiltroFecha && colExtra != null) {
                sql += " AND $colExtra = ?"
                args.add(filtroFecha)
            }

            val cursor = db.rawQuery(sql, args.toTypedArray())

            if (cursor.count > 0 || !usaFiltroFecha) {
                sb.append("=== $titulo ===\n")
                if (cursor.moveToFirst()) {
                    do {
                        val nombre = cursor.getString(cursor.getColumnIndexOrThrow(colNombre))
                        sb.append("• $nombre")
                        if (colExtra != null) {
                            val dato =
                                cursor.getString(cursor.getColumnIndexOrThrow(colExtra)) ?: "N/D"
                            sb.append(" (Fecha: $dato)")
                        }
                        sb.append("\n")
                    } while (cursor.moveToNext())
                } else {
                    sb.append("  Sin registros.\n")
                }
                sb.append("__________________________________________\n\n")
            }
            cursor.close()
        }

        // --- LLAMAMOS A TODAS TUS TABLAS ---
        // Tablas que tienen BOD
        procesarConBod("EMPLEOS", "MOD_EMPLEOS", "Nom_Empleo", "M_Empl_Fecha_Bod", "M_Empl_Nbod")
        procesarConBod("DESTINOS", "MOD_DESTINOS", "Nom_Destino", "M_Dest_Fecha_Bod", "M_Dest_Nbod")
        procesarConBod("MISIONES", "MOD_MISIONES", "Nom_Mision", "M_Misi_Fecha_Bod", "M_Misi_Nbod")
        procesarConBod(
            "COMISIONES DE SERVICIO",
            "MOD_COMISION_SER",
            "Nom_Comision",
            "M_Cser_Fecha_Bod",
            "M_Cser_Nbod"
        )
        procesarConBod(
            "SITUACIÓN ADMINISTRATIVA",
            "MOD_SITUA_ADMIN",
            "Nom_Sit_Admini",
            "M_Sadm_Fecha_Bod",
            "M_Sadm_Nbod"
        )
        procesarConBod(
            "TRIENIOS",
            "MOD_TRIENIOS",
            "Tipo_Trienio",
            "M_Trie_Fecha_Bod",
            "M_Trie_Nbod"
        )
        procesarConBod(
            "RECOMPENSAS",
            "MOD_RECOMPENSAS",
            "Nom_Recompensa",
            "M_Reco_Fecha_Bod",
            "M_Reco_Nbod"
        )
        procesarConBod(
            "DISTINTIVOS",
            "MOD_DISTINTIVOS",
            "Nom_Distintivo",
            "M_Dist_Fecha_Bod",
            "M_Dist_Nbod"
        )
        procesarConBod(
            "APTITUDES",
            "MOD_APTITUDES",
            "Nom_Aptitud",
            "M_Apti_Fecha_Bod",
            "M_Apti_Nbod"
        )
        procesarConBod(
            "ESPECIALIDAD FUNDAMENTAL",
            "MOD_CEE_FUNDAMENTAL",
            "Nom_CEEF",
            "M_Ceef_Fecha_Bod",
            "M_Ceef_Nbod"
        )
        procesarConBod(
            "RELACIONES ADMINISTRACIÓN",
            "MOD_RELA_ADMINISTRACION",
            "Nom_Rel_Admin",
            "M_Radm_Fecha_Bod",
            "M_Radm_Nbod"
        )
        procesarConBod(
            "CURSOS MILITARES",
            "MOD_CUR_MILITAR",
            "Nom_Cur_Mili",
            "M_Cmili_Fecha_Bod",
            "M_Cmili_Nbod"
        )
        procesarConBod(
            "EVALUACIÓN ASCENSO",
            "MOD_EVALUACION_ASCENSO",
            "Nom_Evaluacion",
            "M_Eva_Fecha_Bod",
            "M_Eva_Nbod"
        )
        procesarConBod("IDIOMAS", "MOD_IDIOMA", "Nom_idioma", "M_Idi_Fecha_Bod", "M_Idi_Nbod")
        // Tablas que NO tienen BOD
        procesarSinBod("TÍTULOS CIVILES", "MOD_TITULOS_CIVILES", "Nom_Titulo", null)
        procesarSinBod("HABILITACIONES (HPS)", "MOD_HPS", "Nom_Habilitacion", "Fecha_M_Caducidad")
        procesarSinBod("TARJETA MILITAR (TMI)", "MOD_TMI", "N_Tarjeta", "M_Tmi_Fecha_Cadu")
        procesarSinBod("ARMAS PARTICULARES", "MOD_EXP_ARMAS", "Nom_Arma", "M_EArm_Fecha_Cad")
        procesarSinBod("PRUEBAS FÍSICAS (TCGF)", "MOD_TCGF", "M_Tcgf_Puntuacion", "M_Tcgf_Fecha")
        procesarSinBod("CARNETS DE CONDUCIR", "MOD_CARNET", "Tipo_Carnet", "M_Carn_Fecha_Caducidad")
        if (sb.isEmpty()) {
            return "No se ha encontrado ninguna información con los filtros aplicados."
        }

        return sb.toString()
    }


}