package com.example.snakemath

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.core.view.GestureDetectorCompat
import kotlin.random.Random

class GameView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint()
    private val gridSize = 100f
    private var bolitaX = gridSize
    private var bolitaY = gridSize
    val maxWidth = 350 * resources.displayMetrics.density
    val maxHeight = 600 * resources.displayMetrics.density
    private var currentDirection = Direction.RIGHT
    private val step = gridSize
    private val gestureDetector: GestureDetectorCompat

    private var comidaX = 0f
    private var comidaY = 0f
    private val snakeBody = mutableListOf<Pair<Float, Float>>()
    private val snakeDirections = mutableListOf<Direction>()
    private var headBitmap: Bitmap
    private var bodyBitmap: Bitmap

    private enum class Direction { UP, DOWN, LEFT, RIGHT }
    private val headwidth = 95
    private val headheight = 110

    private var score = 0
    private var scoreTextView: TextView? = null

    init {
        gestureDetector = GestureDetectorCompat(context, GestureListener())
        bolitaX += gridSize * 3 // Mover la cabeza hacia la derecha
        generateRandomComida()

        // Inicializar el cuerpo de la serpiente con tamaño 3
        for (i in 2 downTo 0) {
            snakeBody.add(Pair(bolitaX - gridSize * (3 - i), bolitaY))
            snakeDirections.add(Direction.RIGHT)
        }

        startMovement()

        headBitmap = Bitmap.createScaledBitmap(
            BitmapFactory.decodeResource(resources, R.drawable.chompa),
            headwidth,
            headheight,
            true)

        bodyBitmap = Bitmap.createScaledBitmap(
            BitmapFactory.decodeResource(resources, R.drawable.body2),
            headwidth, headheight, true)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawGrid(canvas)

        for ((index, segment) in snakeBody.withIndex()) {
            val bodyMatrix = Matrix()

            val rotationAngle = when (snakeDirections.getOrNull(index) ?: currentDirection) {
                Direction.RIGHT -> 0f
                Direction.DOWN -> 90f
                Direction.LEFT -> 180f
                Direction.UP -> 270f
            }

            bodyMatrix.postRotate(rotationAngle, bodyBitmap.width / 2f, bodyBitmap.height / 2f)
            bodyMatrix.postTranslate(segment.first, segment.second)
            canvas.drawBitmap(bodyBitmap, bodyMatrix, null)
        }

        val headMatrix = Matrix()
        val headRotationAngle = when (currentDirection) {
            Direction.RIGHT -> 0f
            Direction.DOWN -> 90f
            Direction.LEFT -> 180f
            Direction.UP -> 270f
        }
        headMatrix.postRotate(headRotationAngle, headBitmap.width / 2f, headBitmap.height / 2f)
        headMatrix.postTranslate(bolitaX, bolitaY)
        canvas.drawBitmap(headBitmap, headMatrix, null)

        paint.color = Color.BLACK
        canvas.drawCircle(comidaX + gridSize / 2, comidaY + gridSize / 2, (gridSize / 2) - 10, paint)
    }

    private fun drawGrid(canvas: Canvas) {
        paint.color = Color.parseColor("#AADD55")
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
                invalidate()
                postDelayed(this, 150)
            }
        }, 150)
    }

    private fun moveBolita() {
        val previousPosition = Pair(bolitaX, bolitaY)
        val previousDirection = currentDirection

        when (currentDirection) {
            Direction.RIGHT -> if (bolitaX + step + gridSize / 2 < width) bolitaX += step
            Direction.LEFT -> if (bolitaX - step + gridSize / 2 >= 0) bolitaX -= step
            Direction.UP -> if (bolitaY - step + gridSize / 2 >= 0) bolitaY -= step
            Direction.DOWN -> if (bolitaY + step + gridSize / 2 < height) bolitaY += step
        }

        if (snakeBody.isNotEmpty()) {
            for (i in snakeBody.size - 1 downTo 1) {
                snakeBody[i] = snakeBody[i - 1]
                snakeDirections[i] = snakeDirections[i - 1]
            }
            snakeBody[0] = previousPosition
            snakeDirections[0] = previousDirection
        }

        if (bolitaX < 0) bolitaX = 0f
        if (bolitaX + gridSize > width) bolitaX = width - gridSize
        if (bolitaY < 0) bolitaY = 0f
        if (bolitaY + gridSize > height) bolitaY = height - gridSize
    }

    private fun checkCollisionWithComida() {
        if (bolitaX == comidaX && bolitaY == comidaY) {
            score += 1
            scoreTextView?.text = "Score: $score"
            snakeBody.add(Pair(bolitaX, bolitaY))
            snakeDirections.add(currentDirection)
            generateRandomComida()
        }
    }


    private fun generateRandomComida() {
        do {
            comidaX = Random.nextInt(0, (maxWidth / gridSize).toInt()) * gridSize
            comidaY = Random.nextInt(0, (maxHeight / gridSize).toInt()) * gridSize
        } while (snakeBody.any { it.first == comidaX && it.second == comidaY })
    }

    fun setScoreTextView(textView: TextView) {
        scoreTextView = textView
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
