package com.example.final_nitt.mainscreen.ExploreUsers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.final_nitt.mainscreen.ExploreUsers.ExploreEffect.ShowSnackbar
import com.example.final_nitt.Preference
import com.example.final_nitt.network.DiscoverUser
import com.example.final_nitt.network.GetDiscoverUsersResult
import com.example.final_nitt.network.GetFollowRequestsResult
import com.example.final_nitt.network.Network
import com.example.final_nitt.network.Network.getDiscoverUsers
import com.example.final_nitt.network.Owner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ExploreUsersViewModelFactory(
    private val preference: Preference
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(ExploreUsersViewModel::class.java)) {
            return ExploreUsersViewModel(preference) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class ExploreUsersViewModel(
    private val preference: Preference
) : ViewModel() {
    private val _state = MutableStateFlow(ExploreUsersState())
    val state = _state.asStateFlow()
    private val _effect = MutableSharedFlow<ExploreEffect>()
    val effect = _effect.asSharedFlow()
    init {
        onEvent(ExploreEvent.Refresh)
        onEvent(ExploreEvent.RefreshRequest)
    }
    fun onEvent(event: ExploreEvent){
        when(event){
            is ExploreEvent.AcceptRequest -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _state.update {
                        it.copy(
                            acceptLoading = it.acceptLoading + event.userId
                        )
                    }
                    try {
                        val response = Network.acceptFollowRequest(
                            preference.getToken() ?: "",
                            event.userId
                        )
                        if (response["detail"]?.contains("Request Taken", ignoreCase = true) == true) {
                            _state.update {
                                it.copy(
                                    requests = it.requests.filter { user ->
                                        user.id != event.userId
                                    }
                                )
                            }
                            onEvent2(
                                ShowSnackbar("Follow request accepted")
                            )
                        } else {
                            onEvent2(
                                ShowSnackbar(
                                    response["detail"] ?: "Something went wrong"
                                )
                            )
                        }
                    } catch (e: Exception) {
                        onEvent2(
                            ShowSnackbar(
                                e.message ?: "Something went wrong"
                            )
                        )
                    } finally {
                        _state.update {
                            it.copy(
                                acceptLoading = it.acceptLoading - event.userId
                            )
                        }
                    }
                }
            }
            ExploreEvent.Refresh -> {
                viewModelScope.launch(Dispatchers.IO){
                    _state.update {
                        it.copy(isLoading = true)
                    }
                    val response = getDiscoverUsers(preference.getToken() ?: "")
                    if(response is GetDiscoverUsersResult.Error){
                        onEvent2(ShowSnackbar(response.error))
                    }else if(response is GetDiscoverUsersResult.Success){
                        delay(2000)
                        _state.update {
                            it.copy(discoverUsers = response.users)
                        }
                    }
                    _state.update{
                        it.copy(isLoading = false)
                    }
                }
            }
            is ExploreEvent.RejectRequest -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _state.update {
                        it.copy(
                            rejectLoading = it.rejectLoading + event.userId
                        )
                    }
                    try {
                        val response = Network.rejectFollowRequest(
                            preference.getToken() ?: "",
                            event.userId
                        )
                        if (response["detail"]?.contains("rejected", ignoreCase = true) == true) {
                            _state.update {
                                it.copy(
                                    requests = it.requests.filter { user ->
                                        user.id != event.userId
                                    }
                                )
                            }
                        } else {
                            onEvent2(
                                ShowSnackbar(
                                    response["detail"] ?: "Something went wrong"
                                )
                            )
                        }
                    } catch (e: Exception) {
                        onEvent2(
                            ShowSnackbar(
                                e.message ?: "Something went wrong"
                            )
                        )
                    } finally {
                        _state.update {
                            it.copy(
                                rejectLoading = it.rejectLoading - event.userId
                            )
                        }
                    }
                }
            }
            is ExploreEvent.SearchChanged -> {
                _state.update {
                    it.copy(searchQuery = event.query)
                }
            }
            is ExploreEvent.SelectTab -> {
                _state.update {
                    it.copy(selectedTab = event.tab)
                }
            }
            is ExploreEvent.SendFollowRequest -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _state.update {
                        it.copy(
                            requestLoading = it.requestLoading + event.userId
                        )
                    }
                    try {
                        val response = Network.requestFollow(
                            preference.getToken() ?: "",
                            event.userId
                        )
                        if (response["detail"]!!.contains("requested")) {
                            _state.update {
                                it.copy(
                                    discoverUsers = it.discoverUsers.map { user ->
                                        if (user.id == event.userId)
                                            user.copy(followStatus = "pending")
                                        else
                                            user
                                    }
                                )
                            }
                        } else {
                            onEvent2(
                                ShowSnackbar(
                                    response["detail"] ?: ""
                                )
                            )
                        }
                    } catch (e: Exception) {
                        onEvent2(
                            ShowSnackbar(
                                e.message ?: "Something went wrong"
                            )
                        )
                    } finally {
                        _state.update {
                            it.copy(
                                requestLoading = it.requestLoading - event.userId
                            )
                        }
                    }
                }
            }
            ExploreEvent.RefreshRequest -> {
                    viewModelScope.launch(Dispatchers.IO) {
                        _state.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                        when (
                            val response = Network.getFollowRequests(
                                preference.getToken() ?: ""
                            )
                        ) {
                            is GetFollowRequestsResult.Success -> {
                                _state.update {
                                    it.copy(
                                        requests = response.users,
                                        isLoading = false
                                    )
                                }
                            }

                            is GetFollowRequestsResult.Error -> {
                                _state.update {
                                    it.copy(
                                        isLoading = false
                                    )
                                }
                                onEvent2(
                                    ShowSnackbar(
                                        response.error
                                    )
                                )
                            }
                        }
                    }

            }
        }
    }
    private fun onEvent2(event: ExploreEffect){
        viewModelScope.launch {
            _effect.emit(event)
        }
    }
}

enum class ExploreTab {
    DISCOVER,
    REQUESTS
}

data class ExploreUsersState(
    val selectedTab: ExploreTab = ExploreTab.DISCOVER,
    val searchQuery: String = "",
    val discoverUsers: List<DiscoverUser> = emptyList(),
    val requests: List<Owner> = emptyList(),
    val requestLoading : Set<Int> = emptySet<Int>(),
    val rejectLoading : Set<Int> = emptySet<Int>(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val acceptLoading: Set<Int> = emptySet<Int>()
){
    val filteredUsers: List<DiscoverUser> = discoverUsers.filter{
        searchQuery in it.name
    }
}

sealed interface ExploreEvent {

    data class SelectTab(
        val tab: ExploreTab
    ) : ExploreEvent

    data class SearchChanged(
        val query: String
    ) : ExploreEvent

    data class SendFollowRequest(
        val userId: Int
    ) : ExploreEvent

    data class AcceptRequest(
        val userId: Int
    ) : ExploreEvent

    data class RejectRequest(
        val userId: Int
    ) : ExploreEvent

    object Refresh : ExploreEvent
    object RefreshRequest: ExploreEvent
}

sealed interface ExploreEffect {

    data class ShowSnackbar(
        val message: String
    ) : ExploreEffect
}