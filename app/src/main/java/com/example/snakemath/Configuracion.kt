package com.example.snakemath

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Configuracion : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_configuracion)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.config)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var db: DBsqlite = DBsqlite(this)
        val dinero: TextView = findViewById(R.id.monedas)

        dinero.text = "${db.obtenerDineroTotal().toInt()}"

        val iman: LinearLayout = findViewById(R.id.iman)
        val monedax5: LinearLayout = findViewById(R.id.monedax5)
        val estrella: LinearLayout = findViewById(R.id.estrella)
        var costoiman: TextView = findViewById(R.id.costoiman)
        var costoestrella: TextView = findViewById(R.id.costoestrella)
        var costomoneda: TextView = findViewById(R.id.costomonedas)
        val snakeclasica = findViewById<LinearLayout>(R.id.snakeclasica)
        val snakered: LinearLayout = findViewById(R.id.snakered)
        val snakecyan: LinearLayout = findViewById(R.id.snakecyan)
        val snakeyellow: LinearLayout = findViewById(R.id.snakeyellow)
        val snakegreen: LinearLayout = findViewById(R.id.snakegreen)
        val snakeorange: LinearLayout = findViewById(R.id.snakeorange)

        if(db.obteneriman() == 1){
                costoiman.text = "150"
        }else if(db.obteneriman() == 2){
                costoiman.text = "500"
        }else if(db.obteneriman() == 3){
                costoiman.text = "M A X"
        }

        if(db.obtenermonedax5() == 1){
            costomoneda.text = "150"
        }else if(db.obtenermonedax5() == 2){
            costomoneda.text = "500"
        }else if(db.obtenermonedax5() == 3){
            costomoneda.text = "M A X"
        }

        if(db.obtenerestrella() == 1){
            costoestrella.text = "150"
        }else if(db.obtenerestrella() == 2){
            costoestrella.text = "500"
        }else if(db.obtenerestrella() == 3){
            costoestrella.text = "M A X"
        }


        iman.setOnClickListener{
            if(db.obteneriman() == 0){
                if(db.obtenerDineroTotal().toInt() >= 30){
                    db.actualizariman(1)
                    costoiman.text = "150"
                    db.actualizarDineroTotal(db.obtenerDineroTotal().toInt() - 30)
                    dinero.text = "${db.obtenerDineroTotal().toInt()}"
                }
            }else if(db.obteneriman() == 1){
                if(db.obtenerDineroTotal().toInt() >= 150){
                    db.actualizariman(2)
                    costoiman.text = "500"
                    db.actualizarDineroTotal(db.obtenerDineroTotal().toInt() - 150)
                    dinero.text = "${db.obtenerDineroTotal().toInt()}"
                }
            }else if(db.obteneriman() == 2){
                if(db.obtenerDineroTotal().toInt() >= 500){
                    db.actualizariman(3)
                    costoiman.text = "M A X"
                    db.actualizarDineroTotal(db.obtenerDineroTotal().toInt() - 500)
                    dinero.text = "${db.obtenerDineroTotal().toInt()}"
                }
            }
        }

        monedax5.setOnClickListener {
            if(db.obtenermonedax5() == 0){
                if(db.obtenerDineroTotal().toInt() >= 30){
                    db.actualizarmonedax5(1)
                    costomoneda.text = "150"
                    Log.d("c","Entre")
                    db.actualizarDineroTotal(db.obtenerDineroTotal().toInt() - 30)
                    dinero.text = "${db.obtenerDineroTotal().toInt()}"
                }
            }else if(db.obtenermonedax5() == 1){
                if(db.obtenerDineroTotal().toInt() >= 150){
                    db.actualizarmonedax5(2)
                    costomoneda.text = "500"
                    db.actualizarDineroTotal(db.obtenerDineroTotal().toInt() - 150)
                    dinero.text = "${db.obtenerDineroTotal().toInt()}"
                }
            }else if(db.obtenermonedax5() == 2){
                if(db.obtenerDineroTotal().toInt() >= 500){
                    db.actualizarmonedax5(3)
                    costomoneda.text = "M A X"
                    db.actualizarDineroTotal(db.obtenerDineroTotal().toInt() - 500)
                    dinero.text = "${db.obtenerDineroTotal().toInt()}"
                }
            }
        }

        estrella.setOnClickListener {
            if(db.obtenerestrella() == 0){
                if(db.obtenerDineroTotal().toInt() >= 30){
                    db.actualizarestrella(1)
                    costoestrella.text = "150"
                    db.actualizarDineroTotal(db.obtenerDineroTotal().toInt() - 30)
                    dinero.text = "${db.obtenerDineroTotal().toInt()}"
                }
            }else if(db.obtenerestrella() == 1){
                if(db.obtenerDineroTotal().toInt() >= 150){
                    db.actualizarestrella(2)
                    costoestrella.text = "500"
                    db.actualizarDineroTotal(db.obtenerDineroTotal().toInt() - 150)
                    dinero.text = "${db.obtenerDineroTotal().toInt()}"
                }
            }else if(db.obtenerestrella() == 2){
                if(db.obtenerDineroTotal().toInt() >= 500){
                    db.actualizarestrella(3)
                    costoestrella.text = "M A X"
                    db.actualizarDineroTotal(db.obtenerDineroTotal().toInt() - 500)
                    dinero.text = "${db.obtenerDineroTotal().toInt()}"
                }
            }
        }

        snakeclasica.setOnClickListener{
            db.actualizarTipoSerpiente("serpiente1")
        }

        snakered.setOnClickListener{

        }


    }
}