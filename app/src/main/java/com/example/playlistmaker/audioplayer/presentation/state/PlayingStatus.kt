package com.example.playlistmaker.audioplayer.presentation.state

sealed interface PlayingStatus {
    data class Playing(val position: String): PlayingStatus
    data class Paused(val position: String): PlayingStatus
    data object Completed: PlayingStatus
}