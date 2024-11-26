package com.example.snakemath


import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.Vector

class DBsqlite(context: Context?): SQLiteOpenHelper(context, TABLE_NAME, null, DATABASE_VERSION){
    companion object{
        val DATABASE_VERSION: Int = 1
        val TABLE_NAME: String = "datos"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE $TABLE_NAME (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nivel INTEGER NOT NULL," +          // Para identificar en qué nivel va
                    "nivel_jugando INTEGER NOT NULL," +
                    "mundo INTEGER NOT NULL," +          // Para identificar en qué mundo va
                    "esPrimeraVez INTEGER NOT NULL," +   // 1 para true (sí es la primera vez), 0 para false
                    "dineroTotal REAL NOT NULL DEFAULT 0," +  // Dinero acumulado
                    "tipoSerpiente TEXT NOT NULL" +      // Tipo de serpiente elegida por el jugador
                    ")"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //En caso de una nueva version haria que actualizar las tablas
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //En caso de regresar a una version anterior que habria que actualizar las tablas
    }

    fun datosExistentes(): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_NAME", null)

        var existe = false
        if (cursor.moveToFirst()) {
            existe = cursor.getInt(0) > 0 // Si COUNT(*) es mayor a 0, significa que hay datos
        }

        cursor.close() // Cierra el cursor para liberar recursos
        return existe // Devuelve true si hay datos, false si no
    }


    fun guardarDatos(nivel: Int,nivel_jugando:Int, mundo: Int, primeravez: Int, dineroTotal: Float, tipoSerpiente: String){
        val db = writableDatabase
        db.execSQL(
            "INSERT INTO $TABLE_NAME (nivel,nivel_jugando, mundo, esPrimeraVez, dineroTotal, tipoSerpiente) " +
                    "VALUES($nivel,$nivel_jugando, $mundo, $primeravez, $dineroTotal, '$tipoSerpiente')"
        )

    }

    /*fun borrarDatos(){
        val db = writableDatabase
        db.execSQL("DELETE FROM $TABLE_NAME")
        db.close()
    }*/

    /*fun listaDatos(): Vector<String> {
        val result = Vector<String>()
        val db = readableDatabase
        var cursor = db.rawQuery(
            "SELECT id,nombre " +
                    "FROM " + TABLE_NAME + " ORDER BY id", null
        )
        while (cursor.moveToNext()){
            result.add(
                (cursor.getInt(0).toString() + " "
                        + cursor.getString(1))
            )
        }
        cursor.close()
        return result
    }*/

    // Métodos para actualizar (SET)
    fun actualizarPrimeraVez(primeravez: Int) {
        val db = writableDatabase
        db.execSQL("UPDATE $TABLE_NAME SET esPrimeraVez = $primeravez")
        db.close()
    }

    fun actualizarNivel(nivel: Int) {
        val db = writableDatabase
        db.execSQL("UPDATE $TABLE_NAME SET nivel = $nivel")
        db.close()
    }

    fun actualizarNivelJugando(nivel_jugando: Int) {
        val db = writableDatabase
        db.execSQL("UPDATE $TABLE_NAME SET nivel_jugando = $nivel_jugando")
        db.close()
    }

    fun actualizarMundo(mundo: Int) {
        val db = writableDatabase
        db.execSQL("UPDATE $TABLE_NAME SET mundo = $mundo")
        db.close()
    }

    fun actualizarDineroTotal(dineroTotal: Float) {
        val db = writableDatabase
        db.execSQL("UPDATE $TABLE_NAME SET dineroTotal = $dineroTotal")
        db.close()
    }

    fun actualizarTipoSerpiente(tipoSerpiente: String) {
        val db = writableDatabase
        db.execSQL("UPDATE $TABLE_NAME SET tipoSerpiente = '$tipoSerpiente'")
        db.close()
    }

    // Métodos para obtener valores (GET)
    fun obtenerNivel(): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT nivel FROM $TABLE_NAME", null)
        var nivel = -1
        if (cursor.moveToFirst()) {
            nivel = cursor.getInt(0)
        }
        cursor.close()
        return nivel
    }

    fun obtenerNivelJugando(): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT nivel_jugando FROM $TABLE_NAME", null)
        var nivel = -1
        if (cursor.moveToFirst()) {
            nivel = cursor.getInt(0)
        }
        cursor.close()
        return nivel
    }

    fun obtenerMundo(): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT mundo FROM $TABLE_NAME", null)
        var mundo = -1
        if (cursor.moveToFirst()) {
            mundo = cursor.getInt(0)
        }
        cursor.close()
        return mundo
    }

    fun obtenerPrimeraVez(): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT esPrimeraVez FROM $TABLE_NAME", null)
        var primeravez = -1
        if (cursor.moveToFirst()) {
            primeravez = cursor.getInt(0)
        }
        cursor.close()
        return primeravez
    }

    fun obtenerDineroTotal(): Float {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT dineroTotal FROM $TABLE_NAME", null)
        var dineroTotal = 0.0f
        if (cursor.moveToFirst()) {
            dineroTotal = cursor.getFloat(0)
        }
        cursor.close()
        return dineroTotal
    }

    fun obtenerTipoSerpiente(): String {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT tipoSerpiente FROM $TABLE_NAME", null)
        var tipoSerpiente = ""
        if (cursor.moveToFirst()) {
            tipoSerpiente = cursor.getString(0)
        }
        cursor.close()
        return tipoSerpiente
    }
}