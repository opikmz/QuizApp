package com.example.quizapp.model

import java.io.Serializable

data class Quiz(
    val id: Long = 0,
    val title: String,
    val category: String,
    val description: String,
    val durationSeconds: Int = 60,
    val createdAt: Long = System.currentTimeMillis(),
    var questionCount: Int = 0
) : Serializable
