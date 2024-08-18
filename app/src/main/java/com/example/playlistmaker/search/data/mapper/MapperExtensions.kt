package com.example.playlistmaker.search.data.mapper

import com.example.playlistmaker.search.data.dto.HistoryTrackDTO
import com.example.playlistmaker.search.data.dto.TrackDTO
import com.example.playlistmaker.search.domain.entity.Track

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
        collectionName = collectionName
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
        collectionName = collectionName
    )
}