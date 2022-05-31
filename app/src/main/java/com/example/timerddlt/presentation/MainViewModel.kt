package com.example.timerddlt.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.timerddlt.domain.model.Event
import com.example.timerddlt.domain.repository.TimerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel (
    private val eventsUseCases: TimerRepository
) : ViewModel() {

    // Check loi ham nay
    fun addEvent(event: Event)  = viewModelScope.launch (Dispatchers.Default){
        eventsUseCases.insertEvent(event)
    }

}