package com.example.playlistmaker.common.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.common.presentation.state.PlaylistsViewState
import com.example.playlistmaker.databinding.PlaylistGridItemBinding
import com.example.playlistmaker.databinding.PlaylistLinearItemBinding
import com.example.playlistmaker.playlist.presentation.model.PlaylistItemUI

class PlaylistsAdapter(
    private val state: PlaylistsViewState
) : RecyclerView.Adapter<PlaylistViewHolder>() {

    var playlists: List<PlaylistItemUI> = emptyList()
        set(value) {
            val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int = field.size

                override fun getNewListSize(): Int = value.size

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                    field[oldItemPosition].id == value[newItemPosition].id

                override fun areContentsTheSame(
                    oldItemPosition: Int,
                    newItemPosition: Int
                ): Boolean = field[oldItemPosition] == value[newItemPosition]
            })
            field = value
            diffResult.dispatchUpdatesTo(this)
        }
    var onPlaylistClickListener: PlaylistViewHolder.OnPlaylistClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (state) {
            is PlaylistsViewState.Linear -> PlaylistLinearViewHolder(
                PlaylistLinearItemBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            )

            PlaylistsViewState.Grid -> PlaylistGridViewHolder(
                PlaylistGridItemBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.bind(playlist, onPlaylistClickListener)
    }

    override fun getItemCount() = playlists.size
}