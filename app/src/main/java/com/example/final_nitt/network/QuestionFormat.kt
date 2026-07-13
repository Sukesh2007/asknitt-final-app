package com.example.final_nitt.network


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestionFormat(
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("id")
    val id: Int,
    @SerialName("owner")
    val owner: Owner,
    @SerialName("question")
    val question: String,
    @SerialName("tags")
    val tags: List<String>,
    @SerialName("isSolved")
    val isSolved: Boolean
)