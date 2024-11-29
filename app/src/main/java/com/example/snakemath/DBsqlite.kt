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
                    "mundo_jugando INTEGER NOT NULL," +
                    "esPrimeraVez INTEGER NOT NULL," +   // 1 para true (sí es la primera vez), 0 para false
                    "dineroTotal REAL NOT NULL DEFAULT 0," +  // Dinero acumulado
                    "tipoSerpiente TEXT NOT NULL," +      // Tipo de serpiente elegida por el jugador
                    "iman INTEGER NOT NULL," +
                    "monedax5 INTEGER NOT NULL," +
                    "estrella INTEGER NOT NULL," +
                    "serpiente1 INTEGER NOT NULL," +
                    "serpiente2 INTEGER NOT NULL," +
                    "serpiente3 INTEGER NOT NULL," +
                    "serpiente4 INTEGER NOT NULL," +
                    "serpiente5 INTEGER NOT NULL," +
                    "serpiente6 INTEGER NOT NULL" +
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


    fun guardarDatos(nivel: Int,nivel_jugando:Int, mundo: Int,mundo_jugando: Int, primeravez: Int, dineroTotal: Float, tipoSerpiente: String, iman: Int, monedax5: Int, estrella: Int,
                     serpiente1:Int, serpiente2:Int, serpiente3:Int, serpiente4:Int, serpiente5:Int, serpiente6:Int    ){
        val db = writableDatabase
        db.execSQL(
            "INSERT INTO $TABLE_NAME (nivel,nivel_jugando, mundo,mundo_jugando, esPrimeraVez, dineroTotal, tipoSerpiente, iman, monedax5, estrella,serpiente1, serpiente2, serpiente3, serpiente4, serpiente5, serpiente6 ) " +
                    "VALUES($nivel,$nivel_jugando, $mundo,$mundo_jugando, $primeravez, $dineroTotal, '$tipoSerpiente', $iman, $monedax5, $estrella, $serpiente1, $serpiente2, $serpiente3, $serpiente4, $serpiente5, $serpiente6)"
        )

    }

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

    fun actualizarMundoJugando(mundo_jugando: Int) {
        val db = writableDatabase
        db.execSQL("UPDATE $TABLE_NAME SET mundo_jugando = $mundo_jugando")
        db.close()
    }

    fun actualizarDineroTotal(dineroTotal: Int) {
        val db = writableDatabase
        db.execSQL("UPDATE $TABLE_NAME SET dineroTotal = $dineroTotal")
        db.close()
    }

    fun actualizarTipoSerpiente(tipoSerpiente: String) {
        val db = writableDatabase
        db.execSQL("UPDATE $TABLE_NAME SET tipoSerpiente = '$tipoSerpiente'")
        db.close()
    }

    fun actualizariman(iman: Int) {
        val db = writableDatabase
        db.execSQL("UPDATE $TABLE_NAME SET iman = '$iman'")
        db.close()
    }

    fun actualizarmonedax5(monedax5: Int) {
        val db = writableDatabase
        db.execSQL("UPDATE $TABLE_NAME SET monedax5 = '$monedax5'")
        db.close()
    }

    fun actualizarestrella(estrella: Int) {
        val db = writableDatabase
        db.execSQL("UPDATE $TABLE_NAME SET estrella = '$estrella'")
        db.close()
    }

    fun actualizarserpiente1(serpiente1: Int){
        val db = writableDatabase
        db.execSQL("UPDATE $TABLE_NAME SET serpiente1 = '$serpiente1'")
        db.close()
    }

    fun actualizarserpiente2(serpiente2: Int){
        val db = writableDatabase
        db.execSQL("UPDATE $TABLE_NAME SET serpiente2 = '$serpiente2'")
        db.close()
    }

    fun actualizarserpiente3(serpiente3: Int){
        val db = writableDatabase
        db.execSQL("UPDATE $TABLE_NAME SET serpiente3 = '$serpiente3'")
        db.close()
    }

    fun actualizarserpiente4(serpiente4: Int){
        val db = writableDatabase
        db.execSQL("UPDATE $TABLE_NAME SET serpiente4 = '$serpiente4'")
        db.close()
    }

    fun actualizarserpiente5(serpiente5: Int){
        val db = writableDatabase
        db.execSQL("UPDATE $TABLE_NAME SET serpiente5 = '$serpiente5'")
        db.close()
    }

    fun actualizarserpiente6(serpiente6: Int){
        val db = writableDatabase
        db.execSQL("UPDATE $TABLE_NAME SET serpiente6 = '$serpiente6'")
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

    fun obtenerMundoJugando(): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT mundo_jugando FROM $TABLE_NAME", null)
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

    fun obteneriman(): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT iman FROM $TABLE_NAME", null)
        var iman = -1
        if (cursor.moveToFirst()) {
            iman = cursor.getInt(0)
        }
        cursor.close()
        return iman
    }

    fun obtenermonedax5(): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT monedax5 FROM $TABLE_NAME", null)
        var monedax5 = -1
        if (cursor.moveToFirst()) {
            monedax5 = cursor.getInt(0)
        }
        cursor.close()
        return monedax5
    }

    fun obtenerestrella(): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT estrella FROM $TABLE_NAME", null)
        var estrella = -1
        if (cursor.moveToFirst()) {
            estrella = cursor.getInt(0)
        }
        cursor.close()
        return estrella
    }

    fun obtenerserpiente1(): Int{
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT serpiente1 FROM $TABLE_NAME", null)
        var serpiente1 = 0
        if (cursor.moveToFirst()){
            serpiente1 = cursor.getInt(0)
        }
        cursor.close()
        return serpiente1
    }

    fun obtenerserpiente2(): Int{
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT serpiente2 FROM $TABLE_NAME", null)
        var serpiente2 = 0
        if (cursor.moveToFirst()){
            serpiente2 = cursor.getInt(0)
        }
        cursor.close()
        return serpiente2
    }

    fun obtenerserpiente3(): Int{
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT serpiente3 FROM $TABLE_NAME", null)
        var serpiente3 = 0
        if (cursor.moveToFirst()){
            serpiente3 = cursor.getInt(0)
        }
        cursor.close()
        return serpiente3
    }

    fun obtenerserpiente4(): Int{
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT serpiente4 FROM $TABLE_NAME", null)
        var serpiente4 = 0
        if (cursor.moveToFirst()){
            serpiente4 = cursor.getInt(0)
        }
        cursor.close()
        return serpiente4
    }

    fun obtenerserpiente5(): Int{
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT serpiente5 FROM $TABLE_NAME", null)
        var serpiente5 = 0
        if (cursor.moveToFirst()){
            serpiente5 = cursor.getInt(0)
        }
        cursor.close()
        return serpiente5
    }

    fun obtenerserpiente6(): Int{
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT serpiente6 FROM $TABLE_NAME", null)
        var serpiente6 = 0
        if (cursor.moveToFirst()){
            serpiente6 = cursor.getInt(0)
        }
        cursor.close()
        return serpiente6
    }

}