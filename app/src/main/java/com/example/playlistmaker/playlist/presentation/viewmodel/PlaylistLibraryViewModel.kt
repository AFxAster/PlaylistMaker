package com.example.playlistmaker.playlist.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.playlist.presentation.state.PlaylistLibraryState

class PlaylistLibraryViewModel : ViewModel() {
    private val state: MutableLiveData<PlaylistLibraryState> = MutableLiveData()
    fun getState(): LiveData<PlaylistLibraryState> = state

    init {
        state.value = PlaylistLibraryState.Empty
    }
}