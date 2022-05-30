package com.example.timerddlt.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.timerddlt.databinding.ItemScheduleBinding
import com.example.timerddlt.domain.model.Event

class ScheduleAdapter(private var context: Context, private var schedules: ArrayList<Event>) :
    RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemScheduleBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemScheduleBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val schedule: Event = schedules[position]
        holder.binding.tvTime.text = schedule.startTime.toString()
        holder.binding.tvDescription.text = schedule.description
    }

    override fun getItemCount(): Int {
        return schedules.size
    }
}