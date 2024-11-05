package com.example.playlistmaker.playlist.presentation.state

sealed interface PlaylistLibraryState {
    data object Empty : PlaylistLibraryState
    data object Content : PlaylistLibraryState
}