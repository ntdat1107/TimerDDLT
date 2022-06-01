package com.example.timerddlt.data.data_source

import androidx.room.*
import com.example.timerddlt.domain.model.Event
import kotlinx.coroutines.flow.Flow


@Dao
interface TimerDao {
    @Query("SELECT * FROM event WHERE isSuccess ORDER BY startTime DESC")
    suspend fun getEvents(): List<Event>

    @Query("SELECT * FROM event WHERE isSuccess AND startTime > :date AND startTime < :date + 86400000 ORDER BY startTime DESC")
    suspend fun getEventsByDate(date : Long): List<Event>

    @Query("SELECT * FROM event WHERE isSuccess AND id = :id")
    suspend fun getEventById(id: Int): Event?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: Event)

    @Delete
    suspend fun deleteEvent(event: Event)
}