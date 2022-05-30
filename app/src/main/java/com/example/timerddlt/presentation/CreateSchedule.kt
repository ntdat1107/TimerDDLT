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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.timerddlt.R
import com.example.timerddlt.databinding.ActivityCreateScheduleBinding
import com.example.timerddlt.services.ScheduleReceiver
import java.util.*

class CreateSchedule : AppCompatActivity() {
    private var binding: ActivityCreateScheduleBinding? = null
    private var year: Int = 0
    private var month: Int = 0
    private var dayOfMonth: Int = 0
    private var currentRequestCode: Int = 0
    private var calendarSchedule: Calendar? = null
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateScheduleBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarAddSchedule)
        binding?.toolbarAddSchedule!!.setNavigationOnClickListener {
            onBackPressed()
        }

        prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        currentRequestCode = prefs.getInt("current-request-code", 0)

        val calendar = Calendar.getInstance()
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        binding?.tvDate!!.text = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)

        calendarSchedule = Calendar.getInstance()
        val hour = calendarSchedule!!.get(Calendar.HOUR_OF_DAY)
        val minutes = calendarSchedule!!.get(Calendar.MINUTE)
        binding?.tvTime!!.text = String.format("%02d:%02d", hour, minutes)
        calendarSchedule!!.set(
            year,
            month,
            dayOfMonth,
            hour,
            minutes, 0
        )


        binding?.simpleCalendarView!!.setOnDateChangeListener { view, mYear, mMonth, mDayOfMonth ->
            year = mYear
            month = mMonth
            dayOfMonth = mDayOfMonth
            calendarSchedule!!.set(
                year, month, dayOfMonth,
                calendarSchedule!!.get(Calendar.HOUR_OF_DAY),
                calendarSchedule!!.get(Calendar.MINUTE)
            )
            binding?.tvDate!!.text = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
        }

        binding?.btnStart!!.setOnClickListener {
            scheduleNotification(
                getNotification(
                    "It's time. You must ${binding?.etDescription!!.text} at ${binding?.tvTime!!.text} ${binding?.tvDate!!.text}"
                ),
                calendarSchedule!!.timeInMillis,
                currentRequestCode
            )
            currentRequestCode += 1
            val editor = prefs.edit()
            editor.putInt("current-request-code", currentRequestCode)
            editor.apply()
            finish()
        }

        binding?.iconEdit!!.setOnClickListener {
            changeTime()
        }

    }

    private fun changeTime() {
        val hour = calendarSchedule!!.get(Calendar.HOUR_OF_DAY)
        val minutes = calendarSchedule!!.get(Calendar.MINUTE)
        val mTimePicker = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                calendarSchedule!!.set(
                    year,
                    month,
                    dayOfMonth,
                    hourOfDay,
                    minute, 0
                )
                binding?.tvTime!!.text = String.format("%02d:%02d", hourOfDay, minute)
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