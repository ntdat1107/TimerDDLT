package com.example.timerddlt.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.timerddlt.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {
    private var binding: ActivitySettingBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarSetting)
        binding?.toolbarSetting!!.setNavigationOnClickListener {
            onBackPressed()
        }

        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        var isNotify = prefs.getBoolean("isNotify", true)

        binding?.switchNotification!!.isChecked = isNotify
        binding?.switchNotification!!.setOnClickListener {
            isNotify = binding?.switchNotification!!.isChecked
            val editor = prefs.edit()
            editor.putBoolean("isNotify", isNotify)
            editor.apply()
        }
    }
}