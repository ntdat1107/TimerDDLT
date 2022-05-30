package com.example.timerddlt.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.timerddlt.R
import com.example.timerddlt.databinding.ActivityFinishBinding

class FinishActivity : AppCompatActivity() {
    private var binding: ActivityFinishBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinishBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putInt("timerRunning", 0)
        editor.remove("finished")
        editor.apply()

        binding?.textTaskGoalComplete!!.text = intent.getStringExtra("tag")
        binding?.textTaskCompleteGoal!!.text = intent.getStringExtra("description")
        binding?.btnOkCompleteGoal!!.setOnClickListener {
            finish()
        }

    }
}