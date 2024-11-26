package com.example.snakemath

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.core.view.GestureDetectorCompat
import kotlin.random.Random

class GameView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr,) {

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
    private var comidaActual: Manzanas? = null

    private enum class Direction { UP, DOWN, LEFT, RIGHT }
    private val headwidth = 95
    private val headheight = 110
    private val bodywidth = 100
    private val bodyheight = 100
    private var operaciones_resueltas = 0

    private var score = 0
    private var scoreTextView: TextView? = null
    var manzanasEnElMapa: MutableList<Manzanas> = mutableListOf()  // Inicialización de manzanasEnElMapa
    var manzanasEnElMapa2: MutableList<Manzanas> = mutableListOf()  // Inicialización de manzanasEnElMapa
    var operacionarray: MutableList<Manzanas> = mutableListOf()

    private val manzanas = arrayOf(
        Manzanas(R.drawable.apple1, "numero", 1),
        Manzanas(R.drawable.apple2, "numero", 2),
        Manzanas(R.drawable.apple3, "numero", 3),
        Manzanas(R.drawable.apple4, "numero", 4),
        Manzanas(R.drawable.apple5, "numero", 5),
        Manzanas(R.drawable.apple6, "numero", 6),
        Manzanas(R.drawable.apple7, "numero", 7),
        Manzanas(R.drawable.apple8, "numero", 8),
        Manzanas(R.drawable.apple9, "numero", 9),
        Manzanas(R.drawable.apple10, "numero", 10),
        Manzanas(R.drawable.apple11, "numero", 11),
        Manzanas(R.drawable.apple12, "numero", 12),
        Manzanas(R.drawable.applesum, "operacion", 9996),
        Manzanas(R.drawable.applediv, "operacion", 9997),
        Manzanas(R.drawable.applemult, "operacion", 9998),
        Manzanas(R.drawable.appleres, "operacion", 9999)
    )

    private val manzanas2 = arrayOf(
        Manzanas(R.drawable.apple1, "numero", 1),
        Manzanas(R.drawable.apple2, "numero", 2),
        Manzanas(R.drawable.apple3, "numero", 3),
        Manzanas(R.drawable.apple4, "numero", 4),
        Manzanas(R.drawable.apple5, "numero", 5),
        Manzanas(R.drawable.apple6, "numero", 6),
        Manzanas(R.drawable.apple7, "numero", 7),
        Manzanas(R.drawable.apple8, "numero", 8),
        Manzanas(R.drawable.apple9, "numero", 9),
        Manzanas(R.drawable.apple10, "numero", 10),
        Manzanas(R.drawable.apple11, "numero", 11),
        Manzanas(R.drawable.apple12, "numero", 12),
        Manzanas(R.drawable.apple13, "numero", 13),
        Manzanas(R.drawable.apple14, "numero", 14),
        Manzanas(R.drawable.apple15, "numero", 15),
        Manzanas(R.drawable.apple16, "numero", 16),
        Manzanas(R.drawable.apple17, "numero", 17),
        Manzanas(R.drawable.apple18, "numero", 18),
        Manzanas(R.drawable.apple19, "numero", 19),
        Manzanas(R.drawable.apple20, "numero", 20)
    )

    private var onOperacionGeneradaListener: ((String, Int) -> Unit)? = null

