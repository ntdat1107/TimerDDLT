package com.example.timerddlt.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.timerddlt.databinding.ItemTimelineBinding
import com.example.timerddlt.domain.model.Event
import java.text.SimpleDateFormat
import java.util.*

class TimelineAdapter(private var context: Context, private var timelines: ArrayList<Event>) :
    RecyclerView.Adapter<TimelineAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemTimelineBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemTimelineBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val timeline: Event = timelines[position]
        val id = Locale("en", "US")
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", id)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeline.startTime

        var res = if (timeline.isSuccess) {
            "Success"
        } else {
            "Fail"
        }

        val startTimeInMilis = timeline.startTime
        val endTimeInMilis = timeline.endTime

        var timeLabel = ""
        var min = ((startTimeInMilis / 1000 / 60) % 60).toInt()
        var hour = (startTimeInMilis / 1000 / 3600 % 24).toInt()
        timeLabel = "$hour:$min"
        min = ((endTimeInMilis / 1000 / 60) % 60).toInt()
        hour = (endTimeInMilis / 1000 / 3600 % 24).toInt()
        timeLabel += " - $hour:$min"
        holder.binding.tvTime.text = timeLabel
        holder.binding.tvStartDate.text = simpleDateFormat.format(calendar.time)

        min = (timeline.lasting / 1000 / 60).toInt()
        val sec = timeline.lasting / 1000 % 60
        timeLabel = "$min:"
        if (sec < 10) timeLabel += "0"
        timeLabel += sec

        holder.binding.tvTag.text = "${timeline.title} in $timeLabel ($res)"
        holder.binding.tvDescription.text = timeline.description
    }

    override fun getItemCount(): Int {
        return timelines.size
    }
}