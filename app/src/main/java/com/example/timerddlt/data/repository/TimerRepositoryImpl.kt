package com.example.timerddlt.data.repository

import android.content.Context
import com.example.timerddlt.data.data_source.TimerDao
import com.example.timerddlt.data.data_source.TimerDatabase
import com.example.timerddlt.domain.model.Event
import com.example.timerddlt.domain.repository.TimerRepository
import kotlinx.coroutines.flow.Flow

class TimerRepositoryImpl (context: Context) : TimerRepository {
    val timerDao : TimerDao = TimerDatabase.provideNoteDatabase(context).useTimerDao()

    override fun getEvents(): Flow<List<Event>> {
        TODO("Not yet implemented")
    }

    override suspend fun getEventById(id: Int): Event? {
        TODO("Not yet implemented")
    }

    override suspend fun insertEvent(event: Event) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteEvent(event: Event) {
        TODO("Not yet implemented")
    }
}