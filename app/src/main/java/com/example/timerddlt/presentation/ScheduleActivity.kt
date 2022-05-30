package com.example.timerddlt.presentation

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.RingtoneManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timerddlt.R
import com.example.timerddlt.adapter.ScheduleAdapter
import com.example.timerddlt.databinding.ActivityScheduleBinding
import com.example.timerddlt.domain.model.Event
import com.example.timerddlt.services.ScheduleReceiver
import java.util.*

class ScheduleActivity : AppCompatActivity() {
    private var binding: ActivityScheduleBinding? = null

    private var year: Int = 0
    private var month: Int = 0
    private var dayOfMonth: Int = 0

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarSchedule!!)
        binding?.toolbarSchedule!!.setNavigationOnClickListener {
            onBackPressed()
        }

        binding?.simpleCalendarView!!.setDate(System.currentTimeMillis(), true, true)
        val calendar = Calendar.getInstance()
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        binding?.simpleCalendarView!!.setOnDateChangeListener { view, mYear, mMonth, mDayOfMonth ->
            year = mYear
            month = mMonth
            dayOfMonth = mDayOfMonth

            // Reload schedule
        }

        val schedules: ArrayList<Event> = ArrayList()

        val scheduleAdapter = ScheduleAdapter(this, schedules)
        binding?.rvSchedule!!.adapter = scheduleAdapter
        binding?.rvSchedule!!.layoutManager = LinearLayoutManager(this)

        binding?.rvSchedule!!.visibility = View.VISIBLE
        binding?.tvNoItem!!.visibility = View.GONE

        binding?.rvSchedule!!.visibility = View.GONE
        binding?.tvNoItem!!.visibility = View.VISIBLE

//        val notificationIntent = Intent(this, ScheduleReceiver::class.java)
//
//        val pendingIntent = PendingIntent.getBroadcast(
//            this,
//            1,
//            notificationIntent,
//            PendingIntent.FLAG_UPDATE_CURRENT
//        )
//        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        alarmManager.cancel(pendingIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.schedule_menu, menu!!)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btn_add -> {
                startActivity(Intent(this, CreateSchedule::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }


}