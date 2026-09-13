package com.example.final_nitt.mainscreen.answer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.final_nitt.Preference
import com.example.final_nitt.network.AttachmentAccessResult
import com.example.final_nitt.network.GetAnswerResult
import com.example.final_nitt.network.Network
import com.example.final_nitt.network.Network.getAnswer
import com.example.final_nitt.network.Network.getVotes
import com.example.final_nitt.onBoarding.SignInViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AnswerViewModelFactory(
    private val preference: Preference, val qid: Int
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(AnswerViewModel::class.java)) {
            return AnswerViewModel(preference, qid) as T
        }

        throw IllegalArgumentException("Unknown ViewModel")
    }
}

class AnswerViewModel(private val preference: Preference, qid: Int) : ViewModel(){
    private val _state = MutableStateFlow(AnswersState())
    val state = _state.asStateFlow()

    private val _shared = MutableSharedFlow<AnswerEffect>()
    val shared = _shared.asSharedFlow()

    init{
        onEvent(AnswerStateEvent.Refresh(qid))
    }

    fun onEvent(event: AnswerStateEvent){
        when(event) {
            is AnswerStateEvent.OpenAttachment -> {
                viewModelScope.launch(Dispatchers.IO) {

                    val token = preference.getToken()

                    val response = Network.getAttachment(
                        token ?: "",
                        event.attachmentId
                    )

                    when (response) {

                        is AttachmentAccessResult.Success -> {
                            _shared.emit(
                                AnswerEffect.OpenFile(
                                    response.success.url
                                )
                            )
                        }

                        is AttachmentAccessResult.Error -> {
                            _shared.emit(
                                AnswerEffect.ShowMessage(
                                    response.message ?: "Failed to open file"
                                )
                            )
                        }
                    }
                }
            }
            is AnswerStateEvent.GetVotes -> {
                viewModelScope.launch(Dispatchers.IO){
                    val vote = try{
                        getVotes(event.aid)?.toInt() ?: 0
                    }catch(e: Exception){
                        println("transffering: ${e.message}")
                        0
                    }
                    _state.update { state ->
                        state.copy(
                            mapped = _state.value.mapped + (event.aid to vote)
                        )
                    }
                }
            }
            is AnswerStateEvent.Refresh -> {
                viewModelScope.launch(Dispatchers.IO){
                    val response = getAnswer(event.qid)
                    if(response is GetAnswerResult.Success){
                        _state.update {
                            it.copy(answers = response.message)
                        }
                    }
                }
            }
        }
    }
}