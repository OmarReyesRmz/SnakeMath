package com.example.snakemath

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Level : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level)

        val scoreTextView = findViewById<TextView>(R.id.scoreText)
        val gameView = findViewById<GameView>(R.id.gameView)

        gameView.setScoreTextView(scoreTextView)
    }
}
