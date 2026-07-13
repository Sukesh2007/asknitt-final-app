package com.example.final_nitt.mainscreen.myDoubtScreen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.final_nitt.Preference
import com.example.final_nitt.mainscreen.Answer
import com.example.final_nitt.mainscreen.QuestionPass
import com.example.final_nitt.network.QuestionPost
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoubtScreen(padding: PaddingValues, navController: NavHostController){
    val context = LocalContext.current
    val preference = remember {
        Preference(context.applicationContext)
    }
    val viewModel: DoubtViewModel = viewModel(
        factory = DoubtViewModelFactory(preference)
    )
    val state = viewModel.state.collectAsState()
    val listState = rememberLazyListState()

    if(state.value.showBottomSheet){


            ModalBottomSheet(
                onDismissRequest = { viewModel.onEvent(DoubtEvent.HideBottomSheet) },
                dragHandle = {
                    BottomSheetDefaults.DragHandle()
                },
                modifier = Modifier.padding(padding)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .imePadding()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    Text(
                        text = "Ask a Question",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = state.value.question,
                        onValueChange = {viewModel.onEvent(DoubtEvent.OnQuestionChanged(it))},
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        label = {
                            Text("Question")
                        },
                        placeholder = {
                            Text("Describe your doubt...")
                        },
                        maxLines = 8
                    )

                    OutlinedTextField(
                        value = state.value.tags,
                        onValueChange = {viewModel.onEvent(DoubtEvent.OnTagsChanged(it))},
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text("Tags")
                        },
                        placeholder = {
                            Text("Example: kotlin compose android")
                        },
                        supportingText = {
                            Text("Separate tags using spaces")
                        },
                        singleLine = true
                    )

                    Button(
                        onClick = {viewModel.onEvent(DoubtEvent.SendQuestion)},
                        enabled = !state.value.isPostingQuestion,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {

                        if (state.value.isPostingQuestion) {

                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )

                        } else {

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = null
                            )

                            Spacer(Modifier.width(8.dp))

                            Text("Send")

                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

    }
    LaunchedEffect(Unit) {
        viewModel.shared.collectLatest {
            when(it){
                DoubtEffect.ScrollToBottom -> {
                    listState.animateScrollToItem(
                        state.value.questions.lastIndex
                    )
                }
                is DoubtEffect.ShowMessage -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    Column(modifier = Modifier.padding(padding).fillMaxSize().padding(3.dp)){
        TextField(
            value = state.value.search,
            onValueChange = {
                viewModel.onEvent(DoubtEvent.OnSearchChanged(it))
            },
            label = {Text("Enter Tag")},
            placeholder = {Text("atmost 4")},
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.padding(top = 20.dp, start = 10.dp, end = 10.dp).fillMaxWidth()
        )
        Box(modifier = Modifier.weight(1f).fillMaxWidth()){
            LazyColumn(state = listState, modifier = Modifier.fillMaxSize()){
                itemsIndexed(state.value.filteredQuestions){ index, item->
                    val p: String = item.tags.reduce {acc, curr->
                        "$acc $curr"
                    }
                    val taken : (String) -> Unit = { st: String->
                        if((st == "Solved" && item.isSolved == false ) || (st == "UnSolved" && item.isSolved == true)){
                            viewModel.onEvent(DoubtEvent.ChangeSolvedStatus(item.id, st == "Solved"))
                        }
                    }
                    val press: () -> Unit = {
                        val question = QuestionPass(id = item.id, questionNo = index+1, description = item.question, created_at = item.createdAt, tags = p, isSolved = item.isSolved)
                        navController.currentBackStackEntry?.savedStateHandle?.set("question", question)
                        navController.navigate(Answer.route)
                    }
                    CardQuestion(index + 1, item.createdAt, item.question, p, 10, item.isSolved, taken, press)
                }

            }
            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                onClick = {
                    viewModel.onEvent(DoubtEvent.ShowBottomSheet)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Question"
                )
            }

            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp),
                onClick = {
                    viewModel.onEvent2(DoubtEffect.ScrollToBottom)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowDownward,
                    contentDescription = "Scroll to Bottom"
                )
            }
        }
    }


}