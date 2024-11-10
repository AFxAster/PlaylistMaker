package com.example.playlistmaker.playlistLibrary.presentation.mapper

import com.example.playlistmaker.playlistLibrary.domain.entity.Playlist
import com.example.playlistmaker.playlistLibrary.presentation.model.PlaylistItemUI

fun Playlist.toPlaylistItemUI(): PlaylistItemUI {
    return PlaylistItemUI(
        id = id,
        name = name,
        description = description,
        artworkPath = artworkPath,
        tracksNumber = tracksNumber
    )
}
