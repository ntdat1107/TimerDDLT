package com.example.timerddlt.domain.model
import androidx.room.*

@Entity
data class Event(
    val title: String,
    val description: String,
    val lasting: Int,
    val startTime: Int,
    val endTime: Int,
    @PrimaryKey val id: Int? = null
)