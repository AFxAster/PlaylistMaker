package com.example.playlistmaker.playlistLibrary.domain.repository

import com.example.playlistmaker.common.entity.Track
import com.example.playlistmaker.playlistLibrary.domain.entity.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    fun insertPlaylist(playlist: Playlist): Long
    fun updatePlaylist(playlist: Playlist)
    fun deletePlaylist(id: Long)
    fun getPlaylists(): Flow<List<Playlist>>
    fun getFlowablePlaylists(): Flow<List<Playlist>>
    fun getPlaylistById(id: Long): Flow<Playlist>
    fun getFlowablePlaylistById(id: Long): Flow<Playlist?>
    fun addTrackToPlaylist(track: Track, playlistId: Long)
    fun deleteTrackFromPlaylist(trackId: String, playlistId: Long)
    fun getTracksToPlaylist(playlistId: Long): Flow<List<Track>>
}