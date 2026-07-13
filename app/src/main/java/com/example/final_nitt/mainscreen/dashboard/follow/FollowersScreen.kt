package com.example.final_nitt.mainscreen.dashboard.follow


import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.final_nitt.Preference
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FollowersScreen(
    padding: PaddingValues
) {

    val context = LocalContext.current
    val preference = Preference(context)

    val viewModel: FollowersViewModel = viewModel(
        factory = FollowersViewModelFactory(preference)
    )

    val state = viewModel.state.collectAsState()

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    LaunchedEffect(Unit) {

        viewModel.shared.collectLatest { event ->

            when (event) {

                is FollowersEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }

            }

        }

    }

    Scaffold(

        modifier = Modifier.padding(padding),

        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },

        topBar = {

            TopAppBar(
                title = {
                    Text(
                        text = "Followers (${state.value.users.size})"
                    )
                }
            )

        }

    ) { innerPadding ->

        if (state.value.isLoading) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {

                CircularProgressIndicator()

            }

        } else {

            LazyColumn(

                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),

                contentPadding = PaddingValues(vertical = 8.dp)

            ) {

                items(state.value.users) {user->

                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        elevation = CardDefaults.elevatedCardElevation(3.dp)
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = null,
                                modifier = Modifier.size(56.dp)
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Column {

                                Text(
                                    text = user.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = user.rollno,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray
                                )

                                Text(
                                    text = user.department,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )

                            }

                        }

                    }

                }

                }

            }

        }

    }