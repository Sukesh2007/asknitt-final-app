package com.example.final_nitt.onBoarding

data class OnBoardUiState(
    var username : String = "",
    var password: String = "",
    var rollNo : String = "",
    var isLoading: Boolean = false,
    var department : String = ""
)

sealed class OnBoardUiEvent{
    data class SetUserName(val username: String = ""): OnBoardUiEvent()
    data class SetPassword(val password: String = ""): OnBoardUiEvent()
    data class SetRollNo(val rollNo: String = "") : OnBoardUiEvent()
    data class SetDepartment(val department: String = "") : OnBoardUiEvent()
    object ClickSignUp : OnBoardUiEvent()
    object ClickLogIn: OnBoardUiEvent()
}

sealed class OneTimeOnBoardEvent{
    data class ShowToast(val message: String): OneTimeOnBoardEvent()
    data object NavigateToLoginScreen: OneTimeOnBoardEvent()
    data object NavigateToDashboardScreen: OneTimeOnBoardEvent()
    data object NavigateToSignInScreen: OneTimeOnBoardEvent()
}