package com.example.final_nitt

interface OnBoardDestination {
    val route: String
    val title: String
}

object SignIn: OnBoardDestination {
    override val route: String = "signin"
    override val title: String = "Sign In"
}

object Logs: OnBoardDestination {
    override val route: String = "login"
    override val title: String = "Log In"
}

object Home: OnBoardDestination {
    override val route: String = "home"
    override val title: String = "Home"
}
