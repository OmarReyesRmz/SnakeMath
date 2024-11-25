package com.example.snakemath

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Level : AppCompatActivity() {

    private lateinit var operacionTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level)

        // Inicializamos las vistas
        val scoreTextView = findViewById<TextView>(R.id.scoreText)
        val gameView = findViewById<GameView>(R.id.gameView)
        operacionTextView = findViewById(R.id.Operacion)

        // Configuramos el puntaje
        gameView.setScoreTextView(scoreTextView)

        // Configurar el listener para operaciones
        gameView.setOnOperacionGeneradaListener { operacion ->
            actualizarOperacion(operacion)
        }
    }

    fun actualizarOperacion(operacion: String) {
        operacionTextView.text = operacion
    }

}
