package com.example.playlistmaker.audioplayer.presentation.mapper

import com.example.playlistmaker.audioplayer.presentation.model.PlayerTrackUI
import com.example.playlistmaker.common.entity.Track
import java.text.SimpleDateFormat
import java.util.Locale

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
        collectionName = collectionName,
        isFavourite = isFavourite
    )
}

fun Int.toFormattedPosition(): String {
    return SimpleDateFormat(
        "m:ss",
        Locale.getDefault()
    ).format(this)
}