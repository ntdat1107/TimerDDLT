package com.example.timerddlt.presentation

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Dialog
import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import com.example.timerddlt.R
import com.example.timerddlt.data.repository.TimerRepositoryImpl
import com.example.timerddlt.databinding.ActivityMainBinding
import com.example.timerddlt.databinding.TimePickerDialogBinding
import com.example.timerddlt.domain.model.Event
import com.example.timerddlt.domain.repository.TimerRepository
import com.example.timerddlt.services.BroadcastService
import com.example.timerddlt.services.NoticeReceiver
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.ceil

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var binding: ActivityMainBinding? = null
    private var drawerLayout: DrawerLayout? = null
    private var navigationView: NavigationView? = null
    private var alarmManager: AlarmManager? = null
    private var pendingIntent: PendingIntent? = null
    private var state: Int = 0
    private var intentService: Intent? = null
    private var mTimeInMilis: Long = 600000
    var millisUntilFinished: Long = mTimeInMilis

    private lateinit var timerRepositoryImpl : TimerRepository
    private lateinit var vm : MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        timerRepositoryImpl = TimerRepositoryImpl.provideTimerRepositoryImpl(applicationContext)
        vm = MainViewModel(timerRepositoryImpl)



        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        mTimeInMilis = prefs.getLong("millisLeft", 600000)
        state = prefs.getInt("timerRunning", 0)
        val mEndTime = prefs.getLong("endTime", 0)
        val editor = prefs.edit()
        editor.apply()
        if (state != 0) {
            val timeDiff: Long = abs(mEndTime - System.currentTimeMillis())
            val remainingTimeInMillis: Long = mTimeInMilis - timeDiff
            if (remainingTimeInMillis > 4000) {
                binding?.etTag!!.setText(prefs.getString("tag", "Study"))
                binding?.etDescription!!.setText(prefs.getString("description", "Nothing"))
                editor.putLong("remainingTimeInMillis", remainingTimeInMillis - 2750)
                editor.apply()
                intentService = Intent(this, BroadcastService::class.java)
                startTimer()
                startService(intentService)

            } else {
                // Check if it finish
                state = 0
                editor.putInt("timerRunning", state)
                editor.apply()



                //_____________________________________________________________________
                val event2 = Event(
                    title = "a",
                    description = "a",
                    lasting = 1L,
                    startTime = 1L,
                    endTime = 1L,
                    id = 3
                )
//                vm.addEvent(event2)
//                lifecycleScope.async (Dispatchers.IO) {
////                    vm.addEvent(event2)
//                    timerRepositoryImpl.insertEvent(event)
//                }
                //_____________________________________________________________________


                startActivity(Intent(this, FinishActivity::class.java))
            }
        } else {
            setUpSideBar()
        }



        binding?.btnStart!!.setOnClickListener {
            mTimeInMilis = (binding?.tvTimer!!.text.toString().substring(0, 2)
                .toLong() * 60 + binding?.tvTimer!!.text.toString().substring(3, 5).toLong()) * 1000
            editor.putLong("remainingTimeInMillis", mTimeInMilis)
            editor.apply()
            intentService = Intent(this, BroadcastService::class.java)
            startTimer()

            val currentTimeInMillis: Long = System.currentTimeMillis()
            startService(intentService)

            scheduleNotification(
                getNotification(
                    "You've done ${binding?.etTag!!.text} in ${
                        binding?.tvTimer!!.text.toString().substring(0, 2)
                    }m${binding?.tvTimer!!.text.toString().substring(3, 5)}s"
                ),
                currentTimeInMillis + mTimeInMilis
            )
        }

        binding?.btnPause!!.setOnClickListener {
            stopService(intentService)
            alarmManager!!.cancel(pendingIntent)
            pauseTimer()
        }

        binding?.btnContinue!!.setOnClickListener {
            mTimeInMilis = (binding?.tvTimer!!.text.toString().substring(0, 2)
                .toLong() * 60 + binding?.tvTimer!!.text.toString().substring(3, 5).toLong()) * 1000
            editor.putLong("remainingTimeInMillis", mTimeInMilis)
            editor.apply()
            intentService = Intent(this, BroadcastService::class.java)
            startTimer()
            val currentTimeInMillis: Long = System.currentTimeMillis()

            startService(intentService)

            val intentNotify = Intent(this, NoticeReceiver::class.java)

            pendingIntent = PendingIntent.getBroadcast(this, 0, intentNotify, 0)

            alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

            val timeNotify: Long = currentTimeInMillis + mTimeInMilis
            alarmManager!!.set(AlarmManager.RTC_WAKEUP, timeNotify, pendingIntent)
        }

        binding?.iconEdit!!.setOnClickListener {
            binding?.etTag!!.isEnabled = true
            binding?.etTag!!.requestFocus()
        }

        binding?.tvTimer!!.setOnClickListener {
            if (state == 0) {
                val dialog: Dialog = Dialog(this)
                val dialogBinding = TimePickerDialogBinding.inflate(layoutInflater)
                dialog.setContentView(dialogBinding.root)
                dialogBinding.npMinute.minValue = 0
                dialogBinding.npMinute.maxValue = 90
                dialogBinding.npSecond.minValue = 0
                dialogBinding.npSecond.maxValue = 59
                dialogBinding.npMinute.value =
                    binding?.tvTimer!!.text.toString().substring(0, 2).toInt()
                dialogBinding.npSecond.value =
                    binding?.tvTimer!!.text.toString().substring(3, 5).toInt()
                dialog.setCanceledOnTouchOutside(false)
                dialog.show()
                dialogBinding.btnSubmit.setOnClickListener {
                    val minute = dialogBinding.npMinute.value
                    val second = dialogBinding.npSecond.value
                    if (minute == 0 && second == 0) {
                        Toast.makeText(this, "Please input time greater than 0", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        binding?.tvTimer!!.text = String.format("%02d:%02d", minute, second)
                        dialog.dismiss()
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        when {
            drawerLayout!!.isDrawerOpen(GravityCompat.START) -> {
                drawerLayout!!.close()
            }
            state == 1 || state == 2 -> {

            }
            else -> {
                super.onBackPressed()
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_timeline -> {
                startActivity(Intent(this, TimelineActivity::class.java))
            }
        }
        drawerLayout!!.close()
        return true
    }

    private fun setUpSideBar() {
        drawerLayout = binding?.drawerLayout!!
        navigationView = binding?.navView!!
        setSupportActionBar(binding?.toolbarHome!!)

        navigationView!!.bringToFront()
        val newToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            binding?.toolbarHome,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout!!.addDrawerListener(newToggle)
        newToggle.syncState()

        navigationView!!.setCheckedItem(R.id.nav_home)
        navigationView!!.setNavigationItemSelectedListener(this)

        binding?.btnStart!!.isEnabled = true
        binding?.btnStart!!.visibility = View.VISIBLE
        binding?.btnContinue!!.visibility = View.GONE
        binding?.btnPause!!.visibility = View.GONE

        binding?.etTag!!.isEnabled = false
        binding?.etTag!!.setText(getString(R.string.study_tag))
        binding?.etDescription!!.text.clear()
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // Update UI
            updateUI(intent)
        }
    }

    override fun onResume() {
        navigationView?.setCheckedItem(R.id.nav_home)
        registerReceiver(broadcastReceiver, IntentFilter(BroadcastService.COUNTDOWN_BR))
        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        val editor = prefs.edit()
        val isFinished = prefs.getBoolean("finished", false)
        Log.i("test", isFinished.toString())
        if (state == 0) {
            setUpSideBar()
        }

        if (isFinished) {
            editor.remove("finished")
            editor.apply()
            startActivity(Intent(this, FinishActivity::class.java))
        }

        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(broadcastReceiver)
    }

    override fun onStop() {
        super.onStop()
        try {
            unregisterReceiver(broadcastReceiver)
        } catch (e: Exception) {

        }
    }

    override fun onDestroy() {
        if (state != 0) {
            val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putLong("millisLeft", millisUntilFinished)
            editor.putInt("timerRunning", state)
            editor.putLong("endTime", System.currentTimeMillis())
            editor.putString("tag", binding?.etTag!!.text.toString())
            editor.putString("description", binding?.etDescription!!.text.toString())
            editor.apply()
            stopService(intentService)
        }
        super.onDestroy()
    }

    private fun updateUI(intent: Intent?) {
        if (intent != null && intent.extras != null) {
            millisUntilFinished = intent.getLongExtra("countdown", 0)
            val temp: Float = ceil(millisUntilFinished.toFloat() / 1000)
            millisUntilFinished = (temp * 1000).toLong()

            val minutes: Int = ((millisUntilFinished / 1000) / 60).toInt()
            val seconds: Int = ((millisUntilFinished / 1000) % 60).toInt()
            binding?.tvTimer!!.text = String.format("%02d:%02d", minutes, seconds)

            if (intent.getIntExtra("finish", 0) == 1) {
                stopService(intentService)
                state = 0
                val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
                val editor = prefs.edit()
                editor.putInt("timerRunning", state)
                editor.putBoolean("finished", true)
                editor.apply()
                startActivity(Intent(this, FinishActivity::class.java))
            }
        }
    }


    private fun startTimer() {
        state = 1
        // 1 is running
        binding?.toolbarHome!!.navigationIcon = null
        binding?.tvTitleMargin!!.visibility = View.GONE
        binding?.tvTitleNoMargin!!.visibility = View.VISIBLE

        binding?.btnStart!!.visibility = View.GONE
        binding?.btnStart!!.isEnabled = false
        binding?.btnPause!!.setBackgroundResource(R.drawable.button_background_cant_click)
        binding?.btnPause!!.visibility = View.VISIBLE

        binding?.iconEdit!!.visibility = View.INVISIBLE

        Handler().postDelayed({
            binding?.btnPause!!.setBackgroundResource(R.drawable.button_background_pause)
            binding?.btnPause!!.isEnabled = true
        }, 1500)
        binding?.btnContinue!!.visibility = View.GONE
        binding?.btnContinue!!.isEnabled = false

        binding?.etTag!!.isEnabled = false
        binding?.etDescription!!.isEnabled = false


    }

    private fun pauseTimer() {
        state = 2
        // 2 is pause
        binding?.btnContinue!!.setBackgroundResource(R.drawable.button_background_cant_click)
        binding?.btnContinue!!.visibility = View.VISIBLE
        binding?.btnStart!!.isEnabled = false
        binding?.btnStart!!.setBackgroundResource(R.drawable.button_background_cant_click)
        Handler().postDelayed({
            binding?.btnContinue!!.setBackgroundResource(R.drawable.button_background_pause)
            binding?.btnContinue!!.isEnabled = true
        }, 1500)
        binding?.btnPause!!.visibility = View.GONE
        binding?.btnPause!!.isEnabled = false

        binding?.tvTitleMargin!!.visibility = View.VISIBLE
        binding?.tvTitleNoMargin!!.visibility = View.GONE

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding?.toolbarHome!!.setNavigationIcon(R.drawable.ic_back)
        binding?.toolbarHome!!.setNavigationOnClickListener {
            binding?.iconEdit!!.visibility = View.VISIBLE
            binding?.etDescription!!.isEnabled = true
            setUpSideBar()
            binding?.btnContinue!!.visibility = View.GONE
            binding?.btnContinue!!.isEnabled = false
            binding?.btnStart!!.visibility = View.VISIBLE
            Handler().postDelayed({
                binding?.btnStart!!.setBackgroundResource(R.drawable.button_background_pause)
                binding?.btnStart!!.isEnabled = true
            }, 1500)
            val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
            val editor = prefs.edit()
            state = 0
            editor.putInt("timerRunning", state)
            editor.apply()
            stopService(intentService)
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun scheduleNotification(notification: Notification, alarmTimeInMillis: Long) {
        val notificationIntent = Intent(this, NoticeReceiver::class.java)
        notificationIntent.putExtra(NoticeReceiver.NOTIFICATION_ID, 1)
        notificationIntent.putExtra(NoticeReceiver.NOTIFICATION, notification)

        pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager!!.set(AlarmManager.RTC_WAKEUP, alarmTimeInMillis, pendingIntent)

    }

    @SuppressLint("LaunchActivityFromNotification")
    private fun getNotification(content: String): Notification {
        val tempIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntentTemp = PendingIntent.getBroadcast(
            this,
            0,
            tempIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder = NotificationCompat.Builder(this, "notify-timer")
            .setSmallIcon(R.drawable.ic_diamond)
            .setContentTitle("Congratulation")
            .setContentText(content)
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setChannelId("10001")
        return builder.build()
    }
}
