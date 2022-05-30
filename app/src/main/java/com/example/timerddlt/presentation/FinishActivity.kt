package com.example.timerddlt.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.timerddlt.R

class FinishActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish)

        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putInt("timerRunning", 0)
        editor.apply()

        val isSuccess = intent.getBooleanExtra("isSuccess", true)
        if (!isSuccess) {

        } else {

        }

    }
}