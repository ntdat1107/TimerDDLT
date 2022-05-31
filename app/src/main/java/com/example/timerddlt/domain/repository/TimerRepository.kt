package com.example.timerddlt.domain.repository

import com.example.timerddlt.domain.model.Event
import com.example.timerddlt.domain.model.NextEvent
import kotlinx.coroutines.flow.Flow

interface TimerRepository {
    suspend fun getEvents() : List<Event>
    suspend fun getEventByDate(date : Long) : List<Event>
    suspend fun getEventById(id: Int) : Event?
    suspend fun insertEvent(event: Event)
    suspend fun deleteEvent(event: Event)


    suspend fun getNextEvents() : List<NextEvent>
    suspend fun getNextEventByDate(date : Long) : List<NextEvent>

    suspend fun getNextEventById(id: Int) : NextEvent?
    suspend fun insertNextEvent(event: NextEvent)
    suspend fun deleteNextEvent(event: NextEvent)


}