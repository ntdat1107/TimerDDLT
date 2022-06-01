package com.example.timerddlt.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "next_event")
data class NextEvent(
    val description: String,
    val startTime: Long,
    val requestId: Int,
    @PrimaryKey val id: Int? = null
)