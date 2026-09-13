package com.example.final_nitt.mainscreen.myDoubtScreen.myTable

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {

    @Query("SELECT * FROM questions ORDER BY id DESC")
    fun getAllQuestions(): Flow<List<QuestionEntity>>

    @Upsert
    suspend fun upsertQuestions(
        questions: List<QuestionEntity>
    )

    @Query("""
        UPDATE questions
        SET isSolved = :isSolved
        WHERE id = :questionId
    """)
    suspend fun updateSolvedStatus(
        questionId: Int,
        isSolved: Boolean
    )

    @Query("DELETE FROM questions")
    suspend fun deleteAllQuestions()
}