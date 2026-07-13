package com.example.final_nitt.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    private val _shared = MutableSharedFlow<HomeNavigate>()
    val shard = _shared.asSharedFlow()

    fun onEvent(event: HomeNavigate) = viewModelScope.launch {
        _shared.emit(event)
    }
}