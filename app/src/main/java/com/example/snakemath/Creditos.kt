package com.example.snakemath

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Creditos : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private lateinit var nombresTextView: TextView
    private var screenWidth = 0
    private var screenHeight = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContentView(R.layout.activity_creditos)

        // Configuración de WindowInsets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.creditos)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar TextView
        nombresTextView = findViewById(R.id.nombres)
        nombresTextView.text = "Alan Kaled Guerrero Ortiz\nOmar Reyes Ramirez"

        // Obtener dimensiones de la pantalla
        val displayMetrics = resources.displayMetrics
        screenWidth = displayMetrics.widthPixels
        screenHeight = displayMetrics.heightPixels

        // Configurar sensor
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    override fun onResume() {
        super.onResume()
        accelerometer?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            val x = it.values[0] * -5
            val y = it.values[1] * 5

            moveText(nombresTextView, x, y)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No es necesario manejar esto para este caso
    }

    private fun moveText(textView: TextView, deltaX: Float, deltaY: Float) {
        val newX = (textView.x + deltaX).coerceIn(0f, (screenWidth - textView.width).toFloat())
        val newY = (textView.y + deltaY).coerceIn(0f, (screenHeight - textView.height).toFloat())

        textView.x = newX
        textView.y = newY
    }
}
