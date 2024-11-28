package com.example.snakemath

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
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

    var db: DBsqlite = DBsqlite(context)
    private val snakeBody = mutableListOf<Pair<Float, Float>>()
    private val snakeDirections = mutableListOf<Direction>()
    private var headBitmap: Bitmap
    private var bodyBitmap: Bitmap
    private var new = 0

    private enum class Direction { UP, DOWN, LEFT, RIGHT }
    private val headwidth = 95
    private val headheight = 110
    private val bodywidth = 100
    private val bodyheight = 100
    var onLifeLostListener: OnLifeLostListener? = null
    private var vidasRestantes = 3
    private var operaciones_resueltas = 0

    private var score = 0
    private var scoreTextView: TextView? = null
    private var scoreTextView2: TextView? = null
    var manzanasEnElMapa: MutableList<Manzanas> = mutableListOf()  // Inicialización de manzanasEnElMapa
    var manzanasEnElMapa2: MutableList<Manzanas> = mutableListOf()  // Inicialización de manzanasEnElMapa
    var operacionarray: MutableList<Manzanas> = mutableListOf()

    private val manzanas2 = listOf(
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
        Manzanas(R.drawable.apple20, "numero", 20),
        Manzanas(R.drawable.apple21, "numero", 21),
        Manzanas(R.drawable.apple22, "numero", 22),
        Manzanas(R.drawable.apple23, "numero", 23),
        Manzanas(R.drawable.apple24, "numero", 24),
        Manzanas(R.drawable.apple25, "numero", 25),
        Manzanas(R.drawable.apple26, "numero", 26),
        Manzanas(R.drawable.apple27, "numero", 27),
        Manzanas(R.drawable.apple28, "numero", 28),
        Manzanas(R.drawable.apple29, "numero", 29),
        Manzanas(R.drawable.apple30, "numero", 30),
        Manzanas(R.drawable.apple31, "numero", 31),
        Manzanas(R.drawable.apple32, "numero", 32),
        Manzanas(R.drawable.apple33, "numero", 33),
        Manzanas(R.drawable.apple34, "numero", 34),
        Manzanas(R.drawable.apple35, "numero", 35),
        Manzanas(R.drawable.apple36, "numero", 36),
        Manzanas(R.drawable.apple37, "numero", 37),
        Manzanas(R.drawable.apple38, "numero", 38),
        Manzanas(R.drawable.apple39, "numero", 39),
        Manzanas(R.drawable.apple40, "numero", 40),
        Manzanas(R.drawable.apple41, "numero", 41),
        Manzanas(R.drawable.apple42, "numero", 42),
        Manzanas(R.drawable.apple43, "numero", 43),
        Manzanas(R.drawable.apple44, "numero", 44),
        Manzanas(R.drawable.apple45, "numero", 45),
        Manzanas(R.drawable.apple46, "numero", 46),
        Manzanas(R.drawable.apple47, "numero", 47),
        Manzanas(R.drawable.apple48, "numero", 48),
        Manzanas(R.drawable.apple49, "numero", 49),
        Manzanas(R.drawable.apple50, "numero", 50),
        Manzanas(R.drawable.apple51, "numero", 51),
        Manzanas(R.drawable.apple52, "numero", 52),
        Manzanas(R.drawable.apple53, "numero", 53),
        Manzanas(R.drawable.apple54, "numero", 54),
        Manzanas(R.drawable.apple55, "numero", 55),
        Manzanas(R.drawable.apple56, "numero", 56),
        Manzanas(R.drawable.apple57, "numero", 57),
        Manzanas(R.drawable.apple58, "numero", 58),
        Manzanas(R.drawable.apple59, "numero", 59),
        Manzanas(R.drawable.apple60, "numero", 60),
        Manzanas(R.drawable.apple61, "numero", 61),
        Manzanas(R.drawable.apple62, "numero", 62),
        Manzanas(R.drawable.apple63, "numero", 63),
        Manzanas(R.drawable.apple64, "numero", 64),
        Manzanas(R.drawable.apple65, "numero", 65),
        Manzanas(R.drawable.apple66, "numero", 66),
        Manzanas(R.drawable.apple67, "numero", 67),
        Manzanas(R.drawable.apple68, "numero", 68),
        Manzanas(R.drawable.apple69, "numero", 69),
        Manzanas(R.drawable.apple70, "numero", 70),
        Manzanas(R.drawable.apple71, "numero", 71),
        Manzanas(R.drawable.apple72, "numero", 72),
        Manzanas(R.drawable.apple73, "numero", 73),
        Manzanas(R.drawable.apple74, "numero", 74),
        Manzanas(R.drawable.apple75, "numero", 75),
        Manzanas(R.drawable.apple76, "numero", 76),
        Manzanas(R.drawable.apple77, "numero", 77),
        Manzanas(R.drawable.apple78, "numero", 78),
        Manzanas(R.drawable.apple79, "numero", 79),
        Manzanas(R.drawable.apple80, "numero", 80),
        Manzanas(R.drawable.apple81, "numero", 81),
        Manzanas(R.drawable.apple82, "numero", 82),
        Manzanas(R.drawable.apple83, "numero", 83),
        Manzanas(R.drawable.apple84, "numero", 84),
        Manzanas(R.drawable.apple85, "numero", 85),
        Manzanas(R.drawable.apple86, "numero", 86),
        Manzanas(R.drawable.apple87, "numero", 87),
        Manzanas(R.drawable.apple88, "numero", 88),
        Manzanas(R.drawable.apple89, "numero", 89),
        Manzanas(R.drawable.apple90, "numero", 90),
        Manzanas(R.drawable.apple91, "numero", 91),
        Manzanas(R.drawable.apple92, "numero", 92),
        Manzanas(R.drawable.apple93, "numero", 93),
        Manzanas(R.drawable.apple94, "numero", 94),
        Manzanas(R.drawable.apple95, "numero", 95),
        Manzanas(R.drawable.apple96, "numero", 96),
        Manzanas(R.drawable.apple97, "numero", 97),
        Manzanas(R.drawable.apple98, "numero", 98),
        Manzanas(R.drawable.apple99, "numero", 99),
        Manzanas(R.drawable.apple100, "numero", 100),
        Manzanas(R.drawable.applesum, "operacion", 9996),
        Manzanas(R.drawable.applediv, "operacion", 9997),
        Manzanas(R.drawable.applemult, "operacion", 9998),
        Manzanas(R.drawable.appleres, "operacion", 9999)
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
                manzanaX = Random.nextInt(0, (maxWidth / gridSize).toInt()) * gridSize
                manzanaY = Random.nextInt(0, (maxHeight / gridSize).toInt()) * gridSize
            } while (snakeBody.any { it.first == manzanaX && it.second == manzanaY } || manzanasEnElMapa.any { it.x == manzanaX && it.y == manzanaY })

            // Seleccionamos una manzana aleatoria de las disponibles

            //Log.d("Canvas","${db.obtenerMundoJugando()} Mundo")
            val tamaño =  manzanas2.size
            var manzanaAleatoria = manzanas2[tamaño-1]
            if(db.obtenerMundoJugando()  == 1) {
                while (manzanaAleatoria.tipo != "numero" || manzanaAleatoria.numero >= 50) {
                    manzanaAleatoria = manzanas2.toList().random()
                }
            }else if(db.obtenerMundoJugando() == 2){
                while (manzanaAleatoria.tipo != "numero" || manzanaAleatoria.numero >= 40) {
                    if(operacionarray.size == 2){
                        while (manzanaAleatoria.tipo != "numero" || manzanaAleatoria.numero >= operacionarray[0].numero) {
                            manzanaAleatoria = manzanas2.toList().random()
                        }
                    }else {
                        manzanaAleatoria = manzanas2.toList().random()
                    }
                }
            }else if(db.obtenerMundoJugando()  == 3){
                while (manzanaAleatoria.tipo != "numero" || manzanaAleatoria.numero >= 10) {
                    manzanaAleatoria = manzanas2.toList().random()
                }
            }else if(db.obtenerMundoJugando()  == 4){
                //Log.d("Canvas","${manzanaAleatoria.numero} Manzana encontrada0")
                while (manzanaAleatoria.tipo != "numero" || manzanaAleatoria.numero >= 100) {
                    //Log.d("Canvas","${manzanaAleatoria.numero} Manzana encontrada1")
                    if(operacionarray.size == 2){
                        while (manzanaAleatoria.tipo != "numero" || manzanaAleatoria.numero >= operacionarray[0].numero || operacionarray[0].numero%manzanaAleatoria.numero != 0) {
                            manzanaAleatoria = manzanas2.toList().random()
                            //Log.d("Canvas","${manzanaAleatoria.numero} Manzana encontrada2")
                        }
                        //Log.d("Canvas","Manzana encontrada3")
                    }else {
                        manzanaAleatoria = manzanas2.toList().random()
                    }
                }
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
        }
        invalidate()  // Llamamos a invalidate después de generar las 5 manzanas
    }

    private fun generateOperation(){
        for (i in 1..5) {  // Ciclo que se ejecutará 5 veces
            var manzanaX: Float = 0f
            var manzanaY: Float = 0f

            do {
                manzanaX = Random.nextInt(0, (maxWidth / gridSize).toInt()) * gridSize
                manzanaY = Random.nextInt(0, (maxHeight / gridSize).toInt()) * gridSize
            } while (snakeBody.any { it.first == manzanaX && it.second == manzanaY } || manzanasEnElMapa.any { it.x == manzanaX && it.y == manzanaY })

            // Seleccionamos una manzana aleatoria de las disponibles
            var manzanaAleatoria = manzanas2.toList().random()
            if(db.obtenerMundoJugando()  == 1) {
                while (manzanaAleatoria.numero != 9996) {
                    manzanaAleatoria = manzanas2.toList().random()
                }
            }else if(db.obtenerMundoJugando() == 2){
                while (manzanaAleatoria.numero != 9999) {
                    manzanaAleatoria = manzanas2.toList().random()
                }
            }else if(db.obtenerMundoJugando()  == 3){
                while (manzanaAleatoria.numero != 9998) {
                    manzanaAleatoria = manzanas2.toList().random()
                }
            }else if(db.obtenerMundoJugando()  == 4){
                while (manzanaAleatoria.numero != 9997) {
                    manzanaAleatoria = manzanas2.toList().random()
                }
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

        val tamanoCubo = 140

        for (manzana in manzanasEnElMapa) {
            val manzanaBitmap = BitmapFactory.decodeResource(resources, manzana.imagen)
            val scaledManzanaBitmap = Bitmap.createScaledBitmap(manzanaBitmap, tamanoCubo, tamanoCubo, true)
            canvas.drawBitmap(scaledManzanaBitmap, manzana.x -20, manzana.y - 20, null)
        }


        for (manzana in manzanasEnElMapa2) {
            val manzanaBitmap = BitmapFactory.decodeResource(resources, manzana.imagen)
            val scaledManzanaBitmap = Bitmap.createScaledBitmap(manzanaBitmap, tamanoCubo, tamanoCubo, true)
            canvas.drawBitmap(scaledManzanaBitmap, manzana.x - 20, manzana.y - 20, null)
        }
    }

    private fun drawGrid(canvas: Canvas) {
        paint.color = Color.parseColor("#AADD55")
        val cols = width / gridSize
        val rows = (height / gridSize) + 1
        for (i in 0 until cols.toInt()) {
            for (j in 0 until rows.toInt()) {
                if (db.obtenerMundoJugando() == 1) {
                    // Mundo 1 - Verde clásico
                    paint.color =
                        if ((i + j) % 2 == 0) Color.parseColor("#AADD55") else Color.parseColor("#88CC44")
                } else if (db.obtenerMundoJugando() == 2) {
                    // Mundo 2 - Futurístico metálico
                    paint.color =
                        if ((i + j) % 2 == 0) Color.parseColor("#B0BEC5") else Color.parseColor("#CFD8DC")
                } else if (db.obtenerMundoJugando() == 3) {
                    // Mundo 3 - Blanco y gris claro
                    paint.color =
                        if ((i + j) % 2 == 0) Color.parseColor("#E0F7FA") else Color.parseColor("#B2EBF2")
                } else if (db.obtenerMundoJugando() == 4) {
                    // Mundo 4 - Lava (rojo, naranja, negro)
                    paint.color =
                        if ((i + j) % 2 == 0) Color.parseColor("#FFCC99") else Color.parseColor("#D18B47")
                }
                canvas.drawRect(
                    i * gridSize, j * gridSize, (i + 1) * gridSize, (j + 1) * gridSize, paint
                )
            }
        }
    }

    private var isGameStarted = false // Nueva bandera para controlar el inicio del juego

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isGameStarted) {
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

        if (bolitaX < 0 || bolitaX + gridSize > width || bolitaY < 0 || bolitaY + gridSize > height) {
            resetGame()
            return
        }

        for (segment in snakeBody) {
            if (bolitaX == segment.first && bolitaY == segment.second) {
                resetGame()
                return
            }
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

    private fun resetGame() {
        db.actualizarDineroTotal(new/2)
        onOperacionGeneradaListener?.invoke("perdio",operaciones_resueltas)
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
                    scoreTextView?.text = "S C O R E : $score"
                    new ++
                    scoreTextView2?.text = "$new"
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

                }
                break
            }
        }
        for (manzana in manzanasEnElMapa2) {
            if (bolitaX == manzana.x && bolitaY == manzana.y) {
                snakeDirections.add(currentDirection)
                if (manzana.bandera1){
                    score ++
                    if(operaciones_resueltas + 1 == 5){
                        db.actualizarDineroTotal(new)
                    }
                    operaciones_resueltas ++
                    manzana.bandera1 = false
                    scoreTextView?.text = "S C O R E : $score"
                    new += 2
                    scoreTextView2?.text = "$new"
                    snakeBody.add(Pair(bolitaX, bolitaY))
                    manzanasEnElMapa2.clear()
                    operacionarray.clear()
                    onOperacionGeneradaListener?.invoke("",operaciones_resueltas)
                    generateRandomManzana()
                }else if(!manzana.bandera1){
                    score -= 5
                    if(operaciones_resueltas + 1 == 5){
                        db.actualizarDineroTotal(new)
                    }
                    operaciones_resueltas ++
                    manzana.bandera1 = false
                    snakeBody.removeAt(snakeBody.size - 1)
                    snakeBody.removeAt(snakeBody.size - 1)
                    scoreTextView?.text = "S C O R E : $score"
                    new -= 4
                    if(new <= 0){
                        new = 0
                    }
                    scoreTextView2?.text = "$new"
                    manzanasEnElMapa2.clear()
                    operacionarray.clear()
                    onOperacionGeneradaListener?.invoke("",operaciones_resueltas)
                    generateRandomManzana()

                    // Notifica al listener sobre la pérdida de una vida
                    if (vidasRestantes > 0) {
                        vidasRestantes--
                        onLifeLostListener?.onLifeLost(vidasRestantes)
                    }

                }
                break
            }
        }
    }

    private fun construirOperacion(): String {
        // Suponiendo que las manzanas tienen atributos `tipo` y `numero`
        val operandos = mutableListOf<String>()
        var total = 0

        for (manzana in operacionarray) {
            if (manzana.tipo == "numero") {
                operandos.add(manzana.numero.toString()) // Agregar números
            } else if (manzana.tipo == "operacion") {
                when(manzana.numero){
                    9996 -> {
                        operandos.add("+")
                        total = operacionarray[0].numero + operacionarray[2].numero
                    }
                    9997 -> {
                        operandos.add("/")
                        total = operacionarray[0].numero / operacionarray[2].numero
                    }
                    9998 -> {
                        operandos.add("*")
                        total = operacionarray[0].numero * operacionarray[2].numero
                    }
                    9999 -> {
                        operandos.add("-")
                        total = operacionarray[0].numero - operacionarray[2].numero
                    }
                }

            }
        }
        var random =(0..2).random()
        var banderapass = false
        var manzanaX = 0F
        var manzanaY = 0F
        var manzanaAleatoria = manzanas2.find { it.numero == total }
        for(i in 0..2){
            if(i == random){
                manzanaAleatoria = manzanas2.find { it.numero == total }
                banderapass = true
            }else{
                val manzanasSinTotal = manzanas2.filter { it.numero != total }
                manzanaAleatoria = manzanasSinTotal.random()
                while (manzanaAleatoria?.tipo != "numero") {
                    manzanaAleatoria = manzanasSinTotal.random()
                }
                banderapass = false
            }

            do {
                // Asegúrate de que maxWidth y maxHeight sean valores enteros
                manzanaX = Random.nextInt(0, (maxWidth / gridSize).toInt()) * gridSize
                manzanaY = Random.nextInt(0, (maxHeight / gridSize).toInt()) * gridSize
            } while (snakeBody.any { it.first == manzanaX && it.second == manzanaY } || manzanasEnElMapa.any { it.x == manzanaX && it.y == manzanaY })


            if (manzanaAleatoria == null) {
                manzanaAleatoria = manzanas2.toList().random()
            }
            val nuevaManzana = Manzanas(
                imagen = manzanaAleatoria.imagen,
                tipo = manzanaAleatoria.tipo,
                numero = manzanaAleatoria.numero,
                x = manzanaX,
                y = manzanaY,
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

    fun setScoreTextView2(textView: TextView) {
        scoreTextView2 = textView
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
