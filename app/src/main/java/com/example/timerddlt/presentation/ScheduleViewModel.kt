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

class ScheduleViewModel (
    private val eventsUseCases: TimerRepository
) : ViewModel() {

    private var noteOfDate : List<NextEvent> = emptyList()
    private var output : List<NextEvent> = emptyList()

    fun addEvent(event: NextEvent)  = viewModelScope.launch (Dispatchers.Default){
        eventsUseCases.insertNextEvent(event)
    }

    // Delete next events by Id
    fun deleteNextEventById(id: Int) = viewModelScope.launch (Dispatchers.Default){
        eventsUseCases.deleteNextEventById(id)
    }

    // Delete next events by Id
    fun deleteNextEventByRequestId(id: Int) = viewModelScope.launch (Dispatchers.Default){
        eventsUseCases.deleteNextEventByRequestId(id)
    }



    fun getEventsByID(id: Int)  = viewModelScope.launch (Dispatchers.Default){
        eventsUseCases.getNextEventById(id)
    }

    //Get all Next events
    fun getNextEventsHelper()  = viewModelScope.launch (Dispatchers.Default){
        output = eventsUseCases.getNextEvents()
    }

    fun getNextEventsResult() : List<NextEvent> {
        return output
    }


    //Get all Next events by DATE
    fun getEventsByDateHelper(date: Long)  = viewModelScope.launch (Dispatchers.Default){
        noteOfDate = eventsUseCases.getNextEventByDate(date)
    }

    fun getEventsByDateResult() : List<NextEvent> {
        return noteOfDate
    }

}