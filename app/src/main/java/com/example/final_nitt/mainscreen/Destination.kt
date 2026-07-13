package com.example.final_nitt.mainscreen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.SupervisedUserCircle
import androidx.compose.ui.graphics.vector.ImageVector

interface Destinations {
    val route: String
    val icon: ImageVector
    val title: String
}

object DashBoard: Destinations{
    override val route: String = "dashboard"
    override val title: String = "Dashboard"
    override val icon: ImageVector = Icons.Filled.Dashboard
}

object ExploreUser: Destinations {
    override val icon: ImageVector = Icons.Filled.SupervisedUserCircle
    override val title: String = "Explore User"
    override val route: String = "others"
}

object Doubt: Destinations {
    override val icon: ImageVector = Icons.Default.QuestionMark
    override val title: String = "Doubts"
    override val route: String = "doubt_session"
}

object Clarify: Destinations {
    override val icon: ImageVector = Icons.Default.QuestionAnswer
    override val route: String
        get() = "clarify"
    override val title: String
        get() = "Solve"
}