package com.example.playlistmaker.playlist.presentation.mapper

import com.example.playlistmaker.playlist.domain.entity.Playlist
import com.example.playlistmaker.playlist.presentation.model.PlaylistItemUI

fun Playlist.toPlaylistItemUI(): PlaylistItemUI {
    return PlaylistItemUI(
        id = id,
        name = name,
        description = description,
        artworkPath = artworkPath,
        tracksNumber = tracksNumber
    )
}
