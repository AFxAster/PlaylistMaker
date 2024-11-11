package com.example.playlistmaker.playlist.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.common.entity.Track
import com.example.playlistmaker.playlist.presentation.state.PlaylistState
import com.example.playlistmaker.playlistLibrary.domain.api.PlaylistInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val id: Long,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private val state: MutableLiveData<PlaylistState> = MutableLiveData()
    private val tracks: MutableLiveData<List<Track>> = MutableLiveData()
    fun getState(): LiveData<PlaylistState> = state
    fun getTracks(): LiveData<List<Track>> = tracks

    init {
        loadData()
    }

    private fun loadData() {
        state.value = PlaylistState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.getPlaylistById(id).collect { playlist ->
                state.postValue(PlaylistState.Content(playlist))
            }
            playlistInteractor.getTracksToPlaylist(id).collect { trackList ->
                tracks.postValue(trackList)
            }
        }
    }

    fun deleteTrack(trackId: String) {
        tracks.value = tracks.value!!.toMutableList().apply { removeIf { it.trackId == trackId } }
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.deleteTrackFromPlaylist(trackId, playlistId = id)
        }
    }
}