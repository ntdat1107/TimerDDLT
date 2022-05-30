package com.example.timerddlt.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.timerddlt.R
import com.example.timerddlt.databinding.ActivityTimelineBinding

class TimelineActivity : AppCompatActivity() {
    private var binding: ActivityTimelineBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimelineBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }
}