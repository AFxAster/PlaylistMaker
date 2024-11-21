package com.example.playlistmaker.editplaylist.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.newplaylist.presentation.viewmodel.NewPlaylistViewModel
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

    private val playlist: MutableLiveData<Playlist> = MutableLiveData()
    fun getPlaylist(): LiveData<Playlist> = playlist

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.getPlaylistById(id).collect { playlist ->
                this@EditPlaylistViewModel.playlist.postValue(playlist)
            }
        }
    }

    fun updatePlaylist(playlist: Playlist) {
        viewModelScope.launch(Dispatchers.IO) {
            if (this@EditPlaylistViewModel.playlist.value!!.artworkPath != playlist.artworkPath)
                imageInteractor.saveImage(playlist.artworkPath!!, id)

            playlistInteractor.updatePlaylist(
                playlist.copy(
                    artworkPath = imageInteractor.getImagePathById(id)
                )
            )
        }
    }
}
