package com.example.playlistmaker.common.presentation.state

sealed interface PlaylistsViewState {
    data object Linear : PlaylistsViewState
    data object Grid : PlaylistsViewState
}