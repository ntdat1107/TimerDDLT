package com.example.timerddlt.domain.repository

import com.example.timerddlt.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface TimerRepository {
    fun getEvents() : Flow<List<Event>>
    suspend fun getEventById(id: Int) : Event?
    suspend fun insertEvent(event: Event)
    suspend fun deleteEvent(event: Event)
}