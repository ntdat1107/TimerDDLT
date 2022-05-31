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

class ScheduleViewModel (
    private val eventsUseCases: TimerRepository
) : ViewModel() {

    fun addEvent(event: Event)  = viewModelScope.launch (Dispatchers.Default){
        eventsUseCases.insertEvent(event)
    }

    fun getEventsByID(id: Int)  = viewModelScope.launch (Dispatchers.Default){
        eventsUseCases.getNextEventById(id)
    }

    fun getEventsByMonth(month: Int)  = viewModelScope.launch (Dispatchers.Default){
        eventsUseCases.getNextEventByMonth(month)
    }

    fun getEventsByDate(date: Int)  = viewModelScope.launch (Dispatchers.Default){
        eventsUseCases.getNextEventByDay(date)
    }

}