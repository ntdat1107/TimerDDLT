package com.example.timerddlt.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.timerddlt.databinding.ItemScheduleBinding
import com.example.timerddlt.domain.model.NextEvent
import com.example.timerddlt.presentation.ScheduleActivity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ScheduleAdapter(
    private var activity: ScheduleActivity,
    private var context: Context,
    private var schedules: ArrayList<NextEvent>
) :
    RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemScheduleBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemScheduleBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val schedule: NextEvent = schedules[position]

        val id = Locale("en", "US")
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", id)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = schedule.startTime

        holder.binding.tvTime.text = simpleDateFormat.format(calendar.time)
        holder.binding.tvDescription.text = schedule.description
        holder.binding.btnDelete.setOnClickListener {
            AlertDialog.Builder(context).setTitle("Remove")
                .setMessage("Are you sure to remove this schedule")
                .setPositiveButton("Yes") { dialog, _ ->
                    activity.removeSchedule(schedule.requestId)
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }.show()
        }
    }

    override fun getItemCount(): Int {
        return schedules.size
    }
}