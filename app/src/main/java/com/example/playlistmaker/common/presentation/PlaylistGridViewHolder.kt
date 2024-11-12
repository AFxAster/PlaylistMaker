package com.example.playlistmaker.common.presentation

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistGridItemBinding
import com.example.playlistmaker.playlistLibrary.presentation.model.PlaylistItemUI

class PlaylistGridViewHolder(private val binding: PlaylistGridItemBinding) :
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
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.ic_placeholder)
                .into(artwork)
        }

        itemView.setOnClickListener {
            onPlaylistClickListener?.onPlaylistClick(model.id)
        }
    }
}