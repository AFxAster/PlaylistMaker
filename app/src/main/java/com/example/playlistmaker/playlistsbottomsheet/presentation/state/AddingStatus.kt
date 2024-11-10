package com.example.playlistmaker.playlistsbottomsheet.presentation.state

sealed interface AddingStatus {
    data class AlreadyAdded(val playlistName: String) : AddingStatus
    data class WasAdded(val playlistName: String) : AddingStatus
}