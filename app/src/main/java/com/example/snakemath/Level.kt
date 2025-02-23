package com.example.snakemath

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Level : AppCompatActivity(), OnLifeLostListener {

    private lateinit var operacionTextView: TextView
    private lateinit var corazon1: ImageView
    private lateinit var corazon2: ImageView
    private lateinit var corazon3: ImageView
    private lateinit var intent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        setContentView(R.layout.activity_level)

        intent = Intent(this, Mapa::class.java)

        var db: DBsqlite = DBsqlite(this)
        if(db.obtenerMundo() == 1){
            db.actualizarPrimeraVez(1)
        }
        if(db.obtenerMundo() == 2){
            db.actualizarPrimeraVez(3)
        }
        if(db.obtenerMundo() == 3){
            db.actualizarPrimeraVez(5)
        }
        if(db.obtenerMundo() == 4){
            db.actualizarPrimeraVez(7)
        }
        // Inicializamos las vistas
        val scoreTextView = findViewById<TextView>(R.id.scoreText)
        val scoreTextView2 = findViewById<TextView>(R.id.scoreText2)
        val gameView = findViewById<GameView>(R.id.gameView)
        corazon1 = findViewById(R.id.heart1)
        corazon2 = findViewById(R.id.heart2)
        corazon3 = findViewById(R.id.heart3)

        gameView.onLifeLostListener = this
        operacionTextView = findViewById(R.id.Operacion)

        // Configuramos el puntaje
        gameView.setScoreTextView(scoreTextView)
        gameView.setScoreTextView2(scoreTextView2)

        // Configurar el listener para operaciones
        gameView.setOnOperacionGeneradaListener { operacion, operacionesResueltas, vidas ->
            if(actualizarOperacion(operacion, operacionesResueltas,vidas)){
                if(db.obtenerNivel() == db.obtenerNivelJugando()) {
                    db.actualizarNivel(db.obtenerNivel() + 1)
                }
                if(db.obtenerNivel() == 6 && db.obtenerMundo() == 1) {
                    db.actualizarMundo(2)
                    db.actualizarPrimeraVez(2)
                    startActivity(intent)
                    finish()
                }else if(db.obtenerNivel() == 9 && db.obtenerMundo() == 2){
                    db.actualizarMundo(3)
                    db.actualizarPrimeraVez(4)
                    //termina el level cuando gana
                    startActivity(intent)
                    finish()
                }else if(db.obtenerNivel() == 15 && db.obtenerMundo() == 3){
                    db.actualizarMundo(4)
                    db.actualizarPrimeraVez(6)
                    //termina el level cuando gana
                    startActivity(intent)
                    finish()
                }else{
                    //termina el level cuando gana
                    startActivity(intent)
                    finish()
                }


            }
        }
    }

    override fun onLifeLost(vidasRestantes: Int) {
        if(vidasRestantes == 2){
            corazon3.setImageResource(R.drawable.lifeempty)
        }else if(vidasRestantes == 1){
            corazon2.setImageResource(R.drawable.lifeempty)
            corazon3.setImageResource(R.drawable.lifeempty)
        }else if(vidasRestantes == 0){
            corazon1.setImageResource(R.drawable.lifeempty)
            corazon2.setImageResource(R.drawable.lifeempty)
            corazon3.setImageResource(R.drawable.lifeempty)
            startActivity(intent)
            finish()
        }
    }

    fun actualizarOperacion(operacion: String, Operaciones_resueltas: Int, Vidas: Int): Boolean{
        if(operacion == "perdio") {
            if(Vidas != 0 && Operaciones_resueltas != 5){
                startActivity(intent)
                finish()
            }
        }else{
            operacionTextView.text = operacion
        }
        if(Operaciones_resueltas == 5 && Vidas != 0){
            return true
        }
        return false
    }
    override fun onPause(){
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}
