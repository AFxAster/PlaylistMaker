package com.example.playlistmaker.released.presentation.state

import com.example.playlistmaker.released.domain.entity.Release

sealed interface ReleasedState {
    data object Loading : ReleasedState
    data object Error : ReleasedState

    // todo Empty
    data class Content(val releases: List<Release>) : ReleasedState
}