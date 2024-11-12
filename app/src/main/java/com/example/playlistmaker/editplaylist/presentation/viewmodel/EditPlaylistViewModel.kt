package com.example.playlistmaker.editplaylist.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.newplaylist.presentation.viewmodel.NewPlaylistViewModel
import com.example.playlistmaker.playlist.presentation.state.PlaylistState
import com.example.playlistmaker.playlistLibrary.domain.api.ImageInteractor
import com.example.playlistmaker.playlistLibrary.domain.api.PlaylistInteractor
import com.example.playlistmaker.playlistLibrary.domain.entity.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    private val id: Long,
    playlistInteractor: PlaylistInteractor,
    imageInteractor: ImageInteractor
) : NewPlaylistViewModel(playlistInteractor, imageInteractor) {

    private val state: MutableLiveData<PlaylistState> = MutableLiveData()
    fun getState(): LiveData<PlaylistState> = state

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.getPlaylistById(id).collect { playlist ->
                state.postValue(PlaylistState.Content(playlist))
            }
        }
    }

    fun updatePlaylist(playlist: Playlist) {
        viewModelScope.launch(Dispatchers.IO) {
            if ((state.value as PlaylistState.Content).playlist!!.artworkPath != playlist.artworkPath)
                imageInteractor.saveImage(playlist.artworkPath!!, id)

            playlistInteractor.updatePlaylist(
                playlist.copy(
                    artworkPath = imageInteractor.getImagePathById(id)
                )
            )
        }
    }
}
