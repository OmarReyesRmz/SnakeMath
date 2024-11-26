package com.example.snakemath

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Level : AppCompatActivity() {

    private lateinit var operacionTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level)


        var db: DBsqlite = DBsqlite(this)
        db.actualizarPrimeraVez(1)
        if(db.obtenerMundo() == 2){
            db.actualizarPrimeraVez(3)
        }
        if(db.obtenerMundo() == 3){
            db.actualizarPrimeraVez(5)
        }
        if(db.obtenerMundo() == 4){
            db.actualizarPrimeraVez(7)
        }
        // Inicializamos las vistas
        val scoreTextView = findViewById<TextView>(R.id.scoreText)
        val gameView = findViewById<GameView>(R.id.gameView)
        operacionTextView = findViewById(R.id.Operacion)

        // Configuramos el puntaje
        gameView.setScoreTextView(scoreTextView)

        // Configurar el listener para operaciones
        gameView.setOnOperacionGeneradaListener { operacion, operacionesResueltas ->
            if(actualizarOperacion(operacion, operacionesResueltas)){
                if(db.obtenerNivel() == db.obtenerNivelJugando()) {
                    db.actualizarNivel(db.obtenerNivel() + 1)
                }

                if(db.obtenerNivel() == 6 && db.obtenerMundo() == 1){
                    db.actualizarMundo(2)
                    db.actualizarPrimeraVez(2)
                }
                if(db.obtenerNivel() == 9 && db.obtenerMundo() == 2){
                    db.actualizarMundo(3)
                    db.actualizarPrimeraVez(4)
                }
                if(db.obtenerNivel() == 15 && db.obtenerMundo() == 3){
                    db.actualizarMundo(4)
                    db.actualizarPrimeraVez(6)
                }

                val intent = Intent(this, Mapa::class.java)
                startActivity(intent)
            }
        }
    }

    fun actualizarOperacion(operacion: String, Operaciones_resueltas: Int): Boolean{
        operacionTextView.text = operacion
        if(Operaciones_resueltas == 1){
            return true
        }
        return false
    }

}
