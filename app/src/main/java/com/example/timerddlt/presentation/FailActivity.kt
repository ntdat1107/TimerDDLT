package com.example.timerddlt.presentation

import android.os.Bundle
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

        setSupportActionBar(binding?.toolbarFinish)
        binding?.toolbarFinish!!.setNavigationOnClickListener {
            onBackPressed()
            editor.remove("finished")
            editor.apply()
        }

        editor.putInt("timerRunning", 0)
        editor.apply()

        binding?.textTaskGoalFail!!.text = prefs.getString("tag", "").toString()
        binding?.textTaskFailGoal!!.text = prefs.getString("description", "").toString()
        binding?.btnOkCompleteGoal!!.setOnClickListener {
            editor.remove("finished")
            editor.apply()
            finish()
        }
    }
}