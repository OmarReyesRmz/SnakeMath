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

class TestMultiplicacion : AppCompatActivity() {

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

    // Lista de 20 preguntas de multiplicación
    private val questions = listOf(
        "2 x 2", "3 x 5", "4 x 4", "6 x 1", "5 x 5", "7 x 3", "8 x 4", "3 x 6", "4 x 7", "5 x 3",
        "10 x 2", "6 x 3", "9 x 4", "7 x 2", "3 x 9", "8 x 3", "6 x 6", "7 x 4", "2 x 6", "5 x 4"
    )

    private val answers = listOf(
        listOf("5", "4", "3", "6"), // 2 x 2 = 4 (1)
        listOf("10", "8", "15", "14"), // 3 x 5 = 15 (2)
        listOf("20", "8", "16", "12"), // 4 x 4 = 16 (2)
        listOf("5", "8", "4", "6"), // 6 x 1 = 6 (3)
        listOf("25", "15", "20", "10"), // 5 x 5 = 25 (0)
        listOf("24", "21", "18", "20"), // 7 x 3 = 21 (1)
        listOf("24", "30", "28", "32"), // 8 x 4 = 32 (3)
        listOf("20", "9", "21", "18"), // 3 x 6 = 18 (3)
        listOf("15", "28", "12", "20"), // 4 x 7 = 28 (1)
        listOf("18", "12", "15", "10"), // 5 x 3 = 15 (2)
        listOf("18", "22", "15", "20"), // 10 x 2 = 20 (3)
        listOf("15", "20", "18", "9"), // 6 x 3 = 18 (2)
        listOf("35", "32", "36", "28"), // 9 x 4 = 36 (2)
        listOf("21", "14", "10", "16"), // 7 x 2 = 14 (1)
        listOf("30", "28", "27", "20"), // 3 x 9 = 27 (2)
        listOf("24", "15", "22", "10"), // 8 x 3 = 24 (0)
        listOf("28", "10", "15", "36"), // 6 x 6 = 36 (3)
        listOf("25", "28", "21", "16"), // 7 x 4 = 28 (1)
        listOf("10", "15", "12", "14"), // 2 x 6 = 12 (2)
        listOf("25", "20", "15", "30")  // 5 x 4 = 20 (1)
    )

    private val correctAnswers = listOf(1, 2, 2, 3, 0, 1, 3, 3, 1, 2, 3, 2, 2, 1, 2, 0, 3, 1, 2, 1)



    // Seleccionar aleatoriamente 10 preguntas de la lista de las 20 preguntas
    private val selectedQuestions = (questions.indices).shuffled().take(10)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_testmultiplicacion)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.testmultiplicacion)) { v, insets ->
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
