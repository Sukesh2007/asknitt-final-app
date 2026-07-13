package com.example.final_nitt.mainscreen.dashboard.follow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.final_nitt.Preference
import com.example.final_nitt.network.FollowingResult
import com.example.final_nitt.network.Network
import com.example.final_nitt.network.UnfollowResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FollowingViewModelFactory(
    private val preference: Preference
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(FollowingViewModel::class.java)) {
            return FollowingViewModel(preference) as T
        }

        throw IllegalArgumentException("Unknown ViewModel")

    }
}

class FollowingViewModel(
    private val preference: Preference
) : ViewModel() {

    private val _state = MutableStateFlow(FollowingState())
    val state = _state.asStateFlow()

    private val _shared = MutableSharedFlow<FollowingEffect>()
    val shared = _shared.asSharedFlow()

    init {
        onEvent(FollowingEvent.Refresh)
    }

    fun onEvent(event: FollowingEvent) {

        when (event) {

            FollowingEvent.Refresh -> {

                viewModelScope.launch(Dispatchers.IO) {

                    _state.update {
                        it.copy(isLoading = true)
                    }

                    when (val response = Network.getFollowing(preference.getToken() ?: "")) {

                        is FollowingResult.Success -> {

                            _state.update {
                                it.copy(
                                    users = response.message,
                                    isLoading = false
                                )
                            }

                        }

                        is FollowingResult.Error -> {

                            _state.update {
                                it.copy(isLoading = false)
                            }

                            onEffect(
                                FollowingEffect.ShowSnackbar(response.message)
                            )

                        }

                    }

                }

            }

            is FollowingEvent.Unfollow -> {

                viewModelScope.launch(Dispatchers.IO) {

                    _state.update {
                        it.copy(
                            removingUsers = it.removingUsers + event.userId
                        )
                    }

                    val response = Network.unfollow(
                        token = preference.getToken() ?: "",
                        userId = event.userId
                    )

                    if (response is UnfollowResult.Success) {

                        _state.update {

                            it.copy(
                                users = it.users.filter { user ->
                                    user.id != event.userId
                                },
                                removingUsers = it.removingUsers - event.userId
                            )

                        }

                        onEffect(
                            FollowingEffect.ShowSnackbar(response.message)
                        )

                    } else if (response is UnfollowResult.Error) {

                        _state.update {

                            it.copy(
                                removingUsers = it.removingUsers - event.userId
                            )

                        }

                        onEffect(
                            FollowingEffect.ShowSnackbar(response.message)
                        )

                    }

                }

            }

        }

    }

    private fun onEffect(
        effect: FollowingEffect
    ) {

        viewModelScope.launch {
            _shared.emit(effect)
        }

    }

}