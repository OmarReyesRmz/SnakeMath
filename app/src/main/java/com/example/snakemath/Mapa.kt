package com.example.snakemath

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
class Mapa : AppCompatActivity() {

    private lateinit var canvasMap: CanvasMap
    private lateinit var joystick: Joystick
    private lateinit var navigateButton: Button
    private val velocidad = 0.05f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_map)

        var db: DBsqlite = DBsqlite(this)
        val dinero: TextView = findViewById(R.id.monedas)

        dinero.text = "${db.obtenerDineroTotal().toInt()}"

        canvasMap = findViewById(R.id.map_view)
        joystick = findViewById(R.id.joystick_view)

        navigateButton = findViewById(R.id.start_game_button)
        navigateButton.isVisible = false // Inicialmente oculto

        // Asigna el botón de navegación a CanvasMap
        canvasMap.setNavigateButton(navigateButton)

        navigateButton.setOnClickListener {
            val intent = Intent(this, Level::class.java)
            startActivity(intent)
            finish()
        }

        // Resto del código para joystick y otros elementos
        joystick.setOnJoystickMoveListener { dx, dy ->
            moverPersonaje(dx * velocidad, dy * velocidad)
        }
    }

    private fun moverPersonaje(x: Float, y: Float) {
        canvasMap.actualizarPosicion(x, y)
    }
}
