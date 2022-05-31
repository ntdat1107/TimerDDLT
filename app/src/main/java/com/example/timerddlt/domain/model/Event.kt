package com.example.timerddlt.domain.model
import androidx.room.*

@Entity
class Event(
    var title: String,
    var description: String,
    val lasting: Long,
    val startTime: Long,
    val endTime: Long,
    val isSuccess: Boolean,
    @PrimaryKey
    var id: Int? = null
)