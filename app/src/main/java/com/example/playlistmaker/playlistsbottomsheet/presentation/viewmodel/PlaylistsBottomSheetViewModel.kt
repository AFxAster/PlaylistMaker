package com.example.playlistmaker.playlistsbottomsheet.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.playlist.domain.api.PlaylistInteractor
import com.example.playlistmaker.playlist.domain.entity.Playlist
import com.example.playlistmaker.playlistsbottomsheet.presentation.state.AddingStatus
import com.example.playlistmaker.search.domain.api.TracksInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistsBottomSheetViewModel(
    private val trackId: String,
    private val playlistInteractor: PlaylistInteractor,
    private val tracksInteractor: TracksInteractor
) : ViewModel() {

    private val playlists: MutableLiveData<List<Playlist>> = MutableLiveData()
    private val addingStatus: MutableLiveData<AddingStatus> = MutableLiveData()

    fun getPlaylists(): LiveData<List<Playlist>> = playlists
    fun getAddingStatus(): LiveData<AddingStatus> = addingStatus

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.getFlowablePlaylists().collect { list ->
                playlists.postValue(list)
            }
        }
    }

    fun addToPlaylist(playlistId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val playlist = playlists.value!!.find { it.id == playlistId }!!
            if (playlist.trackIds.contains(trackId)) {
                addingStatus.postValue(AddingStatus.AlreadyAdded(playlist.name))
                return@launch
            }
            tracksInteractor.getTrackById(trackId).collect { track ->
                track?.let {
                    playlistInteractor.addTrackToPlaylist(it, playlistId)
                    addingStatus.postValue(AddingStatus.WasAdded(playlist.name))
                }
            }
        }
    }
}