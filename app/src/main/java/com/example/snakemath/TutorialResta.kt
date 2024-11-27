package com.example.snakemath

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TutorialResta: AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_tutorialresta)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tutorialresta)) { v, insets ->
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

        val videoUrl = "https://www.youtube.com/embed/42vjqtleG9E"
        webView.loadUrl(videoUrl)

        val volver: Button = findViewById(R.id.button)
        val siguiente: Button = findViewById(R.id.button2)
        val test: Button = findViewById(R.id.button3)
        val menu: Button = findViewById(R.id.buttonmenu)

        menu.setOnClickListener{
            val intent = Intent(this, Intro::class.java)
            startActivity(intent)
        }

        volver.setOnClickListener{
            val intent = Intent(this, TutorialSuma::class.java)
            startActivity(intent)
        }

        siguiente.setOnClickListener{
            val intent = Intent(this, TutorialMultipicacion::class.java)
            startActivity(intent)
        }

        test.setOnClickListener{
            val intent = Intent(this, TestResta::class.java)
            startActivity(intent)
        }
    }
}