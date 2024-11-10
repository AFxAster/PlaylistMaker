package com.example.playlistmaker.playlistLibrary.domain.entity

data class Playlist(
    val id: Long,
    val name: String,
    val description: String,
    val artworkPath: String?,
    val trackIds: List<String>
) {
    val tracksNumber: Int
        get() = trackIds.size
}