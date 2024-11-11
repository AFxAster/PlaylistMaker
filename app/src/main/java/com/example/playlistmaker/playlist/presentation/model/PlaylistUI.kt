package com.example.playlistmaker.playlist.presentation.model

data class PlaylistUI(
    val id: Long,
    val name: String,
    val description: String,
    val artworkPath: String?,
    val tracksNumber: Int
)
