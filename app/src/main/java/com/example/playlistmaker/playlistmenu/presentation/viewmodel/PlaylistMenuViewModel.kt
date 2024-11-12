package com.example.playlistmaker.playlistmenu.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.common.entity.Track
import com.example.playlistmaker.playlistLibrary.domain.api.PlaylistInteractor
import com.example.playlistmaker.playlistLibrary.domain.entity.Playlist
import com.example.playlistmaker.settings.domain.api.SharingInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistMenuViewModel(
    private val id: Long,
    private val playlistInteractor: PlaylistInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {

    private val playlist: MutableLiveData<Playlist> = MutableLiveData()
    var tracks: List<Track> = emptyList()
        private set

    fun getPlaylist(): LiveData<Playlist> = playlist

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.getFlowablePlaylistById(id).collect { playlist ->
                this@PlaylistMenuViewModel.playlist.postValue(playlist)
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
