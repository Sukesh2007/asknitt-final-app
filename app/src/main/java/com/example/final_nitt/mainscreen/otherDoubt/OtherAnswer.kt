package com.example.final_nitt.mainscreen.otherDoubt

import android.content.ActivityNotFoundException
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.final_nitt.Preference
import com.example.final_nitt.mainscreen.QuestionPass
import com.example.final_nitt.mainscreen.answer.AnswerStateEvent
import com.example.final_nitt.mainscreen.answer.QuestionHeader
import com.example.final_nitt.network.AnswersForQidItem
import kotlinx.coroutines.flow.collectLatest


@Composable
fun AnswerScreen(
    paddingValues: PaddingValues,
    navController: NavController
) {
    val content = LocalContext.current
    val preference =  Preference(content.applicationContext)
    val question = navController
        .previousBackStackEntry
        ?.savedStateHandle
        ?.get<QuestionPass>("question")

    val viewModel: OtherAnswerViewModel = viewModel(
        factory = OtherAnswerViewModelFactory(preference, question?.id ?: 0)
    )
    val state = viewModel.state.collectAsState()
    LaunchedEffect(state.value.answers) {
        state.value.answers.forEach{item->
            viewModel.onEvent(OtherAnswerEvent.CountVotes(item.id))
        }
    }
    val context = LocalContext.current
    val listState = rememberLazyListState()
    val snackBarHost = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        viewModel.shared.collectLatest { event->
            when(event){
                OtherAnswerEffect.ScrollToBottom -> {
                    listState.animateScrollToItem(
                        state.value.answers.lastIndex
                    )
                }
                is OtherAnswerEffect.ShowSnackbar -> {
                    snackBarHost.showSnackbar(event.message)
                }

                is OtherAnswerEffect.OpenFile -> {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        event.url.toUri()
                    )

                    try {
                        context.startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(
                            context,
                            "No PDF viewer found",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHost)},
        modifier = Modifier.padding(paddingValues),

        bottomBar = {

            androidx.compose.material3.Surface(
                shadowElevation = 8.dp
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    OutlinedTextField(
                        value = state.value.answerText,
                        onValueChange = {
                            viewModel.onEvent(OtherAnswerEvent.OnAnswerChanged(it))
                        },
                        modifier = Modifier.weight(1f),
                        placeholder = {
                            Text("Write your answer...")
                        },
                        maxLines = 3
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    FilledIconButton(
                        onClick = { viewModel.onEvent(OtherAnswerEvent.SendAnswer(state.value.answerText, question?.id ?: 0)) }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send"
                        )
                    }

                }

            }

        }

    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {



            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(
                    bottom = 12.dp
                ),
                state = listState
            ) {
                stickyHeader {
                    QuestionHeader(
                        questionNo = question?.id ?: 0,
                        createdAt = question?.created_at ?: "Unknown",
                        question = question?.description ?: "",
                        tags = question?.tags ?: "",
                        answerCount = 10,
                        isSolved = if(question?.isSolved == null) false else question.isSolved,
                        attachment = question?.attachment ?: emptyList()
                    ){file->
                        viewModel.onEvent(
                            OtherAnswerEvent.OpenAttachment(
                                attachmentId = file.id
                            )
                        )
                    }
                }

                itemsIndexed(state.value.answers){index, item->
                    AnswerCard(
                        answer = item,
                        currentVote = state.value.userVotes[item.id] ?: 0,
                        voteCount = state.value.userVotes[item.id] ?: 0,
                        isVoting = item.id in state.value.votingAnswers,
                        onUpVote = {
                            viewModel.onEvent(OtherAnswerEvent.CastVote(item.id, 1))
                        },
                        onDownVote = {
                            viewModel.onEvent(OtherAnswerEvent.CastVote(item.id, -1))
                        }
                    )
                }

            }

        }

    }

}

//AnswerCard(
//answer = item,
//currentVote = state.userVotes[item.id] ?: item.voteCount,
//isVoting = item.id in state.votingAnswers,
//onUpVote = {
//    onUpVote(item.id)
//},
//onDownVote = {
//    onDownVote(item.id)
//}
//)

@Composable
fun AnswerCard(
    answer: AnswersForQidItem,
    currentVote: Int,
    voteCount: Int,
    isVoting: Boolean,
    onUpVote: () -> Unit,
    onDownVote: () -> Unit
) {

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {

            // Vote Section
            Column(
                modifier = Modifier.width(48.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                if (isVoting) {

                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        strokeWidth = 2.dp
                    )

                } else {

                    IconButton(
                        onClick = onUpVote
                    ) {

                        Icon(
                            Icons.Default.KeyboardArrowUp,
                            contentDescription = "Upvote",
                            tint = if (currentVote == 1)
                                Color(0xFFFF9800)
                            else
                                Color.Gray
                        )

                    }

                    Text(
                        text = voteCount.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(
                        onClick = onDownVote
                    ) {

                        Icon(
                            Icons.Default.KeyboardArrowDown,
                            contentDescription = "Downvote",
                            tint = if (currentVote == -1)
                                Color.Blue
                            else
                                Color.Gray
                        )

                    }

                }

            }

            Spacer(modifier = Modifier.width(12.dp))

            // Answer Content
            Column(
                modifier = Modifier.weight(1f)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Column {

                        Text(
                            text = answer.owner.name,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = answer.owner.rollno,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )

                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = answer.createdAt,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )

                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 10.dp)
                )

                Text(
                    text = answer.answer,
                    style = MaterialTheme.typography.bodyLarge
                )

            }

        }

    }

}