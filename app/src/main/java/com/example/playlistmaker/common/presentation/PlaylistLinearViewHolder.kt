package com.example.playlistmaker.common.presentation

import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistLinearItemBinding
import com.example.playlistmaker.playlist.presentation.model.PlaylistItemUI

class PlaylistLinearViewHolder(private val binding: PlaylistLinearItemBinding) :
    PlaylistViewHolder(binding.root) {

    override fun bind(model: PlaylistItemUI, onPlaylistClickListener: OnPlaylistClickListener?) {
        with(binding) {
            name.text = model.name
            tracksNumber.text =
                binding.root.resources.getQuantityString(
                    R.plurals.track,
                    model.tracksNumber,
                    model.tracksNumber
                )

            Glide.with(itemView)
                .load(model.artworkPath)
                .placeholder(R.drawable.ic_placeholder)
                .into(artwork)
        }

        itemView.setOnClickListener {
            onPlaylistClickListener?.onPlaylistClick(model.id)
        }
    }
}