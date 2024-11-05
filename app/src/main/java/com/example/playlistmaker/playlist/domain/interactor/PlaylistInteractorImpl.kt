package com.example.playlistmaker.playlist.domain.interactor

import com.example.playlistmaker.playlist.domain.api.PlaylistInteractor
import com.example.playlistmaker.playlist.domain.entity.Playlist
import com.example.playlistmaker.playlist.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository
) : PlaylistInteractor {
    override fun insertPlaylist(playlist: Playlist): Long =
        playlistRepository.insertPlaylist(playlist)


    override fun deletePlaylist(playlist: Playlist) {
        playlistRepository.deletePlaylist(playlist)
    }

    override fun getPlaylists(): Flow<List<Playlist>> =
        playlistRepository.getPlaylists()

    override fun getFlowablePlaylists(): Flow<List<Playlist>> =
        playlistRepository.getFlowablePlaylists()

    override fun getPlaylistById(id: Int): Flow<Playlist> =
        playlistRepository.getPlaylistById(id)
}