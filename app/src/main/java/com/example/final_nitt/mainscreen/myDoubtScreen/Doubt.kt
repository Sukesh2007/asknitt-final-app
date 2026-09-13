package com.example.final_nitt.mainscreen.myDoubtScreen

import com.example.final_nitt.mainscreen.myDoubtScreen.myTable.QuestionEntity
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
    val errorMessage: String? = null,

    val selectedFileUri: String? = null,

    val selectedFileName: String? = null,

    val selectedFileSize: Long? = null,

    val selectedFileBytes: ByteArray? = null,

    val selectedFileContentType: String? = null

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DoubtState

        if (isLoading != other.isLoading) return false
        if (isPostingQuestion != other.isPostingQuestion) return false
        if (isUpdatingSolvedStatus != other.isUpdatingSolvedStatus) return false
        if (showBottomSheet != other.showBottomSheet) return false
        if (selectedFileSize != other.selectedFileSize) return false
        if (questions != other.questions) return false
        if (search != other.search) return false
        if (question != other.question) return false
        if (tags != other.tags) return false
        if (errorMessage != other.errorMessage) return false
        if (selectedFileUri != other.selectedFileUri) return false
        if (selectedFileName != other.selectedFileName) return false
        if (!selectedFileBytes.contentEquals(other.selectedFileBytes)) return false
        if (filteredQuestions != other.filteredQuestions) return false

        return true
    }

    override fun hashCode(): Int {
        var result = isLoading.hashCode()
        result = 31 * result + isPostingQuestion.hashCode()
        result = 31 * result + isUpdatingSolvedStatus.hashCode()
        result = 31 * result + showBottomSheet.hashCode()
        result = 31 * result + (selectedFileSize?.hashCode() ?: 0)
        result = 31 * result + questions.hashCode()
        result = 31 * result + search.hashCode()
        result = 31 * result + question.hashCode()
        result = 31 * result + tags.hashCode()
        result = 31 * result + (errorMessage?.hashCode() ?: 0)
        result = 31 * result + (selectedFileUri?.hashCode() ?: 0)
        result = 31 * result + (selectedFileName?.hashCode() ?: 0)
        result = 31 * result + (selectedFileBytes?.contentHashCode() ?: 0)
        result = 31 * result + filteredQuestions.hashCode()
        return result
    }
}
