package com.example.final_nitt.mainscreen.myDoubtScreen.myTable

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

@Entity(tableName = "questions")
data class QuestionEntity(

    @PrimaryKey
    val id: Int,

    val question: String,

    val ownerId: Int,

    val tags: List<String>,

    val isSolved: Boolean,

    val createdAt: String,

    val fileName: String
)

class TagsConverter {

    @TypeConverter
    fun fromTags(tags: List<String>): String {
        return tags.joinToString(",")
    }

    @TypeConverter
    fun toTags(value: String): List<String> {
        return if (value.isEmpty()) {
            emptyList()
        } else {
            value.split(",")
        }
    }
}