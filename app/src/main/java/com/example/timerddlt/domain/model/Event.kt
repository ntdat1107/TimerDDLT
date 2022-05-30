package com.example.timerddlt.domain.model
import androidx.room.*

@Entity
data class Event(
    val title: String,
    val description: String,
    val lasting: Long,
    val startTime: Long,
    val endTime: Long,
    @PrimaryKey val id: Int? = null
)