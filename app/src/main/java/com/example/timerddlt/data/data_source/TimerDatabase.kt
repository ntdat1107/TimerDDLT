package com.example.timerddlt.data.data_source

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.timerddlt.domain.model.Event
import com.example.timerddlt.domain.model.NextEvent


@Database(
    entities = [Event::class, NextEvent::class],
    version = 1
)
abstract class TimerDatabase : RoomDatabase() {
    abstract fun useTimerDao() : TimerDao

    companion object {
        private const val DATABASE_NAME = "timer_db"
        private var timerDatabase : TimerDatabase? = null

        fun provideNoteDatabase(context : Context) : TimerDatabase{
            if (timerDatabase == null) {
                timerDatabase = Room.databaseBuilder(context.applicationContext, TimerDatabase::class.java, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return timerDatabase!!
        }
    }
}