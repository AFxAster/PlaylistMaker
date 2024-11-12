package com.example.playlistmaker.playlist.presentation.viewmodel

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

class PlaylistViewModel(
    private val id: Long,
    private val playlistInteractor: PlaylistInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {

    private val playlist: MutableLiveData<Playlist> = MutableLiveData()
    private val tracks: MutableLiveData<List<Track>> = MutableLiveData()
    fun getPlaylist(): LiveData<Playlist> = playlist
    fun getTracks(): LiveData<List<Track>> = tracks

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.getFlowablePlaylistById(id).collect { playlist ->
                this@PlaylistViewModel.playlist.postValue(playlist)
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.getTracksToPlaylist(id).collect { trackList ->
                tracks.postValue(trackList)
            }
        }
    }

    fun deleteTrack(trackId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.deleteTrackFromPlaylist(trackId, playlistId = id)
        }
    }

    fun sharePlaylist(text: String) {
        sharingInteractor.shareText(text)
    }
}
