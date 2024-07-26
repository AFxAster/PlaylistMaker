package com.example.playlistmaker.presentation.mapper

import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.presentation.model.PlayerTrackUI
import com.example.playlistmaker.presentation.model.TrackUI
import java.text.SimpleDateFormat
import java.util.Locale


fun Track.toTrackPresentation(): TrackUI {
    return TrackUI(
        trackId = trackId,
        trackName = trackName,
        artistName = artistName,
        trackTime = SimpleDateFormat("m:ss", Locale.getDefault()).format(trackTimeMillis),
        artworkUrl100 = artworkUrl100,
        releaseYear = SimpleDateFormat("Y", Locale.getDefault()).format(releaseDate),
        primaryGenreName = primaryGenreName,
        country = country,
        previewUrl = previewUrl,
        collectionName = collectionName
    )
}

fun Track.toPlayerTrackUI(): PlayerTrackUI {
    return PlayerTrackUI(
        trackId = trackId,
        trackName = trackName,
        artistName = artistName,
        trackTime = SimpleDateFormat("m:ss", Locale.getDefault()).format(trackTimeMillis),
        artworkUrl512 = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"),
        releaseYear = SimpleDateFormat("Y", Locale.getDefault()).format(releaseDate),
        primaryGenreName = primaryGenreName,
        country = country,
        previewUrl = previewUrl,
        collectionName = collectionName
    )
}