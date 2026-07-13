package com.example.final_nitt.mainscreen.otherDoubt

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun OtherAnswerQuestion(padding: PaddingValues){
    val navController = rememberNavController()
    val route by remember { mutableStateOf(OtherQuestionScreen.route) }
    NavHost(navController = navController, startDestination = route){
        composable(route = OtherQuestionScreen.route){
            OtherDoubt(padding, navController)
        }
        composable(route = OtherAnswerScreen.route){
            AnswerScreen(padding , navController)
        }
    }
}

interface OtherScreen{
    val route: String
}

object OtherQuestionScreen: OtherScreen{
    override val route: String
        get() = "otherQuestionScreen"
}

object OtherAnswerScreen:  OtherScreen{
    override val route: String
        get() = "otherAnswerScreen"
}