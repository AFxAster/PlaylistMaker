package com.example.playlistmaker.playlist.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.playlist.domain.api.PlaylistInteractor
import com.example.playlistmaker.playlist.presentation.state.PlaylistLibraryState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistLibraryViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {
    private val state: MutableLiveData<PlaylistLibraryState> = MutableLiveData()
    fun getState(): LiveData<PlaylistLibraryState> = state

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.getFlowablePlaylists().collect { playlists ->
                if (playlists.isEmpty())
                    state.postValue(PlaylistLibraryState.Empty)
                else
                    state.postValue(PlaylistLibraryState.Content(playlists))
            }
        }
    }
}