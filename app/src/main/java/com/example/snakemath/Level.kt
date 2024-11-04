package com.example.snakemath

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Level : AppCompatActivity() {

    private lateinit var bolita: View
    private lateinit var areaJuego: View

    private var currentDirection: Direction = Direction.NONE
    private val step = 50f
    private val handler = Handler(Looper.getMainLooper())

    private enum class Direction { NONE, UP, DOWN, LEFT, RIGHT }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.level)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        bolita = findViewById(R.id.bolita)
        areaJuego = findViewById(R.id.areaJuego)

        startMovement()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            val deltaX = event.x - bolita.x
            val deltaY = event.y - bolita.y

            currentDirection = if (Math.abs(deltaX) > Math.abs(deltaY)) {
                if (deltaX > 0) Direction.RIGHT else Direction.LEFT
            } else {
                if (deltaY > 0) Direction.DOWN else Direction.UP
            }
        }
        return super.onTouchEvent(event)
    }

    private fun startMovement() {
        handler.post(object : Runnable {
            override fun run() {
                moveBolita()
                handler.postDelayed(this, 100)
            }
        })
    }

    private fun moveBolita() {
        when (currentDirection) {
            Direction.RIGHT -> if (bolita.x + step + bolita.width <= areaJuego.width) bolita.x += step
            Direction.LEFT -> if (bolita.x - step >= 0) bolita.x -= step
            Direction.UP -> if (bolita.y - step >= 0) bolita.y -= step
            Direction.DOWN -> if (bolita.y + step + bolita.height <= areaJuego.height) bolita.y += step
            else -> {} // No movement if direction is NONE
        }
    }
}
