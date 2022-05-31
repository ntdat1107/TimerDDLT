package com.example.timerddlt.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.timerddlt.databinding.ActivityFinishBinding

class FinishActivity : AppCompatActivity() {
    private var binding: ActivityFinishBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinishBinding.inflate(layoutInflater)
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

        binding?.textTaskGoalComplete!!.text = prefs.getString("tag", "").toString()
        binding?.textTaskCompleteGoal!!.text = prefs.getString("description", "").toString()
        binding?.btnOkCompleteGoal!!.setOnClickListener {
            editor.remove("finished")
            editor.apply()
            finish()
        }

    }

    override fun onDestroy() {
        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        val editor = prefs.edit()
        editor.remove("finished")
        editor.apply()
        super.onDestroy()
    }
}