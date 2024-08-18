package com.example.playlistmaker.search.ui

import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackListItemBinding
import com.example.playlistmaker.search.presentation.model.TrackUI

class TrackViewHolder(private val binding: TrackListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(model: TrackUI, onTrackClickListener: OnTrackClickListener?) {
        binding.trackName.text = model.trackName
        binding.artistName.text = model.artistName
        binding.artistName.updateLayoutParams {
            width = 0
        }
        binding.trackTime.text = model.trackTime
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.ic_placeholder)
            .into(binding.artwork)

        itemView.setOnClickListener {
            onTrackClickListener?.onTrackClick(model.trackId)
        }
    }

    fun interface OnTrackClickListener {
        fun onTrackClick(id: String)
    }
}