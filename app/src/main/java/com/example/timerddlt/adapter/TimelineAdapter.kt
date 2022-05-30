package com.example.timerddlt.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.timerddlt.databinding.ItemTimelineBinding
import com.example.timerddlt.domain.model.Event

class TimelineAdapter(private var context: Context, private var timelines: ArrayList<Event>) :
    RecyclerView.Adapter<TimelineAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemTimelineBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemTimelineBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val timeline: Event = timelines[position]
        holder.binding.tvTag.text = timeline.title
        holder.binding.tvDescription.text = timeline.description
        holder.binding.tvTime.text = timeline.startTime.toString()
    }

    override fun getItemCount(): Int {
        return timelines.size
    }
}