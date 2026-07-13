package com.example.final_nitt.mainscreen.answer

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.final_nitt.Preference
import com.example.final_nitt.mainscreen.Question
import com.example.final_nitt.mainscreen.QuestionPass

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnswerScreen(padding: PaddingValues, navController: NavHostController){
    val question = navController
        .previousBackStackEntry
        ?.savedStateHandle
        ?.get<QuestionPass>("question")
    val context = LocalContext.current
    val preference = remember {
        Preference(context.applicationContext)
    }
    val viewModel: AnswerViewModel = viewModel(
        factory = AnswerViewModelFactory(preference, question!!.id)
    )
    val state = viewModel.state.collectAsState()
    LaunchedEffect(state.value.answers) {
        state.value.answers.forEach {
            viewModel.onEvent(AnswerStateEvent.GetVotes(it.id))
        }
    }
    Scaffold(
        modifier = Modifier.padding(padding),
        topBar = {
            TopAppBar(
                title = {
                    androidx.compose.material3.Text(
                        text = "Answer Screen",
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate(Question.route)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Blue,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )

        },
        containerColor = Color.White,
    ){padding->
        LazyColumn(modifier = Modifier.padding(padding).fillMaxSize()){
            stickyHeader { QuestionHeader(question.questionNo, question.created_at, question.description, question.tags, 0, question.isSolved) }
            itemsIndexed(state.value.answers) {index, item->
                AnswerCard(item.owner.name, item.createdAt, item.answer,state.value.mapped[item.id] ?: 0, (state.value.mapped[item.id] ?: 0) > 0)
            }
        }
    }
}