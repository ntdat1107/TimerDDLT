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
    private var currentRequestCode: Int = 0
    private lateinit var prefs: SharedPreferences

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarSchedule!!)
        binding?.toolbarSchedule!!.setNavigationOnClickListener {
            onBackPressed()
        }

        prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        currentRequestCode = prefs.getInt("current-request-code", 0)

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
                addSchedule()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addSchedule() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minutes = calendar.get(Calendar.MINUTE)
        val mTimePicker = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                calendar.set(
                    year,
                    month,
                    dayOfMonth,
                    hourOfDay,
                    minute, 0
                )
                scheduleNotification(
                    getNotification("Dậy đi ông cháu ơi!!"),
                    calendar.timeInMillis,
                    currentRequestCode
                )
                currentRequestCode += 1
                val editor = prefs.edit()
                editor.putInt("current-request-code", currentRequestCode)
                editor.apply()
            }, hour, minutes, true
        )
        mTimePicker.show()
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun scheduleNotification(
        notification: Notification,
        alarmTimeInMillis: Long,
        requestCode: Int
    ) {
        val notificationIntent = Intent(this, ScheduleReceiver::class.java)
        notificationIntent.putExtra(ScheduleReceiver.NOTIFICATION_ID, requestCode)
        notificationIntent.putExtra(ScheduleReceiver.NOTIFICATION, notification)

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            requestCode,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTimeInMillis, pendingIntent)

    }

    @SuppressLint("LaunchActivityFromNotification")
    private fun getNotification(content: String): Notification {
        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(this, "notify-schedule")
            .setSmallIcon(R.drawable.ic_diamond)
            .setContentTitle("Notice")
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setChannelId("10002")
            .setSound(uri)
        return builder.build()
    }
}