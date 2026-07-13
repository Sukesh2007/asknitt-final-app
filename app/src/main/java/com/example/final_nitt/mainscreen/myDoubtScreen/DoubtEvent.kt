package com.example.final_nitt.mainscreen.myDoubtScreen

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