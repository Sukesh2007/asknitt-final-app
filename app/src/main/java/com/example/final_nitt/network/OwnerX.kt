package com.example.final_nitt.network


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OwnerX(
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("department")
    val department: String,
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("rollno")
    val rollno: String
)