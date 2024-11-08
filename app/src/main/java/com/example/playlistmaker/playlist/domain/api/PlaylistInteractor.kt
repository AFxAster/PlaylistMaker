package com.example.playlistmaker.playlist.domain.api

import com.example.playlistmaker.common.entity.Track
import com.example.playlistmaker.playlist.domain.entity.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    fun insertPlaylist(playlist: Playlist): Long
    fun updatePlaylist(playlist: Playlist)
    fun deletePlaylist(playlist: Playlist)
    fun getPlaylists(): Flow<List<Playlist>>
    fun getFlowablePlaylists(): Flow<List<Playlist>>
    fun getPlaylistById(id: Long): Flow<Playlist>
    fun addTrackToPlaylist(track: Track, playlistId: Long)
}