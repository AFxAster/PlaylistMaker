package com.example.playlistmaker.playlist.data.mapper

import com.example.playlistmaker.common.entity.Track
import com.example.playlistmaker.playlist.data.db.entity.PlaylistEntity
import com.example.playlistmaker.playlist.data.db.entity.TrackEntity
import com.example.playlistmaker.playlist.domain.entity.Playlist

fun Playlist.toPlaylistEntity(): PlaylistEntity {
    return PlaylistEntity(
        id = id,
        name = name,
        description = description,
        artworkPath = artworkPath,
        trackIds = trackIds
    )
}

fun PlaylistEntity.toPlaylist(): Playlist {
    return Playlist(
        id = id,
        name = name,
        description = description,
        artworkPath = artworkPath,
        trackIds = trackIds
    )
}

fun Track.toTrackEntity(): TrackEntity {
    return TrackEntity(
        trackId = trackId,
        trackName = trackName,
        artistName = artistName,
        trackTimeMillis = trackTimeMillis,
        artworkUrl100 = artworkUrl100,
        releaseDate = releaseDate,
        primaryGenreName = primaryGenreName,
        country = country,
        previewUrl = previewUrl,
        collectionName = collectionName,
        isFavourite = isFavourite
    )
}