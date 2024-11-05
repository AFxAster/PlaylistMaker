package com.example.playlistmaker.playlist.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlaylistLibraryItemBinding
import com.example.playlistmaker.playlist.presentation.model.PlaylistItemUI
import com.example.playlistmaker.playlist.presentation.viewmodel.PlaylistViewHolder

class PlaylistsAdapter : RecyclerView.Adapter<PlaylistViewHolder>() {
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
        return PlaylistViewHolder(PlaylistLibraryItemBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.bind(playlist, onPlaylistClickListener)
    }

    override fun getItemCount() = playlists.size

}

