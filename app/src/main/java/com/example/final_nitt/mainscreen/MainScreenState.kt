package com.example.final_nitt.mainscreen

data class MainScreenState(
    val username: String = "",
    val rollNo: String = "",
    val department : String = "",
    val followers: Int = 0,
    val following: Int = 0,
    val isLoading: Boolean = false,
    val token: String = ""
)