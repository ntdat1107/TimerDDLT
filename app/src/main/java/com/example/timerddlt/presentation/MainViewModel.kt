package com.example.timerddlt.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.timerddlt.domain.model.Event
import com.example.timerddlt.domain.model.NextEvent
import com.example.timerddlt.domain.repository.TimerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel (
    private val eventsUseCases: TimerRepository
) : ViewModel() {

    private var events : List<Event> = emptyList()
    private var noteOfDate : List<Event> = emptyList()

    fun addEvent(event: Event)  = viewModelScope.launch (Dispatchers.Default){
        eventsUseCases.insertEvent(event)
    }


    //Get all events
    fun getEvents()  = viewModelScope.launch (Dispatchers.Default){
        events = eventsUseCases.getEvents()
    }

    fun getEventsResult() : List<Event> {
        return events
    }


    //Get all events by DATE
    fun getEventsByDateHelper(date: Long)  = viewModelScope.launch (Dispatchers.Default){
        noteOfDate = eventsUseCases.getEventByDate(date)
    }

    fun getEventsByDateResult() : List<Event> {
        return noteOfDate
    }

}