package com.example.final_nitt.onBoarding

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.final_nitt.Preference
import com.example.final_nitt.network.LoginResult
import com.example.final_nitt.network.Network
import com.example.final_nitt.network.Register
import com.example.final_nitt.network.RegisterResult
import com.example.final_nitt.onBoarding.OneTimeOnBoardEvent.*
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class SignInViewModelFactory(
    private val preference: Preference
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(SignInViewModel::class.java)) {
            return SignInViewModel(preference) as T
        }

        throw IllegalArgumentException("Unknown ViewModel")
    }
}

class SignInViewModel(private val preference: Preference): ViewModel(){
    private val _state = MutableStateFlow(OnBoardUiState())
    val state = _state.asStateFlow()

    private val _shared = MutableSharedFlow<OneTimeOnBoardEvent>()
    val shared = _shared.asSharedFlow()

    fun onEvent2(event: OneTimeOnBoardEvent){
        viewModelScope.launch {
            _shared.emit(event)
        }
    }

    fun onEvent1(event: OnBoardUiEvent){
        when(event){
            OnBoardUiEvent.ClickLogIn -> {
                val data = Register(name = _state.value.username, password = _state.value.password, rollno = _state.value.rollNo, department = _state.value.department)
                viewModelScope.launch {
                    _state.update {
                        it.copy(isLoading = true)
                    }
                    val status = Network.login(data)
                    delay(3000)
                    _state.update {
                        it.copy(username = "", rollNo = "", password = "", isLoading=false)
                    }
                    when(status){
                        is LoginResult.Error -> {
                            onEvent2(ShowToast(status.message ?: "Unknown Error"))
                        }
                        is LoginResult.Success -> {
                            preference.saveToken(status.user.token)
                            onEvent2(NavigateToDashboardScreen)
                        }
                    }
                }
            }
            OnBoardUiEvent.ClickSignUp -> {
                val data = Register(name = _state.value.username, password = _state.value.password, rollno = _state.value.rollNo, department = _state.value.department)
                viewModelScope.launch {
                    _state.update {
                        it.copy(isLoading = true)
                    }
                    val status = Network.register(data)
                    delay(3000)
                    _state.update {
                        it.copy(username = "", rollNo = "", password = "", isLoading=false)
                    }
                    when(status){
                        is RegisterResult.Error -> {
                            onEvent2(ShowToast(status.message ?: "Unknown Error"))
                        }
                        is RegisterResult.Success -> {
                            onEvent2(ShowToast("Successfully registered"))

                        }
                    }
                }
            }
            is OnBoardUiEvent.SetPassword -> {
                _state.update {
                    it.copy(
                        password = event.password
                    )
                }
            }
            is OnBoardUiEvent.SetRollNo -> {
                _state.update {
                    it.copy(
                        rollNo = event.rollNo
                    )
                }
            }
            is OnBoardUiEvent.SetUserName -> {
                _state.update {
                    it.copy(
                        username = event.username
                    )
                }
            }

            is OnBoardUiEvent.SetDepartment -> {
                _state.update {
                    it.copy(department = event.department)
                }
            }
        }
    }
}