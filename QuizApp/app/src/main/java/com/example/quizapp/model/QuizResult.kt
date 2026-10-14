package com.example.quizapp.model

import java.io.Serializable

data class UserQuestionAnswer(
    val question: Question,
    val selectedAnswer: Int // -1 if not answered, 0: A, 1: B, 2: C, 3: D
) : Serializable {
    val isCorrect: Boolean
        get() = selectedAnswer == question.correctAnswer
}

data class QuizSummary(
    val quiz: Quiz,
    val answers: List<UserQuestionAnswer>,
    val timeSpentSeconds: Int
) : Serializable {
    val totalQuestions: Int get() = answers.size
    val correctCount: Int get() = answers.count { it.isCorrect }
    val wrongCount: Int get() = answers.count { !it.isCorrect }
    val scorePercentage: Int
        get() = if (totalQuestions > 0) (correctCount * 100) / totalQuestions else 0
}
