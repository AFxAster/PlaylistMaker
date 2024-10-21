package com.example.playlistmaker.search.presentation.mapper

import com.example.playlistmaker.common.entity.Track
import com.example.playlistmaker.search.presentation.model.TrackUI
import java.text.SimpleDateFormat
import java.util.Locale


fun Track.toTrackUI(): TrackUI {
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