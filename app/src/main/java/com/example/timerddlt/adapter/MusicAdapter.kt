package com.example.timerddlt.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.timerddlt.databinding.MusicItemBinding
import com.example.timerddlt.domain.model.Music
import com.example.timerddlt.presentation.MusicActivity

class MusicAdapter(
    private var activity: MusicActivity,
    private var context: Context,
    private var musics: ArrayList<Music>
) :
    RecyclerView.Adapter<MusicAdapter.ViewHolder>() {
    class ViewHolder(val binding: MusicItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(MusicItemBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val music: Music = musics[position]
        holder.binding.tvMusicName.text = music.name
        holder.binding.tvMusicDescription.text = music.musicDescription
        holder.binding.itemMusic.setOnClickListener {
            activity.settingMP(position, false)
        }
    }

    override fun getItemCount(): Int {
        return musics.size
    }
}