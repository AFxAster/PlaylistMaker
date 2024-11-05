package com.example.playlistmaker.playlist.presentation.viewmodel

import android.util.Log
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
            val id = playlistInteractor.insertPlaylist(playlist)
            playlist.artworkPath?.let { path ->
                imageInteractor.saveImage(path, id.toString())
                playlistInteractor.updatePlaylist(
                    playlist.copy(
                        id = id,
                        artworkPath = imageInteractor.getImagePathById(
                            id.toString()
                        ).also { Log.d("my", it) }
                    )
                )
            }
        }
    }
}