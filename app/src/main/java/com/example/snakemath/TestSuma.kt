package com.example.snakemath

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.random.Random

class TestSuma : AppCompatActivity() {

    private lateinit var questionText: TextView
    private lateinit var option1: TextView
    private lateinit var option2: TextView
    private lateinit var option3: TextView
    private lateinit var option4: TextView
    private lateinit var submitButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var progressText: TextView

    private var currentQuestionIndex = 0
    private var correctAnswer = 0
    private var selectedOptionIndex = -1
    private var correctAnswersCount = 0 // Contador de respuestas correctas

    private lateinit var respcorrecta: MediaPlayer
    private lateinit var respincorrecta: MediaPlayer

    // Lista de 20 preguntas y sus opciones
    private val questions = listOf(
        "2 + 2", "3 + 5", "4 + 4", "6 + 1", "5 + 5", "7 + 3", "8 + 4", "3 + 6", "4 + 7", "5 + 3",
        "10 + 8", "12 + 7", "9 + 4", "7 + 2", "3 + 9", "8 + 3", "6 + 6", "7 + 4", "2 + 6", "5 + 7"
    )

    private val answers = listOf(
        listOf("5", "7", "4", "6"), // 2 + 2 = 4 (2)
        listOf("7", "8", "9", "10"), // 3 + 5 = 8 (1)
        listOf("7", "10", "8", "16"), // 4 + 4 = 8 (2)
        listOf("8", "7", "6", "10"), // 6 + 1 = 7 (1)
        listOf("11", "8", "10", "9"), // 5 + 5 = 10 (2)
        listOf("10", "11", "9", "8"), // 7 + 3 = 10 (0)
        listOf("10", "12", "11", "14"), // 8 + 4 = 12 (1)
        listOf("10", "8", "7", "9"), // 3 + 6 = 9 (3)
        listOf("10", "11", "9", "8"), // 4 + 7 = 11 (1)
        listOf("8", "9", "10", "7"), // 5 + 3 = 8 (0)
        listOf("18", "19", "20", "17"), // 10 + 8 = 18 (0)
        listOf("17", "19", "20", "18"), // 12 + 7 = 19 (1)
        listOf("13", "11", "12", "14"), // 9 + 4 = 13 (0)
        listOf("9", "8", "6", "7"), // 7 + 2 = 9 (0)
        listOf("12", "13", "10", "11"), // 3 + 9 = 12 (0)
        listOf("11", "5", "6", "10"), // 8 + 3 = 11 (0)
        listOf("13", "10", "12", "14"), // 6 + 6 = 12 (2)
        listOf("11", "10", "13", "12"), // 7 + 4 = 11 (0)
        listOf("8", "6", "7", "5"), // 2 + 6 = 8 (0)
        listOf("10", "12", "11", "13") // 5 + 7 = 12 (1)
    )

    private val correctAnswers = listOf(2, 1, 2, 1, 2, 0, 1, 3, 1, 0, 0, 1, 0, 0, 0, 0, 2, 0, 0, 1)

    private val selectedQuestions = (questions.indices).shuffled().take(10)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_testsuma)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.testsuma)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        respcorrecta = MediaPlayer.create(this, R.raw.respcorrecta)
        respincorrecta = MediaPlayer.create(this, R.raw.respincorrecta)

        questionText = findViewById(R.id.question_text)
        option1 = findViewById(R.id.opt_1)
        option2 = findViewById(R.id.opt_2)
        option3 = findViewById(R.id.opt_3)
        option4 = findViewById(R.id.opt_4)
        submitButton = findViewById(R.id.submit)
        progressBar = findViewById(R.id.progress_bar)
        progressText = findViewById(R.id.progress_text)

        loadQuestion()

        val optionViews = listOf(option1, option2, option3, option4)
        for ((index, option) in optionViews.withIndex()) {
            option.setOnClickListener {
                selectOption(index)
            }
        }

        submitButton.setOnClickListener {
            checkAnswer()
        }
    }

    private fun loadQuestion() {
        if (currentQuestionIndex < selectedQuestions.size) {
            val questionIndex = selectedQuestions[currentQuestionIndex]
            questionText.text = questions[questionIndex]
            val options = answers[questionIndex]
            option1.text = options[0]
            option2.text = options[1]
            option3.text = options[2]
            option4.text = options[3]
            progressBar.progress = currentQuestionIndex + 1
            progressText.text = "${currentQuestionIndex + 1}/10"
            correctAnswer = correctAnswers[questionIndex]
            resetOptionColors()
        } else {
            // Fin del test - Lanza la actividad de resultados
            showResults()
        }
    }

    private fun resetOptionColors() {
        option1.setBackgroundResource(android.R.color.transparent)
        option2.setBackgroundResource(android.R.color.transparent)
        option3.setBackgroundResource(android.R.color.transparent)
        option4.setBackgroundResource(android.R.color.transparent)
        selectedOptionIndex = -1
    }

    private fun selectOption(index: Int) {
        resetOptionColors()
        val selectedOption = when (index) {
            0 -> option1
            1 -> option2
            2 -> option3
            else -> option4
        }
        selectedOption.setBackgroundResource(R.color.azul)
        selectedOptionIndex = index
    }

    private fun checkAnswer() {
        if (selectedOptionIndex == -1) {
            Toast.makeText(this, "Selecciona una opción", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedOptionView = when (selectedOptionIndex) {
            0 -> option1
            1 -> option2
            2 -> option3
            else -> option4
        }

        if (selectedOptionIndex == correctAnswer) {
            selectedOptionView.setBackgroundResource(R.color.correct_answer)
            respcorrecta.start()
            correctAnswersCount++ // Incrementa el contador de respuestas correctas
            Toast.makeText(this, "¡Respuesta Correcta!", Toast.LENGTH_SHORT).show()
        } else {
            selectedOptionView.setBackgroundResource(R.color.incorrect_answer)
            respincorrecta.start()
            Toast.makeText(this, "Respuesta Incoorrecta", Toast.LENGTH_SHORT).show()
        }

        Handler(Looper.getMainLooper()).postDelayed({
            currentQuestionIndex++
            loadQuestion()
        }, 1000)
    }

    private fun showResults() {
        val intent = Intent(this, Resultado::class.java)
        intent.putExtra("PUNTAJE_OBTENIDO", correctAnswersCount)
        intent.putExtra("TOTAL_PREGUNTAS", selectedQuestions.size)
        startActivity(intent)
        finish()
    }
}
