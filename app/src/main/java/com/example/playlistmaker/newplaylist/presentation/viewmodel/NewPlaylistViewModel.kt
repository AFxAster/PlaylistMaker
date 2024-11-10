package com.example.playlistmaker.newplaylist.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.playlist.domain.api.ImageInteractor
import com.example.playlistmaker.playlist.domain.api.PlaylistInteractor
import com.example.playlistmaker.playlist.domain.entity.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val imageInteractor: ImageInteractor
) : ViewModel() {

    fun addPlaylist(playlist: Playlist) {
        viewModelScope.launch(Dispatchers.IO) {
            val playlistId = playlistInteractor.insertPlaylist(playlist)
            playlist.artworkPath?.let { path ->
                imageInteractor.saveImage(path, playlistId)
                playlistInteractor.updatePlaylist(
                    playlist.copy(
                        id = playlistId,
                        artworkPath = imageInteractor.getImagePathById(playlistId)
                    )
                )
            }
        }
    }
}