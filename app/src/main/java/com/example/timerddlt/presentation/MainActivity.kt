package com.example.timerddlt.presentation

import android.app.Dialog
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
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.timerddlt.R
import com.example.timerddlt.databinding.ActivityMainBinding
import com.example.timerddlt.databinding.TimePickerDialogBinding
import com.example.timerddlt.services.BroadcastService
import com.google.android.material.navigation.NavigationView
import kotlin.math.ceil

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var binding: ActivityMainBinding? = null
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private var state: Int = 0
    private lateinit var intentService: Intent
    private var mTimeInMilis: Long = 600000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setUpSideBar()


        binding?.btnStart!!.setOnClickListener {
            mTimeInMilis = (binding?.tvTimer!!.text.toString().substring(0, 2)
                .toLong() * 60 + binding?.tvTimer!!.text.toString().substring(3, 5).toLong()) * 1000
            intentService = Intent(this, BroadcastService::class.java)
            intentService.putExtra("start-time", mTimeInMilis.toString())
            startTimer()
            startService(intentService)
        }

        binding?.btnPause!!.setOnClickListener {
            intentService.removeExtra("start-time-continue")
            intentService.removeExtra("start-time")
            intentService.putExtra("pause", 1)
            startService(intentService)
            pauseTimer()
        }

        binding?.btnContinue!!.setOnClickListener {
            mTimeInMilis = (binding?.tvTimer!!.text.toString().substring(0, 2)
                .toLong() * 60 + binding?.tvTimer!!.text.toString().substring(3, 5).toLong()) * 1000
            intentService.removeExtra("start-time-continue")
            intentService.removeExtra("pause")
            intentService.removeExtra("start-time")
            intentService.putExtra("start-time-continue", mTimeInMilis.toString())
            startTimer()
            startService(intentService)
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
            drawerLayout.isDrawerOpen(GravityCompat.START) -> {
                drawerLayout.close()
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
        drawerLayout.close()
        return true
    }

    private fun setUpSideBar() {
        drawerLayout = binding?.drawerLayout!!
        navigationView = binding?.navView!!
        setSupportActionBar(binding?.toolbarHome!!)

        navigationView.bringToFront()
        val newToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            binding?.toolbarHome,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(newToggle)
        newToggle.syncState()

        navigationView.setCheckedItem(R.id.nav_home)
        navigationView.setNavigationItemSelectedListener(this)
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // Update UI
            updateUI(intent)
        }
    }

    override fun onResume() {
        navigationView.setCheckedItem(R.id.nav_home)
        registerReceiver(broadcastReceiver, IntentFilter(BroadcastService.COUNTDOWN_BR))
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
        stopService(intentService)
        super.onDestroy()
    }

    private fun updateUI(intent: Intent?) {
        if (intent != null && intent.extras != null) {
            var millisUntilFinished: Long = intent.getLongExtra("countdown", 0)
            val temp: Float = ceil(millisUntilFinished.toFloat() / 1000)
            millisUntilFinished = (temp * 1000).toLong()

            Log.i("test", millisUntilFinished.toString())

            val minutes: Int = ((millisUntilFinished / 1000) / 60).toInt()
            val seconds: Int = ((millisUntilFinished / 1000) % 60).toInt()
            binding?.tvTimer!!.text = String.format("%02d:%02d", minutes, seconds)

            if (intent.getIntExtra("finish", 0) == 1) {
                stopService(intentService)
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
            Toast.makeText(this, "a", Toast.LENGTH_SHORT).show()
            setUpSideBar()
        }
    }

    private fun updateTextClock() {

    }
}
