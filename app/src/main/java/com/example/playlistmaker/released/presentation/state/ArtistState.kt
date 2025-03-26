package com.example.playlistmaker.released.presentation.state

import com.example.playlistmaker.released.domain.entity.Artist

sealed interface ArtistState {
    data object Loading : ArtistState
    data object Error : ArtistState
    data object Empty : ArtistState
    data class Content(val artists: List<Artist>) : ArtistState
}