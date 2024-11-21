package com.example.playlistmaker.playlistLibrary.presentation.model

data class PlaylistItemUI(
    val id: Long,
    val name: String,
    val description: String,
    val artworkPath: String?,
    val tracksNumber: Int
)