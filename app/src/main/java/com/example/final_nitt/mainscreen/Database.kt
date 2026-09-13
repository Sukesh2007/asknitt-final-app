package com.example.final_nitt.mainscreen

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.final_nitt.mainscreen.myDoubtScreen.myTable.QuestionDao
import com.example.final_nitt.mainscreen.myDoubtScreen.myTable.QuestionEntity
import com.example.final_nitt.mainscreen.myDoubtScreen.myTable.TagsConverter
import com.example.final_nitt.network.QuestionFormat

@Database(
    entities = [QuestionEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(TagsConverter::class)
abstract class AskNittDatabase : RoomDatabase() {
    abstract fun questionDao(): QuestionDao
}

fun QuestionFormat.toEntity(find: String = ""): QuestionEntity {
    return QuestionEntity(
        id = id,
        question = question,
        ownerId = owner.id,
        tags = tags,
        isSolved = isSolved,
        createdAt = createdAt,
        fileName = find
    )
}