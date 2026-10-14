package com.example.quizapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizapp.adapter.QuestionReviewAdapter
import com.example.quizapp.databinding.ActivityResultBinding
import com.example.quizapp.model.QuizSummary

class ResultActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_QUIZ_SUMMARY = "extra_quiz_summary"
    }

    private lateinit var binding: ActivityResultBinding
    private var summary: QuizSummary? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        summary = intent.getSerializableExtra(EXTRA_QUIZ_SUMMARY) as? QuizSummary

        if (summary == null) {
            finish()
            return
        }

        displayResults()
        setupListeners()
    }

    private fun displayResults() {
        val s = summary ?: return

        binding.tvScorePercentage.text = "${s.scorePercentage}%"
        binding.tvScoreFraction.text = "${s.correctCount} dari ${s.totalQuestions} pertanyaan benar"
        binding.tvTotalCorrect.text = s.correctCount.toString()
        binding.tvTotalWrong.text = s.wrongCount.toString()

        val minutes = s.timeSpentSeconds / 60
        val seconds = s.timeSpentSeconds % 60
        binding.tvTimeUsed.text = if (minutes > 0) "${minutes}m ${seconds}s" else "${seconds}s"

        binding.tvScoreTitle.text = when {
            s.scorePercentage >= 80 -> "Luar Biasa! Prestasi Hebat 🎉"
            s.scorePercentage >= 60 -> "Kerja Bagus! Nilai Cukup Baik 👍"
            else -> "Tetap Semangat! Coba Lagi Ya 💪"
        }

        // Setup review recycler view
        val reviewAdapter = QuestionReviewAdapter(this, s.answers)
        binding.rvReviewAnswers.apply {
            layoutManager = LinearLayoutManager(this@ResultActivity)
            adapter = reviewAdapter
        }
    }

    private fun setupListeners() {
        binding.resultToolbar.setNavigationOnClickListener {
            finish()
        }

        binding.btnRestartQuiz.setOnClickListener {
            val s = summary ?: return@setOnClickListener
            val intent = Intent(this, PlayQuizActivity::class.java).apply {
                putExtra(PlayQuizActivity.EXTRA_QUIZ_ID, s.quiz.id)
            }
            startActivity(intent)
            finish()
        }

        binding.btnBackHome.setOnClickListener {
            finish()
        }
    }
}
