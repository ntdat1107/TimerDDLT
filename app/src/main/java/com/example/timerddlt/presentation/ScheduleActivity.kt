package com.example.timerddlt.presentation

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timerddlt.R
import com.example.timerddlt.adapter.ScheduleAdapter
import com.example.timerddlt.data.repository.TimerRepositoryImpl
import com.example.timerddlt.databinding.ActivityScheduleBinding
import com.example.timerddlt.domain.model.NextEvent
import com.example.timerddlt.domain.repository.TimerRepository
import com.example.timerddlt.services.ScheduleReceiver
import java.util.*

class ScheduleActivity : AppCompatActivity() {
    private var binding: ActivityScheduleBinding? = null

    private var year: Int = 0
    private var month: Int = 0
    private var dayOfMonth: Int = 0

    private lateinit var timerRepositoryImpl: TimerRepository
    private lateinit var vm: ScheduleViewModel
    private lateinit var calendar: Calendar

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        timerRepositoryImpl = TimerRepositoryImpl.provideTimerRepositoryImpl(applicationContext)
        vm = ScheduleViewModel(timerRepositoryImpl)

        setSupportActionBar(binding?.toolbarSchedule!!)
        binding?.toolbarSchedule!!.setNavigationOnClickListener {
            onBackPressed()
        }

        binding?.simpleCalendarView!!.setDate(System.currentTimeMillis(), true, true)
        calendar = Calendar.getInstance()
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        binding?.simpleCalendarView!!.setOnDateChangeListener { _, mYear, mMonth, mDayOfMonth ->
            year = mYear
            month = mMonth
            dayOfMonth = mDayOfMonth

            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            // Reload schedule
            vm.getEventsByDateHelper(calendar.timeInMillis)
            Handler().postDelayed({
                val scheduleList = vm.getEventsByDateResult()
                updateRv(scheduleList)
            }, 500)
        }

        vm.getEventsByDateHelper(calendar.timeInMillis)
        Handler().postDelayed({
            val scheduleList = vm.getEventsByDateResult()
            updateRv(scheduleList)
        }, 500)
    }

    private fun updateRv(scheduleList: List<NextEvent>) {

        val schedules: ArrayList<NextEvent> = ArrayList(scheduleList)

        if (schedules.isEmpty()) {
            binding?.rvSchedule!!.visibility = View.GONE
            binding?.tvNoItem!!.visibility = View.VISIBLE
        } else {
            val scheduleAdapter = ScheduleAdapter(this, this, schedules)
            binding?.rvSchedule!!.adapter = scheduleAdapter
            binding?.rvSchedule!!.layoutManager = LinearLayoutManager(this)

            binding?.rvSchedule!!.visibility = View.VISIBLE
            binding?.tvNoItem!!.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.schedule_menu, menu!!)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btn_add -> {
                startActivity(Intent(this, CreateSchedule::class.java))
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun removeSchedule(requestId: Int) {
        val notificationIntent = Intent(this, ScheduleReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            requestId,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)

        vm.deleteNextEventByRequestId(requestId)
        vm.getEventsByDateHelper(calendar.timeInMillis)
        Handler().postDelayed({
            val scheduleList = vm.getEventsByDateResult()
            updateRv(scheduleList)
        }, 500)

    }


}