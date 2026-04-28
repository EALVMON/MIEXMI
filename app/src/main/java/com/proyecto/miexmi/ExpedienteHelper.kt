package com.proyecto.miexmi

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
// imprtaciones necesarias para exportar datos
import org.json.JSONArray
import org.json.JSONObject
class ExpedienteHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "MiExpediente.db"
        const val DATABASE_VERSION = 1

        // ====================================================================
        // === DEFINICIÓN DE TABLAS PRINCIPALES (USUARIO Y LOG)             ===
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

        private const val SQL_CREATE_REGISTRO_ACTIVIDAD = """
            CREATE TABLE REGISTRO_ACTIVIDAD (
                Id_Log INTEGER PRIMARY KEY AUTOINCREMENT,
                Id_Usuario INTEGER,
                DNI TEXT,
                Fecha_Hora TEXT,
                FOREIGN KEY(Id_Usuario) REFERENCES USUARIO(Id_Usuario) ON DELETE CASCADE
            )
        """

        // ====================================================================
        // === TABLAS DE MÓDULOS (RELACIONES 1:N)                           ===
        // ====================================================================

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
        // Activar el soporte para Foreign Keys (importante para el CASCADE)
        db.setForeignKeyConstraintsEnabled(true)
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Ejecutamos la creación de todas las tablas
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
        // Se borran las tablas antiguas en orden inverso a sus dependencias
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

    // Devuelve el ID del nuevo usuario, o -1 si el DNI ya existe (por el UNIQUE)
    fun registrarUsuario(dni: String, contrasena: String): Long {
        val db = this.writableDatabase
        val values = android.content.ContentValues().apply {
            put("Dni", dni)
            put("Contraseña", contrasena)
        }
        // El insert devuelve -1 si falla (ej: DNI repetido)
        return db.insert("USUARIO", null, values)
    }



    // Comprueba si un usuario existe en la base de datos y si su contraseña es correcta.
    // Devuelve el Id_Usuario (ej.: 1, 2, 3...) si totalidad es correcto, o -1 si falla.
    fun comprobarLogin(dni: String, contrasena: String): Int {

        // 1. Abrimos la base de datos en modo LECTURA (readableDatabase).
        // Usamos lectura porque solo vamos a buscar información, no a guardar nada nuevo.
        val db = this.readableDatabase

        // 2. Lanzamos la pregunta (Query) a la base de datos usando el 'Cursor'.
        // Las interrogaciones (?) son un escudo de seguridad: evitan que hackers
        // inyecten código malicioso. Android pondrá el DNI en la primera '?', y la clave en la segunda.
        val cursor = db.rawQuery(
            "SELECT Id_Usuario FROM USUARIO WHERE Dni = ? AND Contraseña = ?",
            arrayOf(dni, contrasena)
        )

        // 3. Preparamos una variable con valor -1 (pesimista: asumimos que no existe por defecto).
        var idUsuario = -1

        // 4. El cursor intenta moverse al primer resultado que encontró en la tabla.
        // Si 'moveToFirst()' es verdadero (true), significa que SÍ encontró a ese usuario.
        if (cursor.moveToFirst()) {

            // Como sí lo encontró, leemos el dato de la columna 0.
            // La columna 0 es la primera que pedimos arriba en el SELECT (Id_Usuario).
            idUsuario = cursor.getInt(0)
        }

        // 5. Siempre hay que cerrar el cursor al terminar.
        // Si no lo hacemos, la memoria RAM del móvil se quedará bloqueada y la app irá lenta.
        cursor.close()

        // 6. Devolvemos la respuesta final a la pantalla de Login (el ID real o él -1).
        return idUsuario
    }

    // Obtiene el DNI de un usuario a partir de su ID
    fun obtenerDniPorId(idUsuario: Int): String? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT Dni FROM USUARIO WHERE Id_Usuario = ?", arrayOf(idUsuario.toString()))

        var dni: String? = null
        if (cursor.moveToFirst()) {
            dni = cursor.getString(0)
        }
        cursor.close()
        return dni
    }

    // Cambia la contraseña si la actual es correcta. Devuelve true si tiene éxito, false si falla.
    fun cambiarContrasena(idUsuario: Int, passActual: String, passNueva: String): Boolean {
        val db = this.writableDatabase

        // 1. Primero comprobamos que la clave actual es la correcta
        val cursor = db.rawQuery(
            "SELECT Id_Usuario FROM USUARIO WHERE Id_Usuario = ? AND Contraseña = ?",
            arrayOf(idUsuario.toString(), passActual)
        )
        val existe = cursor.moveToFirst()
        cursor.close()

        // 2. Si la contraseña actual es correcta, guardamos la nueva
        if (existe) {
            val values = android.content.ContentValues().apply {
                put("Contraseña", passNueva)
            }
            // Actualizamos la fila del usuario
            val filasAfectadas = db.update("USUARIO", values, "Id_Usuario = ?", arrayOf(idUsuario.toString()))
            return filasAfectadas > 0
        }

        // Si la clave actual no era correcta, devolvemos falso
        return false
    }

    // ====================================================================
    // === MÉTODOS DEL MODULO DE FILIACIÓN                              ===
    // ====================================================================

    // Guarda o actualiza los datos personales del militar
    fun guardarFiliacion(idUsuario: Int, nombre: String, apellidos: String, tmi: String, fechaIncorp: String, numEscalafon: Int): Boolean {
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
        val cursor = db.rawQuery("SELECT Id_Filia FROM FILIACION WHERE Id_Usuario = ?", arrayOf(idUsuario.toString()))
        val existe = cursor.moveToFirst()
        cursor.close()

        if (existe) {
            // Si ya existen, ACTUALIZAMOS los datos (UPDATE)
            val filasAfectadas = db.update("FILIACION", values, "Id_Usuario = ?", arrayOf(idUsuario.toString()))
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
        // Devuelve todas las columnas de la tabla FILIACION para ese usuario
        return db.rawQuery("SELECT * FROM FILIACION WHERE Id_Usuario = ?", arrayOf(idUsuario.toString()))
    }

    // ====================================================================
    // === MÉTODOS DEL MÓDULO DE TMI (TARJETA MILITAR DE IDENTIDAD)     ===
    // ====================================================================

    // AÑADIR una nueva TMI
    // 1. AÑADIR una nueva TMI (Con protección anti-duplicados)
    fun anadirTMI(idUsuario: Int, numTarjeta: String, fechaCaducidad: String): Boolean {
        val db = this.readableDatabase

        // PRIMERO: Comprobamos si esta tarjeta ya existe para este usuario
        val cursor = db.rawQuery(
            "SELECT Id_M_Tmi FROM MOD_TMI WHERE Id_Usuario = ? AND N_Tarjeta = ?",
            arrayOf(idUsuario.toString(), numTarjeta)
        )
        val existe = cursor.moveToFirst()
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
    fun obtenerTMIs(idUsuario: Int): android.database.Cursor {
        val db = this.readableDatabase
        // Ordenamos por Id_M_Tmi descendente para ver la más reciente primero
        return db.rawQuery(
            "SELECT * FROM MOD_TMI WHERE Id_Usuario = ? ORDER BY Id_M_Tmi DESC",
            arrayOf(idUsuario.toString())
        )
    }

    // MODIFICAR una TMI existente
    fun modificarTMI(idTmi: Int, numTarjeta: String, fechaCaducidad: String): Boolean {
        val db = this.writableDatabase
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

        if (existe) return false // Si ya existe, bloqueamos el guardado

        // Convertimos el número de BOD a entero (si está vacío, guardamos un 0)
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
        val numBod = numBodStr.toIntOrNull() ?: 0
        val db = this.writableDatabase
        val values = android.content.ContentValues().apply {
            put("Nom_CEEF", nombre)
            put("M_Ceef_Fecha_Bod", fechaBod)
            put("M_Ceef_Nbod", numBod)
        }
        return db.update("MOD_CEE_FUNDAMENTAL", values, "Id_M_CEEF = ?", arrayOf(idCeef.toString())) > 0
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

    fun modificarEmpleo(idEmpleo: Int, nombre: String, fechaBod: String, numBodStr: String): Boolean {
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

    fun anadirDestino(idUsuario: Int, nombre: String, fechaBod: String, numBodStr: String): Boolean {
        val db = this.readableDatabase
        // Filtro anti-duplicados
        val cursor = db.rawQuery(
            "SELECT Id_M_Dest FROM MOD_DESTINOS WHERE Id_Usuario = ? AND Nom_Destino = ?",
            arrayOf(idUsuario.toString(), nombre)
        )
        val existe = cursor.moveToFirst()
        cursor.close()

        if (existe) return false

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

    fun modificarDestino(idDestino: Int, nombre: String, fechaBod: String, numBodStr: String): Boolean {
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

    fun modificarMision(idMision: Int, nombre: String, fechaBod: String, numBodStr: String): Boolean {
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

    fun anadirComision(idUsuario: Int, nombre: String, fechaBod: String, numBodStr: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT Id_M_Cser FROM MOD_COMISION_SER WHERE Id_Usuario = ? AND Nom_Comision = ?",
            arrayOf(idUsuario.toString(), nombre)
        )
        val existe = cursor.moveToFirst()
        cursor.close()

        if (existe) return false

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

    fun modificarComision(idComision: Int, nombre: String, fechaBod: String, numBodStr: String): Boolean {
        val numBod = numBodStr.toIntOrNull() ?: 0
        val db = this.writableDatabase
        val values = android.content.ContentValues().apply {
            put("Nom_Comision", nombre)
            put("M_Cser_Fecha_Bod", fechaBod)
            put("M_Cser_Nbod", numBod)
        }
        return db.update("MOD_COMISION_SER", values, "Id_M_Cser = ?", arrayOf(idComision.toString())) > 0
    }

    fun eliminarComision(idComision: Int): Boolean {
        val db = this.writableDatabase
        return db.delete("MOD_COMISION_SER", "Id_M_Cser = ?", arrayOf(idComision.toString())) > 0
    }

// ====================================================================
    // === MÉTODOS DEL MÓDULO DE EVALUACIÓN PARA EL ASCENSO             ===
    // ====================================================================

    fun anadirEvaluacion(idUsuario: Int, nombre: String, resultadoApto: String, fechaBod: String, numBodStr: String): Boolean {
        val db = this.readableDatabase
        // Comprobamos si ya existe esta evaluación para este usuario
        val cursor = db.rawQuery(
            "SELECT Id_M_Eva FROM MOD_EVALUACION_ASCENSO WHERE Id_Usuario = ? AND Nom_Evaluacion = ?",
            arrayOf(idUsuario.toString(), nombre)
        )
        val existe = cursor.moveToFirst()
        cursor.close()

        if (existe) return false

        val numBod = numBodStr.toIntOrNull() ?: 0
        val dbWrite = this.writableDatabase
        val values = android.content.ContentValues().apply {
            put("Id_Usuario", idUsuario)
            put("Nom_Evaluacion", nombre)
            put("Resultado", resultadoApto) // Guardamos Apto, No Apto, o No Presentado
            put("M_Eva_Fecha_Bod", fechaBod)
            put("M_Eva_Nbod", numBod)
        }
        return dbWrite.insert("MOD_EVALUACION_ASCENSO", null, values) != -1L
    }

    fun obtenerEvaluaciones(idUsuario: Int): android.database.Cursor {
        val db = this.readableDatabase
        return db.rawQuery(
            "SELECT * FROM MOD_EVALUACION_ASCENSO WHERE Id_Usuario = ? ORDER BY Id_M_Eva DESC",
            arrayOf(idUsuario.toString())
        )
    }

    fun modificarEvaluacion(idEvaluacion: Int, nombre: String, resultadoApto: String, fechaBod: String, numBodStr: String): Boolean {
        val numBod = numBodStr.toIntOrNull() ?: 0
        val db = this.writableDatabase
        val values = android.content.ContentValues().apply {
            put("Nom_Evaluacion", nombre)
            put("Resultado", resultadoApto)
            put("M_Eva_Fecha_Bod", fechaBod)
            put("M_Eva_Nbod", numBod)
        }
        return db.update("MOD_EVALUACION_ASCENSO", values, "Id_M_Eva = ?", arrayOf(idEvaluacion.toString())) > 0
    }

    fun eliminarEvaluacion(idEvaluacion: Int): Boolean {
        val db = this.writableDatabase
        return db.delete("MOD_EVALUACION_ASCENSO", "Id_M_Eva = ?", arrayOf(idEvaluacion.toString())) > 0
    }

    // ====================================================================
    // === MÉTODOS DEL MÓDULO DE HABILITACIÓN DE SEGURIDAD (HPS)        ===
    // ====================================================================

    fun anadirHps(idUsuario: Int, nombre: String, fechaConcesion: String, fechaCaducidad: String): Boolean {
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

    fun modificarHps(idHps: Int, nombre: String, fechaConcesion: String, fechaCaducidad: String): Boolean {
        val db = this.writableDatabase
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

    fun anadirRelacionAdmin(idUsuario: Int, nombre: String, fechaBod: String, numBodStr: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT Id_M_Radm FROM MOD_RELA_ADMINISTRACION WHERE Id_Usuario = ? AND Nom_Rel_Admin = ?",
            arrayOf(idUsuario.toString(), nombre)
        )
        val existe = cursor.moveToFirst()
        cursor.close()

        if (existe) return false

        val numBod = numBodStr.toIntOrNull() ?: 0
        val dbWrite = this.writableDatabase
        val values = android.content.ContentValues().apply {
            put("Id_Usuario", idUsuario)
            put("Nom_Rel_Admin", nombre)
            put("M_Radm_Fecha_Bod", fechaBod)
            put("M_Radm_Nbod", numBod)
        }
        return dbWrite.insert("MOD_RELA_ADMINISTRACION", null, values) != -1L
    }

    fun obtenerRelacionesAdmin(idUsuario: Int): android.database.Cursor {
        val db = this.readableDatabase
        return db.rawQuery(
            "SELECT * FROM MOD_RELA_ADMINISTRACION WHERE Id_Usuario = ? ORDER BY Id_M_Radm DESC",
            arrayOf(idUsuario.toString())
        )
    }

    fun modificarRelacionAdmin(idRelacion: Int, nombre: String, fechaBod: String, numBodStr: String): Boolean {
        val numBod = numBodStr.toIntOrNull() ?: 0
        val db = this.writableDatabase
        val values = android.content.ContentValues().apply {
            put("Nom_Rel_Admin", nombre)
            put("M_Radm_Fecha_Bod", fechaBod)
            put("M_Radm_Nbod", numBod)
        }
        return db.update("MOD_RELA_ADMINISTRACION", values, "Id_M_Radm = ?", arrayOf(idRelacion.toString())) > 0
    }

    fun eliminarRelacionAdmin(idRelacion: Int): Boolean {
        val db = this.writableDatabase
        return db.delete("MOD_RELA_ADMINISTRACION", "Id_M_Radm = ?", arrayOf(idRelacion.toString())) > 0
    }

    // ====================================================================
    // === MÉTODOS DEL MÓDULO DE SITUACIONES ADMINISTRATIVAS            ===
    // ====================================================================

    fun anadirSituacion(idUsuario: Int, nombre: String, fechaBod: String, numBodStr: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT Id_M_Sadm FROM MOD_SITUA_ADMIN WHERE Id_Usuario = ? AND Nom_Sit_Admini = ?",
            arrayOf(idUsuario.toString(), nombre)
        )
        val existe = cursor.moveToFirst()
        cursor.close()

        if (existe) return false

        val numBod = numBodStr.toIntOrNull() ?: 0
        val dbWrite = this.writableDatabase
        val values = android.content.ContentValues().apply {
            put("Id_Usuario", idUsuario)
            put("Nom_Sit_Admini", nombre)
            put("M_Sadm_Fecha_Bod", fechaBod)
            put("M_Sadm_Nbod", numBod)
        }
        return dbWrite.insert("MOD_SITUA_ADMIN", null, values) != -1L
    }

    fun obtenerSituaciones(idUsuario: Int): android.database.Cursor {
        val db = this.readableDatabase
        return db.rawQuery(
            "SELECT * FROM MOD_SITUA_ADMIN WHERE Id_Usuario = ? ORDER BY Id_M_Sadm DESC",
            arrayOf(idUsuario.toString())
        )
    }

    fun modificarSituacion(idSituacion: Int, nombre: String, fechaBod: String, numBodStr: String): Boolean {
        val numBod = numBodStr.toIntOrNull() ?: 0
        val db = this.writableDatabase
        val values = android.content.ContentValues().apply {
            put("Nom_Sit_Admini", nombre)
            put("M_Sadm_Fecha_Bod", fechaBod)
            put("M_Sadm_Nbod", numBod)
        }
        return db.update("MOD_SITUA_ADMIN", values, "Id_M_Sadm = ?", arrayOf(idSituacion.toString())) > 0
    }

    fun eliminarSituacion(idSituacion: Int): Boolean {
        val db = this.writableDatabase
        return db.delete("MOD_SITUA_ADMIN", "Id_M_Sadm = ?", arrayOf(idSituacion.toString())) > 0
    }


    // ====================================================================
    // === MÉTODOS DEL MÓDULO DE TRIENIOS                               ===
    // ====================================================================

    fun anadirTrienio(idUsuario: Int, tipoTrienio: String, fechaBod: String, numBodStr: String): Boolean {
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
        // ASC es importante aquí para que el número de fila coincida con el número de trienio cronológico
        return db.rawQuery(
            "SELECT * FROM MOD_TRIENIOS WHERE Id_Usuario = ? ORDER BY Id_M_Trie ASC",
            arrayOf(idUsuario.toString())
        )
    }

    fun modificarTrienio(idTrienio: Int, tipoTrienio: String, fechaBod: String, numBodStr: String): Boolean {
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

    fun anadirAptitud(idUsuario: Int, nombre: String, fechaBod: String, numBodStr: String): Boolean {
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

    fun modificarAptitud(idAptitud: Int, nombre: String, fechaBod: String, numBodStr: String): Boolean {
        val numBod = numBodStr.toIntOrNull() ?: 0
        val db = this.writableDatabase
        val values = android.content.ContentValues().apply {
            put("Nom_Aptitud", nombre)
            put("M_Apti_Fecha_Bod", fechaBod)
            put("M_Apti_Nbod", numBod)
        }
        return db.update("MOD_APTITUDES", values, "Id_M_Apti = ?", arrayOf(idAptitud.toString())) > 0
    }

    fun eliminarAptitud(idAptitud: Int): Boolean {
        val db = this.writableDatabase
        return db.delete("MOD_APTITUDES", "Id_M_Apti = ?", arrayOf(idAptitud.toString())) > 0
    }

    // ====================================================================
    // === MÉTODOS DEL MÓDULO DE RECOMPENSAS                            ===
    // ====================================================================

    fun anadirRecompensa(idUsuario: Int, nombre: String, fechaBod: String, numBodStr: String): Boolean {
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
        return db.rawQuery("SELECT * FROM MOD_RECOMPENSAS WHERE Id_Usuario = ? ORDER BY Id_M_Reco DESC", arrayOf(idUsuario.toString()))
    }

    fun modificarRecompensa(idReco: Int, nombre: String, fechaBod: String, numBodStr: String): Boolean {
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

    fun anadirDistintivo(idUsuario: Int, nombre: String, fechaBod: String, numBodStr: String): Boolean {
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
        return db.rawQuery("SELECT * FROM MOD_DISTINTIVOS WHERE Id_Usuario = ? ORDER BY Id_M_Dist DESC", arrayOf(idUsuario.toString()))
    }

    fun modificarDistintivo(idDistintivo: Int, nombre: String, fechaBod: String, numBodStr: String): Boolean {
        val numBod = numBodStr.toIntOrNull() ?: 0
        val db = this.writableDatabase
        val values = android.content.ContentValues().apply {
            put("Nom_Distintivo", nombre)
            put("M_Dist_Fecha_Bod", fechaBod)
            put("M_Dist_Nbod", numBod)
        }
        return db.update("MOD_DISTINTIVOS", values, "Id_M_Dist = ?", arrayOf(idDistintivo.toString())) > 0
    }

    fun eliminarDistintivo(idDistintivo: Int): Boolean {
        val db = this.writableDatabase
        return db.delete("MOD_DISTINTIVOS", "Id_M_Dist = ?", arrayOf(idDistintivo.toString())) > 0
    }

    // ====================================================================
    // === MÉTODOS DEL MÓDULO DE CURSOS MILITARES                       ===
    // ====================================================================

    fun anadirCursoMilitar(idUsuario: Int, nombre: String, fechaBod: String, numBodStr: String): Boolean {
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
        return db.rawQuery("SELECT * FROM MOD_CUR_MILITAR WHERE Id_Usuario = ? ORDER BY Id_M_Cmili DESC", arrayOf(idUsuario.toString()))
    }

    fun modificarCursoMilitar(idCurso: Int, nombre: String, fechaBod: String, numBodStr: String): Boolean {
        val numBod = numBodStr.toIntOrNull() ?: 0
        val db = this.writableDatabase
        val values = android.content.ContentValues().apply {
            put("Nom_Cur_Mili", nombre)
            put("M_Cmili_Fecha_Bod", fechaBod)
            put("M_Cmili_Nbod", numBod)
        }
        return db.update("MOD_CUR_MILITAR", values, "Id_M_Cmili = ?", arrayOf(idCurso.toString())) > 0
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
        val values = android.content.ContentValues().apply {
            put("Nom_Titulo", nombre)
        }
        return db.update("MOD_TITULOS_CIVILES", values, "Id_M_Tcivi = ?", arrayOf(idTitulo.toString())) > 0
    }

    fun eliminarTituloCivil(idTitulo: Int): Boolean {
        val db = this.writableDatabase
        return db.delete("MOD_TITULOS_CIVILES", "Id_M_Tcivi = ?", arrayOf(idTitulo.toString())) > 0
    }

    // ====================================================================
    // === MÉTODOS DEL MÓDULO DE IDIOMAS SLP                            ===
    // ====================================================================

    fun anadirIdioma(idUsuario: Int, nombre: String, resultadoSlp: String, fechaBod: String, numBodStr: String): Boolean {
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
        return db.rawQuery("SELECT * FROM MOD_IDIOMA WHERE Id_Usuario = ? ORDER BY Id_M_Idi DESC", arrayOf(idUsuario.toString()))
    }

    fun modificarIdioma(idIdioma: Int, nombre: String, resultadoSlp: String, fechaBod: String, numBodStr: String): Boolean {
        val numBod = numBodStr.toIntOrNull() ?: 0
        val db = this.writableDatabase
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

    fun anadirArma(idUsuario: Int, nombre: String, numSerie: String, fechaCaducidad: String): Boolean {
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
        return db.rawQuery("SELECT * FROM MOD_EXP_ARMAS WHERE Id_Usuario = ? ORDER BY Id_M_EArm DESC", arrayOf(idUsuario.toString()))
    }

    fun modificarArma(idArma: Int, nombre: String, numSerie: String, fechaCaducidad: String): Boolean {
        val db = this.writableDatabase
        val values = android.content.ContentValues().apply {
            put("Nom_Arma", nombre)
            put("M_EArm_Nserie", numSerie)
            put("M_EArm_Fecha_Cad", fechaCaducidad)
        }
        return try {
            db.update("MOD_EXP_ARMAS", values, "Id_M_EArm = ?", arrayOf(idArma.toString())) > 0
        } catch (e: Exception) {
            false // Retorna falso si intenta poner un número de serie que ya existe
        }
    }

    fun eliminarArma(idArma: Int): Boolean {
        val db = this.writableDatabase
        return db.delete("MOD_EXP_ARMAS", "Id_M_EArm = ?", arrayOf(idArma.toString())) > 0
    }

    // ====================================================================
    // === MÉTODOS DEL MÓDULO DE CARNETS MILITARES                      ===
    // ====================================================================

    fun anadirCarnet(idUsuario: Int, tipo: String, fechaConcesion: String, fechaCaducidad: String): Boolean {
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
        return db.rawQuery("SELECT * FROM MOD_CARNET WHERE Id_Usuario = ? ORDER BY Id_M_Carnet DESC", arrayOf(idUsuario.toString()))
    }

    fun modificarCarnet(idCarnet: Int, tipo: String, fechaConcesion: String, fechaCaducidad: String): Boolean {
        val db = this.writableDatabase
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
        return db.rawQuery("SELECT * FROM MOD_TCGF WHERE Id_Usuario = ? ORDER BY Id_M_TCGF DESC", arrayOf(idUsuario.toString()))
    }

    fun modificarTcgf(idTcgf: Int, fecha: String, puntuacion: String, apto: String): Boolean {
        val db = this.writableDatabase
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

    // 1. Función para JSON. Lee una tabla y la convierte en una lista de objetos.
    private fun cursorToJsonArray(cursor: android.database.Cursor): JSONArray {
        // Creamos una lista JSON vacía
        val array = JSONArray()

        // Si la tabla tiene al menos un dato, empezamos a leer
        if (cursor.moveToFirst()) {
            // Guardamos los nombres de todas las columnas (ej: "Nombre", "Num_Bod")
            val columnNames = cursor.columnNames

            // Bucle: Repetimos esto por cada fila que haya en la tabla
            do {
                // Creamos un Objeto JSON nuevo para esta fila exacta
                val obj = JSONObject()

                // Repasamos una por una todas las columnas de esta fila
                for (col in columnNames) {
                    val colIndex = cursor.getColumnIndex(col)
                    // Si la celda está vacía ponemos "", si tiene algo lo sacamos como texto
                    val value = if (cursor.isNull(colIndex)) "" else cursor.getString(colIndex)
                    // Metemos el dato en el objeto (Ej: "Nombre" -> "Carlos")
                    obj.put(col, value)
                }
                // Añadimos la fila ya procesada a la lista
                array.put(obj)

            } while (cursor.moveToNext()) // Pasamos a la siguiente fila
        }
        cursor.close() // Siempre hay que cerrar el cursor
        return array
    }

    // 2. Genera el texto gigante en formato JSON llamando a todas las tablas.
    fun exportarTodoAJson(idUsuario: Int): String {
        // Creamos el objeto "Raíz" (El contenedor principal)
        val raiz = JSONObject()

        // Llamamos a nuestra función  tabla por tabla y la metemos en la raíz con una etiqueta
        raiz.put("Filiacion", cursorToJsonArray(obtenerFiliacion(idUsuario)))
        raiz.put("Destinos", cursorToJsonArray(obtenerDestinos(idUsuario)))
        // ... (así con todas las demás tablas) ...

        // Convertimos  esa cantidad  de datos en texto ordenado (poniendo  4 espacios)
        return raiz.toString(4)
    }


    // 3. Función para Excel (CSV). Lee una tabla y la convierte en texto con punto y coma (;).
    private fun agregarCursorACsv(titulo: String, cursor: android.database.Cursor, sb: StringBuilder) {

        // Ponemos el título de la tabla (El \n significa "Intro" o salto de línea)
        sb.append("--- $titulo ---\n")

        if (cursor.moveToFirst()) {
            // Sacamos los nombres de las columnas para hacer la cabecera del Excel
            val columnNames = cursor.columnNames
            // Unimos los nombres con ";" y damos un Intro
            sb.append(columnNames.joinToString(";")).append("\n")

            // Bucle: Repetimos por cada fila
            do {
                val rowValues = mutableListOf<String>()
                for (col in columnNames) {
                    val colIndex = cursor.getColumnIndex(col)
                    val value = if (cursor.isNull(colIndex)) "" else cursor.getString(colIndex)

                    // Si el usuario escribió un ";" en la app,
                    // rompería las columnas del Excel. Aquí reemplazamos los ";" por "," para evitarlo.
                    val cleanValue = value.replace("\n", " ").replace(";", ",")

                    rowValues.add(cleanValue)
                }
                // Unimos todos los datos de la fila con ";" y damos un Intro
                sb.append(rowValues.joinToString(";")).append("\n")

            } while (cursor.moveToNext())
        } else {
            sb.append("Sin registros\n")
        }
        // Damos un intro extra para separar visualmente las tablas
        sb.append("\n")
        cursor.close()
    }

    // 4. Generamos el texto completo en CSV para Excel
    fun exportarACsv(idUsuario: Int): String {
        // StringBuilder esta diseñado para unir textos gigantes muy rápido
        val sb = StringBuilder()

        // '\uFEFF' es una clave  que le dice a Excel que el archivo usa español (UTF-8)
        // para que las tildes y las eñes se vean perfectas.
        sb.append("\uFEFF")

        // Vamos llamando a la función obrera para que escriba las tablas en el cuaderno una a una
        agregarCursorACsv("FILIACIÓN", obtenerFiliacion(idUsuario), sb)
        agregarCursorACsv("DESTINOS", obtenerDestinos(idUsuario), sb)
        // ... (así con todas las demás tablas) ...

        // Devolvemos el conjunto del texto del cuaderno ya terminado
        return sb.toString()
    }

    // ====================================================================
    // === MÉTODOS DEL MÓDULO LOG DE ACTIVIDAD                          ===
    // ====================================================================

    // Este metodo se usa en el LoginActivity justo después de que el usuario meta bien su clave
    fun registrarAcceso(idUsuario: Int, dni: String, fechaHora: String): Boolean {
        val dbWrite = this.writableDatabase
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
        return db.rawQuery("SELECT * FROM REGISTRO_ACTIVIDAD WHERE Id_Usuario = ? ORDER BY Id_Log DESC", arrayOf(idUsuario.toString()))
    }
}