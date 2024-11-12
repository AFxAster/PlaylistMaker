package com.example.playlistmaker.playlist.presentation.state

import com.example.playlistmaker.playlistLibrary.domain.entity.Playlist

sealed interface PlaylistState {
    data object Loading : PlaylistState
    data class Content(val playlist: Playlist?) : PlaylistState
}