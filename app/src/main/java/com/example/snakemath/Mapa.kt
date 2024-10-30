package com.example.snakemath

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Mapa : AppCompatActivity() {

    private lateinit var canvasMap: CanvasMap
    private lateinit var joystick: Joystick
    private val velocidad = 0.05f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        canvasMap = findViewById(R.id.map_view)
        joystick = findViewById(R.id.joystick_view)

        ViewCompat.setOnApplyWindowInsetsListener(canvasMap) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        joystick.setOnJoystickMoveListener { dx, dy ->
            moverPersonaje(dx * velocidad, dy * velocidad)
        }
    }

    private fun moverPersonaje(x: Float, y: Float) {
        canvasMap.actualizarPosicion(x, y)
    }
}
