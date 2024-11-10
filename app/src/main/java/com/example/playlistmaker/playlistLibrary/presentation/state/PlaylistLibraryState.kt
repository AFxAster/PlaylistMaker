package com.example.playlistmaker.playlistLibrary.presentation.state

import com.example.playlistmaker.playlistLibrary.domain.entity.Playlist

sealed interface PlaylistLibraryState {
    data object Empty : PlaylistLibraryState
    data class Content(val playlists: List<Playlist>) : PlaylistLibraryState
}