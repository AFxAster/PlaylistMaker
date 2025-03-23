package com.example.playlistmaker.released.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ReleasedGridItemBinding
import com.example.playlistmaker.released.domain.entity.Release

class ReleaseAdapter : RecyclerView.Adapter<ReleaseAdapter.ReleaseViewHolder>() {
    var releases: List<Release> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReleaseViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ReleaseViewHolder(
            ReleasedGridItemBinding.inflate(layoutInflater)
        )
    }

    override fun onBindViewHolder(holder: ReleaseViewHolder, position: Int) {
        holder.bind(releases[position])
    }

    override fun getItemCount(): Int = releases.size

    class ReleaseViewHolder(private val binding: ReleasedGridItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: Release) {
            with(binding) {
                releaseName.text = model.releaseName
                artistName.text = model.artistName
                type.text = model.type

                Glide.with(itemView)
                    .load(model.artworkUrl100)
                    .placeholder(R.drawable.ic_placeholder)
                    .into(artwork)
            }
        }
    }
}

