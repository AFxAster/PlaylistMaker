package com.example.playlistmaker.playlist.domain.api

import com.example.playlistmaker.playlist.domain.entity.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    fun insertPlaylist(playlist: Playlist): Long

    fun deletePlaylist(playlist: Playlist)

    fun getPlaylists(): Flow<List<Playlist>>

    fun getFlowablePlaylists(): Flow<List<Playlist>>

    fun getPlaylistById(id: Int): Flow<Playlist>
}