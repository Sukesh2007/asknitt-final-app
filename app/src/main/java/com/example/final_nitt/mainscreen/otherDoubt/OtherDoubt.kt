package com.example.final_nitt.mainscreen.otherDoubt

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.final_nitt.Preference
import com.example.final_nitt.mainscreen.QuestionPass
import com.example.final_nitt.network.OtherUserQuestions
import kotlinx.coroutines.flow.collectLatest

@Composable
fun OtherDoubt(padding: PaddingValues, navController: NavController){
    val context = LocalContext.current
    val preference = Preference(context.applicationContext)
    val viewModel: OtherQuestionViewModel = viewModel(
        factory = OtherQuestionViewModelFactory(preference)
    )
    val snackbarHostState = remember { SnackbarHostState() }
    val state = viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.shared.collectLatest {event->
            when(event){
                is OtherQuestionEffect.SnackBar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            modifier = Modifier.padding(padding)
        ) { innerPadding ->

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                state.value.questions.forEach {itemp->
                    val qc = itemp.questions.size
                    stickyHeader {
                        UserStickyHeader(itemp.name, itemp.rollno, qc)
                    }
                    itemsIndexed(itemp.questions){ index, item->
                        val p = item.tags.reduce { acc, curr->
                            "$acc $curr"
                        }
                        QuestionCard(title = "Question ${index+1}", item.question, item.tags, item.createdAt) {
                            val question = QuestionPass(id = item.id, questionNo = index+1, description = item.question, created_at = item.createdAt, tags = p, isSolved = item.isSolved, attachment = item.attachments)
                            navController.currentBackStackEntry?.savedStateHandle?.set("question", question)
                            navController.navigate(OtherAnswerScreen.route)
                        }
                    }
                }
            }
        }
}