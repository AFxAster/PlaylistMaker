package com.example.playlistmaker.audioplayer.presentation.state

import com.example.playlistmaker.audioplayer.presentation.model.PlayerTrackUI

sealed interface AudioPlayerState {
    data object Loading : AudioPlayerState
    data object Error : AudioPlayerState
    data class Content(val data: PlayerTrackUI) : AudioPlayerState
}