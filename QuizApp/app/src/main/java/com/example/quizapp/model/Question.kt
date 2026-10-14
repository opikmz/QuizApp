package com.example.quizapp.model

import java.io.Serializable

data class Question(
    val id: Long = 0,
    var quizId: Long = 0,
    val questionText: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctAnswer: Int // 0: A, 1: B, 2: C, 3: D
) : Serializable {
    fun getOption(index: Int): String {
        return when (index) {
            0 -> optionA
            1 -> optionB
            2 -> optionC
            3 -> optionD
            else -> "Tidak dijawab"
        }
    }

    fun getOptionLetter(index: Int): String {
        return when (index) {
            0 -> "A"
            1 -> "B"
            2 -> "C"
            3 -> "D"
            else -> "-"
        }
    }
}
