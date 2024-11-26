package com.example.snakemath

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Level : AppCompatActivity() {

    private lateinit var operacionTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level)


        var db: DBsqlite = DBsqlite(this)
        db.actualizarPrimeraVez(1)
        // Inicializamos las vistas
        val scoreTextView = findViewById<TextView>(R.id.scoreText)
        val gameView = findViewById<GameView>(R.id.gameView)
        operacionTextView = findViewById(R.id.Operacion)

        // Configuramos el puntaje
        gameView.setScoreTextView(scoreTextView)

        // Configurar el listener para operaciones
        gameView.setOnOperacionGeneradaListener { operacion, operacionesResueltas ->
            if(actualizarOperacion(operacion, operacionesResueltas)){
                db.actualizarNivel(db.obtenerNivel() + 1)
                val intent = Intent(this, Mapa::class.java)
                startActivity(intent)
            }
        }
    }

    fun actualizarOperacion(operacion: String, Operaciones_resueltas: Int): Boolean{
        operacionTextView.text = operacion
        if(Operaciones_resueltas == 3){
            return true
        }
        return false
    }

}
