package com.example.final_nitt.mainscreen.ExploreUsers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.final_nitt.Preference
import kotlinx.coroutines.flow.collectLatest


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreUsersScreen(
    modifier: Modifier = Modifier,
    padding: PaddingValues
) {
    val context = LocalContext.current
    val viewModel: ExploreUsersViewModel = viewModel(
        factory = ExploreUsersViewModelFactory(
            Preference(context)
        )
    )
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest {event->
            when(event){
                is ExploreEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        modifier = modifier.padding(padding),
        topBar = {
            TopAppBar(
                title = {
                    Text("Explore Users")
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)

        ) {
            TabRow(
                selectedTabIndex = state.selectedTab.ordinal
            ) {
                Tab(
                    selected = state.selectedTab == ExploreTab.DISCOVER,
                    onClick = {
                        viewModel.onEvent(
                            ExploreEvent.SelectTab(
                                ExploreTab.DISCOVER
                            )
                        )
                    },
                    text = {
                        Text("Discover")
                    }
                )
                Tab(
                    selected = state.selectedTab == ExploreTab.REQUESTS,
                    onClick = {
                        viewModel.onEvent(
                            ExploreEvent.SelectTab(
                                ExploreTab.REQUESTS
                            )
                        )
                    },
                    text = {
                        Text("Requests")
                    }
                )
            }
            when (state.selectedTab) {
                ExploreTab.DISCOVER -> {
                    DiscoverScreen(
                        users = state.filteredUsers,
                        text = state.searchQuery,
                        loading = state.requestLoading,
                        onFollowClick = { id ->
                            viewModel.onEvent(
                                ExploreEvent.SendFollowRequest(id)
                            )
                        },
                        onCancelRequestClick = { id ->
                            viewModel.onEvent(
                                ExploreEvent.RejectRequest(id)
                            )
                        }
                    ){
                        viewModel.onEvent(ExploreEvent.SearchChanged(it))
                    }
                }
                ExploreTab.REQUESTS -> {
                    RequestsScreen(
                        users = state.requests,
                        acceptLoading = state.acceptLoading,
                        rejectLoading = state.rejectLoading,
                        onAcceptClick = { id ->
                            viewModel.onEvent(
                                ExploreEvent.AcceptRequest(id)
                            )
                        },
                        onRejectClick = { id ->
                            viewModel.onEvent(
                                ExploreEvent.RejectRequest(id)
                            )
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}