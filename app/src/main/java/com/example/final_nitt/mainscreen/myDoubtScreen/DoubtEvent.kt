package com.example.final_nitt.mainscreen.myDoubtScreen

import io.ktor.http.content.OutgoingContent

sealed interface DoubtEvent {

    /**
     * User types in the search TextField.
     */
    data class OnSearchChanged(
        val search: String
    ) : DoubtEvent

    /**
     * User types the question.
     */
    data class OnQuestionChanged(
        val question: String
    ) : DoubtEvent

    /**package com.example.final_nitt.mainscreen.myDoubtScreen

    sealed interface DoubtEvent {

    /**
     * User types in the search TextField.
    */
    data class OnSearchChanged(
    val search: String
    ) : DoubtEvent

    /**
     * User types the question.
    */
    data class OnQuestionChanged(
    val question: String
    ) : DoubtEvent

    /**
     * User types tags.
    */
    data class OnTagsChanged(
    val tags: String
    ) : DoubtEvent

    /**
     * Show the bottom sheet.
    */
    data object ShowBottomSheet : DoubtEvent

    /**
     * Hide the bottom sheet.
    */
    data object HideBottomSheet : DoubtEvent

    /**
     * User clicks the Send button.
    */
    data object SendQuestion : DoubtEvent

    /**
     * Reload all questions.
    */
    data object Refresh : DoubtEvent

    /**
     * Mark a question as solved / unsolved.
    */
    data class ChangeSolvedStatus(val qid: Int , val isSolved: Boolean): DoubtEvent
    }

    sealed interface DoubtEffect {

    /**
     * Display a Toast/Snackbar.
    */
    data class ShowMessage(
    val message: String
    ) : DoubtEffect

    /**
     * Scroll LazyColumn to the latest question.
    */
    data object ScrollToBottom : DoubtEffect

    /**
     * Close the ModalBottomSheet.
    */

    }


     * User types tags.
     */
    data class OnTagsChanged(
        val tags: String
    ) : DoubtEvent

    /**
     * Show the bottom sheet.
     */
    data object ShowBottomSheet : DoubtEvent

    /**
     * Hide the bottom sheet.
     */
    data object HideBottomSheet : DoubtEvent

    /**
     * User clicks the Send button.
     */
    data object SendQuestion : DoubtEvent

    /**
     * Reload all questions.
     */
    data object Refresh : DoubtEvent

    /**
     * Mark a question as solved / unsolved.
     */
    data class ChangeSolvedStatus(val qid: Int , val isSolved: Boolean): DoubtEvent

    data class OnFileSelected(
        val uri: String,
        val fileName: String,
        val fileSize: Long,
        val fileBytes: ByteArray,
        val contentType: String
    ) : DoubtEvent {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as OnFileSelected

            if (fileSize != other.fileSize) return false
            if (uri != other.uri) return false
            if (fileName != other.fileName) return false
            if (!fileBytes.contentEquals(other.fileBytes)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = fileSize.hashCode()
            result = 31 * result + uri.hashCode()
            result = 31 * result + fileName.hashCode()
            result = 31 * result + fileBytes.contentHashCode()
            return result
        }
    }

    data object RemoveFile : DoubtEvent
}

sealed interface DoubtEffect {
    /**
     * Display a Toast/Snackbar.
     */
    data class ShowMessage(
        val message: String
    ) : DoubtEffect
    /**
     * Scroll LazyColumn to the latest question.
     */
    data object ScrollToBottom : DoubtEffect
    /**
     * Close the ModalBottomSheet.
     */
}