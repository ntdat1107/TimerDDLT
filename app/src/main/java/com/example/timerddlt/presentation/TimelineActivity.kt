package com.example.timerddlt.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timerddlt.adapter.TimelineAdapter
import com.example.timerddlt.databinding.ActivityTimelineBinding
import com.example.timerddlt.domain.model.Event

class TimelineActivity : AppCompatActivity() {
    private var binding: ActivityTimelineBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimelineBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarTimeline!!)
        binding?.toolbarTimeline!!.setNavigationOnClickListener {
            onBackPressed()
        }

        val timelines: ArrayList<Event> = ArrayList()
        timelines.add(Event("1", "2", 3, 4, 5, true))
        timelines.add(Event("1", "2", 3, 4, 5, true))
        timelines.add(Event("1", "2", 3, 4, 5, false))

        val timelineAdapter = TimelineAdapter(this, timelines)
        binding?.rvTimeline!!.adapter = timelineAdapter
        binding?.rvTimeline!!.layoutManager = LinearLayoutManager(this)

    }
}