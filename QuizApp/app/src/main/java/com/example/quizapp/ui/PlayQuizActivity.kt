package com.example.quizapp.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.quizapp.R
import com.example.quizapp.data.QuizDbHelper
import com.example.quizapp.databinding.ActivityPlayQuizBinding
import com.example.quizapp.model.Question
import com.example.quizapp.model.Quiz
import com.example.quizapp.model.QuizSummary
import com.example.quizapp.model.UserQuestionAnswer
import com.google.android.material.card.MaterialCardView

class PlayQuizActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_QUIZ_ID = "extra_quiz_id"
    }

    private lateinit var binding: ActivityPlayQuizBinding
    private lateinit var dbHelper: QuizDbHelper

    private var quiz: Quiz? = null
    private var questions: List<Question> = emptyList()
    private var currentIndex: Int = 0
    private lateinit var selectedAnswers: IntArray

    private var countDownTimer: CountDownTimer? = null
    private var totalDurationSeconds: Int = 60
    private var remainingSeconds: Int = 60

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = QuizDbHelper(this)

        val quizId = intent.getLongExtra(EXTRA_QUIZ_ID, -1L)
        if (quizId == -1L) {
            Toast.makeText(this, "Kuis tidak ditemukan.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        loadQuizData(quizId)
    }

    private fun loadQuizData(quizId: Long) {
        val allQuizzes = dbHelper.getAllQuizzes()
        quiz = allQuizzes.find { it.id == quizId }
        questions = dbHelper.getQuestionsForQuiz(quizId)

        if (quiz == null || questions.isEmpty()) {
            Toast.makeText(this, "Pertanyaan kuis kosong atau tidak ditemukan.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        selectedAnswers = IntArray(questions.size) { -1 }
        totalDurationSeconds = quiz!!.durationSeconds
        remainingSeconds = totalDurationSeconds

        setupUI()
        setupListeners()
        startCountdownTimer()
        displayQuestion(0)
    }

    private fun setupUI() {
        val currentQuiz = quiz ?: return
        binding.tvPlayQuizTitle.text = currentQuiz.title
        binding.tvPlayCategory.text = currentQuiz.category.uppercase()
        applyCategoryStyle(currentQuiz.category)
    }

    private fun setupListeners() {
        binding.btnCloseQuiz.setOnClickListener {
            showExitConfirmDialog()
        }

        binding.cardOptionA.setOnClickListener { selectOption(0) }
        binding.cardOptionB.setOnClickListener { selectOption(1) }
        binding.cardOptionC.setOnClickListener { selectOption(2) }
        binding.cardOptionD.setOnClickListener { selectOption(3) }

        binding.btnPrevQuestion.setOnClickListener {
            if (currentIndex > 0) {
                displayQuestion(currentIndex - 1)
            }
        }

        binding.btnNextQuestion.setOnClickListener {
            if (currentIndex < questions.size - 1) {
                displayQuestion(currentIndex + 1)
            }
        }

        binding.btnSubmitQuiz.setOnClickListener {
            showFinishConfirmDialog()
        }
    }

    private fun startCountdownTimer() {
        val totalMillis = totalDurationSeconds * 1000L
        binding.progressCountdown.max = totalDurationSeconds

        countDownTimer = object : CountDownTimer(totalMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainingSeconds = (millisUntilFinished / 1000).toInt()
                updateTimerDisplay(remainingSeconds)
            }

            override fun onFinish() {
                remainingSeconds = 0
                updateTimerDisplay(0)
                handleTimeExpired()
            }
        }.start()
    }

    private fun updateTimerDisplay(secondsLeft: Int) {
        val minutes = secondsLeft / 60
        val seconds = secondsLeft % 60
        val formattedTime = String.format("%02d:%02d", minutes, seconds)
        binding.tvCountdown.text = formattedTime
        binding.progressCountdown.progress = secondsLeft

        if (secondsLeft <= 10) {
            val urgentColor = ContextCompat.getColor(this, R.color.timerUrgent)
            binding.tvCountdown.setTextColor(urgentColor)
            binding.ivTimerIcon.setColorFilter(urgentColor)
            binding.progressCountdown.setIndicatorColor(urgentColor)
        } else {
            val primaryColor = ContextCompat.getColor(this, R.color.colorPrimary)
            binding.tvCountdown.setTextColor(primaryColor)
            binding.ivTimerIcon.setColorFilter(primaryColor)
            binding.progressCountdown.setIndicatorColor(primaryColor)
        }
    }

    private fun handleTimeExpired() {
        AlertDialog.Builder(this)
            .setTitle(R.string.time_up_title)
            .setMessage(R.string.time_up_message)
            .setCancelable(false)
            .setPositiveButton("Lihat Hasil") { _, _ ->
                finishAndSubmitQuiz()
            }
            .show()
    }

    private fun displayQuestion(index: Int) {
        if (index !in questions.indices) return
        currentIndex = index
        val q = questions[index]

        binding.tvQuestionNumber.text = "Pertanyaan ${index + 1} dari ${questions.size}"
        binding.tvQuestionContent.text = q.questionText

        binding.tvOptionA.text = q.optionA
        binding.tvOptionB.text = q.optionB
        binding.tvOptionC.text = q.optionC
        binding.tvOptionD.text = q.optionD

        // Refresh selected option appearance
        val currentSelection = selectedAnswers[currentIndex]
        updateOptionCardsSelection(currentSelection)

        // Update answer status text
        if (currentSelection != -1) {
            binding.tvAnsweredStatus.text = "✓ Sudah dijawab"
            binding.tvAnsweredStatus.setTextColor(ContextCompat.getColor(this, R.color.success))
        } else {
            binding.tvAnsweredStatus.text = "Belum dijawab"
            binding.tvAnsweredStatus.setTextColor(ContextCompat.getColor(this, R.color.textSecondary))
        }

        // Navigation buttons
        binding.btnPrevQuestion.isEnabled = currentIndex > 0

        if (currentIndex == questions.size - 1) {
            binding.btnNextQuestion.visibility = View.GONE
            binding.btnSubmitQuiz.visibility = View.VISIBLE
        } else {
            binding.btnNextQuestion.visibility = View.VISIBLE
            binding.btnSubmitQuiz.visibility = View.GONE
        }
    }

    private fun selectOption(optionIndex: Int) {
        selectedAnswers[currentIndex] = optionIndex
        updateOptionCardsSelection(optionIndex)

        binding.tvAnsweredStatus.text = "✓ Sudah dijawab"
        binding.tvAnsweredStatus.setTextColor(ContextCompat.getColor(this, R.color.success))
    }

    private fun updateOptionCardsSelection(selectedIdx: Int) {
        val cards = listOf(binding.cardOptionA, binding.cardOptionB, binding.cardOptionC, binding.cardOptionD)
        val badges = listOf(binding.tvOptionBadgeA, binding.tvOptionBadgeB, binding.tvOptionBadgeC, binding.tvOptionBadgeD)

        for (i in cards.indices) {
            val card = cards[i]
            val badge = badges[i]

            if (i == selectedIdx) {
                card.setStrokeColor(ContextCompat.getColor(this, R.color.colorPrimary))
                card.strokeWidth = 4
                card.setCardBackgroundColor(Color.parseColor("#EEF2FF"))
                badge.setBackgroundResource(R.drawable.bg_category_badge)
                badge.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
            } else {
                card.setStrokeColor(ContextCompat.getColor(this, R.color.dividerColor))
                card.strokeWidth = 2
                card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.surfaceColor))
                badge.setBackgroundResource(R.drawable.bg_category_badge)
                badge.setTextColor(ContextCompat.getColor(this, R.color.textSecondary))
            }
        }
    }

    private fun showFinishConfirmDialog() {
        val unansweredCount = selectedAnswers.count { it == -1 }
        val message = if (unansweredCount > 0) {
            "Masih ada $unansweredCount pertanyaan yang belum dijawab. Yakin ingin menyelesaikan kuis sekarang?"
        } else {
            "Apakah Anda yakin ingin menyelesaikan kuis dan melihat hasil?"
        }

        AlertDialog.Builder(this)
            .setTitle(R.string.confirm_finish_title)
            .setMessage(message)
            .setPositiveButton(R.string.yes) { _, _ ->
                finishAndSubmitQuiz()
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }

    private fun showExitConfirmDialog() {
        AlertDialog.Builder(this)
            .setTitle("Batalkan Kuis")
            .setMessage("Apakah Anda yakin ingin keluar? Progres kuis saat ini tidak akan disimpan.")
            .setPositiveButton("Keluar") { _, _ ->
                countDownTimer?.cancel()
                finish()
            }
            .setNegativeButton("Tetap Mengerjakan", null)
            .show()
    }

    private fun finishAndSubmitQuiz() {
        countDownTimer?.cancel()

        val timeSpent = totalDurationSeconds - remainingSeconds
        val answerList = mutableListOf<UserQuestionAnswer>()

        for (i in questions.indices) {
            answerList.add(
                UserQuestionAnswer(
                    question = questions[i],
                    selectedAnswer = selectedAnswers[i]
                )
            )
        }

        val summary = QuizSummary(
            quiz = quiz!!,
            answers = answerList,
            timeSpentSeconds = timeSpent
        )

        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra(ResultActivity.EXTRA_QUIZ_SUMMARY, summary)
        }
        startActivity(intent)
        finish()
    }

    private fun applyCategoryStyle(category: String) {
        val (bgColor, textColor) = when (category.lowercase()) {
            "teknologi" -> Pair(
                ContextCompat.getColor(this, R.color.cat_tech_bg),
                ContextCompat.getColor(this, R.color.cat_tech_text)
            )
            "pendidikan" -> Pair(
                ContextCompat.getColor(this, R.color.cat_edu_bg),
                ContextCompat.getColor(this, R.color.cat_edu_text)
            )
            "sains" -> Pair(
                ContextCompat.getColor(this, R.color.cat_science_bg),
                ContextCompat.getColor(this, R.color.cat_science_text)
            )
            "umum" -> Pair(
                ContextCompat.getColor(this, R.color.cat_general_bg),
                ContextCompat.getColor(this, R.color.cat_general_text)
            )
            "kesehatan" -> Pair(
                ContextCompat.getColor(this, R.color.cat_health_bg),
                ContextCompat.getColor(this, R.color.cat_health_text)
            )
            else -> Pair(
                Color.parseColor("#F1F5F9"),
                Color.parseColor("#475569")
            )
        }

        val badgeDrawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = 24f
            setColor(bgColor)
        }
        binding.tvPlayCategory.background = badgeDrawable
        binding.tvPlayCategory.setTextColor(textColor)
    }

    override fun onBackPressed() {
        showExitConfirmDialog()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }
}
