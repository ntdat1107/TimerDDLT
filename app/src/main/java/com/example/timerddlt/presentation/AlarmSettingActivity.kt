package com.example.timerddlt.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.timerddlt.R
import android.media.MediaPlayer
import com.example.timerddlt.databinding.ActivityAlarmSettingBinding
import com.example.timerddlt.domain.model.Event


class AlarmSettingActivity : AppCompatActivity() {

    //  music
    private lateinit var mp: MediaPlayer
    private var binding: ActivityAlarmSettingBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmSettingBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarStatisic!!)
        binding?.toolbarStatisic!!.setNavigationOnClickListener {
            onBackPressed()
        }

        val musics: ArrayList<Event> = ArrayList()
        musics.add(Event("1", "2", 3, 4, 5, true))
        musics.add(Event("1", "2", 3, 4, 5, true))
        musics.add(Event("1", "2", 3, 4, 5, false))
    }
}