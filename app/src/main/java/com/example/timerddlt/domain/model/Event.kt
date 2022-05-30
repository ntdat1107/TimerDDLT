package com.example.timerddlt.domain.model
import android.text.Editable
import androidx.room.*

@Entity
data class Event(
<<<<<<< HEAD
    val title: String,
    val description: String,
    val lasting: Long,
    val startTime: Long,
    val endTime: Long,
=======
    var title: String,
    var description: String,
    val lasting: Long,
    val startTime: Long,
    val endTime: Long,
    val isSuccess: Boolean,
>>>>>>> tiendat
    @PrimaryKey val id: Int? = null
)