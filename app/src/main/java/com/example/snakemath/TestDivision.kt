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

class TestDivision : AppCompatActivity() {

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

    // Lista de 20 preguntas de división
    private val questions = listOf(
        "8 ÷ 4", "12 ÷ 3", "16 ÷ 4", "18 ÷ 6", "10 ÷ 5", "15 ÷ 3", "28 ÷ 4", "24 ÷ 6", "36 ÷ 6", "25 ÷ 5",
        "20 ÷ 4", "18 ÷ 3", "30 ÷ 5", "14 ÷ 7", "9 ÷ 3", "40 ÷ 8", "12 ÷ 4", "32 ÷ 8", "6 ÷ 2", "21 ÷ 7"
    )

    private val answers = listOf(
        listOf("4", "3", "2", "5"), // 8 ÷ 4 = 2 (2)
        listOf("6", "5", "4", "3"), // 12 ÷ 3 = 4 (2)
        listOf("2", "3", "4", "5"), // 16 ÷ 4 = 4 (2)
        listOf("4", "2", "3", "6"), // 18 ÷ 6 = 3 (2)
        listOf("2", "1", "5", "3"), // 10 ÷ 5 = 2 (0)
        listOf("3", "5", "6", "4"), // 15 ÷ 3 = 5 (1)
        listOf("8", "9", "7", "5"), // 28 ÷ 4 = 7 (2)
        listOf("4", "6", "5", "3"), // 24 ÷ 6 = 4 (0)
        listOf("3", "4", "6", "7"), // 36 ÷ 6 = 6 (2)
        listOf("3", "4", "6", "5"), // 25 ÷ 5 = 5 (3)
        listOf("6", "5", "4", "3"), // 20 ÷ 4 = 5 (2)
        listOf("8", "6", "5", "3"), // 18 ÷ 3 = 6 (1)
        listOf("4", "5", "6", "7"), // 30 ÷ 5 = 6 (1)
        listOf("1", "2", "3", "7"), // 14 ÷ 7 = 2 (1)
        listOf("3", "6", "2", "1"), // 9 ÷ 3 = 3 (0)
        listOf("8", "5", "4", "3"), // 40 ÷ 8 = 5 (1)
        listOf("6", "3", "2", "4"), // 12 ÷ 4 = 3 (1)
        listOf("2", "8", "4", "6"), // 32 ÷ 8 = 4 (2)
        listOf("4", "2", "3", "6"), // 6 ÷ 2 = 3 (2)
        listOf("2", "4", "5", "3")  // 21 ÷ 7 = 3 (3)
    )

    private val correctAnswers = listOf(2, 2, 2, 2, 0, 1, 2, 0, 2, 3, 2, 1, 1, 1, 0, 1, 1, 2, 2, 3)



    // Seleccionar aleatoriamente 10 preguntas de la lista de las 20 preguntas
    private val selectedQuestions = (questions.indices).shuffled().take(10)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_testdivision)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.testdivision)) { v, insets ->
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
