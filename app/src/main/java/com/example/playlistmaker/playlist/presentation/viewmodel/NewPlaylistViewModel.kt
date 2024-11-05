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
//        saveImage(playlist.artworkPath, id.toString())
            Log.d("my", "$id")
        }
    }

    private fun saveImage(path: String, id: String) {
        imageInteractor.saveImage(path, id)
    }
}