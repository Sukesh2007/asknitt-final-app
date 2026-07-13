package com.example.final_nitt.mainscreen.myDoubtScreen

import com.example.final_nitt.network.QuestionFormat

data class DoubtState(

    // Questions fetched from the backend
    val questions: List<QuestionFormat> = emptyList(),

    // Search text
    val search: String = "",

    // Question being typed
    val question: String = "",

    // Tags being typed
    val tags: String = "",

    // Loading state while fetching questions
    val isLoading: Boolean = false,

    // Loading state while posting a question
    val isPostingQuestion: Boolean = false,

    // Loading state while updating solved status
    val isUpdatingSolvedStatus: Boolean = false,

    // Whether the bottom sheet is visible
    val showBottomSheet: Boolean = false,

    // Error message (if any)
    val errorMessage: String? = null
) {

    /**
     * Filtered list shown in LazyColumn.
     * Computed from questions + search.
     */
    val filteredQuestions: List<QuestionFormat>
        get() {
            if (search.isBlank()) return questions

            return questions.filter { question ->
                question.tags.any {
                    it.contains(search, ignoreCase = true)
                }
            }
        }
}
