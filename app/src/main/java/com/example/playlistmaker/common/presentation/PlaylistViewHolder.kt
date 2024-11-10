package com.example.playlistmaker.common.presentation

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.playlist.presentation.model.PlaylistItemUI

abstract class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bind(model: PlaylistItemUI, onPlaylistClickListener: OnPlaylistClickListener?)

    fun interface OnPlaylistClickListener {
        fun onPlaylistClick(id: Long)
    }
}