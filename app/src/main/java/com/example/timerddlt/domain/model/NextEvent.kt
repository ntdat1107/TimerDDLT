package com.example.timerddlt.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "next_event")
data class NextEvent(
    val title: String,
    val description: String,
    val lasting: Long,
    val startTime: Long,
    @PrimaryKey val id: Int? = null
)