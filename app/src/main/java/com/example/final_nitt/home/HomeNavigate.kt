package com.example.final_nitt.home

sealed class HomeNavigate {
    object ToSignInScreen: HomeNavigate()
    object ToLoginScreen: HomeNavigate()
}


