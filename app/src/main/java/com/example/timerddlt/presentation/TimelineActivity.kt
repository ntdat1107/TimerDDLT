package com.example.timerddlt.presentation

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timerddlt.adapter.TimelineAdapter
import com.example.timerddlt.data.repository.TimerRepositoryImpl
import com.example.timerddlt.databinding.ActivityTimelineBinding
import com.example.timerddlt.domain.model.Event
import com.example.timerddlt.domain.repository.TimerRepository

class TimelineActivity : AppCompatActivity() {
    private var binding: ActivityTimelineBinding? = null

    private lateinit var timerRepositoryImpl: TimerRepository
    private lateinit var vm: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimelineBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        timerRepositoryImpl = TimerRepositoryImpl.provideTimerRepositoryImpl(applicationContext)
        vm = MainViewModel(timerRepositoryImpl)

        setSupportActionBar(binding?.toolbarTimeline!!)
        binding?.toolbarTimeline!!.setNavigationOnClickListener {
            onBackPressed()
        }

        vm.getEvents()
        Handler().postDelayed({
            val timelineList = vm.getEventsResult()
            if (timelineList.isEmpty()) {
                binding?.tvNoItem!!.visibility = View.VISIBLE
                binding?.rvTimeline!!.visibility = View.GONE
            } else {
                binding?.tvNoItem!!.visibility = View.GONE
                binding?.rvTimeline!!.visibility = View.VISIBLE
                updateRvTimeline(timelineList)
            }
        }, 750)


    }

    private fun updateRvTimeline(timelineList: List<Event>) {
        val timelines = ArrayList(timelineList)
        val timelineAdapter = TimelineAdapter(this, timelines)
        binding?.rvTimeline!!.adapter = timelineAdapter
        binding?.rvTimeline!!.layoutManager = LinearLayoutManager(this)
    }
}