package com.example.playlistmaker.library.presentation.state

sealed interface FavouriteState {
    data object Empty : FavouriteState
    data object Content : FavouriteState
}