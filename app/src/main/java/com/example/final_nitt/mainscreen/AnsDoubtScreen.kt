package com.example.final_nitt.mainscreen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.final_nitt.mainscreen.answer.AnswerScreen
import com.example.final_nitt.mainscreen.myDoubtScreen.DoubtScreen
import android.os.Parcelable
import com.example.final_nitt.network.Attachment
import kotlinx.parcelize.Parcelize

@Composable
fun AnswerDoubtScreen(
    padding: PaddingValues
) {
    val navController2 = rememberNavController()

    NavHost(
        navController = navController2,
        startDestination = Question.route
    ) {

        composable(route = Question.route) {
            DoubtScreen(
                padding = padding,
                navController = navController2
            )
        }

        composable(route = Answer.route) {
            AnswerScreen(
                padding = padding,
                navController = navController2
            )
        }
    }
}

interface AnswerDoubt{
    val route: String
}

object Question : AnswerDoubt{
    override val route: String = "question"
}

object Answer: AnswerDoubt{
    override val route: String
        get() = "answer"
}

@Parcelize
data class QuestionPass(
    val id: Int,
    val questionNo: Int,
    val description: String,
    val created_at: String,
    val tags: String,
    val isSolved: Boolean,
    val attachment: List<Attachment>
) : Parcelable