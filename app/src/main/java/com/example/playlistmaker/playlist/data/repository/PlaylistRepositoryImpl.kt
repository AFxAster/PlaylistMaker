package com.example.playlistmaker.playlist.data.repository

import com.example.playlistmaker.common.data.db.AppDatabase
import com.example.playlistmaker.playlist.data.mapper.toPlaylist
import com.example.playlistmaker.playlist.data.mapper.toPlaylistEntity
import com.example.playlistmaker.playlist.domain.entity.Playlist
import com.example.playlistmaker.playlist.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    private val database: AppDatabase
) : PlaylistRepository {

    override fun insertPlaylist(playlist: Playlist): Long =
        database.getPlaylistDao().insertPlaylist(playlist.toPlaylistEntity())

    override fun updatePlaylist(playlist: Playlist) {
        database.getPlaylistDao().updatePlaylist(playlist.toPlaylistEntity())
    }

    override fun deletePlaylist(playlist: Playlist) {
        database.getPlaylistDao().deletePlaylist(playlist.toPlaylistEntity())
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = database.getPlaylistDao().getPlaylists().map { it.toPlaylist() }
        emit(playlists)
    }

    override fun getFlowablePlaylists(): Flow<List<Playlist>> =
        database.getPlaylistDao().getFlowablePlaylists().map { it.map { it.toPlaylist() } }

    override fun getPlaylistById(id: Long): Flow<Playlist> = flow {
        val playlist = database.getPlaylistDao().getPlaylistById(id).toPlaylist()
        emit(playlist)
    }
}