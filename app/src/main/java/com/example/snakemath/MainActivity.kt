package com.example.snakemath

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        var db: DBsqlite = DBsqlite(this)
        if(!db.datosExistentes()){
            db.guardarDatos(1,0,1,1,0,10000f,"clasica",0,0,1, 1,0,0,0,0,0)
        }

        val intent = Intent(this, Intro::class.java)
        startActivity(intent)
        finish()


    }
}