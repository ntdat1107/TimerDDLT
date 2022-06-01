package com.example.timerddlt.presentation

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timerddlt.R
import com.example.timerddlt.adapter.MusicAdapter
import com.example.timerddlt.databinding.ActivityMusicBinding
import com.example.timerddlt.domain.model.Music

class MusicActivity : AppCompatActivity() {
    private var binding: ActivityMusicBinding? = null
    private var mp: MediaPlayer? = null
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
        musics.add(Music("Raining sound", "rain_sound", "Raining sound for chill"))
        musics.add(Music("Far from home", "far_from_home", "Sad music"))
        musics.add(Music("Lofi study", "lofi_study", "Lofi for studying"))
        musics.add(Music("Chill lofi", "chill_lofi", "Lofi for chilling"))
        musics.add(Music("Relax music", "relax", "Music for relaxing"))

        val musicAdapter = MusicAdapter(this, this, musics)
        binding?.rvMusic!!.adapter = musicAdapter

        binding?.rvMusic!!.layoutManager = LinearLayoutManager(this)

        settingMP(currentMusic)

        binding?.btnPlay!!.setOnClickListener {
            if (mp!!.isPlaying) {
                mp!!.pause()
                binding?.btnPlay!!.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            } else {
                mp!!.start()
                binding?.btnPlay!!.setImageResource(R.drawable.ic_baseline_pause_24)
            }
        }

        binding?.btnNext!!.setOnClickListener {
            mp!!.stop()
            mp = null
            binding?.llNavigation!!.visibility = View.INVISIBLE
            if (currentMusic == musics.size - 1) {
                currentMusic = 0
            } else {
                currentMusic += 1
            }
            Handler().postDelayed({
                settingMP(currentMusic)
                binding?.llNavigation!!.visibility = View.VISIBLE
                mp!!.start()
                binding?.btnPlay!!.setImageResource(R.drawable.ic_baseline_pause_24)
            }, 1250)
        }

        binding?.btnPrev!!.setOnClickListener {
            mp!!.stop()
            mp = null
            binding?.llNavigation!!.visibility = View.INVISIBLE
            if (currentMusic == 0) {
                currentMusic = musics.size - 1
            } else {
                currentMusic -= 1
            }
            Handler().postDelayed({
                settingMP(currentMusic)
                binding?.llNavigation!!.visibility = View.VISIBLE
                mp!!.start()
                binding?.btnPlay!!.setImageResource(R.drawable.ic_baseline_pause_24)
            }, 1250)
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

    fun settingMP(nowPos: Int, isActivity: Boolean) {

        currentMusic = nowPos

        if (!isActivity) {
            mp!!.stop()
            mp = null
        }
        binding?.llNavigation!!.visibility = View.INVISIBLE
        Handler().postDelayed({
            binding?.tvName!!.text = musics[nowPos].name
            mp =
                MediaPlayer.create(
                    this,
                    resources.getIdentifier(musics[nowPos].rawName, "raw", packageName)
                )
            mp!!.isLooping = true
            val totalTime = mp!!.duration
            binding?.sbTime!!.max = totalTime
            binding?.sbTime!!.setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        if (fromUser) {
                            mp!!.seekTo(progress)
                        }
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    }

                })

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
                while (mp != null) {
                    try {
                        if (mp!!.isPlaying) {
                            val msg = Message()
                            msg.what = mp!!.currentPosition
                            handler.sendMessage(msg)
                            Thread.sleep(1000)
                        }
                    } catch (e: InterruptedException) {

                    }
                }
            }).start()

            if (!isActivity) {
                binding?.llNavigation!!.visibility = View.VISIBLE
                mp!!.start()
                binding?.btnPlay!!.setImageResource(R.drawable.ic_baseline_pause_24)
            }
        }, 1250)
    }

    private fun settingMP(nowPos: Int) {
        currentMusic = nowPos
        binding?.tvName!!.text = musics[nowPos].name
        mp =
            MediaPlayer.create(
                this,
                resources.getIdentifier(musics[nowPos].rawName, "raw", packageName)
            )
        mp!!.isLooping = true
        val totalTime = mp!!.duration
        binding?.sbTime!!.max = totalTime
        binding?.sbTime!!.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        mp!!.seekTo(progress)
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
            while (mp != null) {
                try {
                    if (mp!!.isPlaying) {
                        val msg = Message()
                        msg.what = mp!!.currentPosition
                        handler.sendMessage(msg)
                        Thread.sleep(1000)
                    }
                } catch (e: InterruptedException) {

                }
            }
        }).start()
    }

    private fun createTimeLabel(time: Int): String {
        var timeLabel: String
        val min = time / 1000 / 60
        val sec = time / 1000 % 60
        timeLabel = "$min:"
        if (sec < 10) timeLabel += "0"
        timeLabel += sec
        return timeLabel
    }

    override fun onPause() {
        mp!!.pause()
        binding?.btnPlay!!.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        super.onPause()
    }

    override fun onDestroy() {
        mp!!.stop()
        mp = null
        super.onDestroy()
    }
}