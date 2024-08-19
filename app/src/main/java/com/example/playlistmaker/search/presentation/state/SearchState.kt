package com.example.playlistmaker.search.presentation.state

import com.example.playlistmaker.search.presentation.model.TrackUI

sealed interface SearchState {
    data object Loading: SearchState
    data object Error: SearchState
    data class History(val data: List<TrackUI>): SearchState
    data class Content(val data: List<TrackUI>): SearchState
    data object Empty: SearchState
}