package com.example.timerddlt.presentation

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timerddlt.R
import com.example.timerddlt.adapter.MusicAdapter
import com.example.timerddlt.databinding.ActivityMusicBinding
import com.example.timerddlt.domain.model.Music

class MusicActivity : AppCompatActivity() {
    private var binding: ActivityMusicBinding? = null
    private lateinit var mp: MediaPlayer
    private var currentMusic: Int = 0
    private var musics: ArrayList<Music> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarMusic)
        binding?.toolbarMusic!!.setNavigationOnClickListener {
            onBackPressed()
        }

        musics = ArrayList()
        musics.add(Music("Phút ban đầu 1", "phutbandau", "Nothing"))
        musics.add(Music("Phút ban đầu 2", "phutbandau", "Nothing"))
        musics.add(Music("Phút ban đầu 3", "phutbandau", "Nothing"))
        musics.add(Music("Phút ban đầu 4", "phutbandau", "Nothing"))

        val musicAdapter = MusicAdapter(this, this, musics)
        binding?.rvMusic!!.adapter = musicAdapter

        binding?.rvMusic!!.layoutManager = LinearLayoutManager(this)

        settingMP(currentMusic)

        binding?.btnPlay!!.setOnClickListener {
            if (mp.isPlaying) {
                mp.pause()
                binding?.btnPlay!!.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            } else {
                mp.start()
                binding?.btnPlay!!.setImageResource(R.drawable.ic_baseline_pause_24)
            }
        }

        binding?.btnNext!!.setOnClickListener {
            mp.stop()
            if (currentMusic == musics.size - 1) {
                currentMusic = 0
            } else {
                currentMusic += 1
            }
            settingMP(currentMusic)
            mp.start()
            binding?.btnPlay!!.setImageResource(R.drawable.ic_baseline_pause_24)
        }

        binding?.btnPrev!!.setOnClickListener {
            mp.stop()
            if (currentMusic == 0) {
                currentMusic = musics.size - 1
            } else {
                currentMusic -= 1
            }
            settingMP(currentMusic)
            mp.start()
            binding?.btnPlay!!.setImageResource(R.drawable.ic_baseline_pause_24)
        }
        binding?.btnSelect!!.setOnClickListener {
            AlertDialog.Builder(this).setTitle("Setup music")
                .setMessage("Are you sure to set this music for your app")
                .setPositiveButton("Yes") { dialog, _ ->
                    dialog.dismiss()
                    val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
                    val editor = prefs.edit()
                    editor.putString("rawNameMusic", musics[currentMusic].rawName)
                    editor.putString("nameMusic", musics[currentMusic].name)
                    editor.apply()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

    }

    fun settingMP(nowPos: Int, isActivity: Boolean = true) {
        currentMusic = nowPos

        if (!isActivity) {
            mp.stop()
        }

        binding?.tvName!!.text = musics[nowPos].name
        mp =
            MediaPlayer.create(
                this,
                resources.getIdentifier(musics[nowPos].rawName, "raw", packageName)
            )
        mp.isLooping = true
        val totalTime = mp.duration
        binding?.sbTime!!.max = totalTime
        binding?.sbTime!!.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        mp.seekTo(progress)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }

            }
        )

        @SuppressLint("HandlerLeak")
        val handler =
            object : Handler() {
                override fun handleMessage(msg: Message) {
                    val currentPosition = msg.what

                    binding?.sbTime!!.progress = currentPosition
                    val elapsedTime = createTimeLabel(currentPosition)
                    binding?.elapsedTimeLabel!!.text = elapsedTime
                    binding?.remainingTimeLabel!!.text =
                        createTimeLabel(totalTime - currentPosition)
                }
            }

        Thread(Runnable {
            while (true) {
                try {
                    val msg = Message()
                    msg.what = mp.currentPosition
                    handler.sendMessage(msg)
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {

                }
            }
        }).start()

        if (!isActivity) {
            mp.start()
            binding?.btnPlay!!.setImageResource(R.drawable.ic_baseline_pause_24)
        }
    }

    private fun createTimeLabel(time: Int): String {
        var timeLabel = ""
        val min = time / 1000 / 60
        val sec = time / 1000 % 60
        timeLabel = "$min:"
        if (sec < 10) timeLabel += "0"
        timeLabel += sec
        return timeLabel
    }

    override fun onPause() {
        mp.pause()
        binding?.btnPlay!!.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        super.onPause()
    }

    override fun onDestroy() {
        mp.stop()
        super.onDestroy()
    }
}