package com.example.snakemath

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.random.Random

class TestResta : AppCompatActivity() {

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

    // Lista de 20 preguntas de restas
    private val questions = listOf(
        "10 - 3", "12 - 5", "14 - 4", "9 - 3", "20 - 10", "15 - 5", "17 - 8", "18 - 9", "25 - 10", "30 - 15",
        "50 - 25", "40 - 20", "60 - 30", "70 - 35", "80 - 40", "90 - 45", "100 - 50", "35 - 20", "27 - 7", "45 - 15"
    )

    private val answers = listOf(
        listOf("5", "7", "6", "4"), // 10 - 3 = 7 (1)
        listOf("8", "7", "5", "6"), // 12 - 5 = 7 (1)
        listOf("9", "8", "10", "7"), // 14 - 4 = 10 (2)
        listOf("6", "7", "5", "8"), // 9 - 3 = 6 (0)
        listOf("8", "10", "12", "13"), // 20 - 10 = 10 (1)
        listOf("10", "8", "9", "7"), // 15 - 5 = 10 (0)
        listOf("9", "7", "10", "8"), // 17 - 8 = 9 (0)
        listOf("8", "7", "10", "9"), // 18 - 9 = 9 (3)
        listOf("15", "20", "10", "18"), // 25 - 10 = 15 (0)
        listOf("15", "20", "17", "10"), // 30 - 15 = 15 (0)
        listOf("25", "15", "20", "30"), // 50 - 25 = 25 (0)
        listOf("25", "20", "10", "15"), // 40 - 20 = 20 (1)
        listOf("35", "25", "30", "40"), // 60 - 30 = 30 (2)
        listOf("30", "35", "20", "25"), // 70 - 35 = 35 (1)
        listOf("40", "35", "30", "20"), // 80 - 40 = 40 (0)
        listOf("50", "45", "35", "40"), // 90 - 45 = 45 (1)
        listOf("50", "45", "40", "60"), // 100 - 50 = 50 (0)
        listOf("15", "20", "10", "25"), // 35 - 20 = 15 (0)
        listOf("15", "17", "20", "18"), // 27 - 7 = 20 (2)
        listOf("20", "15", "30", "25")  // 45 - 15 = 30 (2)
    )

    private val correctAnswers = listOf(1, 1, 2, 0, 1, 0, 0, 3, 0, 0, 0, 1, 2, 1, 0, 1, 0, 0, 2, 2)



    // Seleccionar aleatoriamente 10 preguntas de la lista de las 20 preguntas
    private val selectedQuestions = (questions.indices).shuffled().take(10)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_testresta)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.testresta)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        questionText = findViewById(R.id.question_text)
        option1 = findViewById(R.id.opt_1)
        option2 = findViewById(R.id.opt_2)
        option3 = findViewById(R.id.opt_3)
        option4 = findViewById(R.id.opt_4)
        submitButton = findViewById(R.id.submit)
        progressBar = findViewById(R.id.progress_bar)
        progressText = findViewById(R.id.progress_text)

        loadQuestion()

        // Listeners de opciones
        val optionViews = listOf(option1, option2, option3, option4)
        for ((index, option) in optionViews.withIndex()) {
            option.setOnClickListener {
                selectOption(index)
            }
        }

        // Listener del botón de enviar
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
            Toast.makeText(this, "¡Test completado!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun resetOptionColors() {
        // Restablece el fondo de todas las opciones
        option1.setBackgroundResource(android.R.color.transparent)
        option2.setBackgroundResource(android.R.color.transparent)
        option3.setBackgroundResource(android.R.color.transparent)
        option4.setBackgroundResource(android.R.color.transparent)
        selectedOptionIndex = -1
    }

    private fun selectOption(index: Int) {
        resetOptionColors() // Limpia la selección anterior
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
            Toast.makeText(this, "¡Respuesta Correcta!", Toast.LENGTH_SHORT).show()
        } else {
            selectedOptionView.setBackgroundResource(R.color.incorrect_answer)
            Toast.makeText(this, "Respuesta Incorrecta", Toast.LENGTH_SHORT).show()
        }

        // Esperar un momento y pasar a la siguiente pregunta
        Handler(Looper.getMainLooper()).postDelayed({
            currentQuestionIndex++
            loadQuestion()
        }, 1000)
    }
}
