package com.example.playlistmaker.playlist.data.mapper

import com.example.playlistmaker.playlist.data.db.entity.PlaylistEntity
import com.example.playlistmaker.playlist.domain.entity.Playlist

fun Playlist.toPlaylistEntity(): PlaylistEntity {
    return PlaylistEntity(
        id = id,
        name = name,
        description = description,
        artworkPath = artworkPath,
        trackIds = trackIds,
        tracksNumber = tracksNumber
    )
}

fun PlaylistEntity.toPlaylist(): Playlist {
    return Playlist(
        id = id,
        name = name,
        description = description,
        artworkPath = artworkPath,
        trackIds = trackIds,
        tracksNumber = tracksNumber
    )
}