package com.example.final_nitt.mainscreen.dashboard.follow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.final_nitt.Preference
import com.example.final_nitt.network.FollowersResult
import com.example.final_nitt.network.Network
import com.example.final_nitt.network.Owner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class FollowingEvent {

    data object Refresh : FollowingEvent()

    data class Unfollow(
        val userId: Int
    ) : FollowingEvent()

}

data class FollowingState(
    val users: List<Owner> = emptyList(),
    val removingUsers: Set<Int> = emptySet(),
    val isLoading: Boolean = false
)

sealed class FollowingEffect {

    data class ShowSnackbar(
        val message: String
    ) : FollowingEffect()

}

class FollowersViewModelFactory(
    private val preference: Preference
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(FollowersViewModel::class.java)) {
            return FollowersViewModel(preference) as T
        }

        throw IllegalArgumentException("Unknown ViewModel")

    }
}

class FollowersViewModel(
    private val preference: Preference
) : ViewModel() {
    private val _state = MutableStateFlow(FollowersState())
    val state = _state.asStateFlow()
    private val _shared = MutableSharedFlow<FollowersEffect>()
    val shared = _shared.asSharedFlow()
    init {
        onEvent(FollowersEvent.Refresh)
    }
    fun onEvent(event: FollowersEvent) {
        when(event){
            FollowersEvent.Refresh -> {
                viewModelScope.launch(Dispatchers.IO){
                    _state.update {
                        it.copy(isLoading = true)
                    }
                    val response = Network.getFollowers(preference.getToken() ?: "")
                    when(response){
                        is FollowersResult.Error -> {
                            onEvent2(FollowersEffect.ShowSnackbar(response.message))
                        }
                        is FollowersResult.Success -> {
                            _state.update {
                                it.copy(
                                    users = response.message
                                )
                            }
                        }
                    }
                    _state.update {
                        it.copy(isLoading = false)
                    }
                }
            }
        }
    }

    private fun onEvent2(event: FollowersEffect){
        viewModelScope.launch {
            _shared.emit(event)
        }
    }
}

data class FollowersState(
    val users: List<Owner> = emptyList(),
    val isLoading: Boolean = false
)

sealed class FollowersEvent{
    data object Refresh: FollowersEvent()
}

sealed class FollowersEffect{
    data class ShowSnackbar(val message: String): FollowersEffect()
}