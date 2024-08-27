package com.example.playlistmaker.library.presentation.state

sealed interface PlaylistLibraryState {
    data object Empty : PlaylistLibraryState
    data object Content : PlaylistLibraryState
}