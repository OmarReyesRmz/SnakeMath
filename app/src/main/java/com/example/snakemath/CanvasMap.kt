package com.example.snakemath

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.media.MediaPlayer
import android.util.AttributeSet
import android.view.View
import com.example.snakemath.R
import android.graphics.Matrix

class CanvasMap @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mapBitmap = BitmapFactory.decodeResource(resources, R.drawable.map_modified)
    private val nextMapBitmap = BitmapFactory.decodeResource(resources, R.drawable.map_nivel_suma)
    // Define el ancho y alto deseados para el personaje
    private val personajeWidth = 200 // ancho deseado en píxeles
    private val personajeHeight = 150 // alto deseado en píxeles

    // Escala el bitmap del personaje al tamaño deseado
    private val personajeBitmap = Bitmap.createScaledBitmap(
        BitmapFactory.decodeResource(resources, R.drawable.snake),
        personajeWidth,
        personajeHeight,
        true
    )
    private val paint = Paint()
    private var fadeProgress = 1f // controla la transición

    private var personajeX = mapBitmap.width / 2f
    private var personajeY = mapBitmap.height / 2f
    private var viewWidth = 0
    private var viewHeight = 0
    private var previousX = personajeX
    private var previousY = personajeY

    private val mediaPlayer: MediaPlayer = MediaPlayer.create(context, R.raw.ocean_floor_156452)

    init {
        mediaPlayer.isLooping = true
        mediaPlayer.start()
        iniciarTransicion()
    }

    private fun iniciarTransicion() {
        val animator = ObjectAnimator.ofFloat(this, "fadeProgress", 1f, 0f)
        animator.duration = 4000
        val animatorSet = AnimatorSet()
        animatorSet.play(animator)
        animatorSet.start()
    }

    fun setFadeProgress(progress: Float) {
        fadeProgress = progress
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewWidth = w
        viewHeight = h
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val offsetX = when {
            personajeX < viewWidth / 2 -> 0f
            personajeX > mapBitmap.width - viewWidth / 2 -> (mapBitmap.width - viewWidth).toFloat()
            else -> personajeX - viewWidth / 2
        }

        val offsetY = when {
            personajeY < viewHeight / 2 -> 0f
            personajeY > mapBitmap.height - viewHeight / 2 -> (mapBitmap.height - viewHeight).toFloat()
            else -> personajeY - viewHeight / 2
        }

        // Dibuja los mapas con el efecto de transición
        paint.alpha = (255 * fadeProgress).toInt()
        canvas.drawBitmap(mapBitmap, -offsetX, -offsetY, paint)
        paint.alpha = (255 * (1 - fadeProgress)).toInt()
        canvas.drawBitmap(nextMapBitmap, -offsetX, -offsetY, paint)

        // Calcula el ángulo de rotación según la dirección de movimiento
        val dx = personajeX - previousX
        val dy = personajeY - previousY
        val angle = Math.toDegrees(Math.atan2(dy.toDouble(), dx.toDouble())).toFloat()

        // Dibuja el personaje con rotación
        val personajeMatrix = Matrix()
        personajeMatrix.postTranslate(-personajeBitmap.width / 2f, -personajeBitmap.height / 2f)
        personajeMatrix.postRotate(angle)
        personajeMatrix.postTranslate(
            personajeX - offsetX,
            personajeY - offsetY
        )
        canvas.drawBitmap(personajeBitmap, personajeMatrix, null)

        // Actualiza las coordenadas previas
        previousX = personajeX
        previousY = personajeY
    }

    fun actualizarPosicion(x: Float, y: Float) {
        personajeX = (personajeX + x).coerceIn(0f, mapBitmap.width.toFloat())
        personajeY = (personajeY + y).coerceIn(0f, mapBitmap.height.toFloat())
        invalidate()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mediaPlayer.release()
    }
}