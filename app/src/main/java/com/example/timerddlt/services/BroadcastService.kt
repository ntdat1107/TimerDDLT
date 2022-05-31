package com.example.timerddlt.services

import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log

class BroadcastService : Service() {
    companion object {
        const val COUNTDOWN_BR = "com.example.timerddlt.services"
    }

    val intent = Intent(COUNTDOWN_BR)

    private var mCountDownTimer: CountDownTimer? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()
        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        val remainingTime: Long = prefs.getLong("remainingTimeInMillis", -1)
        mCountDownTimer = object : CountDownTimer(remainingTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                intent.putExtra("countdown", millisUntilFinished)
                sendBroadcast(intent)
            }

            override fun onFinish() {
                intent.putExtra("countdown", 0)
                sendBroadcast(intent)
                intent.putExtra("finish", 1)
                sendBroadcast(intent)
            }
        }.start()
    }

    override fun onDestroy() {
        mCountDownTimer!!.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

}