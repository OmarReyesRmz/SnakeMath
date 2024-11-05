package com.example.snakemath

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import kotlin.random.Random

class GameView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint()
    private val gridSize = 100f // Tamaño de cada celda ajustado a 100 píxeles
    private var bolitaX = gridSize // Posición inicial X de la bola
    private var bolitaY = gridSize // Posición inicial Y de la bola
    val maxWidth = 350 * resources.displayMetrics.density
    val maxHeight = 600 * resources.displayMetrics.density
    private var currentDirection = Direction.RIGHT
    private val step = gridSize
    private val gestureDetector: GestureDetectorCompat

    private var comidaX = 0f
    private var comidaY = 0f
    private val snakeBody = mutableListOf<Pair<Float, Float>>()


    private enum class Direction { UP, DOWN, LEFT, RIGHT }

    init {
        gestureDetector = GestureDetectorCompat(context, GestureListener())
        generateRandomComida() // Generar la primera posición de la comida
        startMovement()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Dibujar el fondo
        drawGrid(canvas)

        paint.color = Color.GREEN
        for (segment in snakeBody) {
            canvas.drawCircle(segment.first + gridSize / 2, segment.second + gridSize / 2, (gridSize / 2) - 10, paint)
        }

        // Dibujar la bola
        paint.color = Color.RED
        canvas.drawCircle(bolitaX + gridSize / 2, bolitaY + gridSize / 2, (gridSize / 2) - 10, paint)



        // Dibujar la comida
        paint.color = Color.BLACK
        canvas.drawCircle(comidaX + gridSize / 2, comidaY + gridSize / 2, (gridSize / 2) - 10, paint)

    }

    private fun drawGrid(canvas: Canvas) {
        paint.color = Color.parseColor("#AADD55") // Color del fondo
        val cols = width / gridSize
        val rows = (height / gridSize) + 1
        for (i in 0 until cols.toInt()) {
            for (j in 0 until rows.toInt()) {
                paint.color = if ((i + j) % 2 == 0) Color.parseColor("#AADD55") else Color.parseColor("#88CC44")
                canvas.drawRect(
                    i * gridSize, j * gridSize, (i + 1) * gridSize, (j + 1) * gridSize, paint
                )
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    private fun startMovement() {
        postDelayed(object : Runnable {
            override fun run() {
                moveBolita()
                checkCollisionWithComida()
                invalidate() // Redibujar la vista
                postDelayed(this, 150)
            }
        }, 150)
    }

    private fun moveBolita() {

        val previousPosition = Pair(bolitaX, bolitaY)

        when (currentDirection) {
            Direction.RIGHT -> if (bolitaX + step + gridSize / 2 < width) bolitaX += step
            Direction.LEFT -> if (bolitaX - step + gridSize / 2 >= 0) bolitaX -= step
            Direction.UP -> if (bolitaY - step + gridSize / 2 >= 0) bolitaY -= step
            Direction.DOWN -> if (bolitaY + step + gridSize / 2 < height) bolitaY += step
        }

        // Mover cada segmento de la cola
        if (snakeBody.isNotEmpty()) {
            for (i in snakeBody.size - 1 downTo 1) {
                snakeBody[i] = snakeBody[i - 1]
            }
            snakeBody[0] = previousPosition
        }

        // Detectar colisiones con los bordes
        if (bolitaX < 0) bolitaX = 0f
        if (bolitaX + gridSize > width) bolitaX = width - gridSize
        if (bolitaY < 0) bolitaY = 0f
        if (bolitaY + gridSize > height) bolitaY = height - gridSize
    }

    private fun checkCollisionWithComida() {
        if (bolitaX == comidaX && bolitaY ==     comidaY) {
            snakeBody.add(Pair(bolitaX, bolitaY))
            generateRandomComida()
        }
    }

    private fun generateRandomComida() {
        do {
            comidaX = Random.nextInt(0, (maxWidth / gridSize).toInt()) * gridSize
            comidaY = Random.nextInt(0, (maxHeight / gridSize).toInt()) * gridSize
        } while (snakeBody.any { it.first == comidaX && it.second == comidaY })
    }


    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        private val SWIPE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onFling(
            e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float
        ): Boolean {
            val diffX = e2.x - (e1?.x ?: 0f)
            val diffY = e2.y - (e1?.y ?: 0f)

            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0 && currentDirection != Direction.LEFT) {
                        currentDirection = Direction.RIGHT
                    } else if (diffX < 0 && currentDirection != Direction.RIGHT) {
                        currentDirection = Direction.LEFT
                    }
                }
            } else {
                if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0 && currentDirection != Direction.UP) {
                        currentDirection = Direction.DOWN
                    } else if (diffY < 0 && currentDirection != Direction.DOWN) {
                        currentDirection = Direction.UP
                    }
                }
            }
            return true
        }
    }
}
