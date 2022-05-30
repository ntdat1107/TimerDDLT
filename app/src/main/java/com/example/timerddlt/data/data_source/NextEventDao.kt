package com.example.timerddlt.data.data_source

import androidx.room.*
import com.example.timerddlt.domain.model.NextEvent
import kotlinx.coroutines.flow.Flow

@Dao
interface NextEventDao {
    @Query("SELECT * FROM next_event")
    fun getEvents(): Flow<List<NextEvent>>

    @Query("SELECT * FROM next_event WHERE id = :id")
    suspend fun getEventById(id: Int): NextEvent?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(vararg nextEvent: NextEvent)

    @Delete
    suspend fun deleteEvent(nextEvent: NextEvent)
}