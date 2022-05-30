package com.example.timerddlt.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.timerddlt.databinding.ActivityFailBinding

class FailActivity : AppCompatActivity() {
    private var binding: ActivityFailBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putInt("timerRunning", 0)
        editor.remove("finished")
        editor.apply()

        binding?.textTaskGoalFail!!.text = intent.getStringExtra("tag")
        binding?.textTaskFailGoal!!.text = intent.getStringExtra("description")
        binding?.btnOkCompleteGoal!!.setOnClickListener {
            finish()
        }
    }
}