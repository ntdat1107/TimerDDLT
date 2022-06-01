package com.example.timerddlt.data.data_source

import androidx.room.*
import com.example.timerddlt.domain.model.NextEvent
import kotlinx.coroutines.flow.Flow

@Dao
interface NextEventDao {
    @Query("SELECT * FROM next_event")
    suspend fun getEvents(): List<NextEvent>

    @Query("SELECT * FROM next_event WHERE startTime > :date AND startTime < :date + 86400000 ORDER BY startTime DESC")
//    @Query("SELECT * FROM next_event WHERE startTime > :date ORDER BY startTime DESC")
    suspend fun getEventsByDate(date : Long): List<NextEvent>

    @Query("SELECT * FROM next_event WHERE id = :id")
    suspend fun getNextEventById(id: Int): NextEvent?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNextEvent(nextEvent: NextEvent)

    @Delete
    suspend fun deleteNextEvent(nextEvent: NextEvent)

    @Query("DELETE FROM next_event WHERE id = :id")
    suspend fun deleteNextEventById(id: Int)
}