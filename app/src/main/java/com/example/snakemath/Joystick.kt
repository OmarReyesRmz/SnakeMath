package com.example.snakemath

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class Joystick @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var joystickMoveListener: ((Float, Float) -> Unit)? = null

    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    private var centerX = 0f
    private var centerY = 0f
    private var baseRadius = 130f
    private var joystickRadius = 60f
    private var joystickX = 0f
    private var joystickY = 0f

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = w / 2f
        centerY = h / 2f
        joystickX = centerX
        joystickY = centerY
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Dibuja el fondo del joystick
        canvas.drawCircle(centerX, centerY, baseRadius, paint.apply { color = Color.argb(90, 91, 92, 93) })

        // Dibuja el joystick
        canvas.drawCircle(joystickX, joystickY, joystickRadius, paint.apply { color = Color.argb(255, 152, 231, 200) })
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        when (action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                // Limita el movimiento del joystick dentro del rango
                val dx = event.x - centerX
                val dy = event.y - centerY
                val distance = Math.sqrt((dx * dx + dy * dy).toDouble())
                if (distance < baseRadius) {
                    joystickX = event.x
                    joystickY = event.y
                } else {
                    // Si está fuera del radio, limita el movimiento
                    val ratio = baseRadius / distance
                    joystickX = centerX + dx * ratio.toFloat()
                    joystickY = centerY + dy * ratio.toFloat()
                }
                invalidate()
                // Notifica la nueva posición
                joystickMoveListener?.invoke(dx, dy)
            }
            MotionEvent.ACTION_UP -> {
                // Vuelve el joystick a la posición central
                joystickX = centerX
                joystickY = centerY
                invalidate()
                // Notifica una posición neutra (0, 0) al soltar
                joystickMoveListener?.invoke(0f, 0f)
            }
        }
        return true
    }

    // Método para establecer el listener de movimiento
    fun setOnJoystickMoveListener(listener: (Float, Float) -> Unit) {
        this.joystickMoveListener = listener
    }
}
