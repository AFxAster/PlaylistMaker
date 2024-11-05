package com.example.playlistmaker.playlist.presentation.viewmodel

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistLibraryItemBinding
import com.example.playlistmaker.playlist.presentation.model.PlaylistItemUI

class PlaylistViewHolder(private val binding: PlaylistLibraryItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(model: PlaylistItemUI, onPlaylistClickListener: OnPlaylistClickListener?) {
        with(binding) {
            name.text = model.name
            tracksNumber.text = "${model.tracksNumber} треков" // todo

            Glide.with(itemView)
                .load(model.artworkPath)
                .placeholder(R.drawable.ic_placeholder)
                .into(artwork)
        }

        itemView.setOnClickListener {
            onPlaylistClickListener?.onPlaylistClick(model.id)
        }
    }

    fun interface OnPlaylistClickListener {
        fun onPlaylistClick(id: Long)
    }
}