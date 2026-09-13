package com.example.final_nitt.mainscreen.otherDoubt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.final_nitt.Preference
import com.example.final_nitt.mainscreen.answer.AnswerEffect
import com.example.final_nitt.mainscreen.answer.AnswerStateEvent
import com.example.final_nitt.mainscreen.otherDoubt.OtherAnswerEffect.ScrollToBottom
import com.example.final_nitt.mainscreen.otherDoubt.OtherAnswerEffect.ShowSnackbar
import com.example.final_nitt.network.AnswersForQidItem
import com.example.final_nitt.network.AttachmentAccessResult
import com.example.final_nitt.network.GetAnswerResult
import com.example.final_nitt.network.Network
import com.example.final_nitt.network.Network.getAnswer
import com.example.final_nitt.network.Network.getVotes
import com.example.final_nitt.network.Network.postVotes
import com.example.final_nitt.network.PostAnswerResult
import com.example.final_nitt.network.QuestionFormat
import com.example.final_nitt.network.UserVoteResult
import com.example.final_nitt.network.Vote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OtherAnswerViewModelFactory(
    private val preference: Preference,
    val qid: Int
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(OtherAnswerViewModel::class.java)) {
            return OtherAnswerViewModel(preference, qid) as T
        }

        throw IllegalArgumentException("Unknown ViewModel")
    }
}


class OtherAnswerViewModel(private val preference: Preference, val qid: Int) : ViewModel(){
    private val _shared = MutableSharedFlow<OtherAnswerEffect>()
    val shared = _shared.asSharedFlow()

    private val _state = MutableStateFlow<OtherAnswerState>(OtherAnswerState())
    val state = _state.asStateFlow()

    init{
        onEvent(OtherAnswerEvent.Refresh(qid))
    }

    fun onEvent(event: OtherAnswerEvent){
        when(event){
            is OtherAnswerEvent.CastVote -> {
                val castVoe = Vote(event.answerId, event.voteDir)
                viewModelScope.launch(Dispatchers.IO){
                    _state.update {
                        it.copy(
                            votingAnswers = it.votingAnswers + event.answerId
                        )
                    }
                    val response = postVotes(castVoe, preference.getToken() ?: "")
                    onEvent2(ShowSnackbar(response))
                    if(response == "Successfully voted"){
                        _state.update {
                            it.copy(
                                userVotes = it.userVotes + (event.answerId to event.voteDir)
                            )
                        }
                        _state.update {
                            it.copy(voteCounts = it.voteCounts + (event.answerId to (it.voteCounts[event.answerId] ?: 0 )+event.voteDir))
                        }
                    }
                        _state.update {
                            it.copy(
                                votingAnswers = it.votingAnswers - event.answerId
                            )
                        }


                }
            }
            is OtherAnswerEvent.OnAnswerChanged -> {
                _state.update {
                    it.copy(answerText = event.text)
                }
            }
            is OtherAnswerEvent.Refresh -> {
                viewModelScope.launch(Dispatchers.IO){
                    _state.update {
                        it.copy(isLoading = true)
                    }
                    val response = getAnswer(event.qid)
                    if(response is GetAnswerResult.Error){
                        onEvent2(ShowSnackbar(response.message))
                    }else if(response is GetAnswerResult.Success){
                        _state.update {
                            it.copy(answers = response.message)
                        }
                    }
                    val response2 = Network.getUserVotes(preference.getToken() ?: "")
                    if(response2 is UserVoteResult.Success){
                        val voteMap = response2.message.associate {
                            it.answerId to it.voteDir
                        }
                        _state.update {
                            it.copy(
                                userVotes = voteMap
                            )
                        }
                    }
                    _state.update {
                        it.copy(isLoading = false)
                    }
                }
            }
            is OtherAnswerEvent.SendAnswer -> {
                viewModelScope.launch {
                    _state.update { it.copy(isSendingAnswer = true) }
                    val response = Network.postAnswer(preference.getToken() ?: "", event.qid, event.answer)
                    if(response is PostAnswerResult.Success){
                        _state.update {
                            it.copy(answers = it.answers + response.message)
                        }
                        onEvent2(ScrollToBottom)
                    }else if(response is PostAnswerResult.Error){
                        onEvent2(ShowSnackbar(response.message))
                    }
                    _state.update { it.copy(isSendingAnswer = false) }
                }
            }

            is OtherAnswerEvent.CountVotes -> {
                viewModelScope.launch(Dispatchers.IO){
                    val response = getVotes(event.aid)
                    val p = try{
                        response?.toInt() ?: 0
                    }catch(e: Exception){
                        0
                    }
                    _state.update {
                        it.copy(voteCounts = it.voteCounts + (event.aid to p))
                    }
                }
            }
            is  OtherAnswerEvent.OpenAttachment -> {
                viewModelScope.launch(Dispatchers.IO) {

                    val token = preference.getToken()

                    val response = Network.getAttachment(
                        token ?: "",
                        event.attachmentId
                    )

                    when (response) {

                        is AttachmentAccessResult.Success -> {
                            _shared.emit(
                                OtherAnswerEffect.OpenFile(
                                    response.success.url
                                )
                            )
                        }

                        is AttachmentAccessResult.Error -> {
                            _shared.emit(
                                ShowSnackbar(
                                    response.message ?: "Failed to open file"
                                )
                            )
                        }
                    }
                }
            }
        }
    }
    fun onEvent2(event: OtherAnswerEffect){
        viewModelScope.launch {
            _shared.emit(event)
        }
    }
}

data class OtherAnswerState(
    val question: QuestionFormat? = null,
    val answers: List<AnswersForQidItem> = emptyList(),
    val answerText: String = "",
    val isLoading: Boolean = false,
    val isSendingAnswer: Boolean = false,
    val votingAnswers: Set<Int> = emptySet(),
    val userVotes: Map<Int, Int> = emptyMap(),
    val voteCounts: Map<Int, Int> = emptyMap(),
    val isSolved: Boolean = false
)

sealed class OtherAnswerEvent{
    data class Refresh(val qid: Int): OtherAnswerEvent()
    data class SendAnswer(val answer: String, val qid: Int): OtherAnswerEvent()
    data class CastVote(val answerId: Int, val voteDir: Int): OtherAnswerEvent()
    data class OnAnswerChanged(val text: String) : OtherAnswerEvent()
    data class CountVotes(val aid: Int): OtherAnswerEvent()
    data class OpenAttachment(val attachmentId: Int) : OtherAnswerEvent()
}

sealed class OtherAnswerEffect{
    data class ShowSnackbar(val message: String): OtherAnswerEffect()
    data object ScrollToBottom: OtherAnswerEffect()
    data class OpenFile(val url: String) : OtherAnswerEffect()
}