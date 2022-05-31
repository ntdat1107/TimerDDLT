package com.example.timerddlt.data.repository
import android.content.Context
import com.example.timerddlt.data.data_source.NextEventDao
import com.example.timerddlt.data.data_source.TimerDao
import com.example.timerddlt.data.data_source.TimerDatabase
import com.example.timerddlt.domain.model.Event
import com.example.timerddlt.domain.model.NextEvent
import com.example.timerddlt.domain.repository.TimerRepository
import kotlinx.coroutines.flow.Flow

class TimerRepositoryImpl (
    private val timerDao : TimerDao,
    private val nextEventDao : NextEventDao
) : TimerRepository {



    override fun getEvents(): List<Event> {
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


    //________________________________________________________________________



    override suspend fun getNextEvents(): List<NextEvent> {
        return nextEventDao.getEvents()
    }

    override suspend fun getNextEventById(id: Int): NextEvent? {
        return nextEventDao.getNextEventById(id)
    }

    override suspend fun insertNextEvent(event: NextEvent) {
        nextEventDao.insertNextEvent(event)
    }

    override suspend fun deleteNextEvent(event: NextEvent) {
        nextEventDao.deleteNextEvent(event)
    }

    override suspend fun getNextEventByDay(date : Int): List<NextEvent> {
        return nextEventDao.getEventsByDate(date)
    }


    //________________________________________________________________________


    companion object {
        private var timerRepositoryImpl : TimerRepository? = null

        fun provideTimerRepositoryImpl(context : Context): TimerRepository {
            if (timerRepositoryImpl == null) {
                val timerDB = TimerDatabase.provideNoteDatabase(context.applicationContext)
                timerRepositoryImpl = TimerRepositoryImpl(
                    timerDB.useTimerDao(),
                    timerDB.useNextEventDao()
                )
            }
            return timerRepositoryImpl!!
        }
    }
}