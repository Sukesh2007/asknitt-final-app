package com.example.final_nitt.mainscreen.myDoubtScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.final_nitt.Preference
import com.example.final_nitt.network.AllMyQuestionResult
import com.example.final_nitt.network.Network
import com.example.final_nitt.network.Network.solveQuestion
import com.example.final_nitt.network.QuestionPost
import com.example.final_nitt.network.QuestionResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DoubtViewModelFactory(
    private val preference: Preference
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(DoubtViewModel::class.java)) {
            return DoubtViewModel(preference) as T
        }

        throw IllegalArgumentException("Unknown ViewModel")
    }
}

class DoubtViewModel(private val preference: Preference): ViewModel() {
    private val _state = MutableStateFlow<DoubtState>(DoubtState())
    val state = _state.asStateFlow()

    private val _shared = MutableSharedFlow<DoubtEffect>()
    val shared = _shared.asSharedFlow()

    init{
        onEvent(DoubtEvent.Refresh)
    }

    fun onEvent(event: DoubtEvent){
        when(event) {
            is DoubtEvent.ChangeSolvedStatus -> {
                _state.update {
                    it.copy(isUpdatingSolvedStatus = true)
                }
                viewModelScope.launch(Dispatchers.IO){

                        val token = preference.getToken()
                        val response = solveQuestion(token ?: "", event.qid)
                        if(response != "Success"){
                            onEvent2(DoubtEffect.ShowMessage(response))
                        }else{
                            _state.update { state ->
                                state.copy(
                                    questions = state.questions.map { question ->
                                        if (question.id == event.qid) {
                                            question.copy(isSolved = event.isSolved)
                                        } else {
                                            question
                                        }
                                    }
                                )
                            }
                        }

                    delay(2000)
                    _state.update {
                        it.copy(isUpdatingSolvedStatus = false)
                    }
                }

            }
            DoubtEvent.HideBottomSheet -> {
                _state.update{
                    it.copy(showBottomSheet = false)
                }
            }
            is DoubtEvent.OnQuestionChanged -> {
                _state.update{
                    it.copy(question = event.question)
                }
            }
            is DoubtEvent.OnSearchChanged -> {
                _state.update{
                    it.copy(search = event.search)
                }
            }
            is DoubtEvent.OnTagsChanged -> {
                _state.update {
                    it.copy(tags = event.tags)
                }
            }
            DoubtEvent.Refresh -> {
                _state.update {
                    it.copy(isLoading = true)
                }

                viewModelScope.launch(Dispatchers.IO){
                        val token = preference.getToken()
                        val response = Network.getMyQuestion(token ?: "")
                        if(response is AllMyQuestionResult.Success){
                            _state.update {
                                it.copy(questions = response.success)
                            }
                        }else if(response is AllMyQuestionResult.Error){
                            onEvent2(DoubtEffect.ShowMessage(response.message ?: "Error"))
                        }
                    delay(2000)
                    _state.update {
                        it.copy(isLoading = false)
                    }
                }
            }
            DoubtEvent.SendQuestion -> {
                if(_state.value.question.isBlank() || _state.value.tags.isBlank()){
                    onEvent2(DoubtEffect.ShowMessage("Enter all the fields"))
                    return
                }
                val p = _state.value.tags.split(Regex("\\s+"))
                println(p)
                val data = QuestionPost(question = _state.value.question, tags = p)
                println(data)
                _state.update {
                    it.copy(isPostingQuestion = true)
                }
                viewModelScope.launch(Dispatchers.IO){

                        val token = preference.getToken()
                        val response = Network.postQuestion(data, token ?: "")
                        if(response is QuestionResult.Error){
                            onEvent2(DoubtEffect.ShowMessage(response.message ?: "Error"))
                            println(response.message)
                        }else{
                            onEvent(DoubtEvent.Refresh)
                        }
                    delay(2000)
                    _state.update {
                        it.copy(isPostingQuestion = false)
                    }}


            }
            DoubtEvent.ShowBottomSheet -> {
                _state.update{
                    it.copy(showBottomSheet = true)
                }
            }
        }
    }

    fun onEvent2(event: DoubtEffect){
        viewModelScope.launch {
            _shared.emit(event)
        }
    }
}