package com.example.playlistmaker.playlistLibrary.domain.interactor

import com.example.playlistmaker.common.entity.Track
import com.example.playlistmaker.playlistLibrary.domain.api.PlaylistInteractor
import com.example.playlistmaker.playlistLibrary.domain.entity.Playlist
import com.example.playlistmaker.playlistLibrary.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository
) : PlaylistInteractor {
    override fun insertPlaylist(playlist: Playlist): Long =
        playlistRepository.insertPlaylist(playlist)

    override fun updatePlaylist(playlist: Playlist) {
        playlistRepository.updatePlaylist(playlist)
    }

    override fun deletePlaylist(id: Long) {
        playlistRepository.deletePlaylist(id)
    }

    override fun getPlaylists(): Flow<List<Playlist>> =
        playlistRepository.getPlaylists()

    override fun getFlowablePlaylists(): Flow<List<Playlist>> =
        playlistRepository.getFlowablePlaylists()

    override fun getPlaylistById(id: Long): Flow<Playlist> =
        playlistRepository.getPlaylistById(id)

    override fun getFlowablePlaylistById(id: Long): Flow<Playlist?> =
        playlistRepository.getFlowablePlaylistById(id)

    override fun addTrackToPlaylist(track: Track, playlistId: Long) {
        playlistRepository.addTrackToPlaylist(track, playlistId)
    }

    override fun deleteTrackFromPlaylist(trackId: String, playlistId: Long) {
        playlistRepository.deleteTrackFromPlaylist(trackId, playlistId)
    }

    override fun getTracksToPlaylist(playlistId: Long): Flow<List<Track>> =
        playlistRepository.getTracksToPlaylist(playlistId)
}