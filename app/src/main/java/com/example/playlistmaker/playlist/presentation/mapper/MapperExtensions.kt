package com.example.playlistmaker.playlist.presentation.mapper

import com.example.playlistmaker.playlist.presentation.model.PlaylistUI
import com.example.playlistmaker.playlistLibrary.domain.entity.Playlist
import java.text.SimpleDateFormat
import java.util.Locale

fun Playlist.toPlaylistUI(): PlaylistUI {
    return PlaylistUI(
        id = id,
        name = name,
        description = description,
        artworkPath = artworkPath,
        tracksNumber = tracksNumber
    )
}

fun Long.toFormattedTime(): String {
    return SimpleDateFormat(
        "m",
        Locale.getDefault()
    ).format(this)
}