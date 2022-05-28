package com.example.timerddlt.services

import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.os.Handler
import android.os.IBinder

class BroadcastService : Service() {
    companion object {
        const val COUNTDOWN_BR = "com.example.timerddlt.services"
    }

    val intent = Intent(COUNTDOWN_BR)

    private var mCountDownTimer: CountDownTimer? = null
    private var start_time_in_milis: Long = 0

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null && intent.hasExtra("start-time")) {
            start_time_in_milis = intent.getStringExtra("start-time")!!.toLong()
        }
        else if (intent != null && intent.hasExtra("pause")) {
            mCountDownTimer!!.cancel()
        }
        else if (intent != null && intent.hasExtra("start-time-continue")) {
            start_time_in_milis = intent.getStringExtra("start-time-continue")!!.toLong()
            createNewTimer()
            Handler().postDelayed({
                mCountDownTimer!!.start()
            }, 500)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun createNewTimer() {
        mCountDownTimer = object : CountDownTimer(start_time_in_milis - 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
//                    Log.i("test", (millisUntilFinished).toString())
                intent.putExtra("countdown", millisUntilFinished)
                sendBroadcast(intent)
            }

            override fun onFinish() {
                intent.putExtra("countdown", 0)
                intent.putExtra("finish", 1)
                sendBroadcast(intent)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        Handler().postDelayed({
            mCountDownTimer = object : CountDownTimer(start_time_in_milis, 1000) {
                override fun onTick(millisUntilFinished: Long) {
//                    Log.i("test", (millisUntilFinished).toString())
                    intent.putExtra("countdown", millisUntilFinished)
                    sendBroadcast(intent)
                }

                override fun onFinish() {
                    intent.putExtra("countdown", 0)
                    intent.putExtra("finish", 1)
                    sendBroadcast(intent)
                }

            }.start()
        }, 100)

    }

    override fun onDestroy() {
        mCountDownTimer!!.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

}