package com.example.playlistmaker.playlist.presentation.state

import com.example.playlistmaker.playlist.domain.entity.Playlist

sealed interface PlaylistLibraryState {
    data object Empty : PlaylistLibraryState
    data class Content(val playlists: List<Playlist>) : PlaylistLibraryState
}