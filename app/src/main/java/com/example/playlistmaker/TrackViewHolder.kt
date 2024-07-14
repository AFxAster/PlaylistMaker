package com.example.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackNameTextView: TextView = itemView.findViewById(R.id.track_name)
    private val artistNameTextView: TextView = itemView.findViewById(R.id.artist_name)
    private val trackTimeTextView: TextView = itemView.findViewById(R.id.track_time)
    private val artworkImageView: ImageView = itemView.findViewById(R.id.artwork)
    fun bind(model: Track, onTrackClickListener: OnTrackClickListener?) {
        trackNameTextView.text = model.trackName
        artistNameTextView.text = model.artistName
        artistNameTextView.updateLayoutParams {
            width = 0
        }
        trackTimeTextView.text = model.getFormattedTime()
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.ic_placeholder)
            .into(artworkImageView)
        itemView.setOnClickListener {
            onTrackClickListener?.onTrackClick(model)
        }
    }

    fun interface OnTrackClickListener {
        fun onTrackClick(track: Track)
    }
}