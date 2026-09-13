package com.example.final_nitt.network


import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Attachment(
    val id: Int,
    @SerialName("question_id")
    val questionId: Int,
    @SerialName("file_name")
    val fileName: String,
    @SerialName("file_type")
    val fileType: String,
    @SerialName("file_size")
    val fileSize: Int,
    @SerialName("storage_path")
    val storagePath: String
) : Parcelable

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
    val isSolved: Boolean,
    @SerialName("attachment")
    val attachments: List<Attachment>
)

@Serializable
data class AttachmentAccessResponse(
    val id: Int,
    @SerialName("question_id")
    val questionId: Int,
    @SerialName("file_name")
    val fileName: String,
    @SerialName("file_type")
    val fileType: String,
    @SerialName("file_size")
    val fileSize: Int,
    @SerialName("url")
    val url: String
)