    init {
        gestureDetector = GestureDetectorCompat(context, GestureListener())
        bolitaX += gridSize * 3 // Mover la cabeza hacia la derecha
        generateRandomManzana()

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
            bodywidth, bodyheight, true)
    }


    private fun generateRandomManzana() {
        for (i in 1..5) {  // Ciclo que se ejecutará 5 veces
            var manzanaX: Float = 0f
            var manzanaY: Float = 0f

            do {
                // Asegúrate de que maxWidth y maxHeight sean valores enteros
                manzanaX = Random.nextInt(0, (maxWidth / gridSize).toInt()) * gridSize
                manzanaY = Random.nextInt(0, (maxHeight / gridSize).toInt()) * gridSize
                Log.d("GameView", "Intentando generar manzana en ($manzanaX, $manzanaY)")
            } while (snakeBody.any { it.first == manzanaX && it.second == manzanaY } || manzanasEnElMapa.any { it.x == manzanaX && it.y == manzanaY })

            // Seleccionamos una manzana aleatoria de las disponibles
            var manzanaAleatoria = manzanas.toList().random()
            while (manzanaAleatoria.tipo != "numero") {
                // Si el tipo no es "numero", se selecciona una nueva manzana aleatoria
                manzanaAleatoria = manzanas.toList().random()
            }

            val nuevaManzana = Manzanas(
                imagen = manzanaAleatoria.imagen,
                tipo = manzanaAleatoria.tipo,
                numero = manzanaAleatoria.numero,
                x = manzanaX,
                y = manzanaY
            )

            // Añadimos la nueva manzana al vector de manzanas en el mapa
            manzanasEnElMapa.add(nuevaManzana)
            Log.d("GameView", "Manzana generada en ($manzanaX, $manzanaY)")
        }
        invalidate()  // Llamamos a invalidate después de generar las 5 manzanas
    }


    private fun generateOperation(){
        for (i in 1..5) {  // Ciclo que se ejecutará 5 veces
            var manzanaX: Float = 0f
            var manzanaY: Float = 0f

            do {
                // Asegúrate de que maxWidth y maxHeight sean valores enteros
                manzanaX = Random.nextInt(0, (maxWidth / gridSize).toInt()) * gridSize
                manzanaY = Random.nextInt(0, (maxHeight / gridSize).toInt()) * gridSize
                Log.d("GameView", "Intentando generar manzana en ($manzanaX, $manzanaY)")
            } while (snakeBody.any { it.first == manzanaX && it.second == manzanaY } || manzanasEnElMapa.any { it.x == manzanaX && it.y == manzanaY })

            // Seleccionamos una manzana aleatoria de las disponibles
            var manzanaAleatoria = manzanas.toList().random()
            while (manzanaAleatoria.numero != 9996) {
                // Si el tipo no es "numero", se selecciona una nueva manzana aleatoria
                manzanaAleatoria = manzanas.toList().random()
            }

            val nuevaManzana = Manzanas(
                imagen = manzanaAleatoria.imagen,
                tipo = manzanaAleatoria.tipo,
                numero = manzanaAleatoria.numero,
                x = manzanaX,
                y = manzanaY
            )

            // Añadimos la nueva manzana al vector de manzanas en el mapa
            manzanasEnElMapa.add(nuevaManzana)
            Log.d("GameView", "Manzana generada en ($manzanaX, $manzanaY)")
        }
        invalidate()
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

        val tamanoCubo = 100

        for (manzana in manzanasEnElMapa) {
            val manzanaBitmap = BitmapFactory.decodeResource(resources, manzana.imagen)
            val scaledManzanaBitmap = Bitmap.createScaledBitmap(manzanaBitmap, tamanoCubo, tamanoCubo, true)
            canvas.drawBitmap(scaledManzanaBitmap, manzana.x, manzana.y, null)
        }


        for (manzana in manzanasEnElMapa2) {
            val manzanaBitmap = BitmapFactory.decodeResource(resources, manzana.imagen)
            val scaledManzanaBitmap = Bitmap.createScaledBitmap(manzanaBitmap, tamanoCubo, tamanoCubo, true)
            canvas.drawBitmap(scaledManzanaBitmap, manzana.x, manzana.y, null)
        }


        //paint.color = Color.BLACK
        //canvas.drawCircle(comidaX + gridSize / 2, comidaY + gridSize / 2, (gridSize / 2) - 10, paint)
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

    private var isGameStarted = false // Nueva bandera para controlar el inicio del juego

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isGameStarted) {
            // Iniciar el juego con el primer deslizamiento
            isGameStarted = true
            startMovement() // Inicia el movimiento
        }
        gestureDetector.onTouchEvent(event)
        return true
    }

    private fun startMovement() {
        if (!isGameStarted) return
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

    fun setOnOperacionGeneradaListener(listener: (String, Int) -> Unit) {
        onOperacionGeneradaListener = listener
    }

    private fun checkCollisionWithComida() {
        for (manzana in manzanasEnElMapa) {
            if (bolitaX == manzana.x && bolitaY == manzana.y) {
                snakeDirections.add(currentDirection)
                if (manzana.tipo == "operacion" && operacionarray.size != 3){
                    operacionarray.add(manzana)
                    manzanasEnElMapa.clear()
                    generateRandomManzana()
                }else if(manzana.tipo == "numero" && operacionarray.size != 3){
                    score ++
                    scoreTextView?.text = "Score: $score"
                    snakeBody.add(Pair(bolitaX, bolitaY))
                    operacionarray.add(manzana)
                    manzanasEnElMapa.clear()
                    if(operacionarray.size != 3) {
                        generateOperation()
                    }
                }

                // Verificar si ya se tienen 3 elementos en `operacionarray`
                if (operacionarray.size == 3) {

                    val operacionString = construirOperacion()
                    onOperacionGeneradaListener?.invoke(operacionString,operaciones_resueltas)
                    //operacionTextView.text = operacionString

                    // Opcional: Limpiar operacionarray después de mostrar la operación
                    // operacionarray.clear()
                }
                break
            }
        }
        for (manzana in manzanasEnElMapa2) {
            if (bolitaX == manzana.x && bolitaY == manzana.y) {
                snakeDirections.add(currentDirection)
                if (manzana.bandera1){
                    score ++
                    operaciones_resueltas ++
                    manzana.bandera1 = false
                    scoreTextView?.text = "Score: $score"
                    snakeBody.add(Pair(bolitaX, bolitaY))
                    manzanasEnElMapa2.clear()
                    operacionarray.clear()
                    onOperacionGeneradaListener?.invoke("",operaciones_resueltas)
                    generateRandomManzana()
                }else if(!manzana.bandera1){
                    score -= 5
                    operaciones_resueltas ++
                    manzana.bandera1 = false
                    snakeBody.removeAt(snakeBody.size - 1)
                    snakeBody.removeAt(snakeBody.size - 1)
                    scoreTextView?.text = "Score: $score"
                    manzanasEnElMapa2.clear()
                    operacionarray.clear()
                    onOperacionGeneradaListener?.invoke("",operaciones_resueltas)
                    generateRandomManzana()
                }
//                else if(manzana.tipo == "numero" && operacionarray.size != 3){
//                    score ++
//                    scoreTextView?.text = "Score: $score"
//                    snakeBody.add(Pair(bolitaX, bolitaY))
//                    operacionarray.add(manzana)
//                    manzanasEnElMapa.clear()
//                    if(operacionarray.size != 3) {
//                        generateOperation()
//                    }
//                }

                // Verificar si ya se tienen 3 elementos en `operacionarray`
//                if (operacionarray.size == 3) {
//
//                    val operacionString = construirOperacion()
//                    onOperacionGeneradaListener?.invoke(operacionString,operaciones_resueltas)
//                    //operacionTextView.text = operacionString
//
//                    // Opcional: Limpiar operacionarray después de mostrar la operación
//                    // operacionarray.clear()
//                }
                break
            }
        }
    }

    private fun construirOperacion(): String {
        // Suponiendo que las manzanas tienen atributos `tipo` y `numero`
        val operandos = mutableListOf<String>()
        var total = 0
        val coordx = 200f
        val coordy = 1300f

        for (manzana in operacionarray) {
            if (manzana.tipo == "numero") {
                total += manzana.numero
                operandos.add(manzana.numero.toString()) // Agregar números
            } else if (manzana.tipo == "operacion") {
                when(manzana.numero){
                    9996 -> {
                        operandos.add("+")
                    }
                    9997 -> {
                        operandos.add("/")
                    }
                    9998 -> {
                        operandos.add("*")
                    }
                    9999 -> {
                        operandos.add("-")
                    }
                }

            }
        }
        var random =(0..2).random()
        var banderapass = false
        var manzanaAleatoria = manzanas2.find { it.numero == total }
        for(i in 0..2){
            if(i == random){
                manzanaAleatoria = manzanas2.find { it.numero == total }
                banderapass = true
            }else{
                val manzanasSinTotal = manzanas2.filter { it.numero != total }
                manzanaAleatoria = manzanasSinTotal.random()
                banderapass = false
            }

            if (manzanaAleatoria == null) {
                manzanaAleatoria = manzanas2.toList().random()
            }
            val nuevaManzana = Manzanas(
                imagen = manzanaAleatoria.imagen,
                tipo = manzanaAleatoria.tipo,
                numero = manzanaAleatoria.numero,
                x = coordx + (i*200),
                y = coordy,
                bandera1 = banderapass
            )

            manzanasEnElMapa2.add(nuevaManzana)
        }

        invalidate()

        // Combinar los elementos para construir la operación
        return operandos.joinToString(" ")
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
