package com.example.quizapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizapp.R
import com.example.quizapp.data.QuizDbHelper
import com.example.quizapp.databinding.ActivityCreateQuizBinding
import com.example.quizapp.databinding.ItemQuestionInputBinding
import com.example.quizapp.model.Question
import com.example.quizapp.model.Quiz

class CreateQuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateQuizBinding
    private lateinit var dbHelper: QuizDbHelper
    private val questionViews = mutableListOf<ItemQuestionInputBinding>()

    private val categories = arrayOf(
        "Teknologi",
        "Pendidikan",
        "Sains",
        "Umum",
        "Kesehatan",
        "Bisnis",
        "Hiburan",
        "Lainnya"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = QuizDbHelper(this)

        setupToolbar()
        setupCategoryDropdown()
        setupAddQuestion()
        setupSaveQuiz()

        // Add 1 default empty question so user immediately sees question fields
        addNewQuestionCard()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupCategoryDropdown() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, categories)
        binding.actvCategory.setAdapter(adapter)
        binding.actvCategory.setText(categories[0], false)
    }

    private fun setupAddQuestion() {
        binding.btnAddQuestion.setOnClickListener {
            addNewQuestionCard()
        }
    }

    private fun addNewQuestionCard() {
        val questionBinding = ItemQuestionInputBinding.inflate(LayoutInflater.from(this), binding.layoutQuestionsContainer, false)
        questionViews.add(questionBinding)
        binding.layoutQuestionsContainer.addView(questionBinding.root)

        updateQuestionHeaders()

        // Setup delete button
        questionBinding.btnDeleteQuestion.setOnClickListener {
            if (questionViews.size <= 1) {
                Toast.makeText(this, "Kuis harus memiliki minimal 1 pertanyaan.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            binding.layoutQuestionsContainer.removeView(questionBinding.root)
            questionViews.remove(questionBinding)
            updateQuestionHeaders()
        }
    }

    private fun updateQuestionHeaders() {
        for ((index, itemBinding) in questionViews.withIndex()) {
            itemBinding.tvQuestionHeader.text = "Pertanyaan #${index + 1}"
        }
        binding.tvQuestionsHeader.text = "Daftar Pertanyaan (${questionViews.size})"
    }

    private fun setupSaveQuiz() {
        binding.btnSaveQuiz.setOnClickListener {
            val title = binding.etQuizTitle.text?.toString()?.trim().orEmpty()
            val category = binding.actvCategory.text?.toString()?.trim().orEmpty()
            val desc = binding.etQuizDescription.text?.toString()?.trim().orEmpty()
            val durationStr = binding.etQuizDuration.text?.toString()?.trim().orEmpty()

            if (title.isEmpty()) {
                binding.etQuizTitle.error = "Judul kuis tidak boleh kosong"
                binding.etQuizTitle.requestFocus()
                return@setOnClickListener
            }

            if (category.isEmpty()) {
                binding.actvCategory.error = "Kategori harus dipilih"
                return@setOnClickListener
            }

            val durationSeconds = durationStr.toIntOrNull() ?: 60
            if (durationSeconds < 10) {
                binding.etQuizDuration.error = "Durasi countdown minimal 10 detik"
                binding.etQuizDuration.requestFocus()
                return@setOnClickListener
            }

            if (questionViews.isEmpty()) {
                Toast.makeText(this, "Tambahkan minimal 1 pertanyaan.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Parse and validate questions
            val questionList = mutableListOf<Question>()
            for ((index, qb) in questionViews.withIndex()) {
                val qText = qb.etQuestionText.text?.toString()?.trim().orEmpty()
                val optA = qb.etOptionA.text?.toString()?.trim().orEmpty()
                val optB = qb.etOptionB.text?.toString()?.trim().orEmpty()
                val optC = qb.etOptionC.text?.toString()?.trim().orEmpty()
                val optD = qb.etOptionD.text?.toString()?.trim().orEmpty()

                if (qText.isEmpty()) {
                    qb.etQuestionText.error = "Pertanyaan #${index + 1} tidak boleh kosong"
                    qb.etQuestionText.requestFocus()
                    return@setOnClickListener
                }

                if (optA.isEmpty() || optB.isEmpty() || optC.isEmpty() || optD.isEmpty()) {
                    Toast.makeText(this, "Semua pilihan (A, B, C, D) pada Pertanyaan #${index + 1} harus diisi.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val correctIndex = when (qb.rgCorrectAnswer.checkedRadioButtonId) {
                    R.id.rbOptionA -> 0
                    R.id.rbOptionB -> 1
                    R.id.rbOptionC -> 2
                    R.id.rbOptionD -> 3
                    else -> 0
                }

                questionList.add(
                    Question(
                        questionText = qText,
                        optionA = optA,
                        optionB = optB,
                        optionC = optC,
                        optionD = optD,
                        correctAnswer = correctIndex
                    )
                )
            }

            // Save to database
            val newQuiz = Quiz(
                title = title,
                category = category,
                description = desc,
                durationSeconds = durationSeconds
            )

            val createdId = dbHelper.insertQuizWithQuestions(newQuiz, questionList)
            if (createdId != -1L) {
                Toast.makeText(this, "Kuis \"$title\" berhasil dibuat!", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, "Gagal menyimpan kuis. Silakan coba lagi.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
