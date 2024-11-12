package com.example.playlistmaker.playlistmenu.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.common.entity.Track
import com.example.playlistmaker.playlist.presentation.state.PlaylistState
import com.example.playlistmaker.playlistLibrary.domain.api.PlaylistInteractor
import com.example.playlistmaker.settings.domain.api.SharingInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistMenuViewModel(
    private val id: Long,
    private val playlistInteractor: PlaylistInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {

    private val state: MutableLiveData<PlaylistState> = MutableLiveData()
    var tracks: List<Track> = emptyList()
        private set

    fun getState(): LiveData<PlaylistState> = state

    init {
        loadData()
    }

    private fun loadData() {
        state.value = PlaylistState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.getFlowablePlaylistById(id).collect { playlist ->
                state.postValue(PlaylistState.Content(playlist))
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.getTracksToPlaylist(id).collect { trackList ->
                tracks = trackList
            }
        }
    }

    fun deleteThisPlaylist() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.deletePlaylist(id)
        }
    }

    fun sharePlaylist(text: String) {
        sharingInteractor.shareText(text)
    }
}
