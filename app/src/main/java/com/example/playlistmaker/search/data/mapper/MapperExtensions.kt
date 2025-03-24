package com.example.playlistmaker.search.data.mapper

import com.example.playlistmaker.common.entity.Track
import com.example.playlistmaker.released.domain.entity.Release
import com.example.playlistmaker.released.domain.entity.ReleaseType
import com.example.playlistmaker.search.data.dto.HistoryTrackDTO
import com.example.playlistmaker.search.data.dto.ReleaseDTO
import com.example.playlistmaker.search.data.dto.TrackDTO

fun TrackDTO.toTrack(): Track {
    return Track(
        trackId = trackId,
        trackName = trackName,
        artistName = artistName,
        trackTimeMillis = trackTimeMillis,
        artworkUrl100 = artworkUrl100,
        releaseDate = releaseDate,
        primaryGenreName = primaryGenreName,
        country = country,
        previewUrl = previewUrl,
        collectionName = collectionName.run {
            if (endsWith(" - Single"))
                null
            else
                this
        }
    )
}

fun HistoryTrackDTO.toTrack(): Track {
    return Track(
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

fun Track.toHistoryTrackDTO(): HistoryTrackDTO {
    return HistoryTrackDTO(
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

fun ReleaseDTO.toRelease(): Release {
    return Release(
        id = collectionId,
        artistName = artistName,
        artworkUrl100 = artworkUrl100,
        releaseDate = releaseDate,
        releaseName = collectionName.removeSuffix(" - Single"),
        type = collectionName.run {
            if (endsWith(" - Single"))
                ReleaseType.Single
            else
                ReleaseType.Album
        },
    )
}