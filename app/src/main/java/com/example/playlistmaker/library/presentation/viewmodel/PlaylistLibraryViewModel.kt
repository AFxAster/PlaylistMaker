package com.example.playlistmaker.library.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.library.presentation.state.PlaylistLibraryState

class PlaylistLibraryViewModel : ViewModel() {
    private val state: MutableLiveData<PlaylistLibraryState> = MutableLiveData()
    fun getState(): LiveData<PlaylistLibraryState> = state

    init {
        state.value = PlaylistLibraryState.Empty
    }
}