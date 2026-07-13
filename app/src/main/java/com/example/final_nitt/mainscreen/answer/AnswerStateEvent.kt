package com.example.final_nitt.mainscreen.answer

import com.example.final_nitt.network.AnswersForQidItem
import com.example.final_nitt.network.QuestionFormat

data class AnswersState(
    val isLoading: Boolean = false,

    // Selected Question
    val question: QuestionFormat? = null,

    // Answers
    val answers: List<AnswersForQidItem> = emptyList(),

    // Error message
    val error: String? = null,

    // Pull to refresh
    val isRefreshing: Boolean = false,

    // Bottom sheet / dialog
    val showSortBottomSheet: Boolean = false,

    // User input (if users can answer from this screen)
    val answerText: String = "",

    val mapped: Map<Int, Int> = emptyMap<Int, Int>()
)

sealed class AnswerStateEvent{
    data class Refresh(val qid: Int): AnswerStateEvent()
    data class GetVotes(val aid: Int): AnswerStateEvent()

}