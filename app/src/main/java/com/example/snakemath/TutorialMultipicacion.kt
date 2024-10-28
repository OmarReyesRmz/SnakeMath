package com.example.snakemath

import android.content.Intent
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TutorialMultipicacion: AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tutorialmultiplicacion)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tutorialmultiplicacion)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        webView = findViewById(R.id.webview1)

        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true  // Habilitar JavaScript
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true

        webView.webChromeClient = WebChromeClient()

        val videoUrl = "https://www.youtube.com/embed/YFtEaVw5k1A"
        webView.loadUrl(videoUrl)

        val volver: Button = findViewById(R.id.button)
        val siguiente: Button = findViewById(R.id.button2)
        val test: Button = findViewById(R.id.button3)

        volver.setOnClickListener{
            val intent = Intent(this, TutorialResta::class.java)
            startActivity(intent)
        }

        siguiente.setOnClickListener{
            val intent = Intent(this, TutorialDivision::class.java)
            startActivity(intent)
        }

        test.setOnClickListener{
            val intent = Intent(this, TestMultiplicacion::class.java)
            startActivity(intent)
        }

    }
}