package com.proyecto.miexmi

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase

object GeneradorDatos {

    // ====================================================================
    // === CARGA MASIVA DE DATOS REALES (DESDE PDFs OFICIALES)          ===
    // ====================================================================

    fun cargarExpedientePDF(db: SQLiteDatabase, idUsuario: Int) {

        // 1. FILIACIÓN
        db.insert("FILIACION", null, ContentValues().apply {
            put("Id_Usuario", idUsuario)
            put("Nombre", "EDUARDO JOSE")
            put("Apellidos", "ALVAREZ MONTES")
            put("Fech_Incorp", "14/11/1994")
            put("Nun_Escalafon", 3092310)
            put("TMI", "") // Opcional
        })

        // 2. EMPLEOS (Extraídos de Hoja de Servicios)
        val empleos = listOf(
            arrayOf<Any>("Soldado", "14/11/1994", 32),
            arrayOf<Any>("Cabo", "10/09/1997", 234),
            arrayOf<Any>("Cabo 1º", "04/09/2000", 175),
            arrayOf<Any>("Sargento", "10/07/2003", 133),
            arrayOf<Any>("Sargento 1º", "10/07/2011", 138),
            arrayOf<Any>("Brigada", "26/09/2019", 195)
        )
        for (e in empleos) {
            db.insert("MOD_EMPLEOS", null, ContentValues().apply {
                put("Id_Usuario", idUsuario)
                put("Nom_Empleo", e[0].toString())
                put("M_Empl_Fecha_Bod", e[1].toString())
                put("M_Empl_Nbod", e[2] as Int)
            })
        }

        // 3. DESTINOS
        val destinos = listOf(
            arrayOf<Any>("BING BRILAT", "14/11/1994", 0),
            arrayOf<Any>("BON CG BRILAT VII", "14/10/1996", 202),
            arrayOf<Any>("RETES 22 (SIERO)", "05/09/2000", 176),
            arrayOf<Any>("ACADEMIA GENERAL BASICA DE SUBOFICIALES", "10/09/2001", 198),
            arrayOf<Any>("ACADEMIA DE LOGISTICA", "07/01/2002", 7),
            arrayOf<Any>("REGIMIENTO DE TRANSMISIONES 1", "29/07/2003", 145),
            arrayOf<Any>("REGIMIENTO DE INFANTERIA LIGERA PRINCIPE 3", "28/09/2010", 189),
            arrayOf<Any>("REGIMIENTO DE TRANSMISIONES 22 (SIERO)", "15/01/2015", 8),
            arrayOf<Any>("JEFATURA SISTEMAS INFORMACION (POZUELO)", "29/11/2019", 233),
            arrayOf<Any>("REGIMIENTO DE TRANSMISIONES 22 (POZUELO)", "01/07/2024", 128)
        )
        for (d in destinos) {
            db.insert("MOD_DESTINOS", null, ContentValues().apply {
                put("Id_Usuario", idUsuario)
                put("Nom_Destino", d[0].toString())
                put("M_Dest_Fecha_Bod", d[1].toString())
                put("M_Dest_Nbod", d[2] as Int)
            })
        }

        // 4. COMISIONES DE SERVICIO Y MISIONES
        val misiones = listOf(
            arrayOf<Any>("UNPROFOR (Bosnia)", "18/04/1995", 99),
            arrayOf<Any>("SPABRI", "03/04/1997", 99),
            arrayOf<Any>("SPABRI", "02/08/1999", 999),
            arrayOf<Any>("OPERACION INDIA FOXTROT (IRAQ)", "08/12/2003", 99),
            arrayOf<Any>("KFOR-KOSOVO", "27/04/2009", 999)
        )
        for (m in misiones) {
            db.insert("MOD_COMISION_SER", null, ContentValues().apply {
                put("Id_Usuario", idUsuario)
                put("Nom_Comision", m[0].toString())
                put("M_Cser_Fecha_Bod", m[1].toString())
                put("M_Cser_Nbod", m[2] as Int)
            })
        }

        // 5. RECOMPENSAS
        val recompensas = listOf(
            arrayOf<Any>("MEDALLA DE LAS NACIONES UNIDAS (UNPROFOR)", "27/03/1996", 999),
            arrayOf<Any>("MENCION HONORIFICA", "20/08/1996", 170),
            arrayOf<Any>("MEDALLA OTAN (ANTIGUA YUGOSLAVIA)", "29/09/1997", 201),
            arrayOf<Any>("MENCION HONORIFICA", "03/07/2003", 133),
            arrayOf<Any>("CRUZ MERITO MILITAR CON DISTINTIVO BLANCO", "03/02/2006", 30),
            arrayOf<Any>("MEDALLA CONMEMORATIVA POLONIA, OPERACION IRAK", "10/01/2007", 999),
            arrayOf<Any>("MEDALLA OTAN: NO-ARTICULO 5 BALCANES", "29/09/2009", 190),
            arrayOf<Any>("CRUZ MERITO MILITAR CON DISTINTIVO BLANCO", "02/01/2014", 2),
            arrayOf<Any>("CRUZ DE LA REAL Y MILITAR ORDEN DE SAN HERMENEGILDO", "14/11/2014", 28),
            arrayOf<Any>("CRUZ MERITO MILITAR CON DISTINTIVO BLANCO", "19/06/2019", 119),
            arrayOf<Any>("ENCOMIENDA DE LA REAL Y MILITAR ORDEN DE SAN HERMENEGILDO", "14/11/2019", 3),
            arrayOf<Any>("MEDALLA DE CAMPAÑA", "16/04/2024", 82)
        )
        for (r in recompensas) {
            db.insert("MOD_RECOMPENSAS", null, ContentValues().apply {
                put("Id_Usuario", idUsuario)
                put("Nom_Recompensa", r[0].toString())
                put("M_Reco_Fecha_Bod", r[1].toString())
                put("M_Reco_Nbod", r[2] as Int)
            })
        }

        // 6. CURSOS MILITARES
        val cursosMilitares = listOf(
            arrayOf<Any>("ASCENSO A CABO PROFESIONAL", 234),
            arrayOf<Any>("ASCENSO A CABO PRIMERO", 175),
            arrayOf<Any>("ENSEÑANZA GRADO BASICO INFORMATICA PRIMER CURSO", 198),
            arrayOf<Any>("ENSEÑANZA GRADO BASICO INFORMATICA SEGUNDO CURSO", 188),
            arrayOf<Any>("UVICOA WORD", 22),
            arrayOf<Any>("INGLES A DISTANCIA SUPERVIVENCIA PLUS", 12),
            arrayOf<Any>("BASICO STIC ENTORNOS LINUX", 0),
            arrayOf<Any>("BASICO STIC ENTORNOS WINDOWS", 0),
            arrayOf<Any>("CCNA SECURITY", 0),
            arrayOf<Any>("CURSO CISCO CERTIFIED NETWORKING ASOCIATE VOICE", 0),
            arrayOf<Any>("ON LINE PARA CISPOC/OACIS DEL MINISTERIO DE DEFENSA", 97),
            arrayOf<Any>("INSTRUCTOR CCNA SECURITY", 0),
            arrayOf<Any>("ACTUALIZACION ASCENSO A BG ESB/CGET", 12),
            arrayOf<Any>("INSTRUCTOR CCNA ROUTING & SWITCHING", 0),
            arrayOf<Any>("TECNICO INTERMEDIO EN PREVENCION DE RIESGOS LABORALES", 0),
            arrayOf<Any>("CISCO CERTIFIED NETWORK ASSOCIATE", 0)
        )
        for (c in cursosMilitares) {
            db.insert("MOD_CUR_MILITAR", null, ContentValues().apply {
                put("Id_Usuario", idUsuario)
                put("Nom_Cur_Mili", c[0].toString())
                put("M_Cmili_Fecha_Bod", "")
                put("M_Cmili_Nbod", c[1] as Int)
            })
        }

        // 7. TÍTULOS CIVILES
        val titulosCiviles = listOf(
            "BACHILLER SUPERIOR (COU O 6° Y REVALIDA)",
            "CURSO ORIENTACION UNIVERSITARIA SIN SELECTIVIDAD",
            "TECNICO SUPERIOR EN PREVENCION DE RIESGOS PROFESIONALES"
        )
        for (t in titulosCiviles) {
            db.insert("MOD_TITULOS_CIVILES", null, ContentValues().apply {
                put("Id_Usuario", idUsuario)
                put("Nom_Titulo", t)
            })
        }

        // 8. TRIENIOS
        val trienios = listOf(
            arrayOf<Any>("C2", "14/11/1997", 24),
            arrayOf<Any>("C2", "14/11/2000", 24),
            arrayOf<Any>("A2", "14/11/2003", 24),
            arrayOf<Any>("A2", "14/11/2006", 228),
            arrayOf<Any>("A2", "14/11/2009", 239),
            arrayOf<Any>("A2", "14/11/2012", 226),
            arrayOf<Any>("A2", "14/11/2015", 218),
            arrayOf<Any>("A2", "14/11/2018", 224),
            arrayOf<Any>("A2", "14/11/2021", 241)
        )
        for (t in trienios) {
            db.insert("MOD_TRIENIOS", null, ContentValues().apply {
                put("Id_Usuario", idUsuario)
                put("Tipo_Trienio", t[0].toString())
                put("M_Trie_Fecha_Bod", t[1].toString())
                put("M_Trie_Nbod", t[2] as Int)
            })
        }

        // 9. IDIOMAS
        val idiomas = listOf(
            arrayOf<Any>("INGLÉS", "1 2 1 1", "15/05/2003", 0),
            arrayOf<Any>("INGLÉS", "1+ 1 1+ 1", "20/10/2011", 212),
            arrayOf<Any>("INGLÉS", "2 2 2 1", "25/06/2013", 134),
            arrayOf<Any>("INGLÉS", "1+ 1+ 2 2", "13/11/2018", 227)
        )
        for (i in idiomas) {
            db.insert("MOD_IDIOMA", null, ContentValues().apply {
                put("Id_Usuario", idUsuario)
                put("Nom_idioma", i[0].toString())
                put("Resultado", i[1].toString())
                put("M_Idi_Fecha_Bod", i[2].toString())
                put("M_Idi_Nbod", i[3] as Int)
            })
        }

        // 10. CONDICIÓN FÍSICA (TGCF)
        val tcgf = listOf(
            arrayOf<String>("17/05/2018", "234", "Apto"),
            arrayOf<String>("24/10/2019", "227", "Apto"),
            arrayOf<String>("03/03/2020", "227", "Apto"),
            arrayOf<String>("08/11/2021", "217", "Apto"),
            arrayOf<String>("18/10/2022", "192", "Apto"),
            arrayOf<String>("14/09/2023", "250", "Apto"),
            arrayOf<String>("11/09/2024", "266", "Apto")
        )
        for (f in tcgf) {
            db.insert("MOD_TCGF", null, ContentValues().apply {
                put("Id_Usuario", idUsuario)
                put("M_Tcgf_Fecha", f[0])
                put("M_Tcgf_Puntuacion", f[1])
                put("M_Tcgf_Apto", f[2])
            })
        }

        // 11. APTITUDES
        db.insert("MOD_APTITUDES", null, ContentValues().apply {
            put("Id_Usuario", idUsuario)
            put("Nom_Aptitud", "TECNICO INTERMEDIO EN PRL")
            put("M_Apti_Fecha_Bod", "29/06/2020")
            put("M_Apti_Nbod", 135)
        })

        // ====================================================================
        // 12. SITUACIONES ADMINISTRATIVAS (¡Añadido!)
        // ====================================================================
        val situaciones = listOf(
            arrayOf<Any>("SERVICIO MILITAR OBLIGATORIO", "14/11/1994", 999),
            arrayOf<Any>("SERV.ACTIVO-DESTINADO EN UCO,S. DEL MINISDEF", "14/08/1995", 999),
            arrayOf<Any>("SERV.ACTIVO-MILITAR ALUMNO", "10/09/2001", 198),
            arrayOf<Any>("SERV.ACTIVO-PEND.ASIG.DEST.DESTINABLE FORZOSO", "10/07/2003", 133),
            arrayOf<Any>("SERV.ACTIVO-DESTINADO EN UCO,S. DEL MINISDEF", "28/07/2003", 145),
            arrayOf<Any>("SERV.ACTIVO-PEND.ASIG.DEST.DESTINABLE FORZOSO", "05/10/2019", 195),
            arrayOf<Any>("SERV.ACTIVO-DESTINADO EN UCO,S. DEL MINISDEF", "29/11/2019", 233)
        )
        for (s in situaciones) {
            db.insert("MOD_SITUA_ADMIN", null, ContentValues().apply {
                put("Id_Usuario", idUsuario)
                put("Nom_Sit_Admini", s[0].toString())
                put("M_Sadm_Fecha_Bod", s[1].toString())
                put("M_Sadm_Nbod", s[2] as Int)
            })
        }

        // ====================================================================
        // 13. RELACIÓN CON LA ADMINISTRACIÓN (¡Nuevo!)
        // ====================================================================
        val relacionesAdmin = listOf(
            arrayOf<Any>("MTM CON COMPROMISO INICIAL", "14/11/1994", 32),
            arrayOf<Any>("MILITAR ALUMNO (ACCESO A MILITAR DE CARRERA)", "10/09/2001", 143),
            arrayOf<Any>("MILITAR DE CARRERA", "10/07/2003", 133)
        )
        for (ra in relacionesAdmin) {
            db.insert("MOD_RELA_ADMINISTRACION", null, ContentValues().apply {
                put("Id_Usuario", idUsuario)
                put("Nom_Rel_Admin", ra[0].toString())
                put("M_Radm_Fecha_Bod", ra[1].toString())
                put("M_Radm_Nbod", ra[2] as Int)
            })
        }

        // ====================================================================
        // 14. DISTINTIVOS
        // ====================================================================
        val distintivos = listOf(
            arrayOf<Any>("MERITO OPERACIONES DE MANTENIMIENTO DE LA PAZ", "31/05/1996", 116),
            arrayOf<Any>("MERITO OPERACIONES DE MANTO. DE LA PAZ(ADICION)", "20/03/1998", 65),
            arrayOf<Any>("MERITO OPERACIONES DE MANTO. DE LA PAZ(ADICION)", "14/10/2004", 207),
            arrayOf<Any>("MERITO OPERACIONES DE MANTO. DE LA PAZ(ADICION)", "25/03/2010", 66),
            arrayOf<Any>("MERITO OPERACIONES DE MANTO. DE LA PAZ(ADICION)", "25/03/2010", 66)
        )
        for (dis in distintivos) {
            db.insert("MOD_DISTINTIVOS", null, ContentValues().apply {
                put("Id_Usuario", idUsuario)
                put("Nom_Distintivo", dis[0].toString())
                put("M_Dist_Fecha_Bod", dis[1].toString())
                put("M_Dist_Nbod", dis[2] as Int)
            })
        }

        // ====================================================================
        // 15. ESPECIALIDADES FUNDAMENTALES
        // ====================================================================
        val especialidades = listOf(
            arrayOf<Any>("MPTM-ET-INGENIEROS TRANSMISIONES", "14/11/1994", 32),
            arrayOf<Any>("TRANSMISIONES(MPTM-ET-TRS)", "06/09/2000", 175),
            arrayOf<Any>("INFORMATICA/SIN SUBESPECIALIDAD", "10/07/2003", 133)
        )
        for (esp in especialidades) {
            db.insert("MOD_CEE_FUNDAMENTAL", null, ContentValues().apply {
                put("Id_Usuario", idUsuario)
                put("Nom_CEEF", esp[0].toString())
                put("M_Ceef_Fecha_Bod", esp[1].toString())
                put("M_Ceef_Nbod", esp[2] as Int)
            })


        }
    }
}