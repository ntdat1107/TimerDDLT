package com.example.timerddlt.domain.model
import android.text.Editable
import androidx.room.*

@Entity
data class Event(
    var title: String,
    var description: String,
    val lasting: Long,
    val startTime: Long,
    val endTime: Long,
    val isSuccess: Boolean,
    @PrimaryKey val id: Int? = null
)