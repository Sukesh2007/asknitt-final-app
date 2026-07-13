package com.example.final_nitt.network


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnswersForQidItem(
    @SerialName("answer")
    val answer: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("id")
    val id: Int,
    @SerialName("owner")
    val owner: OwnerX,
    @SerialName("owner_id")
    val ownerId: Int,
    @SerialName("question_id")
    val questionId: Int
)