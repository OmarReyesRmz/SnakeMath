package com.example.snakemath

import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Resultado : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_resultado)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.resultado)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Obtén el puntaje usando el nombre del extra correcto
        val puntaje = intent.getIntExtra("PUNTAJE_OBTENIDO", 0)

        // Configura el texto del puntaje
        val scoreTextView = findViewById<TextView>(R.id.score)
        scoreTextView.text = "$puntaje/10"  // Ajusta el total según tu caso

        // Configura el botón de "COMPLETADO"
        val buttonCompleted = findViewById<Button>(R.id.button_completed)
        buttonCompleted.setOnClickListener {
            // Termina esta actividad y regresa a la actividad principal
            finish()
        }
    }
}
