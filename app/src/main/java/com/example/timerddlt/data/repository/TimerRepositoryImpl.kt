package com.example.timerddlt.data.repository

import android.content.Context
import android.util.Log
import com.example.timerddlt.data.data_source.TimerDao
import com.example.timerddlt.data.data_source.TimerDatabase
import com.example.timerddlt.domain.model.Event
import com.example.timerddlt.domain.repository.TimerRepository
import kotlinx.coroutines.flow.Flow

class TimerRepositoryImpl (
    private val timerDao : TimerDao
) : TimerRepository {

    override fun getEvents(): Flow<List<Event>> {
//    override suspend fun getEvents(): List<Event> {
        return timerDao.getEvents()
    }

    override suspend fun getEventById(id: Int): Event? {
        return timerDao.getEventById(id)
    }

    override suspend fun insertEvent(event: Event) {
        timerDao.insertEvent(event)
    }

    override suspend fun deleteEvent(event: Event) {
        timerDao.deleteEvent(event)
    }

    companion object {
        fun provideTimerRepositoryImpl(context : Context): TimerRepository {
            val timerDB = TimerDatabase.provideNoteDatabase(context)
            return TimerRepositoryImpl(timerDB.useTimerDao())
        }
    }

}