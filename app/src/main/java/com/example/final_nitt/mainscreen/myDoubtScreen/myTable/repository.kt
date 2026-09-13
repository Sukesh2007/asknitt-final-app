package com.example.final_nitt.mainscreen.myDoubtScreen.myTable

import com.example.final_nitt.mainscreen.toEntity
import com.example.final_nitt.network.AllMyQuestionResult
import com.example.final_nitt.network.Network
import com.example.final_nitt.network.QuestionFormat
import kotlinx.coroutines.flow.Flow

class QuestionRepository {

    private val questionDao: QuestionDao
    private val ktorClient: Network

    constructor(questionDao: QuestionDao, ktorClient: Network) {
        this.questionDao = questionDao
        this.ktorClient = ktorClient
    }

    fun getLocalQuestions(): Flow<List<QuestionEntity>> {
        return questionDao.getAllQuestions()
    }

    suspend fun saveQuestions(
        questions: List<QuestionFormat>
    ) {
        val entities = questions.map {
            var find = ""
            if(!it.attachments.isEmpty()) find = it.attachments[0].fileName
            it.toEntity(find)
        }

        questionDao.upsertQuestions(entities)
    }

    suspend fun refreshQuestions(token: String) {
        when (val result = ktorClient.getMyQuestion(token)) {
            is AllMyQuestionResult.Success -> {
                saveQuestions(result.success)
            }
            is AllMyQuestionResult.Error -> {
                println("There is an error in the Database Refreshing Questions")
            }
        }
    }

    suspend fun updateSolvedStatus(
        questionId: Int,
        isSolved: Boolean
    ) {
        questionDao.updateSolvedStatus(
            questionId = questionId,
            isSolved = isSolved
        )
    }

    suspend fun clearQuestions() {
        questionDao.deleteAllQuestions()
    }
}