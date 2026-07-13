package com.example.final_nitt.mainscreen.otherDoubt


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.final_nitt.Preference
import com.example.final_nitt.network.Network
import com.example.final_nitt.network.OtherUserQuestionResult
import com.example.final_nitt.network.OtherUserQuestions
import com.example.final_nitt.network.OtherUserQuestionsItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OtherQuestionViewModelFactory(
    private val preference: Preference
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(OtherQuestionViewModel::class.java)) {
            return OtherQuestionViewModel(preference) as T
        }

        throw IllegalArgumentException("Unknown ViewModel")
    }
}

class OtherQuestionViewModel(private val preference: Preference): ViewModel(){
    private val _state = MutableStateFlow<State>(State())
    val state = _state.asStateFlow()
    private val _shared = MutableSharedFlow<OtherQuestionEffect>()
    val shared = _shared.asSharedFlow()
    init {
        onEvent(OtherQuestionEvent.Refresh(preference.getToken() ?: ""))
    }
    fun onEvent(event: OtherQuestionEvent){
        if(event is OtherQuestionEvent.Refresh) {
            viewModelScope.launch(Dispatchers.IO) {
                _state.update {
                    it.copy(isLoading = true)
                }
                val response = Network.getOtherUserQuestion(event.token)
                if (response is OtherUserQuestionResult.Success) {
                    _state.update {
                        it.copy(questions = response.message)
                    }
                } else if (response is OtherUserQuestionResult.Error) {
                    onEvent(OtherQuestionEffect.SnackBar(response.message))
                }
                delay(2000)
                _state.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

    fun onEvent(event: OtherQuestionEffect){
        viewModelScope.launch {
            _shared.emit(event)
        }
    }
}

sealed class OtherQuestionEvent{
    data class Refresh(val token: String): OtherQuestionEvent()
}

sealed class OtherQuestionEffect{
    data class SnackBar(val message: String): OtherQuestionEffect()
}

data class State(
    val questions: OtherUserQuestions = listOf<OtherUserQuestionsItem>(),
    val isLoading: Boolean = false
)

