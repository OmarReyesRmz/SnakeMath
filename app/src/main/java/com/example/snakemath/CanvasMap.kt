package com.example.snakemath

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Matrix
import android.media.MediaPlayer
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.core.view.isVisible
import com.example.snakemath.R

class CanvasMap @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var db: DBsqlite = DBsqlite(context)
    private var mapBitmap = BitmapFactory.decodeResource(resources, R.drawable.map_modified)
    private val nextMapBitmap = BitmapFactory.decodeResource(resources, R.drawable.map_nivel_suma)
    private val mundo2MapBitmap = BitmapFactory.decodeResource(resources, R.drawable.map_nivel_resta)
    private val mundo3MapBitmap = BitmapFactory.decodeResource(resources, R.drawable.map_nivel_multiplicacion)
    private val mundo4MapBitmap = BitmapFactory.decodeResource(resources, R.drawable.map)

    private val personajeWidth = 650
    private val personajeHeight = 500
    private val personajeBitmap = Bitmap.createScaledBitmap(
        BitmapFactory.decodeResource(resources, R.drawable.snake),
        personajeWidth,
        personajeHeight,
        true
    )
    private val personaje2Bitmap = Bitmap.createScaledBitmap(
        BitmapFactory.decodeResource(resources, R.drawable.snake2),
        personajeWidth,
        personajeHeight,
        true
    )

    private val personaje3Bitmap = Bitmap.createScaledBitmap(
        BitmapFactory.decodeResource(resources, R.drawable.snake3),
        personajeWidth,
        personajeHeight,
        true
    )
    private val personaje4Bitmap = Bitmap.createScaledBitmap(
        BitmapFactory.decodeResource(resources, R.drawable.snake4),
        personajeWidth,
        personajeHeight,
        true
    )

    private val personaje5Bitmap = Bitmap.createScaledBitmap(
        BitmapFactory.decodeResource(resources, R.drawable.snake5),
        personajeWidth,
        personajeHeight,
        true
    )

    private val personaje6Bitmap = Bitmap.createScaledBitmap(
        BitmapFactory.decodeResource(resources, R.drawable.snake6),
        personajeWidth,
        personajeHeight,
        true
    )

    private val banderawidth = 300
    private val banderaheight = 300
    private val nextbanderaBitmap = Bitmap.createScaledBitmap(
        BitmapFactory.decodeResource(resources, R.drawable.ic_flag_modified),
        banderawidth,
        banderaheight,
        true
    )
    private val banderaBitmap = Bitmap.createScaledBitmap(
        BitmapFactory.decodeResource(resources, R.drawable.ic_flag),
        banderawidth,
        banderaheight,
        true
    )

    private val paint = Paint()
    private var fadeProgress = 1f

    private var personajeX = mapBitmap.width / 2f
    private var personajeY = mapBitmap.height / 2f
    private var viewWidth = 0
    private var viewHeight = 0
    private var previousX = personajeX
    private var previousY = personajeY

    private val mediaPlayer: MediaPlayer = MediaPlayer.create(context, R.raw.ocean_floor_156452)
    private var navigateButton: Button? = null // Referencia al botón

    // Coordenadas fijas de las banderas en el tamaño original de 1792x1024
    private val banderaPositions = listOf(
        //5 niveles suma
        Pair(940f, 240f),
        Pair(598f, 258f),
        Pair(323f, 500f),
        Pair(1148f, 535f),
        Pair(715f, 835f),
        //niveles mundo 2
        Pair(70f, 535f),
        Pair(1320f, 655f),
        Pair(260f, 820f),
        //niveles mundo 3
        Pair(280f, 260f),
        Pair(400f, 170f),
        Pair(410f, 670f),
        Pair(760f, 390f),
        Pair(1460f, 510f),
        Pair(935f, 500f),
        //niveles mundo 4
        Pair(1110f, 120f),
        Pair(1305f, 410f),
        Pair(1295f, 175f),
        Pair(1700f, 525f)
    )

    init {
        mediaPlayer.isLooping = true
        mediaPlayer.start()
        if(db.obtenerPrimeraVez() == 0) {
            iniciarTransicion()
        }else{
            if(db.obtenerMundo() == 2 && db.obtenerPrimeraVez() == 2){
                iniciarTransicion()
            }else{
                if(db.obtenerMundo() == 3 && db.obtenerPrimeraVez() == 4){
                    iniciarTransicion()
                }else{
                    if(db.obtenerMundo() == 4 && db.obtenerPrimeraVez() == 6){
                        iniciarTransicion()
                    }
                }
            }
        }
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

        val tipoSerpiente = db.obtenerTipoSerpiente()

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
        //Mundo1
        if(db.obtenerPrimeraVez() == 0 && db.obtenerMundo() == 1) {
            paint.alpha = (255 * fadeProgress).toInt()
            canvas.drawBitmap(mapBitmap, -offsetX, -offsetY, paint)
            paint.alpha = (255 * (1 - fadeProgress)).toInt()
            canvas.drawBitmap(nextMapBitmap, -offsetX, -offsetY, paint)
        }
        else if (db.obtenerPrimeraVez() == 1 && db.obtenerMundo() == 1){
            paint.alpha = (255 * fadeProgress).toInt()
            canvas.drawBitmap(nextMapBitmap, -offsetX, -offsetY, paint)
        }
        //Mundo2
        if(db.obtenerMundo() == 2 && db.obtenerPrimeraVez() == 2){
            paint.alpha = (255 * fadeProgress).toInt()
            canvas.drawBitmap(nextMapBitmap, -offsetX, -offsetY, paint)
            paint.alpha = (255 * (1 - fadeProgress)).toInt()
            canvas.drawBitmap(mundo2MapBitmap, -offsetX, -offsetY, paint)
        }else if(db.obtenerMundo() == 2 && db.obtenerPrimeraVez() == 3){
            paint.alpha = (255 * fadeProgress).toInt()
            canvas.drawBitmap(mundo2MapBitmap, -offsetX, -offsetY, paint)
        }
        //Mundo3
        if(db.obtenerMundo() == 3 && db.obtenerPrimeraVez() == 4){
            paint.alpha = (255 * fadeProgress).toInt()
            canvas.drawBitmap(mundo2MapBitmap, -offsetX, -offsetY, paint)
            paint.alpha = (255 * (1 - fadeProgress)).toInt()
            canvas.drawBitmap(mundo3MapBitmap, -offsetX, -offsetY, paint)
        }else if(db.obtenerMundo() == 3 && db.obtenerPrimeraVez() == 5){
            paint.alpha = (255 * fadeProgress).toInt()
            canvas.drawBitmap(mundo3MapBitmap, -offsetX, -offsetY, paint)
        }
        //Mundo4
        if(db.obtenerMundo() == 4 && db.obtenerPrimeraVez() == 6){
            paint.alpha = (255 * fadeProgress).toInt()
            canvas.drawBitmap(mundo3MapBitmap, -offsetX, -offsetY, paint)
            paint.alpha = (255 * (1 - fadeProgress)).toInt()
            canvas.drawBitmap(mundo4MapBitmap, -offsetX, -offsetY, paint)
        }else if(db.obtenerMundo() == 4 && db.obtenerPrimeraVez() == 7){
            paint.alpha = (255 * fadeProgress).toInt()
            canvas.drawBitmap(mundo4MapBitmap, -offsetX, -offsetY, paint)
        }

        val dx = personajeX - previousX
        val dy = personajeY - previousY
        val angle = Math.toDegrees(Math.atan2(dy.toDouble(), dx.toDouble())).toFloat()

        val personajeMatrix = Matrix()
        personajeMatrix.postTranslate(-personajeBitmap.width / 2f, -personajeBitmap.height / 2f)
        personajeMatrix.postRotate(angle)
        personajeMatrix.postTranslate(
            personajeX - offsetX,
            personajeY - offsetY
        )

        // Calcula la escala según el tamaño actual del mapa
        val scaleX = mapBitmap.width / 1792f
        val scaleY = mapBitmap.height / 1024f

        // Dibuja las banderas escaladas
        banderaPositions.forEachIndexed { index, (x, y) ->
            val banderaX = x * scaleX - offsetX
            val banderaY = y * scaleY - offsetY
            if(db.obtenerPrimeraVez() == 0 || db.obtenerPrimeraVez() == 2 || db.obtenerPrimeraVez() == 4 || db.obtenerPrimeraVez() == 6) {
                paint.alpha = (255 * fadeProgress).toInt()
                canvas.drawBitmap(nextbanderaBitmap, banderaX, banderaY, paint)
                if (index < db.obtenerNivel()) {
                    paint.alpha = (255 * (1 - fadeProgress)).toInt()
                    canvas.drawBitmap(banderaBitmap, banderaX, banderaY, paint)
                } else {
                    paint.alpha = (255 * (1 - fadeProgress)).toInt()
                    canvas.drawBitmap(nextbanderaBitmap, banderaX, banderaY, paint)
                }
            }else if(db.obtenerPrimeraVez() == 1 || db.obtenerPrimeraVez() == 3 || db.obtenerPrimeraVez() == 5 || db.obtenerPrimeraVez() == 7){
                if (index < db.obtenerNivel()) {
                    paint.alpha = (255 * fadeProgress).toInt()
                    canvas.drawBitmap(banderaBitmap, banderaX, banderaY, paint)
                } else {
                    paint.alpha = (255 * fadeProgress).toInt()
                    canvas.drawBitmap(nextbanderaBitmap, banderaX, banderaY, paint)
                }
            }
        }

        // Dibuja el personaje
        if (tipoSerpiente == "serpiente1"){
            canvas.drawBitmap(personajeBitmap, personajeMatrix, null)
        }else if (tipoSerpiente == "serpiente2"){
            canvas.drawBitmap(personaje2Bitmap, personajeMatrix, null)
        }else if (tipoSerpiente == "serpiente3"){
            canvas.drawBitmap(personaje3Bitmap, personajeMatrix, null)
        }else if (tipoSerpiente == "serpiente4"){
            canvas.drawBitmap(personaje4Bitmap, personajeMatrix, null)
        }else if(tipoSerpiente == "serpiente5"){
            canvas.drawBitmap(personaje5Bitmap, personajeMatrix, null)
        }else if (tipoSerpiente == "serpiente6"){
            canvas.drawBitmap(personaje6Bitmap, personajeMatrix, null)
        }


        // Verificar si el personaje está cerca de una de las primeras cinco banderas
        val isNearFlag = banderaPositions.take(db.obtenerNivel()).any { (flagX, flagY) ->
            val banderaX = (flagX * scaleX) + 50
            val banderaY = (flagY * scaleY) + 100
            val distance = Math.hypot((personajeX - banderaX).toDouble(), (personajeY - banderaY).toDouble())
            distance < 150 // Umbral de proximidad
        }

        val banderaCercana = banderaPositions.take(db.obtenerNivel()).indexOfFirst { (flagX, flagY) ->
            val banderaX = (flagX * scaleX) + 50
            val banderaY = (flagY * scaleY) + 100
            val distance = Math.hypot((personajeX - banderaX).toDouble(), (personajeY - banderaY).toDouble())
            distance < 150 // Umbral de proximidad
        }

        //Asginar el mundo que estamos jugando cunado se da click al boton
        if((banderaCercana+1) <= 5){
            db.actualizarMundoJugando(1)
        }else if((banderaCercana+1) <= 8){
            db.actualizarMundoJugando(2)
        }else if((banderaCercana+1) <= 14){
            db.actualizarMundoJugando(3)
        }else if((banderaCercana+1) <= 18){
            db.actualizarMundoJugando(4)
        }

        //Asignar el nivel que estamos jugando
        db.actualizarNivelJugando(banderaCercana + 1)

        // Mostrar u ocultar el botón de navegación según la proximidad
        navigateButton?.isVisible = isNearFlag

        previousX = personajeX
        previousY = personajeY
    }

    fun setNavigateButton(button: Button) {
        navigateButton = button
        navigateButton?.setOnClickListener {

        }
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
