package com.example.snakemath

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
class CanvasMap @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Carga el mapa como un Bitmap
    private val mapBitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.map)
    private val paint = Paint()

    // Variables para la posición del personaje
    private var personajeX = mapBitmap.width / 2f
    private var personajeY = mapBitmap.height / 2f

    // Tamaño de la pantalla o de la vista
    private var viewWidth = 0
    private var viewHeight = 0

    init {
        // Opcional: Configura el paint o cualquier inicialización extra aquí
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewWidth = w
        viewHeight = h
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Calcula el desplazamiento para centrar la vista en el personaje,
        // pero se detiene al llegar a los bordes del mapa
        val offsetX = when {
            personajeX < viewWidth / 2 -> 0f
            personajeX > mapBitmap.width - viewWidth / 2 -> (mapBitmap.width - viewWidth).toFloat()
            else -> personajeX - viewWidth / 2
        }

        val offsetY = when {
            personajeY < viewHeight / 2 -> 0f
            personajeY > mapBitmap.height - viewHeight / 2 -> (mapBitmap.height - viewHeight).toFloat()
            else -> personajeY - (viewHeight / 2)
        }

        // Dibuja la parte visible del mapa
        canvas.drawBitmap(mapBitmap, -offsetX, -offsetY, paint)

        // Dibuja el personaje en su posición relativa dentro de la vista
        val personajeEnVistaX = (personajeX - offsetX).coerceIn(0f, viewWidth.toFloat())
        val personajeEnVistaY = (personajeY - offsetY).coerceIn(0f, viewHeight.toFloat())
        canvas.drawCircle(personajeEnVistaX, personajeEnVistaY, 30f, Paint().apply { color = 0xFFFF0000.toInt() })
    }

    // Método para actualizar la posición del personaje
    fun actualizarPosicion(x: Float, y: Float) {
        // Actualiza la posición del personaje, manteniéndolo dentro de los límites del mapa
        personajeX = (personajeX + x).coerceIn(0f, mapBitmap.width.toFloat())
        personajeY = (personajeY + y).coerceIn(0f, mapBitmap.height.toFloat())

        // Redibuja la vista con la nueva posición
        invalidate()
    }
}
