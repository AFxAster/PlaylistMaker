package com.example.playlistmaker.library.presentation.state

import com.example.playlistmaker.common.entity.Track

sealed interface FavouriteState {
    data object Empty : FavouriteState
    data class Content(val data: List<Track>) : FavouriteState
}