package com.example.timerddlt.data.data_source

import androidx.room.*
import com.example.timerddlt.domain.model.Event
import kotlinx.coroutines.flow.Flow


@Dao
interface TimerDao {
    @Query("SELECT * FROM event")
    fun getEvents(): Flow<List<Event>>

    @Query("SELECT * FROM event WHERE id = :id")
    suspend fun getEventById(id: Int): Event?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(vararg event: Event)

    @Delete
    suspend fun deleteEvent(event: Event)
}