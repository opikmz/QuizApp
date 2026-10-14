package com.example.quizapp.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.R
import com.example.quizapp.databinding.ItemReviewAnswerBinding
import com.example.quizapp.model.UserQuestionAnswer

class QuestionReviewAdapter(
    private val context: Context,
    private val answers: List<UserQuestionAnswer>
) : RecyclerView.Adapter<QuestionReviewAdapter.ReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemReviewAnswerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(answers[position], position + 1)
    }

    override fun getItemCount(): Int = answers.size

    inner class ReviewViewHolder(private val binding: ItemReviewAnswerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: UserQuestionAnswer, questionNum: Int) {
            val q = item.question
            binding.tvReviewQuestionNum.text = "Soal #$questionNum"
            binding.tvReviewQuestionText.text = q.questionText

            val correctLetter = q.getOptionLetter(q.correctAnswer)
            val correctText = q.getOption(q.correctAnswer)
            binding.tvCorrectAnswer.text = "Kunci Jawaban: $correctLetter. $correctText"

            if (item.selectedAnswer == -1) {
                // Not answered
                binding.tvReviewStatus.text = "Tidak Dijawab"
                applyStatusBadge(Color.parseColor("#FEF3C7"), Color.parseColor("#92400E"))
                binding.tvUserAnswer.text = "Jawaban Anda: (Kosong / Waktu Habis)"
                binding.tvUserAnswer.setTextColor(ContextCompat.getColor(context, R.color.textSecondary))
            } else if (item.isCorrect) {
                // Correct
                val userLetter = q.getOptionLetter(item.selectedAnswer)
                val userText = q.getOption(item.selectedAnswer)
                binding.tvReviewStatus.text = "✓ Benar"
                applyStatusBadge(ContextCompat.getColor(context, R.color.successLight), ContextCompat.getColor(context, R.color.success))
                binding.tvUserAnswer.text = "Jawaban Anda: $userLetter. $userText"
                binding.tvUserAnswer.setTextColor(ContextCompat.getColor(context, R.color.success))
            } else {
                // Wrong
                val userLetter = q.getOptionLetter(item.selectedAnswer)
                val userText = q.getOption(item.selectedAnswer)
                binding.tvReviewStatus.text = "✕ Salah"
                applyStatusBadge(ContextCompat.getColor(context, R.color.dangerLight), ContextCompat.getColor(context, R.color.danger))
                binding.tvUserAnswer.text = "Jawaban Anda: $userLetter. $userText"
                binding.tvUserAnswer.setTextColor(ContextCompat.getColor(context, R.color.danger))
            }
        }

        private fun applyStatusBadge(bgColor: Int, textColor: Int) {
            val shape = GradientDrawable().apply {
                this.shape = GradientDrawable.RECTANGLE
                cornerRadius = 16f
                setColor(bgColor)
            }
            binding.tvReviewStatus.background = shape
            binding.tvReviewStatus.setTextColor(textColor)
        }
    }
}
