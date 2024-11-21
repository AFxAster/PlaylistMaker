package com.example.playlistmaker.playlist.presentation.mapper

import com.example.playlistmaker.playlist.presentation.model.PlaylistUI
import com.example.playlistmaker.playlistLibrary.domain.entity.Playlist

fun Playlist.toPlaylistUI(): PlaylistUI {
    return PlaylistUI(
        id = id,
        name = name,
        description = description,
        artworkPath = artworkPath,
        tracksNumber = tracksNumber
    )
}