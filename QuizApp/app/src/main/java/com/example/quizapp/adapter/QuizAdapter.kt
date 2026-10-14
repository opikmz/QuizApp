package com.example.quizapp.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.R
import com.example.quizapp.databinding.ItemQuizCardBinding
import com.example.quizapp.model.Quiz

class QuizAdapter(
    private val context: Context,
    private var quizzes: List<Quiz>,
    private val onQuizClick: (Quiz) -> Unit,
    private val onQuizLongClick: ((Quiz) -> Unit)? = null
) : RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {

    fun updateData(newQuizzes: List<Quiz>) {
        this.quizzes = newQuizzes
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val binding = ItemQuizCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuizViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        holder.bind(quizzes[position])
    }

    override fun getItemCount(): Int = quizzes.size

    inner class QuizViewHolder(private val binding: ItemQuizCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(quiz: Quiz) {
            binding.tvQuizTitle.text = quiz.title
            binding.tvQuizDescription.text = quiz.description.ifBlank { "Tidak ada deskripsi." }
            binding.tvCategory.text = quiz.category.uppercase()
            binding.tvDuration.text = "${quiz.durationSeconds} Detik"
            binding.tvQuestionCount.text = "${quiz.questionCount} Pertanyaan"

            applyCategoryStyle(quiz.category)

            binding.root.setOnClickListener {
                onQuizClick(quiz)
            }

            binding.btnStartQuiz.setOnClickListener {
                onQuizClick(quiz)
            }

            binding.root.setOnLongClickListener {
                onQuizLongClick?.invoke(quiz)
                true
            }
        }

        private fun applyCategoryStyle(category: String) {
            val (bgColor, textColor) = when (category.lowercase()) {
                "teknologi" -> Pair(
                    ContextCompat.getColor(context, R.color.cat_tech_bg),
                    ContextCompat.getColor(context, R.color.cat_tech_text)
                )
                "pendidikan" -> Pair(
                    ContextCompat.getColor(context, R.color.cat_edu_bg),
                    ContextCompat.getColor(context, R.color.cat_edu_text)
                )
                "sains" -> Pair(
                    ContextCompat.getColor(context, R.color.cat_science_bg),
                    ContextCompat.getColor(context, R.color.cat_science_text)
                )
                "umum" -> Pair(
                    ContextCompat.getColor(context, R.color.cat_general_bg),
                    ContextCompat.getColor(context, R.color.cat_general_text)
                )
                "kesehatan" -> Pair(
                    ContextCompat.getColor(context, R.color.cat_health_bg),
                    ContextCompat.getColor(context, R.color.cat_health_text)
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
            binding.tvCategory.background = badgeDrawable
            binding.tvCategory.setTextColor(textColor)
        }
    }
}
